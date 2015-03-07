package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
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

	private TimePicker startTime;
	private TimePicker endTime;
	private TimePicker meetingDuration;

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
		startTime = (TimePicker) findViewById(R.id.startTime);
		endTime = (TimePicker) findViewById(R.id.endTime);
		meetingDuration = (TimePicker) findViewById(R.id.duration);

	//Spinner
		dayComboBox = (Spinner) findViewById(R.id.dayComboBox);

	} //End public void initializeFields()


} //End class MeetingParams extends ActionBarActivity
