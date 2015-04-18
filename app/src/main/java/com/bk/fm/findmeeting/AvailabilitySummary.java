/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
 */

package com.bk.fm.findmeeting;

import android.content.Intent;
import android.graphics.Color;
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

    private ArrayList<String> schedule;
    private Button newAvailabilityButton;
    private Button saveButton;
    private Button newObligationButton;
    private ListView AvailObligListView;
    //private Map<Day, Interval> availabilitiesObligations;
    private ArrayList<Map> availabilitiesObligationsArray;
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

        availabilitiesObligationsArray = (ArrayList)getIntent().getSerializableExtra("AVAILABILITY_ARRAY");
        updateAvailability();
    }

    private View.OnClickListener addAvailObligClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (availabilitiesObligationsArray == null)
            {
                availabilitiesObligationsArray = new ArrayList<>();
                //availabilitiesObligationsArray.add(new TreeMap<Day, Interval>());
                //availabilitiesObligationsArray.add(new TreeMap<Day, Interval>());
            }

            if (v.getId() == R.id.newAvailabilityButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class);
                i.putExtra("SCHEDULE_TYPE", "Availability");
                i.putExtra("AVAILABILITY_ARRAY", availabilitiesObligationsArray);
                startActivity(i);
            }
            else if (v.getId() == R.id.newObligationButton)
            {
                Intent i = new Intent(getBaseContext(), NewObligAvail.class);
                i.putExtra("AVAILABILITY_ARRAY", availabilitiesObligationsArray);
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
        if (availabilitiesObligationsArray != null)
        {
            schedule = new ArrayList<>();

            int i = 1;
            for (Map<Day, Interval> m: availabilitiesObligationsArray) {
                for (Map.Entry<Day, Interval> obligation : m.entrySet()) {

                    String day = obligation.getKey().toString(this);
                    String timeInterval = obligation.getValue().toString();
                    if (i == 1)
                    {
                        schedule.add("Avail " + day + ": " + timeInterval);
                    }
                    else
                    {
                        schedule.add("Oblig " + day + ": " + timeInterval);
                    }
                }
                i++;
            }

            // TODO: add availabilities first, then call the adapter, then color them in a loop, then repeat for the obligations
            //scheduleAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, schedule);
            scheduleAdapter = new AvailabilityArrayAdapter(this, schedule);

            AvailObligListView.setAdapter(scheduleAdapter);
        }
    }
}
