/**
 * Created by Kellen on 3/4/2015.
 */

package com.bk.fm.findmeeting;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;


public class Person implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private String name;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Person(String name) {
		this.name = name;

	}

//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//ToDo: implement parcelable
	}

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------


//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------



} //End public class Person
