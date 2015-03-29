/*
This file contains the Java code to describe the behavior of the Availability Summary view
The Activity's layout information is contained in the xml file under /res/layout/
This view is meant to summarize the availability of one Person.
 */

package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class AvailabilitySummary extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_availability);
	}
}
