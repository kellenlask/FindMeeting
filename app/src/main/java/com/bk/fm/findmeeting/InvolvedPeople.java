/*
This file contains the Java code to describe the behavior of the Involved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to maintain a list of people who are involved in a meeting event that can be
interacted with.
 */

package com.bk.fm.findmeeting;

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

import java.util.ArrayList;
import java.util.Collections;

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
	private ListView peopleList;
	private Button addPersonButton;
	private Button findTimesButton;

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
				putMeeting();

				Intent i = new Intent(getBaseContext(), SavedPeople.class);
				startActivity(i);
			}
		});

		//Go to final Activity
		findTimesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(meeting.getInvolvedPeople() == null) { //Nobody has been added to the meeting
                    Toast.makeText(getApplicationContext(), "Please add some people.", Toast.LENGTH_SHORT).show();
				}
                else {
                    // Loop through the people and check that each person has at least one availability or obligation
                    boolean ready = true;
                    for (Person p : meeting.getInvolvedPeople()) {
                        if (p.getAvailability() == null) {
                            ready = false;
                            break;
                        }
                    }

                    if (ready == false) // One or more people have no availability or obligation
                    {
                        Toast.makeText(getApplicationContext(), "Please enter at least one obligation or availability for each person.", Toast.LENGTH_SHORT).show();
                    } else // People have been added to the meeting and have added obligation(s) / availabilitie(s)
                    {
                        putMeeting();

                        Intent i = new Intent(getBaseContext(), Results.class);
                        startActivity(i);
                    }
                }
			}
		});

		//On Long-Press: bring up delete menu
		//TODO: set InvolvedPeople List's action handlers

		//Go to AvailabilitySummary for a given person onShortPress
		peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String personName = peopleList.getItemAtPosition(position).toString(); //temporary
				Person p = meeting.getInvolvedPeople().get(peopleNames.indexOf(personName));

				Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
				i.putExtra("PERSON", (Parcelable) p);
				startActivity(i);

			}
		});
	} //End addEventHandlers()

    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), Summary.class);
        i.putExtra("MEETING", (Parcelable) meeting);
        startActivity(i);
    }

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

	//Put the meeting object into sharedpreferences
	public void putMeeting() {
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("MEETING", Meeting.serializeMeeting(meeting));
		editor.commit();
	}
} //End Class
