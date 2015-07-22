package com.bk.fm.findmeeting;

import android.app.Fragment;

/**
 * Created by kellen on 7/22/15.
 */
public class FragmentFactory {
	public static Fragment getParamEntryFrag() {
		return new ParamEntry();

	} //End getParamEntryFrag()

	public static Fragment getSummaryFrag() {
		return new Summary();

	} //End getParamEntryFrag()

	public static Fragment getInvolvedPeopleFrag() {
		return new InvolvedPeople();

	} //End getParamEntryFrag()

	public static Fragment getNewObligAvailFrag() {
		return new NewObligAvail();

	} //End getParamEntryFrag()

	public static Fragment getPersonalSummaryFrag() {
		return new PersonalSummary();

	} //End getParamEntryFrag()

	public static Fragment getResultsFrag() {
		return new Results();

	} //End getParamEntryFrag()

	public static Fragment getSavedPeopleFrag() {
		return new SavedPeople();

	} //End getParamEntryFrag()

} //End class
