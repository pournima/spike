package com.checkinlibrary.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;



public class MyCheckinsDbAdapter extends DbAdapter {
    public MyCheckinsDbAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
        this.dbName = "my_checkins";
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] {"_id", "business_id", "checkin_date"};
    }    
    
    ContentValues createContentValues(Integer businessId, String checkin_day) {
        ContentValues values = new ContentValues();
        values.put("checkin_date", checkin_day);
        values.put("business_id", businessId);

        return values;
    }

    public long create(Integer businessId, String checkin_day) {
        ContentValues initialValues = createContentValues(businessId, checkin_day);
        return super.create(initialValues);
    }

    public boolean update(long rowId, Integer businessId, String checkin_day) {
        ContentValues updateValues = createContentValues(businessId, checkin_day);
        return super.update(rowId, updateValues);
    }
    
    public String getList(Integer businessId) {       
        MyCheckinsDbAdapter mycheckin_db = new MyCheckinsDbAdapter(context);
        Cursor mycheckinC = mycheckin_db.fetchAll("business_id=" + businessId.toString(), "1");
        
        String my_checkin_date = null;
        
        int checkindayColumn = mycheckinC.getColumnIndex("checkin_date");
        
        if ( mycheckinC.moveToFirst() ) {
               my_checkin_date =mycheckinC.getString(checkindayColumn); 
        }
        mycheckinC.close();
        db.close();
        return my_checkin_date;
    }
}            
