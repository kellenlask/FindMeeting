package com.bk.fm.Scheduling;

/**
 * Created by Kellen on 3/14/2015.
 */

import android.content.Context;

import com.bk.fm.HumanTime.Range;
import com.bk.fm.findmeeting.R;

import java.io.Serializable;

/*
This file contains the Java code for a Schedule Object class definition. This is the super class for
Availability, and Obligation. Other than that, this class's primary purpose is to allow for
polymorphism between Availability and Obligation objects.
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
