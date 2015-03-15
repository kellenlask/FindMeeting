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

	//Is this Range smaller than the passed Range?
	public boolean smallerThan(Range r) {
		return r.getInterval().getLengthInMinutes() < interval.getLengthInMinutes();
	}

	public boolean smallerThan(Interval i) {
		return i.getLengthInMinutes() < interval.getLengthInMinutes();
	}

	public String toString(Context c) {
		return day.toString(c) + ": " + interval.toString();
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


}
