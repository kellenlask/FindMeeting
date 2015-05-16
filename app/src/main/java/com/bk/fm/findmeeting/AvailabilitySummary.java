/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AvailabilitySummary extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
    private ListView AvailObligListView;

    private Button newAvailabilityButton;
    private Button doneButton;
    private Button newObligationButton;

	private Meeting meeting;
    private ArrayList<String> schedule;
    private Person person;
	private ArrayAdapter<String> scheduleAdapter;

//----------------------------------------------------
//
//	onCreate()
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_availability);

        initializeFields();

		addActionHandlers();
	}

//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------
	@Override
	//Define the context menu items.
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu, view, menuInfo);

		menu.setHeaderTitle("Schedule Object");

		menu.add(getString(R.string.edit));
		menu.add(getString(R.string.delete));
	}


//----------------------------------------------------
//
//	Action Handlers
//
//----------------------------------------------------
	@Override
	//When one of the context menu items is pressed, do this:
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		String selectedItem = item.getTitle().toString();

		//Grab the selected element
		ScheduleObject schObj = null;
		for(int i = 0; i < person.getAvailability().size(); i++) {
			schObj = person.getAvailability().get(i);

			if (scheduleAdapter.getItem(info.position).equals(schObj.toString(this))) {
				break;

			}

		} //End for-loop

		//Handle the selected element accordingly
		if(selectedItem.equals(getString(R.string.edit))) { //"Edit" Pressed
			Intent i = new Intent(getBaseContext(), NewObligAvail.class);
			i.putExtra("PERSON", (Parcelable) person);
			i.putExtra("SCHEDULE_OBJECT_INDEX", person.getAvailability().indexOf(schObj));

			if (schObj.isObligation()) {
				i.putExtra("ACTIVITY_TYPE", "Edit Obligation");

			} else {
				i.putExtra("ACTIVITY_TYPE", "Edit Availability");
			}

			startActivity(i);

		} else {  //"Delete" Pressed
			person.getAvailability().remove(schObj);

			updateAvailability();

		}

		return false;

	} //End public boolean onContextItemSelected(MenuItem)

	public void addActionHandlers() {

		//Save the person's availability and go back to InvolvedPeople
		View.OnClickListener doneClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Simplify the person's availability down to simplest terms
				person.reduceAvailability();

				//Update the database entry
				DataBase db = new DataBase(getBaseContext());
				db.updatePersonAvail(person);

				//Update the person in the Meeting Object
				addPersonToMeeting();

				//Put the Meeting into SharedPreferences
				SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
				SharedPreferences.Editor editor = sp.edit();
				editor.putString("MEETING", Meeting.serializeMeeting(meeting));
				editor.commit();

				//Move on to the InvolvedPeople Activity
				Intent i = new Intent(getBaseContext(), InvolvedPeople.class);
				startActivity(i);
			}
		};

		//Setup the done button
		doneButton.setOnClickListener(doneClickListener);

		//Go to the NewObligAvail Activity to create a new obligation/availability
		View.OnClickListener addAvailObligClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				if (v.getId() == R.id.newAvailabilityButton)
				{
					Intent i = new Intent(getBaseContext(), NewObligAvail.class);
					i.putExtra("PERSON", (Parcelable) person);
					i.putExtra("ACTIVITY_TYPE", "New Availability");
					startActivity(i);
				}
				else if (v.getId() == R.id.newObligationButton)
				{
					Intent i = new Intent(getBaseContext(), NewObligAvail.class);
					i.putExtra("PERSON", (Parcelable) person);
					i.putExtra("ACTIVITY_TYPE", "New Obligation");
					startActivity(i);
				}
			}
		};

		//Setup the new Availability and Obligation buttons
		newAvailabilityButton.setOnClickListener(addAvailObligClickListener);
		newObligationButton.setOnClickListener(addAvailObligClickListener);

		//Register the ListView's items for opening the context menu
		registerForContextMenu(AvailObligListView);

	} //End public void addActionHandlers()

	@Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), InvolvedPeople.class);
        startActivity(i);
    }
//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------

    private void initializeFields() {
		//Pull meeting from sharedpreferences
		SharedPreferences sp = getSharedPreferences("prefs", getBaseContext().MODE_PRIVATE);
		meeting = Meeting.deserializeMeeting(sp.getString("MEETING", ""));

		//Pull the Person from the Intent
		person = (Person)getIntent().getSerializableExtra("PERSON");

        // Initialize list view
        AvailObligListView = (ListView) findViewById(R.id.AvailObligListView);
		updateAvailability();

		//Initialize Title Text
		TextView title = (TextView) findViewById(R.id.titleText);
		title.setText(title.getText().toString() + " " + person.getName()); //Does use a String Resource

        // Initialize buttons
        newAvailabilityButton = (Button) findViewById(R.id.newAvailabilityButton);
        doneButton = (Button) findViewById(R.id.saveButton);
        newObligationButton = (Button) findViewById(R.id.newObligationButton);

    } //End private void initializeFields()

	//
    private void updateAvailability() {
        if (person.getAvailability() != null) {
            schedule = new ArrayList<>();

            for (ScheduleObject s : person.getAvailability()) {
                schedule.add(s.toString(this));
            }

			scheduleAdapter = new AvailabilityArrayAdapter(this, schedule);

            AvailObligListView.setAdapter(scheduleAdapter);
        }

    } //End private void updateAvailability()

	public void addPersonToMeeting() {
		for(Person p : meeting.getInvolvedPeople()) {
			if(person.equals(p)) {
				p.setAvailability(person.getAvailability());
			}
		}

	} //End public void addPersonToMeeting()

} //End Class
