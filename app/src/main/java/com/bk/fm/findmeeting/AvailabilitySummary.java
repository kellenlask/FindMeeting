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
import android.widget.Button;


public class AvailabilitySummary extends ActionBarActivity {

    Button newAvailabilityButton;
    Button saveButton;
    Button newObligationButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_availability);

        initializeFields();
	}

    private void initializeFields() {
        // Initialize buttons
        newAvailabilityButton = (Button) findViewById(R.id.newAvailabilityButton);
        saveButton = (Button) findViewById(R.id.saveButton);
        newObligationButton = (Button) findViewById(R.id.newObligationButton);

        // Initialize button listeners
        newAvailabilityButton.setOnClickListener(addAvailObligClickListener);
        //saveButton.setOnClickListener();
        newObligationButton.setOnClickListener(addAvailObligClickListener);
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
}
