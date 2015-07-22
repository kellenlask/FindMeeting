package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bk.fm.Scheduling.Meeting;
import com.bk.fm.Scheduling.Person;
import com.bk.fm.Scheduling.ScheduleObject;

import java.util.ArrayList;
import java.util.Collections;

/*
This file contains the Java code to describe the behavior of the Saved People view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present a list of people currently in the database, and allow for the creation
and editing of people.
*/

public class SavedPeople extends Fragment {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
    private MainActivity parent;

    private Button addPersonButton;
    private ListView savedPeopleList;

    private ArrayList<String> peopleNames;
    private ArrayList<Person> people;
    private static Meeting meeting;
	private Database db;

	private String name; //For edit/new person dialog

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
		View view = inflater.inflate(R.layout.activity_saved_people, container, false);

		initializeFields();

		setActionHandlers();

		return view;

	} //End public View onCreateView(LayoutInflater, ViewGroup, Bundle)

	public void initializeFields() {
		//Pull the meeting object from sharedpreferences
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		addPersonButton = (Button) parent.findViewById(R.id.addPersonButton);
		savedPeopleList = (ListView) parent.findViewById(R.id.savedPeopleList);
		registerForContextMenu(savedPeopleList); //Allow for onLongClick events

		populateSavedPeople();
	} //End  public void initializeFields()

//----------------------------------------------------
//
//	Event Handlers
//
//----------------------------------------------------

	public void setActionHandlers() {

		//Add person to the meeting when shortClicked
		savedPeopleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				for (Person p: people) {
					if (p.getName() == peopleNames.get(position)) {
						if (meeting.getInvolvedPeople().contains(p)) {
							Toast.makeText(parent.getContext(), getString(R.string.person_already_added),Toast.LENGTH_SHORT).show();
						} else {
							meeting.getInvolvedPeople().add(p);
							Toast.makeText(parent.getContext(), getString(R.string.person_added),Toast.LENGTH_SHORT).show();
						}

					} //End outer if
				} //End for loop

				updateListView();
				putMeeting();

			}
		});//Add person to the meeting when shortClicked


		//Show dialog to create a new person when addButton clicked
		addPersonButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Show a dialog allowing for text input
				AlertDialog.Builder builder = new AlertDialog.Builder(parent);
				builder.setTitle("Name");

				// Set up the input
				final EditText input = new EditText(parent.getBaseContext());
				builder.setView(input);

				// Set up the buttons
				builder.setPositiveButton(getString(R.string.create), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						//Make a new person from the text from the dialog
						Person p = new Person(input.getText().toString());

						Database db = new Database(parent.getBaseContext());

						if (db.contains(p.getName())) { // Make sure there are no duplicate people in the db
							Toast.makeText(parent.getApplicationContext(), getString(R.string.person_exists),Toast.LENGTH_SHORT).show();
						} else {
							p.setAvailability(new ArrayList<ScheduleObject>());

							//Store the person to the database
							db = new Database(parent.getBaseContext());
							db.addPerson(p);

							//Reset the name & refresh the list
							populateSavedPeople();
						}
					}
				});

				builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

				//Show the dialog
				builder.show();
			}
		});//Show dialog to create a new person when addButton clicked
	} //End public void setActionHandlers()

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Person");
        name = (String) savedPeopleList.getItemAtPosition(info.position);

        menu.add(getString(R.string.edit));
        menu.add(getString(R.string.delete));
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        StringBuilder sb = new StringBuilder(item.getTitle());
        String selectedItem = sb.toString();

        if(selectedItem.equals(getString(R.string.edit)))
        {
            //Show a dialog allowing for text input
            AlertDialog.Builder builder = new AlertDialog.Builder(parent);
            builder.setTitle("Edit Name");

            // Set up the input
            final EditText input = new EditText(parent.getBaseContext());
            input.setText(peopleNames.get(info.position));
            builder.setView(input);

            // Set up the buttons
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    db = new Database(parent.getBaseContext());
                    // Use a foreach loop to go over the saved people array
                    for (Person p: people)
                    {
                        if (p.getName() == name)
                        {
                            if (peopleNames.contains(input.getText().toString())) { // Check for a duplicate (editing a person's name to match another person's name)
                                Toast.makeText(parent.getApplicationContext(), getString(R.string.person_exists),Toast.LENGTH_SHORT).show();
                            } else {
                                p.setName(input.getText().toString());
                                db.updatePerson(p);

                                peopleNames.remove(name);
                                peopleNames.add(input.getText().toString());
                            }
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
        else if(selectedItem.equals(getString(R.string.delete)))
        {
            db = new Database(parent.getBaseContext());
            db.deletePerson(peopleNames.get(info.position));

            peopleNames.remove(info.position);
            updateListView();
        }

        return false;
    }


//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------

    public void populateSavedPeople() {

        Database db = new Database(parent.getBaseContext());

        people = new ArrayList<>();
        peopleNames = new ArrayList<>();

        for (Person p : db.getAllPeople()) {
            people.add(p);
            peopleNames.add(p.getName());
        }

        updateListView();

    } //End public void populateSavedPeople()

    public void updateListView()
    {
        updatePeople();

        ArrayAdapter<String> data = new ArrayAdapter<>(parent, android.R.layout.simple_list_item_1, peopleNames);
        savedPeopleList.setAdapter(data);
    }

	public void updatePeople() {
		if(meeting.getInvolvedPeople() != null) {
			for (Person p : meeting.getInvolvedPeople()) {
				if (people.contains(p)) {
					people.remove(p);
					peopleNames.remove(p.getName());
				}
			}
		}

        Collections.sort(peopleNames);
	}

    public void onBackPressed()
    {
		putMeeting();
        Intent i = new Intent(parent.getBaseContext(), InvolvedPeople.class);
        startActivity(i);
    }

	//put the meeting into sharedpreferences
	public void putMeeting() {
		SharedPreferences sp = parent.getSharedPreferences("prefs", parent.getBaseContext().MODE_PRIVATE);
		SharedPreferences.Editor editor = sp.edit();
		editor.putString("MEETING", Meeting.serializeMeeting(meeting));
		editor.commit();
	}
} //End Class
