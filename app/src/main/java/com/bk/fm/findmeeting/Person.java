/**
 * Created by Kellen on 3/4/2015.
 */

package com.bk.fm.findmeeting;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.LinkedList;


public class Person implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private String name;
	private LinkedList<ScheduleObject> availability;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Person(String name) {
		this.name = name;

	}

	public Person(Parcel in) {
		this.name = in.readString();
		this.availability = (LinkedList<ScheduleObject>) in.readSerializable();
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
		dest.writeString(name);
		dest.writeSerializable(availability);
	}

	//Technically a field
	public static final Creator<Person> CREATOR = new Creator<Person>() {
		@Override
		public Person createFromParcel(Parcel source) {
			return new Person(source);
		}

		@Override
		public Person[] newArray(int size) {
			return new Person[size];
		}
	};

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------

	public LinkedList<ScheduleObject> getAvailability() {
		return availability;
	}


//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void addScheduleObject(ScheduleObject o) {
		availability.addFirst(o);
	}


} //End public class Person
