/*
This file contains the class definition for a Meeting object which would represent a prospective
meeting's desired parameters. EX: We're willing to meet either Tuesday from 12am - 3pm or Thursday
from 8am to 6pm; the meeting should last about a half-hour. As well, this class maintains a list of
involved people, and can calculate the overlaps between their various availabilities.
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
import java.util.Iterator;
import java.util.TreeMap;
import java.util.TreeSet;

public class Meeting implements Parcelable, Serializable {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private TreeMap<Day, Range> possibleDays;
	private Interval meetingDuration;

	//Populated post-construction
	private TreeMap<Integer, TreeSet<Range>> totalAvailability;
	private ArrayList<Person> involvedPeople;

//----------------------------------------------------
//
//	Constructors
//
//----------------------------------------------------
	public Meeting(TreeMap<Day, Range> days, Interval meetingDuration) {
		this.setPossibleDays(days);
		this.setMeetingDuration(meetingDuration);

	} //End constructor

//----------------------------------------------------
//
//	Interfaces
//
//----------------------------------------------------
	//Parcelable
	@Override
	public int describeContents() {
		return 0;
	}

	public Meeting(Parcel in) {
		possibleDays = (TreeMap<Day, Range>) in.readSerializable();
		meetingDuration = (Interval) in.readSerializable();

		if (in.dataSize() == 3) {
			in.readSerializable();
		}
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeSerializable(possibleDays);
		dest.writeSerializable(meetingDuration);

		if (totalAvailability != null) {
			dest.writeSerializable(totalAvailability);
		}
	}

	//This is actually a field, shhhhh...
	public static final Creator<Meeting> CREATOR = new Creator<Meeting>() {
		@Override
		public Meeting createFromParcel(Parcel source) {
			return new Meeting(source);
		}

		@Override
		public Meeting[] newArray(int size) {
			return new Meeting[size];
		}
	};

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------

	public TreeMap<Day, Range> getPossibleDays() {
		return possibleDays;
	}

	public Interval getMeetingDuration() {
		return meetingDuration;
	}

	public ArrayList<Person> getInvolvedPeople() {
		return involvedPeople;
	}

	public boolean isValid() {
		// Loop through the people and check that each person has at least one availability or obligation
		for (Person p : involvedPeople) {
			if (p.getAvailability() == null) {
				return false;
			}
		}

		return true;
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setInvolvedPeople(ArrayList<Person> people) {
		involvedPeople = people;
	}

	public void setPossibleDays(TreeMap<Day, Range> possibleDays) {
		this.possibleDays = possibleDays;
	}

	public void setMeetingDuration(Interval meetingDuration) {
		this.meetingDuration = meetingDuration;
	}

//----------------------------------------------------
//
//	Overlap Processing
//
//----------------------------------------------------

	//Calculates the overlaps in all of the people's availabilities and obligations
	public TreeMap<Integer, TreeSet<Range>> calcTotalAvailability() {
		//Setup the blank TreeMap
		initializeAverages();

		/*	YES, THIS LOOKS TERRIBLE -- LET ME EXPLAIN:
		*
		* - For each person
		* 	- Grab their availability
		* 	- For each of their Obligations in the Availability
		* 		- Compare the Obligation against every Range in the collective availability Map
		* 			- If the person's Obligation intersects a Range in the collective availability
		* 				- Adjust the collective availability (down)
		*/

		for(Person p : involvedPeople)
		{
			ArrayList<ScheduleObject> list = p.getAvailability();

			for(int j = 0; j < list.size(); j++)
			{
				ScheduleObject o = list.get(j);


				for(Integer i : totalAvailability.keySet())
				{
					for(Range r : totalAvailability.get(i))
					{

						if(r.overlaps(o) != 0) {
							fixOverlap(o, r, i);

						}

					}
				} //End for-each integer in totalAvailability
			} //End for each element in ArrayList Loop
		} //End for-each person in people

		pruneMap(); //Remove ranges smaller than the meeting duration

		return totalAvailability;

	} //End public TreeMap<Integer, TreeSet<Range>> calcTotalAvailability()


	//Fixes a given overlap between a Person's Obligation and a Range in the Collective Availability
	public void fixOverlap(ScheduleObject o, Range r, int index) {
		int type = r.overlaps(o);

		/*	THERE ARE 9 WAYS FOR TWO RANGES TO OVERLAP
		*
		* 	As you can imagine, two ranges can overlap in many different ways.
		* 	However, the same solution can resolve multiple types of overlap.
		*/

		switch(type) {
			case 1:
			case 2:
			case 7:
			case 8:
				moveRange(r.removeOverlap(o), o.isObligation(), index);
				break;

			case 3:
				totalAvailability.get(index).remove(r);

				//Add the two new ranges to the map
				Range r1 = new Range(r.getStartTime(), o.getStartTime(), r.getDay());
				Range r2 = new Range(o.getStopTime(), r.getStopTime(), r.getDay());
				totalAvailability.get(index).add(r1);
				totalAvailability.get(index).add(r2);

				moveRange(o.clone(), o.isObligation(), index);
				break;

			case 4:
			case 5:
			case 6:
			case 9:
				totalAvailability.get(index).remove(r);
				moveRange(r, o.isObligation(), index);
				break;

		} //End Switch


	} //End public void fixOverlap(ScheduleObject, Range, int)


	public void moveRange(Range r, boolean obligation, int index) {
		if(obligation && totalAvailability.containsKey(index - 1)) {
			totalAvailability.get(index - 1).add(r);

		} else if(totalAvailability.containsKey(index + 1)) {
			totalAvailability.get(index + 1).add(r);
		}

	} //End public void moveRange(Range, boolean, int)


	//Remove all Ranges from the map that are shorter than the meeting time
	//After this, the Map will only contain valid meeting time Ranges
	private void pruneMap() {

		//For each TreeSet<Range>
		for(Integer i : totalAvailability.keySet()) {

			//Iterate over the TreeSet<Range>
			Iterator itr = totalAvailability.get(i).iterator();

			while(itr.hasNext()) {
				Range r = (Range) itr.next();

				//Remove the small Range objects
				if(r.smallerThan(meetingDuration)) {
					itr.remove();
				}
			} //End While

		} //End For-Each Loop

	} //End private void pruneMap()


	public void initializeAverages() {
		totalAvailability = new TreeMap<>();

		//For each possible level of availability, create a TreeSet<Range> to store the matching time ranges
		for(int i = 0; i <= involvedPeople.size(); i++) {
			totalAvailability.put(i, new TreeSet<Range>());
		}

		//Throw the meeting params into the 100% available time range
		totalAvailability.get(involvedPeople.size()).addAll(possibleDays.values());

	}//End public void initializeAverages(ArrayList<Person>)

//----------------------------------------------------
//
//	Serialization Methods
//
//----------------------------------------------------

	public static String serializeMeeting(Meeting m) {
		try {
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			ObjectOutputStream so = new ObjectOutputStream(bo);
			so.writeObject(m);
			so.close();

			return bo.toString("ISO-8859-1");

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static Meeting deserializeMeeting(String s) {
		try {
			byte[] bytes = s.getBytes("ISO-8859-1");
			ObjectInputStream io = new ObjectInputStream( new ByteArrayInputStream(bytes) );
			Meeting m = (Meeting) io.readObject();

			return m;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

} //End public class Meeting
