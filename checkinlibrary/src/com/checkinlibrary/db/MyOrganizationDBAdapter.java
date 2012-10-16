package com.checkinlibrary.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;

import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.models.OrganizationResult.cause_profile_result;


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
        this.dbColumns = new String[] { "_id", "name", "remote_id","users_count","support_count",
					"title","image_link","about_us"};
    }    
    
	ContentValues createContentValues(Long id, String name,int users_count,
			int support_count,String title,String image_link,String about_us) {
    	
        ContentValues values = new ContentValues();
        values.put("remote_id", id);
        values.put("name", name);
        values.put("users_count", users_count);
		values.put("support_count", support_count);
		values.put("title", title);
		values.put("image_link", image_link);
		values.put("about_us", about_us);
        Log.v("CHECKINFORGOOD", "SUPPORTED FOR " + id.toString());
        return values;
    }

    public List<OrganizationResult> getList() {
        Cursor cursor;
        List<OrganizationResult> result;
        cursor = fetchAll(null, null);

        result =  cursorToResultList(cursor);
        cursor.close();

        return result;
    }

    private List<OrganizationResult> cursorToResultList (Cursor c) {        
        List<OrganizationResult> result = new ArrayList<OrganizationResult>();

        Log.v("CHECKINFORGOOD", "Organization count: " + c.getCount());

        if ( c.moveToFirst() ) {
            int nameColumn = c.getColumnIndex("name"); 
            int remoteIdColumn = c.getColumnIndex("remote_id");
            int userCntColumn = c.getColumnIndex("users_count"); 
			int supoCntColumn = c.getColumnIndex("support_count");
			int titleColumn = c.getColumnIndex("title");
			int imgLinkColumn = c.getColumnIndex("image_link"); 
			int abtUsColumn = c.getColumnIndex("about_us");
			
            do {
            	cause_profile_result mResult=new cause_profile_result(c.getString(titleColumn),c.getString(abtUsColumn),
    					c.getString(imgLinkColumn));
            	result.add(new OrganizationResult(mResult,c.getInt(supoCntColumn),c.getString(nameColumn),
						c.getInt(userCntColumn),c.getInt(remoteIdColumn),true));
            } while ( c.moveToNext() );
        }

        return result;
    }

    public long create(Long remoteId, String name,int users_count,
			int support_count,String title,String image_link,String about_us) 
    {
    	if(title==null)
    		title="";
    	if(image_link == null)
    		image_link="";
    	if(about_us == null)
    		about_us="";
    	
        ContentValues initialValues = createContentValues(remoteId, name,users_count,
				support_count,title,image_link,about_us);
        return super.create(initialValues);
    }
    
    public void refresh(List<OrganizationResult> result) {
        try {
            db.beginTransaction();
            delete();

            if(!result.isEmpty()) {
                for ( OrganizationResult cause: result ) {
                	String title,image_link,about_us;
                	if(cause.getOrganization().getCause_profile() == null){
                		title="";image_link="";about_us="";
                	}else{
                		title=cause.getOrganization().getCause_profile().getTitle();
                		image_link=cause.getOrganization().getCause_profile().getImage_link();
                		about_us=cause.getOrganization().getCause_profile().getAbout_us();
                	}
                	
                	if ( create((long)cause.getOrganization().getId(), cause.getOrganization().getName(), cause.getOrganization().getUsers_count(),cause.getOrganization().getSupport_count(),
                			title,image_link,about_us) == -1 ){
                        Log.v("CHECKINFORGOOD", "CREATION FAILED FOR " + cause.getOrganization().getName());
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