/*
This file contains the class definition for a DataBase object which is intended to interface
between the client code and the SQLite database containing saved People objects. The code below
describes the database interactions.
 */

package com.bk.fm.findmeeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bk.fm.Scheduling.Person;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Kellen on 3/15/2015.
 */
public class Database extends SQLiteOpenHelper {
//----------------------------------------------------
//
//	Fields
//
//----------------------------------------------------
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MeetingFinder";

	//Tables
	private static final String TABLE_PEOPLE = "people";

	//Fields
	private static final String PRIMARY_KEY = "name";
	private static final String AVAIL_KEY = "availability";


//----------------------------------------------------
//
//	Constructor
//
//----------------------------------------------------

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//----------------------------------------------------
//
//	SQLiteOpenHelper
//
//----------------------------------------------------

	@Override
	public void onCreate(SQLiteDatabase db) {
		//This creates the Database when the app is run for the first time

		String CREATE_PEOPLE_TABLE = "CREATE TABLE IF NOT EXISTS "
				+ TABLE_PEOPLE + "("
				+ PRIMARY_KEY + " TEXT NOT NULL," //Name Key
				+ AVAIL_KEY + " TEXT NOT NULL" + ")"; //The person's availability (serialized to a byte[])
		db.execSQL(CREATE_PEOPLE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//This upgrades it when the version changes
		//So, we don't care about this.
	}

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------
	public ArrayList<Person> getAllPeople() {
		ArrayList<Person> people = new ArrayList<>();
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PEOPLE, null);

		if(cursor.moveToFirst()) { //If we got stuff
			do { //Make people from the stuff
				Person p = new Person();

				p.setName(cursor.getString(0));
				p.setAvailability(cursor.getString(1));

				people.add(p);

			} while(cursor.moveToNext()); //While there are people left from the database
		} //End if

		cursor.close();

		return people;
	}

	public Person getPerson(String name) {
		Person p = null;

		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_PEOPLE + " WHERE " + PRIMARY_KEY + " = '" + name + "'";
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()) { //If we got stuff
			p.setName(cursor.getString(0));
			p.setAvailability(cursor.getString(1));
		}

		cursor.close();

		return p;
	}

	public boolean contains(String name) {
		SQLiteDatabase db = this.getWritableDatabase();
		String query = "SELECT * FROM " + TABLE_PEOPLE + " WHERE " + PRIMARY_KEY + " = '" + name + "'";
		Cursor cursor = db.rawQuery(query, null);

		if(cursor.moveToFirst()) { //If we got stuff
			return true;
		}

		cursor.close();

		return false;
	}

	public boolean contains(Person p) {
		SQLiteDatabase db = this.getReadableDatabase();

		//query(TABLE, COLUMNS, SELECTION, SELECTION ARGS, OTHER CRAP)
		Cursor cursor = db.query(TABLE_PEOPLE, new String[] {PRIMARY_KEY},
				PRIMARY_KEY + " = ?", new String[] {p.getName()},
				null, null, null, null);

		boolean results = (cursor != null);

		cursor.close();
		db.close();

		return results;
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void deletePerson(String name) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PEOPLE, (PRIMARY_KEY + " = '" + name + "'"), null);

		db.close();
	}

	public boolean addPerson(Person p) {
		if(contains(p.getName())) {
			return false;
		} else {
			try {
				SQLiteDatabase db = this.getWritableDatabase();

				ContentValues values = new ContentValues();
				values.put(PRIMARY_KEY, p.getName());
				values.put(AVAIL_KEY, p.getSerializedAvial());

				// Inserting Row
				db.insert(TABLE_PEOPLE, null, values);
				db.close(); // Closing database connection

				return true;

			} catch(IOException e) {
				return false;
			} //End try-catch
		} //End if-else
	} //End public boolean addPerson(Person)

	public int updatePerson(Person p) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(PRIMARY_KEY, p.getName());
			values.put(AVAIL_KEY, p.getSerializedAvial());

			// Updating Row
			int updates = db.update(TABLE_PEOPLE, values, PRIMARY_KEY + " = ?",
					new String[] {p.getName()});
			db.close(); // Closing database connection

			return updates;

		} catch(IOException e) {
			return 0;
		}
	}

	public int updatePersonAvail(Person p) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put(AVAIL_KEY, p.getSerializedAvial());

			int updates = db.update(TABLE_PEOPLE, values, PRIMARY_KEY + " = '" + p.getName() + "'", null);

			db.close();

			return updates; //Number of fields changed

		} catch(IOException e) {
			return 0;
		}

	}//End public int updatePersonAvail(Person)

    public void updatePersonName(String oldName, String newName) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PRIMARY_KEY, newName);

        db.update(TABLE_PEOPLE, values, PRIMARY_KEY + " = '" + oldName + "'", null);

        db.close();
    }

}
