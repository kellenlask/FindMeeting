package com.bk.fm.findmeeting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

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
	private static final String PRIMARY_KEY = "id";
	private static final String NAME_KEY = "name";
	private static final String AVAIL_KEY = "availability";


//----------------------------------------------------
//
//	SQLiteOpenHelper
//
//----------------------------------------------------

	@Override
	public void onCreate(SQLiteDatabase db) {
		//This creates the Database when the app is run for the first time

		String CREATE_PEOPLE_TABLE = "CREATE TABLE IF NOT EXISTS"
				+ TABLE_PEOPLE + "("
				+ PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT," //Arbitrary primary key
				+ NAME_KEY + " TEXT NOT NULL," //The person's name
				+ AVAIL_KEY + " BLOB NOT NULL" + ")"; //The person's availability (serialized to a byte[])
		db.execSQL(CREATE_PEOPLE_TABLE);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//This upgrades it when the version changes
		//So, we don't care about this.
	}

//----------------------------------------------------
//
//	Constructor
//
//----------------------------------------------------

	public DataBase(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

//----------------------------------------------------
//
//	Accessors
//
//----------------------------------------------------
	public ArrayList<Person> getAllPeople() {
		ArrayList<Person> people = new ArrayList<>();


		return people;
	}

	public Person getPerson(int id) {
		Person p = null;

		return p;
	}

	public ArrayList<Person> getPerson(String name) {
		ArrayList<Person> people = new ArrayList<>();


		return people;
	}

	public boolean contains(Person p) {
		SQLiteDatabase db = this.getReadableDatabase();

		//query(TABLE, COLUMNS, SELECTION, SELECTION ARGS, OTHER CRAP)
		Cursor cursor = db.query(TABLE_PEOPLE, new String[] {PRIMARY_KEY, NAME_KEY},
				PRIMARY_KEY + " = ?", new String[] {String.valueOf(p.getPrimaryKey())},
				null, null, null, null);

		boolean results = (cursor != null);

		db.close();

		return results;
	}

//----------------------------------------------------
//
//	Mutators
//
//----------------------------------------------------
	public void deletePerson(Person p) {

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_PEOPLE, PRIMARY_KEY + " = ?", new String[] { String.valueOf(p.getPrimaryKey()) });

		db.close();


	}

	public boolean addPerson(Person p) {
		try {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(NAME_KEY, p.getName());
			values.put(AVAIL_KEY, p.getSerializedAvial());

			// Inserting Row
			db.insert(TABLE_PEOPLE, null, values);
			db.close(); // Closing database connection

			return true;

		} catch(IOException e) {
			return false;
		}
	}



}