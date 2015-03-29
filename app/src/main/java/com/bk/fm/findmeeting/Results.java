/*
This file contains the Java code to describe the behavior of the Results view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to present the user with a list of time ranges ordered first by the number of people
available for that time range, then by earliness in the week.
 */

package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class Results extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_results);
	}
}
