/*
This file contains the Java code to describe the behavior of the Results view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present the user with a list of time ranges ordered first by the number of people
available for that time range, then by earliness in the week.
 */

package com.bk.fm.findmeeting;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bk.fm.HumanTime.Range;
import com.bk.fm.Scheduling.Meeting;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.TreeSet;


public class Results extends Fragment {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private MainActivity parent;

	private ListView listView;
	private static Meeting meeting;
	private ArrayList<String> stringList;

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
		View view = inflater.inflate(R.layout.activity_results, container, false);

		initializeFields();

		return view;

	} //End public View onCreateView(LayoutInflater, ViewGroup, Bundle)

	public void initializeFields() {
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		listView = (ListView) parent.findViewById(R.id.resultsList);
		stringList = new ArrayList<>();

		makeList();
		updateList();

	} //End initializeFields()

//----------------------------------------------------
//
//	Results Methods
//
//----------------------------------------------------

	public void makeList() {
		TreeMap<Integer, TreeSet<Range>> map = meeting.calcTotalAvailability();

		for(Integer i : map.keySet()) {
			for(Range r : map.get(i)) {
                double p = (((double) i / (map.keySet().size() - 1)) * 100);
                int percent = (int) p;
				stringList.add(0, percent + "% : " + r.toString(parent.getBaseContext()));
			}
		}

	} //End makeList()

	public void updateList() {
		ArrayAdapter<String> data = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, stringList);
		listView.setAdapter(data);

	} //End updateList()

} //End Class
