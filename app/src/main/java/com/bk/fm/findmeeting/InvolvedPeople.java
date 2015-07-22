package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bk.fm.Scheduling.Meeting;
import com.bk.fm.Scheduling.Person;

import java.util.ArrayList;
import java.util.Collections;

/*
This file contains the Java code to describe the behavior of the Involved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to maintain a list of people who are involved in a meeting event that can be
interacted with.
 */

//Basic Logic:
//
//Maintain an ArrayList<Person> of involved people
//On long press, deletion menu
//On press, availability page
//On plus button press, go to add person screen
//
public class InvolvedPeople extends Fragment {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private MainActivity parent;


	//GUI Fields
	private ListView peopleList;
	private Button addPersonButton;
	private Button findTimesButton;

	//Logic Fields
    private ArrayList<String> peopleNames;
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

	} //End protected void onCreate()

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
		View view = inflater.inflate(R.layout.activity_involved_people, container, false);

		initializeFields();

		setActionHandlers();

		return view;

	} //End public View onCreateView(LayoutInflater, ViewGroup, Bundle)

	public void onBackPressed() {
		putMeeting();

		Intent i = new Intent(parent.getBaseContext(), Summary.class);
		startActivity(i);
	}

	public void initializeFields() {
		//List of people names to throw into the ListView
		peopleNames = new ArrayList<>();

		//Pull Meeting object out
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		addPersonButton = (Button) parent.findViewById(R.id.addPersonButton);
		findTimesButton = (Button) parent.findViewById(R.id.findTimesButton);
		peopleList = (ListView) parent.findViewById(R.id.peopleList);

		updateList();

	} //End initializeFields()

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------
	public void setActionHandlers() {
		//Go to SavedPeople
		addPersonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { //Show the SavedPeople Activity on AddPerson Button-Click
				goToActivity(SavedPeople.class);
			}
		});

		//Go to final Activity
		findTimesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(meeting.getInvolvedPeople().size() != 2) { //People have been added to the meeting
                    if (meeting.isValid()) { //Everybody has some availability set
						goToActivity(Results.class);

                    } else { // People have been added to the meeting and have added obligation(s) / availabilitie(s)
						Toast.makeText(parent.getApplicationContext(), getString(R.string.moreObligations), Toast.LENGTH_SHORT).show();

					}

				} else { //Meeting object is null
					Toast.makeText(parent.getApplicationContext(), getString(R.string.morePeople), Toast.LENGTH_SHORT).show();

                } //End outer if-else
			}
		});

		//On Long-Press: bring up delete menu
		peopleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				String personName = peopleList.getItemAtPosition(position).toString();
				meeting.getInvolvedPeople().remove(peopleNames.indexOf(personName));

				updateList();

				return true;
			}
		});

		//Go to PersonalSummary for a given person onShortPress
		peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedPerson = peopleList.getItemAtPosition(position).toString();
				Person p = meeting.getPerson(selectedPerson);

				Intent i = new Intent(parent.getContext(), PersonalSummary.class);
				i.putExtra("PERSON", (Parcelable) p);
				startActivity(i);

			}
		});

    } //End setActionHandlers()

//----------------------------------------------------
//
//	Other Methods
//
//----------------------------------------------------

	public void updateList() {
        if (meeting.getInvolvedPeople() != null) {
            peopleNames.clear();

            for (Person p : meeting.getInvolvedPeople()) {
                peopleNames.add(p.getName());
            }

            Collections.sort(peopleNames);

            updateListView();
        }
	} //End updateList()

	public void updateListView() {
		ArrayAdapter<String> data = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, peopleNames);
		peopleList.setAdapter(data);
	}

	public void goToActivity(Class c) {
		putMeeting();

		Intent i = new Intent(parent.getBaseContext(), c);
		startActivity(i);
	}

	//Put the meeting object into sharedpreferences
	public void putMeeting() {
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString("MEETING", Meeting.serializeMeeting(meeting));

		editor.commit();

	} //End public void putMeeting()

} //End Class
