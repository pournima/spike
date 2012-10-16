package com.checkinlibrary.db;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.BusinessResult.CheckinTimeResult;
import com.checkinlibrary.models.BusinessResult.LocationResult.BusinessResultResource.BizOrgSupportsResult;


//@SuppressLint("UseValueOf")
public class BusinessDbAdapter extends DbAdapter {
	private String mDbName,mDbBusOrgsName,mDbCheckinTimeName;
	
    public BusinessDbAdapter(Context context,String DbName,String DbBusOrgsName,String DbCheckinTimeName) {
        super(context);
        this.mDbName=DbName;
        this.mDbBusOrgsName=DbBusOrgsName;
        this.mDbCheckinTimeName=DbCheckinTimeName;
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
       // this.dbName = "businesses";
    	 this.dbName = mDbName;
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] { "_id", "remote_id", "distance", "name", "promotional_offer", "address", 
                "private_checkin_amount", "public_checkin_amount", "latitude", "longitude","barcode_number",
                "reservation_url","is_snap" };
    }    
    
    ContentValues createContentValues(BusinessResult business) {
        ContentValues values = new ContentValues();
        values.put("remote_id", business.getId());
        values.put("name", business.getName());
        values.put("distance", business.getDistance());
        values.put("promotional_offer", business.getPromotionalOffer());
        values.put("address", business.getAddress());
        values.put("private_checkin_amount", business.getPrivateCheckinAmount().toString());
        values.put("public_checkin_amount", business.getPublicCheckinAmount().toString());
        values.put("is_snap", business.needsSnap()?1:0);
        values.put("barcode_number", business.getBarcodeNumber());
        values.put("reservation_url", business.getReservationUrl());
        Double [] latLng = business.getLatLng();
        values.put("latitude", latLng[0]);
        values.put("longitude", latLng[1]);
        return values;
    }
    
    public long create(BusinessResult business) {
        ContentValues initialValues = createContentValues(business);
        return super.create(initialValues);
    }
    
    public Boolean createAssociated(BusinessResult business) {
        try {
            this.create(business);
            
            List<HashMap<String, String>> orgs = business.getOrganizations();        
            BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this.context,this.mDbBusOrgsName);
        
            for ( HashMap<String, String> org: orgs ) {
                bus_org_db.create(business.getId(), new Integer(org.get("id")), org.get("name"));
            }

            CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this.context,this.mDbCheckinTimeName);
            List<CheckinTimeResult> checkins = business.getCheckinTimes();
        
            for ( CheckinTimeResult checkin: checkins ) {
                checkin_db.create(business.getId(), checkin);
            }

            return true;
        } catch ( Exception e ) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackString = sw.toString();
            Log.e("CHECKINFORGOOD", "Exception in BusinessDbAdapter::saveAssociated: " + e.getMessage() +
                    " Exception type: " + e.getClass().toString() + " " + stackString);
            return false;
        }        
    }
    
    //Completely flush the db
    public void deleteAllAssociated() {
        try {
            db.beginTransaction();
            BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this.context,this.mDbBusOrgsName);
            bus_org_db.delete();
            CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this.context,this.mDbCheckinTimeName);
            checkin_db.delete();
            this.delete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }
    }
    
    public void deleteAssociated(Integer businessId) {
        try {
            db.beginTransaction();
            BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this.context,this.mDbBusOrgsName);
            bus_org_db.delete("business_id=" + businessId);
            CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this.context,this.mDbCheckinTimeName);
            checkin_db.delete("business_id=" + businessId);
        
            this.delete("remote_id=" + businessId);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }

    }
    
    public BusinessResult getResult(Integer remoteId) {
        BusinessResult business = null;
        Cursor c = this.fetchAll("remote_id=" + remoteId.toString(), "1");
        
        if ( c.moveToFirst() ) {
            
            HashMap<String, String> businessParams = getResultParamsFromCursor(c);

            BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this.context,this.mDbBusOrgsName);    
            List<BizOrgSupportsResult> orgs = bus_org_db.getList(remoteId);                 
                
            CheckinTimeDbAdapter checkin_db = new CheckinTimeDbAdapter(this.context,this.mDbCheckinTimeName);    
            List<CheckinTimeResult> checkins = checkin_db.getList(remoteId); 
            
            business = new BusinessResult(businessParams, checkins, orgs);
        }
        
        c.close();
        
        return business;
    }
    
    //made this function public because we want to call fetchAll from BusinessFragment to fetch list of all businesses
    public HashMap<String, String> getResultParamsFromCursor(Cursor c) {
        String [] columnNames = new String[] {"remote_id", "name", "distance", "promotional_offer", "address",
                "private_checkin_amount", "public_checkin_amount", "latitude", "longitude","barcode_number",
                "reservation_url","is_snap"};
        HashMap<String, String> businessParams = new HashMap<String, String>();
        Integer columnIndex;
        
        for ( String columnName: columnNames ) {
            columnIndex = c.getColumnIndex(columnName);
            businessParams.put(columnName, c.getString(columnIndex));
        }
        
        return businessParams;
    }

    public boolean update(long rowId, BusinessResult business) {
        ContentValues updateValues = createContentValues(business);
        return super.update(rowId, updateValues);
    }
    
}            