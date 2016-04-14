package com.bk.fm.FindMeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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
public class InvolvedPeople extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	//GUI Fields
	private ListView peopleList;
	private Button addPersonButton;
	private Button findTimesButton;

	//Logic Fields
    private ArrayList<String> peopleNames;
	private Meeting meeting;

//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_involved_people);

		initializeFields();

		addEventHandlers();

	} //End protected void onCreate()

	public void onBackPressed() {
		putMeeting();

		Intent i = new Intent(getBaseContext(), Summary.class);
		startActivity(i);
	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------
	public void addEventHandlers() {
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
						Toast.makeText(getApplicationContext(), getString(R.string.moreObligations), Toast.LENGTH_SHORT).show();

					}

				} else { //Meeting object is null
					Toast.makeText(getApplicationContext(), getString(R.string.morePeople), Toast.LENGTH_SHORT).show();

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

		//Go to AvailabilitySummary for a given person onShortPress
		peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String selectedPerson = peopleList.getItemAtPosition(position).toString();
				Person p = meeting.getPerson(selectedPerson);

				Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
				i.putExtra("PERSON", (Parcelable) p);
				startActivity(i);

			}
		});

    } //End addEventHandlers()

//----------------------------------------------------
//
//	Other Methods
//
//----------------------------------------------------
	public void initializeFields() {
		//List of people names to throw into the ListView
		peopleNames = new ArrayList<>();

		//Pull Meeting object out
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		addPersonButton = (Button) findViewById(R.id.addPersonButton);
		findTimesButton = (Button) findViewById(R.id.findTimesButton);
		peopleList = (ListView) findViewById(R.id.peopleList);

		updateList();

	} //End initializeFields()

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
		ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peopleNames);
		peopleList.setAdapter(data);
	}

	public void goToActivity(Class c) {
		putMeeting();

		Intent i = new Intent(getBaseContext(), c);
		startActivity(i);
	}

	//Put the meeting object into sharedpreferences
	public void putMeeting() {
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();

		editor.putString("MEETING", Meeting.serializeMeeting(meeting));

		editor.commit();

	} //End public void putMeeting()

} //End Class
