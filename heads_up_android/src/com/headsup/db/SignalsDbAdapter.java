package com.headsup.db;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.checkinlibrary.db.DbAdapter;
import com.headsup.models.SignalsResult;
import com.headsup.models.SignalsResult.SignalCategory;

public class SignalsDbAdapter extends DbAdapter {
	
    public SignalsDbAdapter(Context context) {
        super(context);
       
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
    	 this.dbName = "signals";
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] { "_id", "catagory", "image_link_small", "image_link_large", "text_heading", "text_desc" };
    }    
    
    ContentValues createContentValues(SignalCategory signal) {
        ContentValues values = new ContentValues();
        values.put("catagory",signal.getCatagory_name());
        values.put("image_link_small", signal.getImage_link_small());
        values.put("image_link_large", signal.getImage_link_large());
        values.put("text_heading", signal.getText_heading());
        values.put("text_desc", signal.getText_desc());
        return values;
    }
    
    
    
    public long create(SignalCategory signal) {
        ContentValues initialValues = createContentValues(signal);
        return super.create(initialValues);
    }
    
    //Completely flush the db
    public void deleteAllAssociated() {
        try {
            db.beginTransaction();
            this.delete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }
    }
    
    public void deleteAssociated(String imageLinkLarge) {
        try {
            db.beginTransaction();
        
            this.delete("image_link_large=" + imageLinkLarge );
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }

    }
   
    public SignalsResult getList(int catagory) {
    	SignalsResult signal = null;
    	Cursor c= fetchAll("catagory=" + catagory,"1");
    	signal = new SignalsResult();
    	SignalCategory mSignalCategory;
    	List<SignalCategory> result = new ArrayList<SignalCategory>();
    	while (c.moveToNext()) {
    		
             HashMap<String, String> signalParams = getResultParamsFromCursor(c);
             mSignalCategory = signal.new SignalCategory(signalParams);
			result.add(mSignalCategory);
		}
        signal.setCatagories(result);
        c.close();
        return signal;
    }
    
    //made this function public because we want to call fetchAll from BusinessFragment to fetch list of all businesses
    public HashMap<String, String> getResultParamsFromCursor(Cursor c) {
        String [] columnNames = new String[] {"catagory", "image_link_small", "image_link_large", "text_heading", "text_desc" };
        HashMap<String, String> signalsParams = new HashMap<String, String>();
        Integer columnIndex;
        
        for ( String columnName: columnNames ) {
            columnIndex = c.getColumnIndex(columnName);
            signalsParams.put(columnName, c.getString(columnIndex));
        }
        
        return signalsParams;
    }

    public boolean update(long rowId, SignalCategory signals) {
        ContentValues updateValues = createContentValues(signals);
        return super.update(rowId, updateValues);
    }
    
    //*****SEARCH BY SIGNAL HEAD ******
    public List<SignalCategory> getSearchList(String searchHeading) {
    	SignalsResult signal = null;
    	//Cursor c= fetchAll("text_heading LIKE '%" + searchHeading +"%'","5");
    	String sqlQuery="select * from signals where text_heading LIKE '%"+ searchHeading +"%' group by text_heading";
    	Cursor c = db.rawQuery(sqlQuery, null);
    	signal = new SignalsResult();
    	SignalCategory mSignalCategory;
    	List<SignalCategory> result = new ArrayList<SignalCategory>();
    	while (c.moveToNext()) {
    		
             HashMap<String, String> signalParams = getResultParamsFromCursor(c);
             mSignalCategory = signal.new SignalCategory(signalParams);
			result.add(mSignalCategory);
		}
        signal.setCatagories(result);
        c.close();
        return result;
    }
   

}            