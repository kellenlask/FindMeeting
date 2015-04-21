/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Map;

public class AvailabilitySummary extends ActionBarActivity {

    private ArrayList<String> schedule;
    private Button newAvailabilityButton;
    private Button saveButton;
    private Button newObligationButton;
    private ListView AvailObligListView;
    private ArrayAdapter<String> scheduleAdapter;

    private Person person;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_availability);

        initializeFields();
	}

    private void initializeFields() {
        // Initialize list view
        AvailObligListView = (ListView) findViewById(R.id.AvailObligListView);

        // Initialize buttons
        newAvailabilityButton = (Button) findViewById(R.id.newAvailabilityButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        newObligationButton = (Button) findViewById(R.id.newObligationButton);

        // Initialize button listeners
        newAvailabilityButton.setOnClickListener(addAvailObligClickListener);
        saveButton.setOnClickListener(saveClickListener);
        newObligationButton.setOnClickListener(addAvailObligClickListener);

        //Pull the person object out
        person = (Person)getIntent().getSerializableExtra("PERSON");

        updateAvailability();

        registerForContextMenu(AvailObligListView);
    }

    private View.OnClickListener addAvailObligClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.newAvailabilityButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class);
                i.putExtra("PERSON", (Parcelable) person);
                i.putExtra("SCHEDULE_TYPE", "Availability");
                startActivity(i);
            }
            else if (v.getId() == R.id.newObligationButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class);
                i.putExtra("PERSON", (Parcelable) person);
                i.putExtra("SCHEDULE_TYPE", "Obligation");
                startActivity(i);
            }
        }
    };

    private View.OnClickListener saveClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private void updateAvailability()
    {
        if (person.getAvailability() != null) {
            schedule = new ArrayList<>();

            //TODO: loop through all items in the person availability linked list and get the days and times for the textview
            for (ScheduleObject s : person.getAvailability()) {
                schedule.add(s.toString(this));
            }
            scheduleAdapter = new AvailabilityArrayAdapter(this, schedule);

            AvailObligListView.setAdapter(scheduleAdapter);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, view, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        menu.setHeaderTitle("Availability / Obligation");

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

        }
        else if(selectedItem.equals("Delete"))
        {
            // Delete that availability / obligation from the availability array
            // TODO: Implement this method

            updateAvailability();
        }

        return false;
    }

}
