/*
This file contains the Java code to describe the behavior of the Saved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present a list of people currently in the database, and allow for the creation
and editing of people.
 */

package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;


public class SavedPeople extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private Button addPersonButton;
	private ListView savedPeopleList;
    private ArrayAdapter listAdapter;
	private String name;
    private DataBase db;

//----------------------------------------------------
//
//	onCreate
//
//----------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_people);

		initializeFields();
		populateSavedPeople();

		addButtonEventHandler();

	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------

	//TODO: Add on short/long click events for the savedPeopleList

	public void addButtonEventHandler() {
		addPersonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

			    //Show a dialog allowing for text input
				AlertDialog.Builder builder = new AlertDialog.Builder(SavedPeople.this);
				builder.setTitle("Name");

				// Set up the input
				final EditText input = new EditText(getBaseContext());
				builder.setView(input);

				// Set up the buttons

				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						name = input.getText().toString();

                        //Make a new person from the text from the dialog
                        Person p = new Person();
                        p.setName(name);
                        p.setAvailability(new LinkedList<ScheduleObject>());

                        //Toast.makeText(getApplicationContext(), p.getName(), Toast.LENGTH_SHORT).show();

                        //Store the person to the database
                        db = new DataBase(getBaseContext());
                        db.addPerson(p);

                        //Reset the name & refresh the list
                        name = "";
                        populateSavedPeople();
					}
				});

				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				//Show the dialog
				builder.show();
			}
		});
	}


//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------
	public void initializeFields() {
		addPersonButton = (Button) findViewById(R.id.addPersonButton);
		savedPeopleList = (ListView) findViewById(R.id.savedPeopleList);
	}

	public void populateSavedPeople() {
        //if (savedPeople != null) {
            ArrayList<String> people = new ArrayList<>();
            DataBase db = new DataBase(getBaseContext());

            for (Person p : db.getAllPeople()) {
                people.add(p.getName());
            }

            ArrayAdapter<String> data = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, people);
            savedPeopleList.setAdapter(data);
        //}

	} //End public void populateSavedPeople()
}
