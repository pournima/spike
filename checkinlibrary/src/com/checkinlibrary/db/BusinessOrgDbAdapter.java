package com.checkinlibrary.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.checkinlibrary.models.BusinessResult.LocationResult.BusinessResultResource.BizOrgSupportsResult;


public class BusinessOrgDbAdapter extends DbAdapter {
	private String mDbName;
    public BusinessOrgDbAdapter(Context context,String mDbName) {
        super(context);
        this.mDbName=mDbName;
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
       // this.dbName = "business_orgs";
    	 this.dbName = mDbName;
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] {"_id", "business_id", "org_id", "name"};
    }    
    
    ContentValues createContentValues(Integer businessId, Integer orgId, String name) {
        ContentValues values = new ContentValues();
        values.put("business_id", businessId);
        values.put("org_id", orgId);
        values.put("name", name);

        return values;
    }

    public long create(Integer businessId, Integer orgId, String name) {
        ContentValues initialValues = createContentValues(businessId, orgId, name);
        return super.create(initialValues);
    }

    public boolean update(long rowId, Integer businessId, Integer orgId, String name) {
        ContentValues updateValues = createContentValues(businessId, orgId, name);
        return super.update(rowId, updateValues);
    }
    
    public List<BizOrgSupportsResult> getList(Integer businessId) {
        BusinessOrgDbAdapter bus_org_db = new BusinessOrgDbAdapter(this.context,this.dbName);
        Cursor busOrgC = bus_org_db.fetchAll("business_id=" + businessId.toString(), null);
        List<BizOrgSupportsResult> orgs = new ArrayList<BizOrgSupportsResult>();

        Integer idColumn = busOrgC.getColumnIndex("org_id");
        Integer orgNameColumn = busOrgC.getColumnIndex("name");
        
        if ( busOrgC.moveToFirst() ) {
            do {
                BizOrgSupportsResult org = new BizOrgSupportsResult(busOrgC.getInt(idColumn), busOrgC.getString(orgNameColumn));
                orgs.add(org);
            } while ( busOrgC.moveToNext() );                    
        }
        
        busOrgC.close();
        
        return orgs;
    }
}            
