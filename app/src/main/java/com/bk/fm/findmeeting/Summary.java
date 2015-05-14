/*
This file contains the Java code to describe the behavior of the Meeting Summary view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to summarize the Meeting parameters to the user before he/she moves on to add people
to the meeting.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
    private TextView meetingLengthTextView;
    private ListView daysTimesListView;
	private Button nextButton;

    private ArrayList<String> daysTimes;
    private ArrayAdapter<String> daysTimesAdapter;
	private  Meeting meeting;

//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

		initializeFields();

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
				SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("MEETING", Meeting.serializeMeeting(meeting));
				editor.commit();

				Intent i = new Intent(getBaseContext(), InvolvedPeople.class);
				startActivity(i);

			}
		});
	} //End public void addEventHandlers()

    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MeetingParams.class);
        startActivity(i);
    }

//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------
	public void initializeFields() {
		//Pull meeting from sharedpreferences
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		//Set the meeting length in the GUI
		meetingLengthTextView = (TextView)findViewById(R.id.meetingLengthLabel);
		meetingLengthTextView.setText(" " + meeting.getMeetingDuration().toString("Single")); //Todo: Rename this shit.

		//Populate the list of days and times
		daysTimesListView = (ListView)findViewById(R.id.daysTimesListView);
		setDaysTimes();

		nextButton = (Button) findViewById(R.id.nextButton);
	} //End public void initializeFields()


	//Set the days and the times into the ListView
	public void setDaysTimes() {
		daysTimes = new ArrayList<>();

		for(Day d: meeting.getPossibleDays().keySet()) {
			daysTimes.add(meeting.getPossibleDays().get(d).toString(this));
		}

		daysTimesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, daysTimes);
		daysTimesListView.setAdapter(daysTimesAdapter);
	}

} //End public class Summary extends ActionBarActivity
