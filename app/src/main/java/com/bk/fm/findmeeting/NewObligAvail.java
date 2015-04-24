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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.LinkedList;


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

    private Button saveButton;
    private Button startTime;
    private Button endTime;

    private Person person;
    private String activityType;
    private int scheduleObjectIndex;

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
        setTimeInputActionHandlers();
	}

//----------------------------------------------------
//
//	Action Handlers
//
//----------------------------------------------------
	private View.OnClickListener saveButtonListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {

			addScheduleObject();

			Intent i = new Intent(getBaseContext(), AvailabilitySummary.class);
			i.putExtra("PERSON", (Parcelable) person);
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
//	Logical Methods
//
//----------------------------------------------------

    public void initializeFields() {

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

        // Time Inputs
        startTime = (Button) findViewById(R.id.startTime);
        endTime = (Button) findViewById(R.id.endTime);

        // Initialize activity type and person object
        activityType = (String)getIntent().getSerializableExtra("ACTIVITY_TYPE");
        person = (Person)getIntent().getSerializableExtra("PERSON");

        // Set title
        TextView title = (TextView) findViewById(R.id.titleTextView);
        title.setText(activityType);

        // Check if this activity is an edit. If so, preload the checkboxes and time buttons
        if (activityType.contains("Edit"))
        {
            // Pull the index of the schedule object in the linked list
            scheduleObjectIndex = (int)getIntent().getSerializableExtra("SCHEDULE_OBJECT_INDEX");

            loadEditActivity();
        }
    } //End  private void initializeFields()

    //Update the map to reflect the check boxes
    public void addScheduleObject() {

        boolean[] days = getSelectedDays();
        Range range = null;
        Interval interval = null;

        try
        {
            interval = getInterval();
        }
        catch (Exception e)
        {
            Toast.makeText(getApplicationContext(), "Invalid Interval", Toast.LENGTH_SHORT).show();
        }

        for (int i = 0; i < days.length; i++) {

            if (days[i] == true) {
                switch (i) {
                    case 0: range = new Range(interval, Day.SUNDAY);
                            break;
                    case 1: range = new Range(interval, Day.MONDAY);
                            break;
                    case 2: range = new Range(interval, Day.TUESDAY);
                            break;
                    case 3: range = new Range(interval, Day.WEDNESDAY);
                            break;
                    case 4: range = new Range(interval, Day.THURSDAY);
                            break;
                    case 5: range = new Range(interval, Day.FRIDAY);
                            break;
                    case 6: range = new Range(interval, Day.SATURDAY);
                            break;
                }

                if (activityType.contains("Availability"))
                {
                    if (person.getAvailability() == null)
                    {
                        ScheduleObject schedOb = new ScheduleObject(false, range);
                        person.setAvailability(new LinkedList<ScheduleObject>());
                        person.addScheduleObject(schedOb);
                    }
                    else
                    {
                        ScheduleObject schedOb = new ScheduleObject(false, range);
                        person.addScheduleObject(schedOb);
                    }
                }
                else if (activityType.contains("Obligation"))
                {
                    if (person.getAvailability() == null)
                    {
                        ScheduleObject schedOb = new ScheduleObject(true, range);
                        person.setAvailability(new LinkedList<ScheduleObject>());
                        person.addScheduleObject(schedOb);
                    }
                    else
                    {
                        ScheduleObject schedOb = new ScheduleObject(true, range);
                        person.addScheduleObject(schedOb);
                    }
                }
            }
        }

    } //End public void addScheduleObject()

    public void loadEditActivity() {

        ScheduleObject schedObj = person.getAvailability().get(scheduleObjectIndex);

        // Set the checkbox
        String day = schedObj.getDay().toString(this);

        if (day.equals("Sunday"))
        {
            sunday.setChecked(true);
        }
        else if (day.equals("Monday"))
        {
            monday.setChecked(true);
        }
        else if (day.equals("Tuesday"))
        {
            tuesday.setChecked(true);
        }
        else if (day.equals("Wednesday"))
        {
            wednesday.setChecked(true);
        }
        else if (day.equals("Thursday"))
        {
            thursday.setChecked(true);
        }
        else if (day.equals("Friday"))
        {
            friday.setChecked(true);
        }
        else if (day.equals("Saturday"))
        {
            saturday.setChecked(true);
        }

        // Set time interval buttons
        String start = schedObj.getStartTime().toString();
        String end = schedObj.getStopTime().toString();

        startTime.setText(start);
        endTime.setText(end);

        // Delete the schedule object
        person.getAvailability().remove(scheduleObjectIndex);
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

    } //End public boolean[] getSelectedDays()

    //Get an interval of the start and end times
    public Interval getInterval() throws Exception {
        Time start = new Time(startTime.getText());
        Time end = new Time(endTime.getText());

        return new Interval(start, end);

    } //End public Interval getInterval()


}
