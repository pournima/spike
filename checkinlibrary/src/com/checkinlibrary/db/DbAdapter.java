package com.checkinlibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

abstract public class DbAdapter {
    protected static SQLiteDatabase db;
    protected static CheckinNativeDatabaseHelper dbHelper;
    protected String dbName;
    protected String[] dbColumns;
    protected Context context;

    public DbAdapter(Context context) {
        super();
        this.context = context;
        if ( unopened() ) DbAdapter.open(context);
    }
    
    abstract protected void setDbName();
    abstract protected void setDbColumns();

	final public static void open(Context context) throws SQLException {

		try {
			int dbVersionName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;

			dbHelper = new CheckinNativeDatabaseHelper(context, dbVersionName);

			db = dbHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			Log.w("CHECKINFORGOOD",
					"OrganizationDbAdapter::getWritableDatabase error: "
							+ e.getMessage());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

    final public static Boolean unopened() {                
        return db == null || !db.isOpen();
    }

    final public static void close() {
        if ( !unopened() ) {
            dbHelper.close();
        }
    }

    public long create(ContentValues initialValues) {
        return db.insert(dbName, null, initialValues);
    }

    public boolean update(Long rowId, ContentValues updateValues) {
        return db.update(dbName, updateValues, "_id=" + rowId, null) > 0;
    }

    public final boolean delete(String where) {
        return db.delete(dbName, where, null) > 0;
    }

    public final void delete() {
        db.delete(dbName, null, null);
    }

    public Cursor fetchAll(String where, String limit) {
        return db.query(dbName, dbColumns,
                where, null, null, null, limit);
    }

    public Cursor fetch(long rowId) throws SQLException {
        Cursor mCursor = db.query(true, dbName, dbColumns,
                "_id=" + rowId, null, null, null, null, null);
        
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
    
        return mCursor;
    }

    final public int getCount() {
        int cnt;
        
        Cursor c = db.rawQuery("SELECT count(*) AS our_count FROM " + dbName, null);
        
        if ( c.moveToFirst() ) {
            cnt = c.getInt(0);
        } else {
            cnt = 0;
        }
        
        c.close();
    
        return cnt;
    }
    
    final public void beginTransaction() {
        db.beginTransaction();
    }
    
    final public void endTransaction() {
        db.endTransaction();
    }

    final public void succeedTransaction() {
        db.setTransactionSuccessful();
    }
}