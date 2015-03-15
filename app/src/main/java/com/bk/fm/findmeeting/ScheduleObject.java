package com.bk.fm.findmeeting;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by Kellen on 3/14/2015.
 */
public class ScheduleObject extends Range implements Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	protected boolean obligation;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------


	public ScheduleObject(boolean obligation, Range range) {
		super(range.getInterval(), range.getDay());
		this.obligation = obligation;

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

	public String toString(Context c) {
		if(obligation) {
			return c.getString(R.string.obligation) + " " + super.toString(c);
		} else {
			return c.getString(R.string.availability) + " " + super.toString(c);
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
}
