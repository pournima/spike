package com.checkinlibrary.db;



import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.checkinlibrary.offers.BusinessResult.CheckinTimeResult;

public class CheckinTimeDbAdapter extends DbAdapter {
	private String mDbName;
    public CheckinTimeDbAdapter(Context context,String mDbName) {
        super(context);
        this.mDbName=mDbName;
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
        //this.dbName = "checkin_times";
    	this.dbName = mDbName;
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] {"_id", "business_id", "day", "start_time", "end_time"};
    }    
    
    ContentValues createContentValues(Integer businessId, CheckinTimeResult checkin) {
        ContentValues values = new ContentValues();
        values.put("day", checkin.day);
        values.put("business_id", businessId);
        values.put("start_time", checkin.start_time.toString());
        values.put("end_time", checkin.end_time.toString());

        return values;
    }

    public long create(Integer businessId, CheckinTimeResult checkin) {
        ContentValues initialValues = createContentValues(businessId, checkin);
        return super.create(initialValues);
    }

    public boolean update(long rowId, Integer businessId, CheckinTimeResult checkin) {
        ContentValues updateValues = createContentValues(businessId, checkin);
        return super.update(rowId, updateValues);
    }
    
    public List<CheckinTimeResult> getList(Integer businessId) {
        CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this.context,this.dbName);
        Cursor checkinC = checkin_db.fetchAll("business_id=" + businessId.toString(), null);
        List<CheckinTimeResult> checkins = new ArrayList<CheckinTimeResult>();
        
        int dayColumn = checkinC.getColumnIndex("day");
        int startDateColumn = checkinC.getColumnIndex("start_time");
        int endDateColumn = checkinC.getColumnIndex("end_time");
        
        if ( checkinC.moveToFirst() ) {
            do {
                CheckinTimeResult checkin = new CheckinTimeResult(checkinC.getString(dayColumn), 
                        checkinC.getString(startDateColumn), checkinC.getString(endDateColumn) );
                checkins.add(checkin);
            } while ( checkinC.moveToNext() );
        }
        
        checkinC.close();
        
        return checkins;
    }
}            
