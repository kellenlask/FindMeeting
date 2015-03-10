package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/9/2015.
 */
public class Interval implements Comparable<Interval>, Cloneable{
//Fields
	private Time startTime;
	private Time stopTime;

//Constructors
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

//Accessors
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

	//Compares by length
	public int compareTo(Interval i) {
		return getLengthInMinutes() - i.getLengthInMinutes();
	}

	public Interval clone() {
		return new Interval(startTime.clone(), stopTime.clone());
	}

//Mutators
	public void setStartTime(Time t) throws IllegalArgumentException {
		if(stopTime == null || t.getTimeInMinutes() < stopTime.getTimeInMinutes()) {
			startTime = t;

		} else {
			throw new IllegalArgumentException("Invalid Start Time.");
		}
	}

	public void setStartTime(int hours, int minutes) throws IllegalArgumentException {
		setStartTime(new Time(hours, minutes));
	}

	public void setStartTime(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setStartTime(new Time(hours, minutes, amPM));
	}

	public void setStopTime(Time t) throws IllegalArgumentException {
		if(startTime == null || startTime.getTimeInMinutes() < t.getTimeInMinutes()) {
			stopTime = t;

		} else {
			throw new IllegalArgumentException("Invalid Start Time.");
		}
	}

	public void setStopTime(int hours, int minutes) throws IllegalArgumentException {
		setStopTime(new Time(hours, minutes));
	}

	public void setStopTime(int hours, int minutes, char amPM) throws IllegalArgumentException {
		setStopTime(new Time(hours, minutes, amPM));
	}
}
