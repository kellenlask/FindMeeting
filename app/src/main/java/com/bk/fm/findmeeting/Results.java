/*
This file contains the Java code to describe the behavior of the Results view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present the user with a list of time ranges ordered first by the number of people
available for that time range, then by earliness in the week.
 */

package com.bk.fm.findmeeting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class Results extends ActionBarActivity {

	private ListView listView;
	private Meeting meeting;
	private ArrayList<String> stringList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		initializeFields();
	}

	public void initializeFields() {
		listView = (ListView) findViewById(R.id.resultsList);
		SharedPreferences sp = getSharedPreferences("MEETING", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("Meeting", ""));
		//meeting = (Meeting)getIntent().getSerializableExtra("MEETING");
		stringList = new ArrayList<>();

		makeList();
		updateList();

	}

	public void makeList() {
		TreeMap<Integer, TreeSet<Range>> map = meeting.calcTotalAvailability();

		for(Integer i : map.keySet()) {
			for(Range r : map.get(i)) {
				stringList.add(r.toString(getBaseContext()));

			}
		}

	}

	public void updateList() {
		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
		listView.setAdapter(data);

	}

}
