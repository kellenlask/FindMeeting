/*
This file contains the Java code to describe the behavior of the Obligation/Availability view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to accept the parameters for a new Obligation or Availability (the view should change
its type based on the button pressed in the Availability Summary Activity)
 */

package com.bk.fm.findmeeting;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.TreeMap;


public class NewObligAvail extends ActionBarActivity {

    private TreeMap<Day, Range> map;

    private CheckBox sunday;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;

    private Button saveButon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_oblig_avail);

        initializeFields();
        setCheckBoxActionHandlers();
	}

    private void initializeFields() {
        map = new TreeMap<>(); //To store the selected days and their time ranges

        // Initialize text boxes
        sunday = (CheckBox) findViewById(R.id.sunday);
        monday = (CheckBox) findViewById(R.id.monday);
        tuesday = (CheckBox) findViewById(R.id.tuesday);
        wednesday = (CheckBox) findViewById(R.id.wednesday);
        thursday = (CheckBox) findViewById(R.id.thursday);
        friday = (CheckBox) findViewById(R.id.friday);
        saturday = (CheckBox) findViewById(R.id.saturday);

        // Initialize save button
        saveButon = (Button) findViewById(R.id.saveButton);

    }

    public void setCheckBoxActionHandlers() {
        CompoundButton.OnCheckedChangeListener comboBoxListener = new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                updateMap(); //Update the internal map to reflect the checkBoxes
            }
        };

        sunday.setOnCheckedChangeListener(comboBoxListener);
        monday.setOnCheckedChangeListener(comboBoxListener);
        tuesday.setOnCheckedChangeListener(comboBoxListener);
        wednesday.setOnCheckedChangeListener(comboBoxListener);
        thursday.setOnCheckedChangeListener(comboBoxListener);
        friday.setOnCheckedChangeListener(comboBoxListener);
        saturday.setOnCheckedChangeListener(comboBoxListener);

    } //End public void setCheckBoxActionHandlers()



//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------



    //Update the map to reflect the check boxes
    public void updateMap() {
        boolean[] days = getSelectedDays();

        //Update the internal map to reflect the checkBoxes
        for (int i = 0; i < days.length; i++) {
            if (!days[i] && map.containsKey(Day.getDay(i))) {
                map.remove(Day.getDay(i));
            }
        }
    }

    //Make a boolean array representing the available days as selected by the user via checkboxes.
    public boolean[] getSelectedDays() {
        boolean[] days = new boolean[7];

        days[0] = sunday.isChecked();
        days[1] = monday.isChecked();
        days[2] = tuesday.isChecked();
        days[3] = wednesday.isChecked();
        days[4] = thursday.isChecked();
        days[5] = friday.isChecked();
        days[6] = saturday.isChecked();

        return days;
    }

    // TODO: Add time picker functionality to this activity (replace edit texts with buttons?)
    // TODO: Add the storeDay and addDay methods from MeetingParams to this activity (This didn't look right. I think we need to change the functionality of those methods to not rely on the combo box)
}
