package com.bk.fm.findmeeting;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Kellen on 3/14/2015.
 */
public class ScheduleObject implements Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private boolean obligation;
	private Range range;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------

	public ScheduleObject(boolean obligation, Range range) {
		this.obligation = obligation;
		this.range = range;
	}


//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------

	public boolean isObligation() {
		return obligation;
	}

	public Range getRange() {
		return range;
	}

	public String toString(Context c) {
		if(obligation) {
			return c.getString(R.string.obligation) + " " + range.toString(c);
		} else {
			return c.getString(R.string.availability) + " " + range.toString(c);
		}
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setObligation(boolean obligation) {
		this.obligation = obligation;
	}

	public void setRange(Range range) {
		this.range = range;
	}
}
