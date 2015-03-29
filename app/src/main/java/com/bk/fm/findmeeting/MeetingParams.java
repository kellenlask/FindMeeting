/*
This file contains the Java code to describe the behavior of the Meeting Parameters view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to take in the Meeting's parameters and construct a Meeting object from them.
 */

package com.bk.fm.findmeeting;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

public class MeetingParams extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private TreeMap<Day, Range> map;

	private CheckBox sunday;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;

	private Spinner dayComboBox;

	private Button startTime;
	private Button endTime;

	private Button meetingDuration;

	private Button nextButton;

//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_params);

		//Setup the view references
		initializeFields();

		//Add our event handlers
		setCheckBoxActionHandlers();
		setButtonActionHandler();
		setTimeInputActionHandlers();
		setSpinnerActionHandler();

	} //End protected void onCreate(Bundle)

//----------------------------------------------------
//
//	Action Handlers
//
//----------------------------------------------------

	//When the Next Button is clicked...
	public void setButtonActionHandler() {
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			try {
				//Ensure that our internal storage matches the User's inputs
				updateMap();

				//If a time is set for all available days, make a usable map to reflect that
				String day = (String) dayComboBox.getSelectedItem();
				if (day.equals(getString(R.string.all_selected_days))) {
					boolean[] days = getSelectedDays();

					Interval inter = getInterval();

					for (int i = 0; i < days.length; i++) {
						if (days[i]) {
							map.put(Day.getDay(i), new Range(inter, Day.getDay(i)));
						}
					}
				} //End If

				//If the user has selected a day...
				if (!map.isEmpty()) {
					//Get the meeting duration, and turn it into a hacky indefinite interval
					Time srt = new Time(0, 0);
					Time stp = new Time(meetingDuration.getText());
					Interval meetingLength = new Interval(srt, stp);

					//Make the meeting object
					Meeting meeting = new Meeting(map, meetingLength);

					//Send the Meeting Object along to the Summary Activity (Make an intent)
					Intent i = new Intent(getBaseContext(), Summary.class);
					i.putExtra("MEETING", (Parcelable) meeting);
					startActivity(i);
				} else {
					throw new Exception("No days selected.");
				} //End if-else

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), "Invalid Meeting Configuration", Toast.LENGTH_SHORT).show();
			} //End Try-Catch

		} //End public void onClick(View)
		});
	} //End public void setButtonActionHandler()


	public void setCheckBoxActionHandlers() {
		CompoundButton.OnCheckedChangeListener comboBoxListener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				updateComboBox(); //Update the comboBox when a checkbox is clicked.
			}
		};

		sunday.setOnCheckedChangeListener(comboBoxListener);
		monday.setOnCheckedChangeListener(comboBoxListener);
		tuesday.setOnCheckedChangeListener(comboBoxListener);
		wednesday.setOnCheckedChangeListener(comboBoxListener);
		thursday.setOnCheckedChangeListener(comboBoxListener);
		friday.setOnCheckedChangeListener(comboBoxListener);
		saturday.setOnCheckedChangeListener(comboBoxListener);

	} //End public void setCheckBoxActionHandlers()


	public void setTimeInputActionHandlers() {
		//Show the time picker when the field is clicked.
		View.OnClickListener getTime = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePicker((Button) v);
			}
		};

		startTime.setOnClickListener(getTime);
		endTime.setOnClickListener(getTime);
		meetingDuration.setOnClickListener(getTime);

	} //End public void setTimeInputActionHandlers()


	public void setSpinnerActionHandler() {
		dayComboBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			//This will be triggered twice: once on creation, and then once every time the user selects something...
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				//Pull up the day's start and stop times (if any) and throw them in the boxes
				updateTimeFields();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {} //Empty Override.
		});
	}


//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------

	//Set the fields' values.
	public void initializeFields() {
		map = new TreeMap<>(); //To store the selected days and their time ranges

		//Checkboxes
		sunday = (CheckBox) findViewById(R.id.sunday);
		monday = (CheckBox) findViewById(R.id.monday);
		tuesday = (CheckBox) findViewById(R.id.tuesday);
		wednesday = (CheckBox) findViewById(R.id.wednesday);
		thursday = (CheckBox) findViewById(R.id.thursday);
		friday = (CheckBox) findViewById(R.id.friday);
		saturday = (CheckBox) findViewById(R.id.saturday);

		//Spinner
		dayComboBox = (Spinner) findViewById(R.id.dayComboBox);
		updateComboBox();

		//Time Inputs
		startTime = (Button) findViewById(R.id.startTime);
		endTime = (Button) findViewById(R.id.endTime);
		meetingDuration = (Button) findViewById(R.id.duration);

		//Button
		nextButton = (Button) findViewById(R.id.nextButton);

	} //End public void initializeFields()


	//For each selected checkbox, add the day to the comboBox
	public void updateComboBox() {
		boolean[] days = getSelectedDays();
		ArrayList<String> selectedDays = new ArrayList<>(); //This will be the list we hand the comboBox.

		//Update the internal map to reflect the checkBoxes
		updateMap();

		//If we don't have times for any particular day, they can set one for all days.
		if (map.isEmpty()) {
			selectedDays.add(getString(R.string.all_selected_days));
		}

		//For each day, if selected, add it to the list
		for (int i = 0; i < days.length; i++) {
			if (days[i]) {
				selectedDays.add(Day.getDay(i).toString(this)); //You've got to pass the context to toString() in order to access the strings.xml resources.
			}
		}

		//Throw the list into the combo box.
		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedDays);
		data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		dayComboBox.setAdapter(data);

	}//End public void updateComboBox()


	//Make sure the time fields are showing the time associated with the selected day
	public void updateTimeFields() {
		String day = (String) dayComboBox.getSelectedItem();

		if (day.equals(getString(R.string.sunday)) && map.containsKey(Day.SUNDAY)) {
			startTime.setText(map.get(Day.SUNDAY).getStartTime().toString());
			endTime.setText(map.get(Day.SUNDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.monday)) && map.containsKey(Day.MONDAY)) {
			startTime.setText(map.get(Day.MONDAY).getStartTime().toString());
			endTime.setText(map.get(Day.MONDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.tuesday)) && map.containsKey(Day.TUESDAY)) {
			startTime.setText(map.get(Day.TUESDAY).getStartTime().toString());
			endTime.setText(map.get(Day.TUESDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.wednesday)) && map.containsKey(Day.WEDNESDAY)) {
			startTime.setText(map.get(Day.WEDNESDAY).getStartTime().toString());
			endTime.setText(map.get(Day.WEDNESDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.thursday)) && map.containsKey(Day.THURSDAY)) {
			startTime.setText(map.get(Day.THURSDAY).getStartTime().toString());
			endTime.setText(map.get(Day.THURSDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.friday)) && map.containsKey(Day.FRIDAY)) {
			startTime.setText(map.get(Day.FRIDAY).getStartTime().toString());
			endTime.setText(map.get(Day.FRIDAY).getStopTime().toString());

		} else if (day.equals(getString(R.string.saturday)) && map.containsKey(Day.SATURDAY)) {
			startTime.setText(map.get(Day.SATURDAY).getStartTime().toString());
			endTime.setText(map.get(Day.SATURDAY).getStopTime().toString());

		}
	} //End public void updateTimeFields()


	//Display a Time Picker, and change the Button argument's text to match the selected time.
	public void showTimePicker(final Button txtTime) {
		//Grab the current selected time
		int hours = Time.parseHours(txtTime.getText());
		int minutes = Time.parseMinutes(txtTime.getText());

		//Make the Time Picker with the current selected time
		TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				String h = "" + hour;
				String m = "" + minute;

				//Ensure the output hour is two digits.
				if (h.length() == 1) {
					h = "0" + h;
				}

				//Ensure the output minutes are two digits.
				if (m.length() == 1) {
					m = "0" + m;
				}

				// Display Selected time in button
				txtTime.setText(h + ":" + m);
				storeDay();
			}
		}, hours, minutes, true);
		tpd.show();
	} //End public void showTimePicker(final TextView)


//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------


	//Store the current times to a time Range
	public void storeDay() {
		try {
			//Grab selection from comboBox
			String day = (String) dayComboBox.getSelectedItem();

			if (day.equals(getString(R.string.sunday))) {
				addDay(Day.SUNDAY);

			} else if (day.equals(getString(R.string.monday))) {
				addDay(Day.MONDAY);

			} else if (day.equals(getString(R.string.tuesday))) {
				addDay(Day.TUESDAY);

			} else if (day.equals(getString(R.string.wednesday))) {
				addDay(Day.WEDNESDAY);

			} else if (day.equals(getString(R.string.thursday))) {
				addDay(Day.THURSDAY);

			} else if (day.equals(getString(R.string.friday))) {
				addDay(Day.FRIDAY);

			} else if (day.equals(getString(R.string.saturday))) {
				addDay(Day.SATURDAY);

			}

		} catch (Exception e) {
			//R.string.invalidInterval_1 +
			Toast.makeText(getApplicationContext(), "Invalid Time Range", Toast.LENGTH_SHORT).show();
		}
	} //End public void storeDay()


	//Update/add a day in the TreeMap<Day, Range>
	public void addDay(Day d) throws Exception {
		if (map.containsKey(d)) {
			map.remove(d);
			map.put(d, new Range(getInterval(), d));

		} else {
			map.put(d, new Range(getInterval(), d));
		}

	}//End public void addDay(Day, CharSequence, CharSequence)

	//Get an interval of the start and end times
	public Interval getInterval() throws Exception {
		Time start = new Time(startTime.getText());
		Time end = new Time(endTime.getText());

		return new Interval(start, end);

	} //End public Interval getInterval()

	//Update the map to reflect the check boxes
	public void updateMap() {
		boolean[] days = getSelectedDays();

		//Update the internal map to reflect the checkBoxes
		for (int i = 0; i < days.length; i++) {
			if (!days[i] && map.containsKey(Day.getDay(i))) {
				map.remove(Day.getDay(i));
			}
		}
	}

	//Make a boolean array representing the available days as selected by the user via checkboxes.
	public boolean[] getSelectedDays() {
		boolean[] days = new boolean[7];

		days[0] = sunday.isChecked();
		days[1] = monday.isChecked();
		days[2] = tuesday.isChecked();
		days[3] = wednesday.isChecked();
		days[4] = thursday.isChecked();
		days[5] = friday.isChecked();
		days[6] = saturday.isChecked();

		return days;
	} //End public boolean[] getSelectedDays()

} //End class MeetingParams extends ActionBarActivity