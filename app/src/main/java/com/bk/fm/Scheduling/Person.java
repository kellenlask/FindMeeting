package com.bk.fm.Scheduling;

/**
 * Created by Kellen on 3/4/2015.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.bk.fm.HumanTime.Range;
import com.bk.fm.HumanTime.Time;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/*
This file contains the Java code for the class definition of a Person Object. This object should
store the information of a person, i.e. Name, Database Primary Key, and a list of
Availabilities/Obligations.
*/

public class Person implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private String name;
	private ArrayList<ScheduleObject> availability;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Person() {
		this.name = "";
		this.availability = new ArrayList<>();
	}

	public Person(String name) {
		this.name = name;
        this.availability = new ArrayList<>();
	}

	public Person(Parcel in) {
		this.name = in.readString();
		this.availability = (ArrayList<ScheduleObject>) in.readSerializable();

		//Empty serialized lists are transfered as null...
		if(availability == null) {
			availability = new ArrayList<>();
		}

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
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ObjectOutputStream outStream = new ObjectOutputStream(byteOut);
			outStream.writeObject(availability);

			return byteOut.toString("ISO-8859-1");

	} //End public String getSerializedAvial()

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		Person other = (Person) obj;

		return (this.name == null) ? (other.name == null) : this.name.equals(other.name);
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

	public void setAvailability(ArrayList<ScheduleObject> availability) {
		this.availability = availability;
	}

	public void setAvailability(String serializedObj) {
		ArrayList<ScheduleObject> avail = null;
		try {
			byte[] bts = serializedObj.getBytes("ISO-8859-1"); //If this doesn't work, here is where it's messing up

			ByteArrayInputStream inputStream = new ByteArrayInputStream(bts);
			ObjectInputStream si = new ObjectInputStream(inputStream);

			avail = (ArrayList<ScheduleObject>) si.readObject();

			if(avail ==  null) {
				avail = new ArrayList<>();

			}

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

	//Simplify the Person's entered availabilities and obligations down into the smallest list possible
	//of only obligations for ease of calculation later
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
					int type = availability.get(i).overlaps(availability.get(j));

					if(type != 0 && availability.get(j).isObligation())
					{
						//Fix the overlap, and change indexing if necessary
						int[] indexes = fixOverlap(i, j, type);

						//Keep the indexes in the bounds of the ArrayList
						if (!(indexes[0] >= availability.size() || indexes[0] < 0)) {
							i = indexes[0];
						} else {
							i = (indexes[0] >= availability.size()) ? (availability.size() - 1) : (0);
						}

						if (!(indexes[1] >= availability.size() || indexes[1] < 0)) {
							j = indexes[1];
						} else {
							j = (indexes[1] >= availability.size()) ? (availability.size() - 1) : (0);
						}

					} //End if #2

				} //End inner for loop

			} //End if #1

		} //End outer for loop

		//
		//Remove all left-over availabilities (The default is being available)
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
				if(availability.get(i).overlaps(availability.get(j)) != 0) {
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

	public int[] fixOverlap(int i, int j, int type) {
		int[] indexes = new int[]{i, j};

		//Pull the Schedule Objects
		ScheduleObject objI = availability.get(i);
		ScheduleObject objJ = availability.get(j);


		//Fix the overlap between i and j (overlaps() called on i)
		if (i < j) { //If j has greater priority
			switch(type) {
				case 1: //Subtract from i
				case 2: //Subtract from i
				case 7: //Subtract from i
				case 8: //Subtract from i
					objI.removeOverlap(objJ);
					break;

				case 3: //Split i in two
					availability.remove(i);

					ScheduleObject o1 = null;
					ScheduleObject o2 = null;

					try {
						o1 = new ScheduleObject(objI.isObligation(),
								new Range(objI.getStartTime().clone(),
										objJ.getStartTime().clone(),
										objI.getDay()));
					} catch (Exception e) {	}

					try {
						o2 = new ScheduleObject(objI.isObligation(),
							 new Range(objJ.getStopTime().clone(),
							 objI.getStopTime().clone(),
							 objI.getDay()));
					} catch (Exception e) {	}

					//Add the ranges back in
					if (o1 != null) {
						availability.add(i, o1);
						indexes[0]--;
					}
					if (o2 != null) {
						availability.add(i, o2);
						indexes[1]++;
					}
					break;

				case 4: //Remove i
				case 5: //Remove i
				case 6: //Remove i
				case 9: //Remove i
					//Remove the lesser element
					availability.remove(i);

					//Update the indexing
					indexes[0] = i--;
					break;

			} //End Switch


		} else { //if i has greater priority
			switch(type) {
				case 1: //Subtract from j
				case 2: //Subtract from j
				case 5: //Subtract from j
				case 6: //Subtract from j
					objJ.removeOverlap(objI);
					break;

				case 9: //Remove j
				case 7: //Remove j
				case 8: //Remove j
				case 3: //Remove j
					availability.remove(j);

					//Update the indexing
					indexes[1]--;
					indexes[0]--;
					break;

				case 4: //Split j in two
					availability.remove(j);

					ScheduleObject o1 = null;
					ScheduleObject o2 = null;

					try {
						o1 = new ScheduleObject(objJ.isObligation(),
							 new Range(objJ.getStartTime().clone(),
							 objI.getStartTime().clone(),
							 objJ.getDay()));
					} catch (Exception e) {	}

					try {
						o2 = new ScheduleObject(objJ.isObligation(),
							 new Range(objI.getStopTime().clone(),
							 objJ.getStopTime().clone(),
							 objJ.getDay()));
					} catch (Exception e) {	}

					//Add the new ranges
					if (o1 != null) {
						availability.add(j, o1);
						indexes[0]++;
					}

					if (o2 != null) {
						availability.add(j, o2);
						indexes[1]++;
					}
					break;

			} //End Switch

		} //End if(i < j) else

		return indexes;

	} //End public void fix(int, int)

} //End public class Person
