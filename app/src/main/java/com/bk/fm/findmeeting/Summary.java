package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class Summary extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private Meeting meeting; //ToDo: figure out how to de-parselize this into an instantiated field.

    private ListView dayListView;
    private ArrayList<String> days;
    private ArrayAdapter<String> daysAdapter;

    private ListView timeListView;
    private ArrayList<String> times;
    private ArrayAdapter<String> timesAdapter;
//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summary);

        meeting = (Meeting)getIntent().getSerializableExtra("MEETING");
        Interval newInterval = meeting.getMeetingDuration();

        days = new ArrayList<String>();
        dayListView = (ListView)findViewById(R.id.dayListView);
        daysAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);

        times = new ArrayList<String>();
        timeListView = (ListView)findViewById(R.id.timeListView);
        timesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, times);

        for(Day d: meeting.getPossibleDays().keySet()) {
            days.add(d.toString());
        }

        for(Range r: meeting.getPossibleDays().values()) {
            times.add(r.toString(getBaseContext())); // This toString appears to show the location in memory rather than than the toString method in Range.java
        }

        dayListView.setAdapter(daysAdapter);
        timeListView.setAdapter(timesAdapter);

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








}
