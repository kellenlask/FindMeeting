/**
 * Created by Kellen on 3/4/2015.
 */

package com.bk.fm.findmeeting;

import android.os.Parcel;
import android.os.Parcelable;

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
		totalAvailability = new TreeMap<>();

		//For each possible level of availability, create a TreeSet<Range> to store the matching time ranges
		for(int i = 0; i <= people.size(); i++) {
			totalAvailability.put(i, new TreeSet<Range>());
		}


		//ToDo: Add the logic (I'm too tired right now, I'll fuck it up)


		pruneMap();

		return totalAvailability;
	}

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
