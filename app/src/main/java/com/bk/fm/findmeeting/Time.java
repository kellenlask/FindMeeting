package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/9/2015.
 */
public class Time implements Comparable<Time> {
//Fields
	private int hours;
	private int minutes;

//Constructor
	public Time(int hours, int minutes) throws IllegalArgumentException {
		setHours(hours);
		setMinutes(minutes);

	} //End Constructor

	public Time(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setHours(hours, amPM);
		setMinutes(minutes);

	} //End Constructor

//Accessors
	public int getHours() {
		return hours;
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

	public int getMinutes() {
		return minutes;
	}

	public int getTimeInMinutes() {
		return (hours * 60) + minutes;
	}

	public boolean equals(Time t) {
		return getTimeInMinutes() == t.getTimeInMinutes();
	}

	public int compareTo(Time t) {
		return getTimeInMinutes() - t.getTimeInMinutes();
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

	public char getAMPM(){
		if(hours < 12) {
			return 'p';
		} else {
			return 'a';
		}
	}

//Mutators
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

}
