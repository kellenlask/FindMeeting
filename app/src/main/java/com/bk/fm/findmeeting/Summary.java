package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

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
        TreeMap newMap = meeting.getPossibleDays();
        Interval newInterval = meeting.getMeetingDuration();

        ArrayList<String> days = new ArrayList<String>();
        for (Map.Entry<Day, Range> entry : newMap.entrySet()) {

        }

        /*
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(newMap.toString());
        builder1.setCancelable(true);

        AlertDialog alert11 = builder1.create();
        alert11.show();
        */
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
