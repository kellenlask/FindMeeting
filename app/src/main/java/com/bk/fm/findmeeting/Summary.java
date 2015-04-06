/*
This file contains the Java code to describe the behavior of the Meeting Summary view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to summarize the Meeting parameters to the user before he/she moves on to add people
to the meeting.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class Summary extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private Meeting meeting;
    private TextView meetingLengthTextView;
    /*
    private ListView dayListView;
    private ArrayList<String> days;
    private ArrayAdapter<String> daysAdapter;

    private ListView timeListView;
    private ArrayList<String> times;
    private ArrayAdapter<String> timesAdapter;
    */
    private ListView daysTimesListView;
    private ArrayList<String> daysTimes;
    private ArrayAdapter<String> daysTimesAdapter;

	private Button nextButton;
//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

		//Pull Meeting object out
        meeting = (Meeting)getIntent().getSerializableExtra("MEETING");

		initializeFields();

		//Populate the GUI
		setDaysTimes();
		setMeetingDuration();

		addEventHandlers();
	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------
	public void addEventHandlers() {
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getBaseContext(), InvolvedPeople.class);
				i.putExtra("MEETING", (Parcelable) meeting);
				startActivity(i);
			}
		});
	}

//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------
	public void initializeFields() {
		meetingLengthTextView = (TextView)findViewById(R.id.meetingLengthLabel);
		daysTimesListView = (ListView)findViewById(R.id.daysTimesListView);
		nextButton = (Button) findViewById(R.id.nextButton);
	}

	public void setDaysTimes() {
		daysTimes = new ArrayList<String>();

		for(Day d: meeting.getPossibleDays().keySet()) {
			daysTimes.add(meeting.getPossibleDays().get(d).toString(this));
		}

		daysTimesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysTimes);
		daysTimesListView.setAdapter(daysTimesAdapter);
	}

    /*
    public void setTimes() {
		times = new ArrayList<String>();

		for(Range r: meeting.getPossibleDays().values()) {
			times.add(r.getInterval().toString("Interval"));
		}

		timesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, times);
		timeListView.setAdapter(timesAdapter);
	}
    */
	public void setMeetingDuration() {

		meetingLengthTextView.setText(" " + meeting.getMeetingDuration().toString("Single"));

	}

} //End public class Summary extends ActionBarActivity
