package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;


public class MeetingParams extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
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

		setButtonActionHandler();

	} //End protected void onCreate(Bundle)

//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------
//Set the Next Button's action handler
	public void setButtonActionHandler() {
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Make Meeting Object
				

				//Send the Meeting Object along to the Summary Activity


			}
		});
	} //End public void setButtonActionHandler()

//Make a boolean array representing the available days as selected by the user via checkboxes.
	public boolean[] makeDaysArray(){
		boolean[] days = new boolean[7];

		days[0] = sunday.isChecked();
		days[1] = monday.isChecked();
		days[2] = tuesday.isChecked();
		days[3] = wednesday.isChecked();
		days[4] = thursday.isChecked();
		days[5] = friday.isChecked();
		days[6] = saturday.isChecked();

		return days;
	} //End public boolean[] makeDaysArray()


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

	} //End public void initializeFields()


} //End class MeetingParams extends ActionBarActivity
