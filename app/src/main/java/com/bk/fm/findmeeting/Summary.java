package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
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
    private ArrayAdapter<String> adapter;
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
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, days);

        for(Day d: meeting.getPossibleDays().keySet()) {
            days.add(d.toString());
        }

        dayListView.setAdapter(adapter);
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
