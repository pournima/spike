package com.campaignslibrary.dbAdapters;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.campaignslibrary.models.CauseCategoryResult;
import com.checkinlibrary.db.DbAdapter;

public class CauseCategoryDBAdapter extends DbAdapter {

    public CauseCategoryDBAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }

    protected void setDbName() {
        this.dbName = "CauseCategory";
    }

    protected void setDbColumns() {
        this.dbColumns = new String[] { "_id", "name", "remote_id"};
    }    
    
	ContentValues createContentValues(Long id, String name) {
    	
        ContentValues values = new ContentValues();
        values.put("remote_id", id);
        values.put("name", name);
        Log.v("CHECKINFORGOOD", "SUPPORTED FOR " + id.toString());
        return values;
    }

    public List<CauseCategoryResult> getList() {
        Cursor cursor;
        List<CauseCategoryResult> result;
        cursor = fetchAll(null, null);

        result =  cursorToResultList(cursor);
        cursor.close();

        return result;
    }

    private List<CauseCategoryResult> cursorToResultList (Cursor c) {        
        List<CauseCategoryResult> result = new ArrayList<CauseCategoryResult>();

        Log.v("CHECKINFORGOOD", "Cause category result count: " + c.getCount());

        if ( c.moveToFirst() ) {
            int nameColumn = c.getColumnIndex("name"); 
            int remoteIdColumn = c.getColumnIndex("remote_id");
            CauseCategoryResult mResult;
            do {
            	mResult=new CauseCategoryResult(c.getString(nameColumn),c.getInt(remoteIdColumn));
            	result.add(mResult);
            } while ( c.moveToNext() );
        }
        return result;
    }

    public long create(Long remoteId, String name) 
    {	
        ContentValues initialValues = createContentValues(remoteId, name);
        return super.create(initialValues);
    }
    
    public void refresh(List<CauseCategoryResult> result) {
        try {
            db.beginTransaction();
            delete();

            if(!result.isEmpty()) {
                for ( CauseCategoryResult causeCat: result ) {
                	if ( create((long)causeCat.getCategory().getId(), causeCat.getCategory().getName()) == -1 ){
                        Log.v("CHECKINFORGOOD", "CREATION FAILED FOR " + causeCat.getCategory().getName());
                    }
                }
            }
            db.setTransactionSuccessful();
        } catch ( SQLException e ) {
            Log.v("CHECKINFORGOOD", "Sql barf in CauseCategoryDbAdapter::refresh: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

}            