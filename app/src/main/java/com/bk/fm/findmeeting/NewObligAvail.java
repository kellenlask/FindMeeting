/*
This file contains the Java code to describe the behavior of the Obligation/Availability view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to accept the parameters for a new Obligation or Availability (the view should change
its type based on the button pressed in the Availability Summary Activity)
 */

package com.bk.fm.findmeeting;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import java.util.TreeMap;


public class NewObligAvail extends ActionBarActivity {

    private TreeMap<Day, Interval> map;

    private CheckBox sunday;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;

    private Button saveButton;
    private Button startTime;
    private Button endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_oblig_avail);

        initializeFields();
        setTimeInputActionHandlers();
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
        saveButton = (Button) findViewById(R.id.saveButton);
        saveButton.setOnClickListener(saveButtonListener);

        //Time Inputs
        startTime = (Button) findViewById(R.id.startTime);
        endTime = (Button) findViewById(R.id.endTime);

    }

    private View.OnClickListener saveButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            updateMap();

            Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
            i.putExtra("AVAILABILITY_OBLIGATION", map);
            startActivity(i);
        }
    };

    public void setTimeInputActionHandlers() {
        //Show the time picker when the field is clicked.
        View.OnClickListener getTime = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePicker((Button) v);
            }
        };

        startTime.setOnClickListener(getTime);
        endTime.setOnClickListener(getTime);

    } //End public void setTimeInputActionHandlers()

    //Display a Time Picker, and change the Button argument's text to match the selected time.
    public void showTimePicker(final Button txtTime) {
        //Grab the current selected time
        int hours = Time.parseHours(txtTime.getText());
        int minutes = Time.parseMinutes(txtTime.getText());

        //Make the Time Picker with the current selected time
        TimePickerDialog tpd = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                String h = "" + hour;
                String m = "" + minute;

                //Ensure the output hour is two digits.
                if (h.length() == 1) {
                    h = "0" + h;
                }

                //Ensure the output minutes are two digits.
                if (m.length() == 1) {
                    m = "0" + m;
                }

                // Display Selected time in button
                txtTime.setText(h + ":" + m);
            }
        }, hours, minutes, true);
        tpd.show();
    } //End public void showTimePicker(final TextView)




//----------------------------------------------------
//
//	Logical Methods
//
//----------------------------------------------------

    //Update the map to reflect the check boxes
    public void updateMap() {
        try
        {
            Interval inter = getInterval();
            boolean[] days = getSelectedDays();

            //Update the internal map to reflect the checkBoxes
            for (int i = 0; i < days.length; i++) {
                if (days[i]) {
                    map.put(Day.getDay(i), inter);
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Invalid Interval", Toast.LENGTH_SHORT).show();
        } //End Try-Catch
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

    //Get an interval of the start and end times
    public Interval getInterval() throws Exception {
        Time start = new Time(startTime.getText());
        Time end = new Time(endTime.getText());

        return new Interval(start, end);

    } //End public Interval getInterval()

    // TODO: Create onclick listener for save button. There call the adapted storeDay method to save the day and time interval

}
