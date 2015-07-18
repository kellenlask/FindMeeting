/*
This file contains the Java code for a Time object class definition. This object is supposed to
represent a slice of time, such as 8:46am. It is capable of accepting both 24h and 12h time.
 */

package com.bk.fm.HumanTime;

import java.io.Serializable;

/**
 * Created by Kellen on 3/9/2015.
 */
public class Time implements Comparable<Time>, Cloneable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private int hours;
	private int minutes;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Time(int hours, int minutes) throws IllegalArgumentException {
		setHours(hours);
		setMinutes(minutes);

	} //End Constructor

	public Time(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setHours(hours, amPM);
		setMinutes(minutes);

	} //End Constructor

	public Time(int minutes) throws Exception {
		this.minutes = 0;
		this.hours = 0;
		addMinutes(minutes);
	} //End Constructor

	public Time(CharSequence t) {

			hours = parseHours(t);
			minutes = parseMinutes(t);

	} //End Constructor

//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------
	@Override
	public Time clone() {
		return new Time(hours, minutes);
	}

	//Earlier < Later: 1:00 < 3:00
	public int compareTo(Time t) {
		return getTimeInMinutes() - t.getTimeInMinutes();
	}

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------
	public int getHours() {
		return hours;
	}

	public int getMinutes() {
		return minutes;
	}

	public char getAMPM(){
		if(hours < 12) {
			return 'p';
		} else {
			return 'a';
		}
	}

	public int getAMPMHours() {
		if(hours == 0) {
			return 12;
		}

		if(hours <= 12) {
			return hours;

		}  else {
			return hours - 12;
		}
	}

	public int getTimeInMinutes() {
		return (hours * 60) + minutes;
	}

	public boolean equals(Time t) {
		return getTimeInMinutes() == t.getTimeInMinutes();
	}

	public String toString() {
		String h = hours + "";
		String m = minutes + "";

		if(h.length() == 1) {
			h = "0" + h;
		}

		if(m.length() == 1) {
			m = "0" + m;
		}

		return h + ":" + m;
	}

	public String toAMPMString() {
		String h = "" + getAMPMHours();
		String m = "" + minutes;

		if(h.length() == 1) {
			h = "0" + h;
		}

		if(m.length() == 1) {
			m = "0" + m;
		}

		return h + ":" + m + getAMPM();
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setHours(int hours) throws IllegalArgumentException {
		if(hours >= 0 && hours < 24) {
			this.hours = hours;

		}  else {
			throw new IllegalArgumentException("\"" + hours + "\" is an illegal number of hours.");
		}
	}

	public void setMinutes(int minutes) throws IllegalArgumentException {
		if(minutes >= 0 && minutes < 60) {
			this.minutes = minutes;
		} else {
			throw new IllegalArgumentException("\"" + minutes + "\" is an illegal number of minutes.");
		}
	}

	public void setHours(int hours, char amPM) throws IllegalArgumentException {
		if(hours > 12 || hours < 1) {
			throw new IllegalArgumentException("\"" + hours + "\" is an illegal number of AM/PM hours.");
		}

		if(amPM == 'a' && hours == 12) {
			this.hours = 0;

		} else if(amPM == 'a') {
			this.hours = hours;

		} else if(amPM == 'p' && hours == 12) {
			this.hours = 12;

		} else if(amPM == 'p') {
			this.hours = hours + 12;

		}

		else {
			throw new IllegalArgumentException("\"" + amPM + "\" does not constitute an appropriate time delineation. Accepted: 'a'(AM) or 'p'(PM)");
		}
	}

	public void setAmPm(char amPM) {
		if(amPM == 'a' && hours >= 12) {
			hours -= 12;
		} else if(amPM == 'p' && hours < 12) {
			hours += 12;
		} else {
			throw new IllegalArgumentException("\"" + amPM + "\" does not constitute an appropriate time delineation. Accepted: 'a'(AM) or 'p'(PM)");
		}
	}

	public void addHours(int hours) throws Exception {
		if(this.hours + hours <= 23){
			this.hours += hours;
		} else {
			throw new Exception("Adding " + hours + " hours to " + this.hours + " hours makes for an illegal time. Note: this may have been caused by adding minutes.");
		}
	}

	public void addMinutes(int minutes) throws Exception {
		if(this.minutes + minutes >= 60) {
			addHours((this.minutes + minutes) / 60);
			addMinutes((this.minutes + minutes) % 60);

		} else {
			this.minutes += minutes;
		}
	}

//----------------------------------------------------
//
//	Static Methods
//
//----------------------------------------------------

//Tests whether or not a given combination of hours and minutes is a valid time
	public static boolean isValidTime(int hours, int minutes) {
		boolean valid = true;

		if(hours < 0 || hours >= 24) {
			valid = false;
		}

		if(minutes < 0 || minutes >= 60) {
			valid = false;
		}

		return valid;
	}

	public static boolean isValidAMPMTime(int hours, int minutes) {
		boolean valid = true;

		if(hours < 1 || hours > 12) {
			valid = false;
		}

		if(minutes < 0 || minutes >= 60) {
			valid = false;
		}

		return valid;
	}

//Parse hours or minutes from a String/CharSequence
	public static int parseHours(String s) {
		try {
			return Integer.parseInt(s.substring(0, 2));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseHours(String)

	public static int parseMinutes(String s) {
		try {
			return Integer.parseInt(s.substring(3));

		} catch(Exception e) {
			return 0;
		}

	} //End public int parseMinutes(String)

	public static int parseHours(CharSequence seq) {
		try {
			StringBuilder str = new StringBuilder(seq);
			String s = str.toString();

			return Integer.parseInt(s.substring(0, 2));

		} catch(Exception e) {
			return -1;
		}

	} //End public int parseHours(CharSequence)

	public static int parseMinutes(CharSequence seq) {
		try {
			StringBuilder str = new StringBuilder(seq);
			String s = str.toString();

			return Integer.parseInt(s.substring(3));

		} catch(Exception e) {
			return -1;
		}

	} //End public int parseMinutes(CharSequence)
}
