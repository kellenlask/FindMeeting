package com.bk.fm.findmeeting;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Kellen on 3/15/2015.
 */
public class DataBase extends SQLiteOpenHelper {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MeetingFinder";
	private static final String TABLE_PEOPLE = "people";


//----------------------------------------------------
//
//	SQLiteOpenHelper
//
//----------------------------------------------------

	@Override
	public void onCreate(SQLiteDatabase db) {
		//This creates the Database when the app is run for the first time
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//This upgrades it when the version changes
	}

//----------------------------------------------------
//
//	Constructor
//
//----------------------------------------------------

	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

}
