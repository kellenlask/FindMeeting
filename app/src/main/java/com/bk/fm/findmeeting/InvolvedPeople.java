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
	private static Meeting meeting;
	private ListView peopleList;
	private Button addPersonButton;
	private Button findTimesButton;
    private ArrayList<String> peopleNames;

//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_involved_people);

		//Pull Meeting object out
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		//meeting = (Meeting)getIntent().getSerializableExtra("MEETING");

       	//addedPeople = (ArrayList<Person>)getIntent().getSerializableExtra("ADDED_PEOPLE");
		//meeting.setInvolvedPeople(addedPeople);

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
				Intent i = new Intent(getBaseContext(), SavedPeople.class);
				putMeeting();
				//i.putExtra("MEETING", (Serializable) meeting);
				startActivity(i);
			}
		});

		//Go to final Activity
		findTimesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(meeting.getInvolvedPeople().size() != 0) {
					Intent i = new Intent(getBaseContext(), Results.class);
					putMeeting();
					//i.putExtra("MEETING", (Serializable) meeting);
					startActivity(i);
				} else {
					Toast.makeText(getApplicationContext(), "Please add some people.", Toast.LENGTH_SHORT).show();
				}

			}
		});

		//On Long-Press: bring up delete menu
		//TODO: set InvolvedPeople List's action handlers

		//On Short Press: bring up AvailabilitySummary Activity

		//Go to AvailabilitySummary for a given person
		peopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				String personName = peopleList.getItemAtPosition(position).toString();
				Person p = meeting.getInvolvedPeople().get(peopleNames.indexOf(personName));

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
		peopleNames = new ArrayList<>();

		addPersonButton = (Button) findViewById(R.id.addPersonButton);
		findTimesButton = (Button) findViewById(R.id.findTimesButton);
		peopleList = (ListView) findViewById(R.id.peopleList);

		updateList();
	} //End initializeFields()

	public void updateList() {
        if (meeting.getInvolvedPeople() != null)
        {
            peopleNames.clear();

            for (Person p : meeting.getInvolvedPeople()) {
                peopleNames.add(p.getName());
            }

            Collections.sort(peopleNames);
            ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peopleNames);
            peopleList.setAdapter(data);
        }
	} //End updateList()

	public void putMeeting() {
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("MEETING", Meeting.serializeMeeting(meeting));
		editor.commit();
	}
} //End Class
