package com.bk.fm.findmeeting;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MeetingParams extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private HashMap<Day, Range> map;

	private CheckBox sunday;
	private CheckBox monday;
	private CheckBox tuesday;
	private CheckBox wednesday;
	private CheckBox thursday;
	private CheckBox friday;
	private CheckBox saturday;

	private Button nextButton;

	private TextView startTime;
	private TextView endTime;
	private TextView meetingDuration;

	private Spinner dayComboBox;

//----------------------------------------------------
//
//	"Constructor"
//
//----------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_meeting_params);

		//Give the View variables a value
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
	public void setButtonActionHandler() {
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Make Meeting Object


				//Send the Meeting Object along to the Summary Activity (Make an intent)


			}
		});
	} //End public void setButtonActionHandler()

	public void setCheckBoxActionHandlers() {
		View.OnClickListener comboBoxListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateComboBox(); //Update the comboBox when a checkbox is clicked.
			}
		};

		sunday.setOnClickListener(comboBoxListener);
		monday.setOnClickListener(comboBoxListener);
		tuesday.setOnClickListener(comboBoxListener);
		wednesday.setOnClickListener(comboBoxListener);
		thursday.setOnClickListener(comboBoxListener);
		friday.setOnClickListener(comboBoxListener);
		saturday.setOnClickListener(comboBoxListener);

	} //End public void setCheckBoxActionHandlers()

	public void setTimeInputActionHandlers() {
		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePicker((TextView) v);
			}
		};

		//Currently, all three have the same listener, as the input is only in 24h format.
		startTime.setOnClickListener(listener);
		endTime.setOnClickListener(listener);
		meetingDuration.setOnClickListener(listener);

	} //End public void setTimeInputActionHandlers()

	public void setSpinnerActionHandler() {
		dayComboBox.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {} //This had to be here, but is useless.
		});
	}

//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------

//Set the fields' values.
	public void initializeFields() {
		map = new HashMap<>();

		//Checkboxes
		sunday = (CheckBox) findViewById(R.id.sunday);
		monday = (CheckBox) findViewById(R.id.monday);
		tuesday = (CheckBox) findViewById(R.id.tuesday);
		wednesday = (CheckBox) findViewById(R.id.wednesday);
		thursday = (CheckBox) findViewById(R.id.thursday);
		friday = (CheckBox) findViewById(R.id.friday);
		saturday = (CheckBox) findViewById(R.id.saturday);

		//Button
		nextButton = (Button) findViewById(R.id.nextButton);

		//Time Inputs
		startTime = (TextView) findViewById(R.id.startTime);
		endTime = (TextView) findViewById(R.id.endTime);
		meetingDuration = (TextView) findViewById(R.id.duration);

		//Spinner
		dayComboBox = (Spinner) findViewById(R.id.dayComboBox);
		updateComboBox();

	} //End public void initializeFields()


//Determine whether or not the time fields contain valid times
	public boolean validTimes() {
		if(!(Time.isValidTime(Time.parseHours(startTime.getText()), Time.parseMinutes(startTime.getText())))) {
			return false;
		}

		if(!(Time.isValidTime(Time.parseHours(endTime.getText()), Time.parseMinutes(endTime.getText())))) {
			return false;
		}

		return true;
	}

//Store the current GUI config to a Time Range
	public void storeDay() {
		if(validTimes()) {
			try {
				//Grab selection from comboBox
				TextView selected = (TextView) dayComboBox.getSelectedItem();
				StringBuilder str = new StringBuilder(selected.getText());

				switch (str.toString()) {
					case "Sunday":
						addDay(Day.SUNDAY, startTime.getText(), endTime.getText());
						break;

					case "Monday":
						addDay(Day.MONDAY, startTime.getText(), endTime.getText());
						break;

					case "Tuesday":
						addDay(Day.TUESDAY, startTime.getText(), endTime.getText());
						break;

					case "Wednesday":
						addDay(Day.WEDNESDAY, startTime.getText(), endTime.getText());
						break;

					case "Thursday":
						addDay(Day.THURSDAY, startTime.getText(), endTime.getText());
						break;

					case "Friday":
						addDay(Day.FRIDAY, startTime.getText(), endTime.getText());
						break;

					case "Saturday":
						addDay(Day.SATURDAY, startTime.getText(), endTime.getText());
						break;

					default:
						break;

				}

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(), getString(R.string.invalidInterval_1), Toast.LENGTH_LONG).show();
			}
		}
	}

	//Update an existing day in the HashMap<Day, Range>
	public void addDay(Day d, CharSequence startTime, CharSequence endTime) {
		Time start = new Time(Time.parseHours(startTime), Time.parseMinutes(startTime));
		Time end = new Time(Time.parseHours(endTime), Time.parseMinutes(endTime));

		map.put(d, new Range(start, end, d));
	}

//Make a boolean array representing the available days as selected by the user via checkboxes.
	public boolean[] getSelectedDays(){
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

//For each selected checkbox, add the day to the comboBox
	public void updateComboBox() {
		boolean[] days = getSelectedDays();
		ArrayList<String> selectedDays = new ArrayList<>();

		if(map.size() == 0) {
			selectedDays.add(getString(R.string.all_selected_days));
		}

		for(int i = 0; i < days.length; i++) {
			if(days[i]) {
				selectedDays.add(Day.getDay(i).toString(this)); //You've got to pass the context to toString() in order to access the strings.xml resources.
			}
		}

		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedDays);
		data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		dayComboBox.setAdapter(data);

	}//End public void updateComboBox()


//Get the time for a time input
	public void showTimePicker(final TextView txtTime) {

		int hours = Time.parseHours(txtTime.getText());
		int minutes = Time.parseMinutes(txtTime.getText());

		TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				String h = "" + hour;
				String m = "" + minute;

				//Ensure the output hour is two digits.
				if(h.length() == 1) {
					h = "0" + h;
				}

				//Ensure the output minutes are two digits.
				if(m.length() == 1) {
					m = "0" + m;
				}

				// Display Selected time in textbox
				txtTime.setText(h + ":" + m);
			}
		}, hours, minutes, true);
		tpd.show();
	} //End public void showTimePicker(final TextView)
} //End class MeetingParams extends ActionBarActivity