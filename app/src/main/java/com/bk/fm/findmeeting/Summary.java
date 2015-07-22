package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bk.fm.HumanTime.Day;
import com.bk.fm.Scheduling.Meeting;

import java.util.ArrayList;

/*
This file contains the Java code to describe the behavior of the Meeting Summary view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to summarize the Meeting parameters to the user before he/she moves on to add people
to the meeting.
 */

public class Summary extends Fragment {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private MainActivity parent;

	private TextView meetingLengthTextView;
    private ListView daysTimesListView;
	private Button nextButton;

    private ArrayList<String> daysTimes;
    private ArrayAdapter<String> daysTimesAdapter;
	private Meeting meeting;

//----------------------------------------------------
//
//	Initialization
//
//----------------------------------------------------

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		parent = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.activity_summary, container, false);

		initializeFields();

		setActionHandlers();

		return view;

	} //End public View onCreateView(LayoutInflater, ViewGroup, Bundle)

	public void initializeFields() {
		//Pull meeting from sharedpreferences
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		//Set the meeting length in the GUI
		meetingLengthTextView = (TextView) parent.findViewById(R.id.meetingLengthLabel);
		meetingLengthTextView.setText(" " + meeting.getMeetingDuration().toString("Single")); //Todo: Use a more descriptive name.

		//Populate the list of days and times
		daysTimesListView = (ListView) parent.findViewById(R.id.daysTimesListView);
		setDaysTimes();

		nextButton = (Button) parent.findViewById(R.id.nextButton);
	} //End public void initializeFields()

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------
	public void setActionHandlers() {
		nextButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("MEETING", Meeting.serializeMeeting(meeting));
				editor.commit();

				parent.changeToFragment(Shard.INVOLVED_PEOPLE);

			}
		});
	} //End public void setActionHandlers()

//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------

	//Set the days and the times into the ListView
	public void setDaysTimes() {
		daysTimes = new ArrayList<>();

		for(Day d: meeting.getPossibleDays().keySet()) {
			daysTimes.add(meeting.getPossibleDays().get(d).toString(parent));
		}

		daysTimesAdapter = new ArrayAdapter<String>(parent, android.R.layout.simple_list_item_1, daysTimes);
		daysTimesListView.setAdapter(daysTimesAdapter);
	}

} //End public class Summary extends ActionBarActivity
