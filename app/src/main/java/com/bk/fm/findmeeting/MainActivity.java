package com.bk.fm.findmeeting;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;


public class MainActivity extends ActionBarActivity {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	protected FragmentManager manager;
	protected int container = R.id.fragmentContainer;

//----------------------------------------------------
//
//	Initialization
//
//----------------------------------------------------
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		manager = getFragmentManager();

		//Push through the first transaction to the container
		FragmentTransaction transact = manager.beginTransaction();

		if(manager.findFragmentByTag(Shard.PARAM_ENTRY.toString()) == null) {
			transact.add(container, new ParamEntry(), Shard.PARAM_ENTRY.toString());
		}

		transact.commit();

	} //End onCreate

	public void changeToFragment(Shard frag) {
		Fragment fragInstance = null;

		//Instantiate the appropriate fragment
		switch(frag) {
			case INVOLVED_PEOPLE:
				fragInstance = FragmentFactory.getInvolvedPeopleFrag();
				break;

			case NEW_OBLIG_AVAIL:
				fragInstance = FragmentFactory.getNewObligAvailFrag();
				break;

			case RESULTS:
				fragInstance = FragmentFactory.getResultsFrag();
				break;

			case PERSONAL_SUMMARY:
				fragInstance = FragmentFactory.getPersonalSummaryFrag();
				break;

			case SUMMARY:
				fragInstance = FragmentFactory.getSummaryFrag();
				break;

			case SAVED_PEOPLE:
				fragInstance = FragmentFactory.getSavedPeopleFrag();
				break;

			default: //ParamEntry
				fragInstance = FragmentFactory.getParamEntryFrag();
				break;

		} //End Switch

		//Replace the existing fragment with the new one
		FragmentTransaction transact = manager.beginTransaction();
		transact.replace(container, fragInstance);
		transact.addToBackStack(null);
		transact.commit();

	} //End changeToFragment(Shard)

} //End Class
