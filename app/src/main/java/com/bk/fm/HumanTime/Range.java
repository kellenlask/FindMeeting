/*
This file contains the Java code for the class definition of a Range object. Range objects are
similar to Intervals, except they include a day, and are compared differently. They also add a few
extra methods. In the future, this class may extend the Interval class.
 */

package com.bk.fm.HumanTime;

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
		return i.getLengthInMinutes() > interval.getLengthInMinutes();
	}

	public String toString(Context c) {
		return day.toString(c) + ": " + interval.toString();
	}

	//Determines if two ranges overlap, and returns an integer representing the overlap type
	public int overlaps(Range r) {
		//0 means no overlap
		int overlap = 0;

		if(day.equals(r.getDay())) {
			int os = r.getStartTime().getTimeInMinutes();
			int oe = r.getStopTime().getTimeInMinutes();
			int s = getStartTime().getTimeInMinutes();
			int e = getStopTime().getTimeInMinutes();


			if(os < s && oe < e && s < oe) {
				//    |---------|  <---- This
				// |---------| <---- Other
				overlap = 1;

			} else if(s < os && e < oe && os < e) {
				// |----------|
				//    |----------|
				overlap = 2;

			} else if(s < os && oe < e) {
				// |------------|
				//    |------|
				overlap = 3;

			} else if(os < s && e < oe) {
				//    |------|
				// |------------|
				overlap = 4;

			} else if(oe == e && os < s) {
				//    |-----|
				// |--------|
				overlap = 5;

			} else if(os == s && e < oe) {
				//	|------|
				//	|----------|
				overlap = 6;

			} else if(os == s && oe < e) {
				// |----------|
				// |------|
				overlap = 7;

			} else if(oe == e && s < os) {
				// |----------|
				//    |-------|
				overlap = 8;

			} else if(os == s && oe == e) {
				// |--------|
				// |--------|
				overlap = 9;
			}

			return overlap;

		} else {
			return overlap;
		}
	}

	public boolean contains(Range r) { //Is the passed range completely contained within this one?
		boolean days = r.getDay().equals(day);
		boolean startTimes = getStartTime().getTimeInMinutes() < r.getStartTime().getTimeInMinutes();
		boolean stopTimes = getStopTime().getTimeInMinutes() > r.getStopTime().getTimeInMinutes();

		return startTimes && stopTimes && days;
	}

	public boolean containsInclusive(Range r) {
		boolean days = r.getDay().equals(day);
		boolean startTimes = getStartTime().getTimeInMinutes() <= r.getStartTime().getTimeInMinutes();
		boolean stopTimes = getStopTime().getTimeInMinutes() >= r.getStopTime().getTimeInMinutes();

		return startTimes && stopTimes && days;
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

		//Store the starts and stops
		//int os = r.getStartTime().getTimeInMinutes();
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
