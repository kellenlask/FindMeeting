/*
This file contains the Java code to describe the behavior of the Saved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present a list of people currently in the database, and allow for the creation
and editing of people.
 */

package com.bk.fm.findmeeting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
    ArrayList<String> peopleNames;
    ArrayList<Person> allSavedPeople;
    ArrayList<Person> addedPeople;

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

        savedPeopleList.setOnItemClickListener(listItemClicked);

		addButtonEventHandler();
	}

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------

    private OnItemClickListener listItemClicked = new OnItemClickListener() {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            for (Person p: allSavedPeople)
            {
                if (p.getName() == peopleNames.get(position))
                {
                    addedPeople.add(p);
                }
            }

			//TODO: Either remove selected person from the list, or start a new intent back to invloved people

        }
    };

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Person");

        menu.add("Edit");
        menu.add("Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        StringBuilder sb = new StringBuilder(item.getTitle());
        String selectedItem = sb.toString();

        if(selectedItem.equals("Edit"))
        {
            //Show a dialog allowing for text input
            AlertDialog.Builder builder = new AlertDialog.Builder(SavedPeople.this);
            builder.setTitle("Edit Name");

            // Set up the input
            final EditText input = new EditText(getBaseContext());
            input.setText(peopleNames.get(info.position));
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db = new DataBase(getBaseContext());
                    // Use a foreach loop to go over the saved people array
                    for (Person p: allSavedPeople)
                    {
                        if (p.getName() == peopleNames.get(info.position))
                        {
                            p.setName(input.getText().toString());
                            db.updatePerson(p);

                            peopleNames.remove(info.position);  // Wrong
                            peopleNames.add(input.getText().toString());
                        }
                    }

                    updateListView();
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
        else if(selectedItem.equals("Delete"))
        {
            db = new DataBase(getBaseContext());
            db.deletePerson(peopleNames.get(info.position));

            peopleNames.remove(info.position);
            updateListView();
        }

        return false;
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
                        Person p = new Person(name);
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
        addedPeople = new ArrayList<>();
	}

	public void populateSavedPeople() {

            DataBase db = new DataBase(getBaseContext());

            allSavedPeople = new ArrayList<>();
            peopleNames = new ArrayList<>();

            for (Person p : db.getAllPeople()) {
                allSavedPeople.add(p);
            }

            updateListView();

	} //End public void populateSavedPeople()

    public void updateListView()
    {

        peopleNames.clear();

        for (Person p : allSavedPeople) {
            peopleNames.add(p.getName());
        }

        Collections.sort(peopleNames);
        ArrayAdapter<String> data = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, peopleNames);
        savedPeopleList.setAdapter(data);
    }

    public void onBackPressed()
    {
        Intent i = new Intent(getBaseContext(), InvolvedPeople.class);
        i.putExtra("ADDED_PEOPLE", addedPeople);
        startActivity(i);
    }
}
