/*
This file contains the Java code to describe the behavior of the Results view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present the user with a list of time ranges ordered first by the number of people
available for that time range, then by earliness in the week.
 */

package com.bk.fm.FindMeeting;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bk.fm.HumanTime.Range;
import com.bk.fm.Scheduling.Meeting;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class Results extends ActionBarActivity {

	private ListView listView;
	private static Meeting meeting;
	private ArrayList<String> stringList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);

		initializeFields();
	}

	public void initializeFields() {
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		listView = (ListView) findViewById(R.id.resultsList);
		stringList = new ArrayList<>();

		makeList();
		updateList();

	} //End initializeFields()

	public void makeList() {
		TreeMap<Integer, TreeSet<Range>> map = meeting.calcTotalAvailability();

		for(Integer i : map.keySet()) {
			for(Range r : map.get(i)) {
                double p = (((double) i / (map.keySet().size() - 1)) * 100);
                int percent = (int) p;
				stringList.add(0, percent + "% : " + r.toString(getBaseContext()));
			}
		}

	} //End makeList()

	public void updateList() {
		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, stringList);
		listView.setAdapter(data);

	} //End updateList()

} //End Class
