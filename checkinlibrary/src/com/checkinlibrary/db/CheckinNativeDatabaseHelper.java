package com.checkinlibrary.db;

import com.checkinlibrary.CheckinLibraryActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CheckinNativeDatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "checkin_native";
	//private static final int DATABASE_VERSION = 1;
	public static boolean bGetVideoLinks = false;

	public CheckinNativeDatabaseHelper(Context context,int dbVersion) {
		super(context, DATABASE_NAME, null, dbVersion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			//Android requires _id
			String createSql;
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
							"barcode_number TEXT NOT NULL, " + 
							"reservation_url TEXT NOT NULL, " + 
							"is_snap INTEGER NOT NULL)";
			createTable(db, createSql);
			
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
							"barcode_number TEXT NOT NULL, " + 
							"reservation_url TEXT NOT NULL, " + 
							"is_snap INTEGER NOT NULL)";
			createTable(db, createSql);
			
			//cause category-------------------------------
			createSql = "CREATE TABLE CauseCategory (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT NOT NULL, " +
					"remote_id INTEGER NOT NULL);";
			createTable(db, createSql);
			
			//-----------Create my campaigns table
			createSql = 
					"CREATE TABLE MyCampaigns (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"remote_id INTEGER UNIQUE, " +
							"end_date TEXT, " +
							"fb_link REAL , " +
							"created_at TEXT NOT NULL, " +
							"category_id INTEGER NOT NULL, " +
							"campaign_sub_type TEXT NOT NULL, " +
							"start_date TEXT NOT NULL, " +
							"is_active INTEGER NOT NULL, " + 
							"description TEXT NOT NULL, " +
							"donation_type TEXT, " +
							"video_link TEXT , " +
							"pledge_for TEXT, " +
							"campaign_type TEXT NOT NULL, " +
							"updated_at TEXT NOT NULL, " +
							"name TEXT NOT NULL, " + 
							"organization_id INTEGER NOT NULL, " +
							"organization_name TEXT NOT NULL, " +
							"supported_by TEXT NOT NULL, " +
							"is_featured INTEGER NOT NULL, " +
							"goal INTEGER NOT NULL, " +
							"public_link TEXT)";
			createTable(db, createSql);
			
			//my campaigns photo -------------------------------
			createSql = "CREATE TABLE MyCampaignsPhotos (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"image_file_name TEXT NOT NULL, " +
					"image_link TEXT NOT NULL, " +
					"campaign_id INTEGER NOT NULL, " +
					"photo_id INTEGER UNIQUE);";
			createTable(db, createSql);
			
			createSql = "CREATE TABLE signals (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"catagory INTEGER NOT NULL, " +
					"image_link_small TEXT NOT NULL, " +
					"image_link_large TEXT NOT NULL, " +
					"text_heading TEXT NOT NULL, " +
					"text_desc TEXT NULL);";
			createTable(db, createSql);

			createSql = "CREATE TABLE organizations (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT NOT NULL, " +
					"remote_id INTEGER NOT NULL, " +
					"is_supported INTEGER NOT NULL, " +
					"users_count INTEGER NOT NULL, " +
					"support_count INTEGER NOT NULL, " +
					"title TEXT NOT NULL, " +
					"image_link TEXT NOT NULL, " +
					"about_us TEXT NOT NULL);";
			createTable(db, createSql);

			createSql = "CREATE TABLE my_causes (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
					"name TEXT NOT NULL, " +
					"remote_id INTEGER NOT NULL, " +
					"users_count INTEGER NOT NULL, " +
					"support_count INTEGER NOT NULL, " +
					"title TEXT NOT NULL, " +
					"image_link TEXT NOT NULL, " +
					"about_us TEXT NOT NULL);";
			createTable(db, createSql);

			createSql = 
					"CREATE TABLE business_orgs (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"business_id INTEGER NOT NULL, " +
							"org_id INTEGER NOT NULL, " +
							"name TEXT NOT NULL); ";
			createTable(db, createSql);

			createSql = 
					"CREATE TABLE checkin_times (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER NOT NULL, " +
							"day TEXT NOT NULL, " + 
							"start_time TEXT NOT NULL,  " +
							"end_time TEXT NOT NULL);";
			createTable(db, createSql);

			//-----------Create table to store todays checkins---
			createSql = 
					"CREATE TABLE my_checkins (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER UNIQUE, " +
							"checkin_date TEXT NOT NULL);";
			createTable(db, createSql);

			//-----------------------------------------------------
			createSql = 
					"CREATE TABLE Mybusiness_orgs (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
							"business_id INTEGER NOT NULL, " +
							"org_id INTEGER NOT NULL, " +
							"name TEXT NOT NULL); ";
			createTable(db, createSql);

			createSql = 
					"CREATE TABLE MyOrgsBusCheckin_times (_id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
							"business_id INTEGER NOT NULL, " +
							"day TEXT NOT NULL, " + 
							"start_time TEXT NOT NULL,  " +
							"end_time TEXT NOT NULL);";
			createTable(db, createSql);
			//-------------------------------------------------------------------
		} catch ( Exception e ) {
			Log.e("CHECKINFORGOOD", "Sql creation failed: " + e.getMessage());
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			Log.w("CHECKINFORGOOD", "Upgrading database from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS organizations");
			db.execSQL("DROP TABLE IF EXISTS my_causes");
			db.execSQL("DROP TABLE IF EXISTS businesses");
			db.execSQL("DROP TABLE IF EXISTS MyOrgsbusinesses");
			onCreate(db);
			CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.appStatus.
					MY_CAUSES_UPGRADE_NEEDED, true);
			CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.appStatus.
					ALL_CAUSE_UPGRADE_NEEDED, true);
			bGetVideoLinks = true;
		}
	}
	
	private void createTable(SQLiteDatabase db,String query){
		try{
			Log.v("CHECKINFORGOOD", "Creating db: " + query);
			db.execSQL(query);
		}catch ( Exception e ) {
			Log.e("CHECKINFORGOOD", "Sql creation failed: " + e.getMessage());
		}
	}
}