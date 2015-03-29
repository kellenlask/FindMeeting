/*
This file contains the Java code to describe the behavior of the Meeting Summary view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to summarize the Meeting parameters to the user before he/she moves on to add people
to the meeting.
 */

package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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

    private ListView dayListView;
    private ArrayList<String> days;
    private ArrayAdapter<String> daysAdapter;

    private ListView timeListView;
    private ArrayList<String> times;
    private ArrayAdapter<String> timesAdapter;

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
		setDays();
		setTimes();
		setMeetingDuration();
	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------


//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------
	public void initializeFields() {
		meetingLengthTextView = (TextView)findViewById(R.id.meetingLengthLabel);
		dayListView = (ListView)findViewById(R.id.dayListView);
		timeListView = (ListView)findViewById(R.id.timeListView);
		nextButton = (Button) findViewById(R.id.nextButton);
	}

	public void setTimes() {
		times = new ArrayList<String>();

		for(Range r: meeting.getPossibleDays().values()) {
			times.add(r.getInterval().toString("Interval"));
		}

		timesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, times);
		timeListView.setAdapter(timesAdapter);
	}

	public void setDays() {
		days = new ArrayList<String>();

		for(Day d: meeting.getPossibleDays().keySet()) {
			days.add(d.toString());
		}

		daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);
		dayListView.setAdapter(daysAdapter);
	}

	public void setMeetingDuration() {

		meetingLengthTextView.setText(" " + meeting.getMeetingDuration().toString("Single"));

	}

} //End public class Summary extends ActionBarActivity
