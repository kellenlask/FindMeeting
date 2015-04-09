/*
This file contains the Java code to describe the behavior of the Saved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present a list of people currently in the database, and allow for the creation
and editing of people.
 */

package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;


public class SavedPeople extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private Button addPersonButton;
	private ListView savedPeopleList;
	private String name;
    private DataBase db;
    ArrayList<String> people;

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

        //savedPeopleList.setOnItemLongClickListener(deletePersonClickListener);

		addButtonEventHandler();

	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------

	//TODO: Add on short/long click events for the savedPeopleList
    /*
    private OnItemLongClickListener deletePersonClickListener = new OnItemLongClickListener()
    {
        @Override
        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int index, long arg3) {
            db = new DataBase(getBaseContext());
            db.deletePerson(savedPeopleList.getItemAtPosition(index).toString());

            populateSavedPeople();

            return true;
        }
    };
    */


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        StringBuilder sb = new StringBuilder(item.getTitle());
        String selectedItem = sb.toString();

        if(selectedItem.equals("Edit"))
        {
            // Need to set the textbox text to the name selected

            db = new DataBase(getBaseContext());
            db.updatePersonName(people.get(info.position), "Bob"); //TODO: Remove the hardcoded "Bob" here and three lines below. Replace with the text from a textfield popup.

            people.remove(info.position);
            people.add("Bob");

            updateListView();
        }
        else if(selectedItem.equals("Delete"))
        {
            db = new DataBase(getBaseContext());
            db.deletePerson(people.get(info.position));

            people.remove(info.position);
            updateListView();
        }

        return false;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        //if(view.getId() == R.id.linksList) {
        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Person");

        menu.add("Edit");
        menu.add("Delete");
        //}
    }

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
        registerForContextMenu(savedPeopleList);
	}

	public void populateSavedPeople() {
        //if (savedPeople != null) {

            DataBase db = new DataBase(getBaseContext());

            people = new ArrayList<>();

            for (Person p : db.getAllPeople()) {
                people.add(p.getName());
            }
            
            updateListView();
        //}

	} //End public void populateSavedPeople()

    public void updateListView() {
        Collections.sort(people);
        ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, people);
        savedPeopleList.setAdapter(data);
    }
}
