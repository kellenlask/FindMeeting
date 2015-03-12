package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/9/2015.
 */
public class Interval implements Comparable<Interval>, Cloneable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private Time startTime;
	private Time stopTime;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Interval(Time startTime, Time stopTime) throws IllegalArgumentException {
		setStartTime(startTime);
		setStopTime(stopTime);
	}

	public Interval(int startHours, int startMinutes, int stopHours, int stopMinutes) throws IllegalArgumentException {
		setStartTime(startHours, startMinutes);
		setStopTime(stopHours, stopMinutes);
	}

	public Interval(int startHours, int startMinutes, char startAMPM, int stopHours, int stopMinutes, char stopAMPM) throws IllegalArgumentException {
		setStartTime(startHours, startMinutes, startAMPM);
		setStopTime(stopHours, stopMinutes, stopAMPM);
	}

//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------
	//Compares by length
	public int compareTo(Interval i) {
	return getLengthInMinutes() - i.getLengthInMinutes();
}

	public Interval clone() {
		return new Interval(startTime.clone(), stopTime.clone());
	}


//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------

	public boolean contains(Time t) {
		int time = t.getTimeInMinutes();

		return startTime.getTimeInMinutes() <= time && time <= stopTime.getTimeInMinutes();
	}

	public int getLengthInMinutes(){
		return stopTime.getTimeInMinutes() - startTime.getTimeInMinutes();
	}

	public double getLengthInHours() {
		return (stopTime.getTimeInMinutes() - startTime.getTimeInMinutes()) / 60;
	}

	public Time getStartTime() {
		return startTime;
	}

	public Time getStopTime() {
		return stopTime;
	}

	public boolean equals(Interval i) {
		return startTime.equals(i.getStartTime()) && stopTime.equals(i.getStopTime());
	}

	public boolean contains(Interval i) {
		return contains(i.getStartTime()) && contains(i.getStopTime());
	}

	public boolean overlaps(Interval i) {
		if(contains(i.getStartTime())) {
			return true;

		} else if(contains(i.getStopTime())) {
			return true;

		} else {
			return false;

		}
	}

	public boolean startsSooner(Interval i) {
		return startTime.getTimeInMinutes() < i.getStartTime().getTimeInMinutes();
	}

	public boolean stopsSooner(Interval i) {
		return stopTime.getTimeInMinutes() < i.getStopTime().getTimeInMinutes();
	}

	//ToDo: Double-check this method:
	public Interval getOverlap(Interval i) {
		if (overlaps(i)) {
			if (i.contains(this)) {
				return this.clone();

			} else if (contains(i)) {
				return i.clone();

			} else if(startsSooner(i)) {
				return new Interval(i.getStartTime().clone(), stopTime.clone());

			} else {
				return new Interval(startTime.clone(), i.getStopTime().clone());
			}
		}

		return null;
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setStartTime(Time t) throws IllegalArgumentException {
		if(stopTime == null) {
			startTime = t;
		} else if(!stopTime.equals(t)) {
			startTime = t;
		} else {
			throw new IllegalArgumentException("Invalid Start Time.");
		}

		//If the times are defined backwards, switch them.
		if(stopTime != null && startTime.getTimeInMinutes() > stopTime.getTimeInMinutes()) {
			switchTimes();
		}

	}

	public void setStopTime(Time t) throws IllegalArgumentException {
		if(startTime == null) {
			stopTime = t;
		} else if(!startTime.equals(t)) {
			stopTime = t;
		} else {
			throw new IllegalArgumentException("Invalid Stop Time.");
		}

		//If the times are defined backwards, switch them.
		if(stopTime != null && startTime.getTimeInMinutes() > stopTime.getTimeInMinutes()) {
			switchTimes();
		}
	}

	public void setStartTime(int hours, int minutes) throws IllegalArgumentException {
		setStartTime(new Time(hours, minutes));
	}

	public void setStartTime(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setStartTime(new Time(hours, minutes, amPM));
	}

	public void setStopTime(int hours, int minutes) throws IllegalArgumentException {
		setStopTime(new Time(hours, minutes));
	}

	public void setStopTime(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setStopTime(new Time(hours, minutes, amPM));
	}

	public void addTime(int hours, int minutes) throws Exception {
		stopTime.addHours(hours);
		stopTime.addMinutes(minutes);
	}

	public void addTime(int minutes) throws Exception {
		stopTime.addMinutes(minutes);
	}

	public void switchTimes(){
		Time tmp = startTime;
		startTime = stopTime;
		stopTime = tmp;
	}

//----------------------------------------------------
//
//	Static Methods
//
//----------------------------------------------------
	public static boolean isValidInterval(Time start, Time stop) {
		return start.getTimeInMinutes() < stop.getTimeInMinutes();
	}

	public static boolean isValidInterval(int startHours, int startMinutes, int stopHours, int stopMinutes) {
		if(Time.isValidTime(startHours, startMinutes) && Time.isValidTime(stopHours, stopMinutes)) {
			Time start = new Time(startHours, startMinutes);
			Time stop = new Time(stopHours, stopMinutes);
			return isValidInterval(start, stop);
		}

		return false;
	}
}
