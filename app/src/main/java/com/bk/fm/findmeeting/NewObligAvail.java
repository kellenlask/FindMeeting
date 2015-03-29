/*
This file contains the Java code to describe the behavior of the Obligation/Availability view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to accept the parameters for a new Obligation or Availability (the view should change
its type based on the button pressed in the Availability Summary Activity)
 */

package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class NewObligAvail extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_oblig_avail);
	}
}
