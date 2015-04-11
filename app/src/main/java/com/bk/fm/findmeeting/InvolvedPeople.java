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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

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
    public static ArrayList<String> addedPeople; //TODO: Use this array list to populate people that have been selected (clicked) on the saved people page


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

		initializeFields();

	} //End protected void onCreate()

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
			Toast.makeText(getApplicationContext(), "Failed To Get List", Toast.LENGTH_SHORT).show();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "Failed To Get List", Toast.LENGTH_SHORT).show();
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
		ArrayList<String> persons = new ArrayList<>();

		for(Person p : people) {
			persons.add(p.getName());
		}

		ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, persons);
		peopleList.setAdapter(data);
	}

	public void initializeFields() {
		people = new ArrayList<>();

		addPersonButton = (Button) findViewById(R.id.addPersonButton);
		findTimesButton = (Button) findViewById(R.id.findTimesButton);
		peopleList = (ListView) findViewById(R.id.peopleList);

        addAddPersonActionHandler();
		updateList();
	}
}
