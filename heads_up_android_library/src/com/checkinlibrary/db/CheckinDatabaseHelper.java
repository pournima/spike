package com.checkinlibrary.db;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CheckinDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "checkin_native";
	private static final int DATABASE_VERSION = 1;

	public CheckinDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			//Android requires _id
			String createSql = "CREATE TABLE organizations (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT NOT NULL, " +
					"remote_id INTEGER NOT NULL, " +
					"is_supported INTEGER NOT NULL);";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			
			createSql = 
					"CREATE TABLE businesses (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"remote_id INTEGER UNIQUE, " +
							"name TEXT NOT NULL, " +
							"distance REAL NOT NULL, " +
							"promotional_offer TEXT NOT NULL, " +
							"address TEXT NOT NULL, " +
							"private_checkin_amount TEXT NOT NULL, " +
							"public_checkin_amount TEXT NOT NULL, " +
							"latitude TEXT NOT NULL, " + 
							"longitude TEXT NOT NULL, " +
							"is_snap INTEGER NOT NULL)";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			
			createSql = 
					"CREATE TABLE business_orgs (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"business_id INTEGER NOT NULL, " +
							"org_id INTEGER NOT NULL, " +
							"name TEXT NOT NULL); ";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			
			createSql = 
					"CREATE TABLE checkin_times (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER NOT NULL, " +
							"day TEXT NOT NULL, " + 
							"start_time TEXT NOT NULL,  " +
							"end_time TEXT NOT NULL);";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);

			//-----------Create table to store todays checkins---
			createSql = 
					"CREATE TABLE my_checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER UNIQUE, " +
							"checkin_date TEXT NOT NULL);";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);

			createSql = "CREATE TABLE my_causes (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT NOT NULL, " +
					"remote_id INTEGER NOT NULL);";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			//-----------------------------------------------------

			//-----------Create list of tables for Businesses supporting my causes
			createSql = 
					"CREATE TABLE MyOrgsbusinesses (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"remote_id INTEGER UNIQUE, " +
							"name TEXT NOT NULL, " +
							"distance REAL NOT NULL, " +
							"promotional_offer TEXT NOT NULL, " +
							"address TEXT NOT NULL, " +
							"private_checkin_amount TEXT NOT NULL, " +
							"public_checkin_amount TEXT NOT NULL, " +
							"latitude TEXT NOT NULL, " + 
							"longitude TEXT NOT NULL, " +
							"is_snap INTEGER NOT NULL)";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			
			createSql = 
					"CREATE TABLE Mybusiness_orgs (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"business_id INTEGER NOT NULL, " +
							"org_id INTEGER NOT NULL, " +
							"name TEXT NOT NULL); ";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			
			createSql = 
					"CREATE TABLE MyOrgsBusCheckin_times (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER NOT NULL, " +
							"day TEXT NOT NULL, " + 
							"start_time TEXT NOT NULL,  " +
							"end_time TEXT NOT NULL);";
			Log.v("CHECKINFORGOOD", "Creating db: " + createSql);
			db.execSQL(createSql);
			//-------------------------------------------------------------------
		} catch ( Exception e ) {
			Log.e("CHECKINFORGOOD", "Sql creation failed: " + e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w("CHECKINFORGOOD", "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS organizations");
		onCreate(db);
	}
}