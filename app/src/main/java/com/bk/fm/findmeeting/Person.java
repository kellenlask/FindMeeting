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
import java.util.ArrayList;


public class Person implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private String name;
	private long primaryKey;
	private ArrayList<ScheduleObject> availability;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Person() {

	}

	public Person(String name) {
		this.name = name;
        this.availability = new ArrayList<>();
	}

	public Person(Parcel in) {
		this.name = in.readString();
		this.availability = (ArrayList<ScheduleObject>) in.readSerializable();
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

	public ArrayList<ScheduleObject> getAvailability() {
		return availability;
	}

	public String getName() {
		return name;
	}

	public String getSerializedAvial() throws IOException {

		try {
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
			outStream.writeObject(availability);

			return byteOut.toString("ISO-8859-1");

		} catch (IOException e) {
			throw new IOException("Invalid Byte Array");
		}

	}

	public long getPrimaryKey() {
		return primaryKey;
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

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void addScheduleObject(ScheduleObject o) {
		availability.add(o);
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrimaryKey(long primaryKey) {
		this.primaryKey = primaryKey;
	}

	public void setAvailability(ArrayList<ScheduleObject> availability) {
		this.availability = availability;
	}

	public void setAvailability(String serializedObj) {
		ArrayList<ScheduleObject> avail = null;
		try {
			byte[] bts = serializedObj.getBytes(); //If this doesn't work, here is where it's messing up

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bts);
			ObjectInputStream si = new ObjectInputStream(inputStream);

			avail = (ArrayList<ScheduleObject>) si.readObject();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		setAvailability(avail);
	} //End public void setAvailability(String)

//----------------------------------------------------
//
//	Availability Reduction
//
//----------------------------------------------------

	public void reduceAvailability() {
		//
		//Cancel overlaps of different ScheduleObjects based on priority
		//
		for(int i = 0; i < availability.size(); i++)
		{
			if (!availability.get(i).isObligation()) //If the object isn't an obligation
			{
				for(int j = 0; j < availability.size(); j++)
				{
					//If the two objects overlap, and they're not of the same type
					if(availability.get(i).overlaps(availability.get(j)) && availability.get(j).isObligation())
					{
						int[] indexes = fixOverlap(i, j);
						i = indexes[0];
						j = indexes[1];

					} //End if #2

				} //End inner for loop

			} //End if #1

		} //End outer for loop

		//
		//Remove all left-over availabilities
		//
		for(int i = 0; i < availability.size(); i++) {
			if(!availability.get(i).isObligation()) {
				availability.remove(i);
				i--;
			}
		}

		//
		//Join elements now that everything has the same priority
		//
		for(int i = 0; i < availability.size(); i++)
		{
			for(int j = i + 1; j < availability.size(); j++)
			{
				if(availability.get(i).touches(availability.get(j))) {
					combine(j, i);
					j--;
				}

			} //Inner for-loop
		} //Outer for-loop

	} //End public void reduceAvailability()

	//Combines two schedule objects of the same type in the availability list
	public void combine(int i, int j) {
		ScheduleObject objI = availability.get(i);
		ScheduleObject objJ = availability.get(j);

		int iStart = objI.getStartTime().getTimeInMinutes();
		int iEnd = objI.getStopTime().getTimeInMinutes();
		int jStart = objJ.getStartTime().getTimeInMinutes();
		int jEnd = objJ.getStopTime().getTimeInMinutes();

		Time startTime = (iStart < jStart) ? (objI.getStartTime().clone()) : (objJ.getStartTime().clone());
		Time endTime = (iEnd > jEnd) ? (objI.getStopTime().clone()) : (objJ.getStopTime().clone());

		availability.set(i, new ScheduleObject(true, new Range(startTime, endTime, objJ.getDay())));
		availability.remove(j);

	} //End public void combine(int, int)

	public int[] fixOverlap(int i, int j) {
		int[] indexes = new int[]{i, j};

		ScheduleObject objI = availability.get(i);
		ScheduleObject objJ = availability.get(j);

		int iStart = objI.getStartTime().getTimeInMinutes();
		int iEnd = objI.getStopTime().getTimeInMinutes();
		int jStart = objJ.getStartTime().getTimeInMinutes();
		int jEnd = objJ.getStopTime().getTimeInMinutes();

		//Determine the type of overlap
		int type;
		if(jStart < iStart) {
			//	i:		|----------|
			//	j: |----------|
			type = 0;

		} else if(jEnd > iEnd) {
			//	i: |----------|
			//	j:        |----------|
			type = 0;

		} else if(objI.contains(objJ)) { //.touches() is .contains(), only inclusive.
			//	i: |-------------------|
			//	j:     |----------|
			type = 1;

		} else {
			//	i:     |----------|
			//	j: |-------------------|
			type = 2;
		}

		//Fix the overlap
		if (i < j) { //If j has greater priority
			switch(type) {
				case 0:
					objI.removeOverlap(objJ);
					break;

				case 1:
					//Split i into two ranges
					availability.remove(i);
					ScheduleObject o1 = new ScheduleObject(objI.isObligation(), new Range(objI.getStartTime().clone(), objJ.getStartTime().clone(), objI.getDay()));
					ScheduleObject o2 = new ScheduleObject(objI.isObligation(), new Range(objJ.getStopTime().clone(), objI.getStopTime().clone(), objI.getDay()));

					//Add the ranges back in
					availability.add(i, o1);
					availability.add(i, o2);

					//Change the indexing
					indexes[0]--;
					indexes[1]++;
					break;

				default: //case 2
					//Remove the lesser element
					availability.remove(i);

					//Update the indexing
					indexes[0] = i--;
					break;
			} //End Switch

		} else { //if i has greater priority
			switch(type) {
				case 0:
					objJ.removeOverlap(objI);
					break;

				case 1:
					//Remove the totally contained, lesser element
					availability.remove(j);

					//Update the indexing
					indexes[1]--;
					indexes[0]--;
					break;

				default: //case 2
					//Split the range
					availability.remove(j);
					ScheduleObject o1 = new ScheduleObject(objJ.isObligation(), new Range(objJ.getStartTime().clone(), objI.getStartTime().clone(), objJ.getDay()));
					ScheduleObject o2 = new ScheduleObject(objJ.isObligation(), new Range(objI.getStopTime().clone(), objJ.getStopTime().clone(), objJ.getDay()));

					//Add the new ranges
					availability.add(j, o1);
					availability.add(j, o2);

					//Update the indexing
					indexes[0]++;
					indexes[1]++;
					break;
			} //End Switch

		} //End if(i < j) else

		return indexes;

	} //End public void fix(int, int)

} //End public class Person
