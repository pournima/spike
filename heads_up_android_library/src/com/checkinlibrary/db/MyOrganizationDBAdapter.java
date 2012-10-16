package com.checkinlibrary.db;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.checkinlibrary.orgs.CauseResult;

public class MyOrganizationDBAdapter extends DbAdapter {

    public MyOrganizationDBAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }

    protected void setDbName() {
        this.dbName = "my_causes";
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

    public List<CauseResult> getList() {
        Cursor cursor;
        List<CauseResult> result;
        cursor = fetchAll(null, null);

        result =  cursorToResultList(cursor);
        cursor.close();

        return result;
    }

    private List<CauseResult> cursorToResultList (Cursor c) {        
        List<CauseResult> result = new ArrayList<CauseResult>();

        Log.v("CHECKINFORGOOD", "Organization count: " + c.getCount());

        if ( c.moveToFirst() ) {
            int nameColumn = c.getColumnIndex("name"); 
            int remoteIdColumn = c.getColumnIndex("remote_id");
            do {
                result.add(new CauseResult(Integer.toString(c.getInt(remoteIdColumn)),
                        c.getString(nameColumn), "true"));
            } while ( c.moveToNext() );
        }

        return result;
    }

    public long create(Long remoteId, String name) {
        ContentValues initialValues = createContentValues(remoteId, name);
        return super.create(initialValues);
    }
    
    public void refresh(List<CauseResult> result) {
        try {
            db.beginTransaction();
            delete();
            Log.v("CHECKINFORGOOD", "In DB refresh");
            if(!result.isEmpty()) {
                for ( CauseResult cause: result ) {
                	Log.v("CHECKINFORGOOD", "In DB refresh, result is not empty");
                    if ( create((long)cause.getId(), cause.getName()) == -1 ) {
                        Log.v("CHECKINFORGOOD", "CREATION FAILED FOR " + cause.getName());
                    }
                }
            }
            db.setTransactionSuccessful();
        } catch ( SQLException e ) {
            Log.v("CHECKINFORGOOD", "Sql barf in OrganizationDbAdapter::refresh: " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

}            