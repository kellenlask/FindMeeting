package com.bk.fm.findmeeting;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Kellen on 3/11/2015.
 */
public class Range implements Comparable<Range>, Cloneable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	protected Day day;
	protected Interval interval;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Range(Interval i, Day d) {
		day = d;
		interval = i;
	}

	public Range(Interval i, int day) {
		this.day = Day.getDay(day);
		interval = i;
	}

	public Range(Time start, Time stop, Day d) {
		interval = new Interval(start, stop);
		day = d;
	}

//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------
	public int compareTo(Range r) {
		if(!isSameDay(r)) {
			return day.getIndex() - r.getDay().getIndex();

		} else {
			return interval.compareTo(r.getInterval());
		}
	}

	public Range clone() {
		return new Range(interval.clone(), day.getIndex());
	}

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------

	public boolean isSameDay(Range r) {
		return day.getIndex() == r.getDay().getIndex();
	}

	public Day getDay() {
		return day;
	}

	public Interval getInterval() {
		return interval;
	}

	public Time getStartTime() {
		return interval.getStartTime();
	}

	public Time getStopTime() {
		return interval.getStopTime();
	}

	//Is this Range smaller than the passed Interval?
	public boolean smallerThan(Interval i) {
		return i.getLengthInMinutes() < interval.getLengthInMinutes();
	}

	public String toString(Context c) {
		return interval.toString();
	}

	public boolean contains(Range r) { //Is the passed range completely contained within this one?
		boolean days = r.getDay().equals(day);
		boolean startTimes = getStartTime().getTimeInMinutes() < r.getStartTime().getTimeInMinutes();
		boolean stopTimes = getStopTime().getTimeInMinutes() > r.getStopTime().getTimeInMinutes();

		return startTimes && stopTimes && days;
	}

	public boolean overlaps(Range r) {
		if(day.equals(r.getDay())) {
			int os = r.getStartTime().getTimeInMinutes();
			int oe = r.getStopTime().getTimeInMinutes();
			int s = getStartTime().getTimeInMinutes();
			int e = getStopTime().getTimeInMinutes();

			//    (The other's end time falls between our start and stop)
			// or (The other's start time falls between our start and stop)
			return (oe > s && oe < e) || (os < e && os > s);

		} else {
			return false;
		}
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setDay(Day d) {
		day = d;
	}

	public void setInterval(Interval i) {
		interval = i;
	}

	public void setInterval(Time start, Time stop) {
		interval = new Interval(start, stop);
	}

	public void setInterval(int startHours, int startMinutes, int stopHours, int stopMinutes) {
		interval = new Interval(startHours, startMinutes, stopHours, stopMinutes);
	}

	public void setStartTime(Time t) {
		interval.setStartTime(t);
	}

	public void setStopTime(Time t) {
		interval.setStopTime(t);
	}

	public Range removeOverlap(Range r) {
		//If the ranges don't overlap, then return null
		if(!overlaps(r)) {
			return null;
		}

		//Store the starts and stops
		int os = r.getStartTime().getTimeInMinutes();
		int oe = r.getStopTime().getTimeInMinutes();
		int s = getStartTime().getTimeInMinutes();
		int e = getStopTime().getTimeInMinutes();
		Range returnRange;


		//The other's end time falls between our start and stop
		if(oe > s && oe < e) {
			returnRange = new Range(getStartTime(), r.getStopTime(), getDay()); //This is the overlap
			setStartTime(r.getStopTime()); //Hack off the overlap

		} else { // os < e && os > s -- The other's start time falls between our start and stop
			returnRange = new Range(r.getStartTime(), getStopTime(), getDay()); //This is the overlap
			setStopTime(r.getStartTime()); //Hack off the overlap
		}

		return returnRange;
	} //End public Range removeOverlap(Range)


} //End public class Range
