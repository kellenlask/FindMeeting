/*
This file contains the Java code to describe the behavior of the Involved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to maintain a list of people who are involved in a meeting event that can be
interacted with.
 */

package com.bk.fm.findmeeting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
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
	private ArrayList<Person> people;
	private Meeting meeting;

	private ListView peopleList;
	private Button addPersonButton;
	private Button findTimesButton;
    ArrayList<String> peopleNames;
    ArrayList<Person> addedPeople;


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
		meeting = (Meeting)getIntent().getSerializableExtra("MEETING");
        addedPeople = (ArrayList<Person>)getIntent().getSerializableExtra("ADDED_PEOPLE");

		initializeFields();

	} //End protected void onCreate()

    private AdapterView.OnItemClickListener listItemClicked = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
            startActivity(i);

        }
    };

	@Override
	protected void onPause() { //When the user leaves the activity save ArrayList to sharedPrefs
		super.onPause();
		//Store the ArrayList to the SharedPrefs
		SharedPreferences prefs = getSharedPreferences("peopleStorage", Context.MODE_PRIVATE);
		SharedPreferences.Editor edit = prefs.edit();

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
			outStream.writeObject(people);
			outStream.flush();
			edit.putString("people", outStream.toString());

		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Failed To Store List", Toast.LENGTH_SHORT).show();
		}

		edit.commit();
	} //End protected void onPause()

	@Override
	protected void onResume() { //When the user returns to the activity, update the people list
		super.onResume();
		SharedPreferences prefs = getSharedPreferences("peopleStorage", Context.MODE_PRIVATE);
		//SharedPreferences.Editor edit = prefs.edit();

		try {
			String s = prefs.getString("people", "");
			byte[] bytes = s.getBytes();

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
			ObjectInputStream si = new ObjectInputStream(inputStream);

			people = (ArrayList<Person>) si.readObject();
			updateList();

		} catch(IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	} //End protected void onResume()

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------
	public void addAddPersonActionHandler() {
		addPersonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) { //Show the SavedPeople Activity on AddPerson Button-Click
				Intent i = new Intent(getBaseContext(), SavedPeople.class);
				//i.putExtra("meeting", (Parcelable) meeting);
				startActivity(i);
			}
		});
	}

	public void addFindTimesActionHandler() {
		findTimesButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			//TODO: Set
			}
		});
	}

	public void addListActionHandler() {
	//On Long-Press: bring up delete menu
	//TODO: set InvolvedPeople List's action handlers

	//On Short Press: bring up AvailabilitySummary Activity


	}

//----------------------------------------------------
//
//	Other Methods
//
//----------------------------------------------------
	public void updateList() {
        if (addedPeople != null)
        {
            peopleNames.clear();

            for (Person p : addedPeople) {
                peopleNames.add(p.getName());
            }

            Collections.sort(peopleNames);
            ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peopleNames);
            peopleList.setAdapter(data);
        }
	}

	public void initializeFields() {
		people = new ArrayList<>();
        peopleNames = new ArrayList<>();

		addPersonButton = (Button) findViewById(R.id.addPersonButton);
		findTimesButton = (Button) findViewById(R.id.findTimesButton);
		peopleList = (ListView) findViewById(R.id.peopleList);
        peopleList.setOnItemClickListener(listItemClicked);

        addAddPersonActionHandler();
		updateList();
	}
}
