/*
This file contains the Java code for the class definition of a Person Object. This object should
store the information of a person, i.e. Name, Database Primary Key, and Availabilities/Obligations.
 */

package com.bk.fm.findmeeting;

/**
 * Created by Kellen on 3/4/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.LinkedList;


public class Person implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private String name;
	private long primaryKey;
	private LinkedList<ScheduleObject> availability;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Person() {

	}

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

	public String getName() {
		return name;
	}

	public byte[] getSerializedAvial() throws IOException {

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
			outStream.writeObject(availability);

			return byteOut.toByteArray();

		} catch (IOException e) {
			throw new IOException("Invalid Byte Array");
		}

	}

	public long getPrimaryKey() {
		return primaryKey;
	}


//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void addScheduleObject(ScheduleObject o) {
		availability.addFirst(o);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setAvailability(LinkedList<ScheduleObject> availability) {
		this.availability = availability;
	}

	public void setAvailability(String serializedObj) {
		LinkedList<ScheduleObject> avail = null;
		try {
			byte[] bts = serializedObj.getBytes(); //If this doesn't work, here is where it's messing up

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bts);
			ObjectInputStream si = new ObjectInputStream(inputStream);

			avail = (LinkedList<ScheduleObject>) si.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		setAvailability(avail);
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Person other = (Person) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }

        return true;
    }

} //End public class Person
