/**
 * Created by Kellen on 3/4/2015.
 */

package com.bk.fm.findmeeting;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
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
	private TreeMap<Integer, TreeSet<Range>> totalAvailability;

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


//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void setPossibleDays(TreeMap<Day, Range> possibleDays) {
		this.possibleDays = possibleDays;
	}

	public void setMeetingDuration(Interval meetingDuration) {
		this.meetingDuration = meetingDuration;
	}

	public TreeMap<Integer, TreeSet<Range>> calcTotalAvailability(ArrayList<Person> people) {
		initializeAverages(people);

		for(Person p : people) {
			LinkedList<ScheduleObject> list = p.getAvailability();

			while(list.size() > 0) {
				ScheduleObject o = list.getLast();
				list.removeLast();

				for(Integer i : totalAvailability.keySet()) {
					for(Range r : totalAvailability.get(i)) {

						if(r.overlaps(o)) {
							fixOverlap(o, r, i);
						}

					}
				} //End for-each integer in totalAvailability
			} //End While LinkedList is Loop 
		} //End for-each person in people

		pruneMap();

		return totalAvailability;
	}


	public void initializeAverages(ArrayList<Person> people) {
		totalAvailability = new TreeMap<>();

		//For each possible level of availability, create a TreeSet<Range> to store the matching time ranges
		for(int i = 0; i <= people.size(); i++) {
			totalAvailability.put(i, new TreeSet<Range>());
		}

		//Throw the meeting params into the 100% available time range
		totalAvailability.get(people.size()).addAll(possibleDays.values());

	}//End public void initializeAverages(ArrayList<Person>)


	public void fixOverlap(ScheduleObject o, Range r, int index) {
		if (r.contains(o)) { //We need to make two ranges
			totalAvailability.get(index).remove(r);

			//Add the two new ranges to the map
			Range r1 = new Range(r.getStartTime(), o.getStartTime(), r.getDay());
			Range r2 = new Range(o.getStopTime(), r.getStopTime(), r.getDay());
			totalAvailability.get(index).add(r1);
			totalAvailability.get(index).add(r2);

			moveRange(o.clone(), o.isObligation(), index);

		} else if (o.contains(r) || r.equals(o)) { //The whole Range is moving
			totalAvailability.get(index).remove(r);
			moveRange(r, o.isObligation(), index);

		} else { //We can safely truncate and move the Range
			moveRange(r.removeOverlap(o), o.isObligation(), index);

		}


	} //End public void fixOverlap(ScheduleObject, Range, int)


	public void moveRange(Range r, boolean obligation, int index) {
		if(obligation) {
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
			TreeSet<Range> tmp = totalAvailability.get(i);

			//Iterate over the TreeSet<Range>
			Iterator itr = tmp.iterator();

			while(itr.hasNext()) {
				Range r = (Range) itr.next();

				//Remove the small Range objects
				if(r.smallerThan(meetingDuration)) {
					tmp.remove(r);
				}
			} //End While

		} //End For-Each Loop

	} //End private void pruneMap()


} //End public class Meeting
