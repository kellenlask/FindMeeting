/*
This file contains the Java code to describe the days of the week (an enumeration)
 */

package com.bk.fm.HumanTime;

/**
 * Created by Kellen on 3/4/2015.
 */

import android.content.Context;

import com.bk.fm.FindMeeting.R;

import java.io.Serializable;	//Serializable in order to fully serialize objects that use Day.

public enum Day implements Serializable {
	//Each day has an index for convenient mapping between arrays and Days
	SUNDAY (0),
	MONDAY (1),
	TUESDAY (2),
	WEDNESDAY (3),
	THURSDAY (4),
	FRIDAY (5),
	SATURDAY (6);

	private final int index;

	Day(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	} //End public int getIndex()

	//Return the appropriate String resource from /res/values/strings.xml which requires the
	//application's context (for language purposes)
	public String toString(Context c) {
		switch(index) {
			case 0:
				return c.getString(R.string.sunday);
			case 1:
				return c.getString(R.string.monday);
			case 2:
				return c.getString(R.string.tuesday);
			case 3:
				return c.getString(R.string.wednesday);
			case 4:
				return c.getString(R.string.thursday);
			case 5:
				return c.getString(R.string.friday);
			default:
				return c.getString(R.string.saturday);
		}
	} //End public String toString()

	//Return the appropriate Day given its index.
	public static Day getDay(int index) {
		switch(index) {
			case 0:
				return Day.SUNDAY;
			case 1:
				return Day.MONDAY;
			case 2:
				return Day.TUESDAY;
			case 3:
				return Day.WEDNESDAY;
			case 4:
				return Day.THURSDAY;
			case 5:
				return Day.FRIDAY;
			default:
				return Day.SATURDAY;
		}

	} //End public Day getDay(int)
} //End public enum Day