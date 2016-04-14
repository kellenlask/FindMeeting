package com.bk.fm.FindMeeting;

/**
 * Created by Kellen on 3/15/2015.
 */

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bk.fm.HumanTime.Day;
import com.bk.fm.HumanTime.Interval;
import com.bk.fm.HumanTime.Range;
import com.bk.fm.HumanTime.Time;
import com.bk.fm.Scheduling.Person;
import com.bk.fm.Scheduling.ScheduleObject;

/*
This file contains the Java code to describe the behavior of the Obligation/Availability view
The Activity's layout information is contained in the xml file under /res/layout/
The intent is to accept the parameters for a new Obligation or Availability (the view should change
its type based on the button pressed in the Availability Summary Activity)
*/

public class NewObligAvail extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
    private CheckBox sunday;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;

	private ToggleButton toggle;
    private Button saveButton;
    private Button startTime;
    private Button endTime;

    private Person person;
    private String activityType;

//----------------------------------------------------
//
//	onCreate()
//
//----------------------------------------------------

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_oblig_avail);

        initializeFields();

		setActionHandlers();
	}

//----------------------------------------------------
//
//	Action Handlers
//
//----------------------------------------------------
	public void setActionHandlers() {
		//Save Button
		saveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Make sure at least one day was selected
				boolean noneChecked = true;
				for(boolean b : getSelectedDays()) {
					if(b) {
						noneChecked = false;
						break;
					}
				} //End for-loop

				//If one was selected, store the ScheduleObject and get the hell out.
				if (!noneChecked) {

					try {
						addScheduleObject(); //Throws an error with an invalid time range (e.g. 11:00 - 11:00)

						Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
						i.putExtra("PERSON", (Parcelable) person);
						startActivity(i);
					} catch (Exception e) {
						Toast.makeText(getApplicationContext(), getString(R.string.invalidRange), Toast.LENGTH_SHORT).show();
					}

				} else {
					Toast.makeText(getApplicationContext(), getString(R.string.selectDay), Toast.LENGTH_SHORT).show();
				}
			}
		});

		//Time Input Dialog
		View.OnClickListener getTime = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showTimePicker((Button) v);
			}
		};

		startTime.setOnClickListener(getTime);
		endTime.setOnClickListener(getTime);

	} //End public void setActionHandlers()

    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
        i.putExtra("PERSON", (Parcelable) person);
        startActivity(i);
    }

//----------------------------------------------------
//
//	GUI Methods
//
//----------------------------------------------------

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
//	Mutating Methods
//
//----------------------------------------------------

    public void initializeFields() {
		//Grab the Availability/Obligation toggle
		toggle = (ToggleButton) findViewById(R.id.toggleButton);

        // Initialize text boxes
        sunday = (CheckBox) findViewById(R.id.sunday);
        monday = (CheckBox) findViewById(R.id.monday);
        tuesday = (CheckBox) findViewById(R.id.tuesday);
        wednesday = (CheckBox) findViewById(R.id.wednesday);
        thursday = (CheckBox) findViewById(R.id.thursday);
        friday = (CheckBox) findViewById(R.id.friday);
        saturday = (CheckBox) findViewById(R.id.saturday);

        // Grab the buttons
        saveButton = (Button) findViewById(R.id.saveButton);
        startTime = (Button) findViewById(R.id.startTime);
        endTime = (Button) findViewById(R.id.endTime);

		//Pull the person object
        person = (Person)getIntent().getSerializableExtra("PERSON");

        // Check if this activity is an edit. If so, preload the checkboxes and time buttons
		activityType = (String)getIntent().getSerializableExtra("ACTIVITY_TYPE");
        if (activityType.contains("Edit"))
        {
            // Pull the index of the ScheduleObject
            int scheduleObjectIndex = (int) getIntent().getSerializableExtra("SCHEDULE_OBJECT_INDEX");

			//Populate the activity from the existing object
            loadEditActivity(scheduleObjectIndex);
        }
    } //End  private void initializeFields()

    //Add the ScheduleObject to the person's availability ArrayList
    public void addScheduleObject() throws IllegalArgumentException {

        boolean[] days = getSelectedDays();
		Interval interval = getInterval();

        for (int i = 0; i < days.length; i++) {

            if (days[i] == true) {
				Range range = new Range(interval, Day.getDay(i));

				ScheduleObject schedOb = new ScheduleObject(toggle.isChecked(), range);
				person.addScheduleObject(schedOb);

            } //End outer if
        } //End for loop

    } //End public void addScheduleObject()

    public void loadEditActivity(int scheduleObjectIndex) {

        ScheduleObject schedObj = person.getAvailability().get(scheduleObjectIndex);

		//Set the ScheduleObject Type
		toggle.setChecked(schedObj.isObligation());

        // Set the checkbox
        int day = schedObj.getDay().getIndex();

		switch(day) {
			case 0:
				sunday.setChecked(true);
				break;

			case 1:
				monday.setChecked(true);
				break;

			case 2:
				tuesday.setChecked(true);
				break;

			case 3:
				wednesday.setChecked(true);
				break;

			case 4:
				thursday.setChecked(true);
				break;

			case 5:
				friday.setChecked(true);
				break;

			case 6:
				saturday.setChecked(true);
				break;
		} //End Switch

        // Set time interval buttons
        String start = schedObj.getStartTime().toString();
        String end = schedObj.getStopTime().toString();

        startTime.setText(start);
        endTime.setText(end);

        // Delete the schedule object
        person.getAvailability().remove(scheduleObjectIndex);

    } //End public void loadEditActivity(int)

//----------------------------------------------------
//
//	Accessing Methods
//
//----------------------------------------------------

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

    } //End public boolean[] getSelectedDays()

    //Get an interval of the start and end times
    public Interval getInterval() throws IllegalArgumentException {
        Time start = new Time(startTime.getText());
        Time end = new Time(endTime.getText());

        return new Interval(start, end);

    } //End public Interval getInterval()


} //End Class
