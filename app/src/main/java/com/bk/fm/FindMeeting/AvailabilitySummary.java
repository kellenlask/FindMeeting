package com.bk.fm.FindMeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

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
import android.widget.Toast;

import com.bk.fm.Scheduling.Meeting;
import com.bk.fm.Scheduling.Person;
import com.bk.fm.Scheduling.ScheduleObject;

import java.util.ArrayList;

/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
*/

public class AvailabilitySummary extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	//GUI Elements
    private ListView AvailObligListView;
    private Button newItemButton;
    private Button doneButton;

	//Logical Elements
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

		menu.setHeaderTitle(getString(R.string.changeItem));

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
		final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
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
			i.putExtra("ACTIVITY_TYPE", "Edit");
			startActivity(i);

		} else {  //"Delete" Pressed
			person.getAvailability().remove(schObj);

			updateAvailability();

		}

		return false;

	} //End public boolean onContextItemSelected(MenuItem)

	public void addActionHandlers() {

		//Done Button
		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Simplify the person's availability down to simplest terms
				person.reduceAvailability();

				//Update the database entry
				Database db = new Database(getBaseContext());
				int updates = db.updatePersonAvail(person);
				Toast.makeText(getApplicationContext(), "Updated: " + updates, Toast.LENGTH_SHORT).show();

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
		});

		newItemButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
					Intent i = new Intent(getBaseContext(), NewObligAvail.class);
					i.putExtra("PERSON", (Parcelable) person);
					i.putExtra("ACTIVITY_TYPE", ""); //(Not "edit")
					startActivity(i);
			}
		});

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
		title.setText(person.getName());

        // Initialize buttons
		newItemButton = (Button) findViewById(R.id.addButton);
        doneButton = (Button) findViewById(R.id.saveButton);


    } //End private void initializeFields()

    private void updateAvailability() {
            schedule = new ArrayList<>();

            for (ScheduleObject s : person.getAvailability()) {
                schedule.add(s.toString(this));
            }

			scheduleAdapter = new AvailabilityArrayAdapter(this, schedule);

            AvailObligListView.setAdapter(scheduleAdapter);

    } //End private void updateAvailability()

	public void addPersonToMeeting() {
		for(Person p : meeting.getInvolvedPeople()) {
			if(person.equals(p)) {
				p.setAvailability(person.getAvailability());
			}
		}

	} //End public void addPersonToMeeting()

} //End Class
