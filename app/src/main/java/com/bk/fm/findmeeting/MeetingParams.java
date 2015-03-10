package com.bk.fm.findmeeting;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.HashMap;

public class MeetingParams extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private HashMap<Day, Interval> map;

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

		initializeFields();

		setCheckBoxActionHandlers();

		setButtonActionHandler();

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


				//Send the Meeting Object along to the Summary Activity


			}
		});
	} //End public void setButtonActionHandler()

	public void setCheckBoxActionHandlers() {
		View.OnClickListener comboBoxListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				updateComboBox();
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
		


	} //End public void setTimeInputActionHandlers()

//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------

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


	public void updateComboBox() {
		boolean[] days = getSelectedDays();
		ArrayList<String> selectedDays = new ArrayList<>();

		selectedDays.add(getString(R.string.all_selected_days));

		for(int i = 0; i < days.length; i++) {
			if(days[i]) {
				selectedDays.add(Day.getDay(i).toString(this)); //You've got to pass the context to toString() in order to access the strings.xml resources.
			}
		}

		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, selectedDays);
		data.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		dayComboBox.setAdapter(data);

	}//End public void updateComboBox()


//Set the fields' values.
	public void initializeFields() {
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

	} //End public void initializeFields()


//Get the time for a time input
	public void showTimePicker(final TextView txtTime) {

		int hours = parseHours(txtTime.getText());
		int minutes = parseMinutes(txtTime.getText());

		TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker view, int hour, int minute) {
				// Display Selected time in textbox
				txtTime.setText(hour + ":" + minute);
			}
		}, hours, minutes, true);
		tpd.show();


	} //End public void showTimePicker(final TextView)

//Parse hours or minutes
	public int parseHours(String s) {
		try {

			return Integer.parseInt(s.substring(0, 1));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseHours(String)

	public int parseMinutes(String s) {
		try {

			return Integer.parseInt(s.substring(3));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseMinutes(String)

	public int parseHours(CharSequence seq) {
		try {

			StringBuilder str = new StringBuilder(seq);

			String s = str.toString();

			return Integer.parseInt(s.substring(0, 1));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseHours(CharSequence)

	public int parseMinutes(CharSequence seq) {
		try {

			StringBuilder str = new StringBuilder(seq);

			String s = str.toString();

			return Integer.parseInt(s.substring(3));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseMinutes(CharSequence)

} //End class MeetingParams extends ActionBarActivity
