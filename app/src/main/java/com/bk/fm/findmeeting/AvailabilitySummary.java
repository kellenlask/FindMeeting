/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;


public class AvailabilitySummary extends ActionBarActivity {

    ArrayList<String> schedule;
    Button newAvailabilityButton;
    Button saveButton;
    Button newObligationButton;
    ListView AvailObligListView;
    private Map<Day, Interval> availabilitiesObligations;
    private ArrayAdapter<String> scheduleAdapter;

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

        availabilitiesObligations = (Map<Day, Interval>)getIntent().getSerializableExtra("AVAILABILITY_OBLIGATION");
        updateAvailability();
    }

    private View.OnClickListener addAvailObligClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.newAvailabilityButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class); // These cases are broken up so a parameter could be added to the intent if needed
                startActivity(i);
            }
            else if (v.getId() == R.id.newObligationButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class); // These cases are broken up so a parameter could be added to the intent if needed
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
        if (availabilitiesObligations != null)
        {
            schedule = new ArrayList<>();

            for (Map.Entry<Day, Interval> obligation : availabilitiesObligations.entrySet()) {
                String day = obligation.getKey().toString(this);
                String timeInterval = obligation.getValue().toString();
                schedule.add(day + ": " + timeInterval);
            }

            scheduleAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, schedule);
            AvailObligListView.setAdapter(scheduleAdapter);
        }
    }

}
