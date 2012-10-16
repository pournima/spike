package com.campaignslibrary.dbAdapters;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.CreateCampaignResult.CampgnPhotos;
import com.checkinlibrary.db.DbAdapter;

public class MyCampaignsDbAdapter extends DbAdapter {

    public MyCampaignsDbAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
    	 this.dbName = "MyCampaigns";
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] { "_id", "remote_id", "end_date",	"fb_link","created_at",
				"category_id","campaign_sub_type","start_date","is_active","description","donation_type",
				"video_link","pledge_for","campaign_type","updated_at","name","organization_id","organization_name",
				"supported_by","is_featured","goal","public_link" };
    }    
    
    ContentValues createContentValues(CreateCampaignResult mCampaignResult) {
        ContentValues values = new ContentValues();
        values.put("remote_id", mCampaignResult.getCampaign().getId());
        values.put("end_date", mCampaignResult.getCampaign().getEnd_date());
        values.put("fb_link", mCampaignResult.getCampaign().getFb_link());
        values.put("created_at", mCampaignResult.getCampaign().getCreated_at());
        values.put("category_id", mCampaignResult.getCampaign().getCategory_id());
        values.put("campaign_sub_type", mCampaignResult.getCampaign().getCampaign_sub_type());
        values.put("start_date", mCampaignResult.getCampaign().getStart_date());
        values.put("is_active", mCampaignResult.getCampaign().getIs_active()?1:0);
        values.put("description", mCampaignResult.getCampaign().getDescription());
        values.put("donation_type", mCampaignResult.getCampaign().getDonation_type());
        values.put("video_link", mCampaignResult.getCampaign().getVideo_link());
        values.put("pledge_for", mCampaignResult.getCampaign().getPledge_for());
        values.put("campaign_type", mCampaignResult.getCampaign().getCampaign_type());
        values.put("updated_at", mCampaignResult.getCampaign().getUpdated_at());
        values.put("name", mCampaignResult.getCampaign().getName());
        values.put("organization_id", mCampaignResult.getCampaign().getOrganization().getId());
        values.put("organization_name", mCampaignResult.getCampaign().getOrganization().getName());
        values.put("supported_by", mCampaignResult.getCampaign().getSupported_by());
        values.put("is_featured", mCampaignResult.getCampaign().getIs_featured());
        values.put("goal", mCampaignResult.getCampaign().getGoal());
        values.put("public_link", mCampaignResult.getCampaign().getPublic_link());
        
        return values;
    }
    
    public long create(CreateCampaignResult mCampaignResult) {
        ContentValues initialValues = createContentValues(mCampaignResult);
        return super.create(initialValues);
    }
    
    public Boolean createAssociated(CreateCampaignResult mCampaignResult) {
        try {
            this.create(mCampaignResult);
            
          /*  MyCampgnsPhotoDbAdapter photos_db = new MyCampgnsPhotoDbAdapter(this.context);
            List<CampgnPhotos> mCampgnPhotosList = mCampaignResult.getCampaign().getPhotos();
        
            for ( CampgnPhotos mCampgnPhotos: mCampgnPhotosList ) {
            	photos_db.create(mCampaignResult.getCampaign().getId(),mCampgnPhotos.getImage_file_name(),
            			mCampgnPhotos.getImage_link());
            }*/
            return true;
        } catch ( Exception e ) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String stackString = sw.toString();
            Log.e("CHECKINFORGOOD", "Exception in MycampaignsDbAdapter::saveAssociated: " + e.getMessage() +
                    " Exception type: " + e.getClass().toString() + " " + stackString);
            return false;
        }        
    }
    
    //Completely flush the db
    public void deleteAllAssociated() {
        try {
            db.beginTransaction();
            MyCampgnsPhotoDbAdapter photos_db = new MyCampgnsPhotoDbAdapter(this.context);
            photos_db.delete();
            this.delete();
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }
    }
    
    public void deleteAssociated(Integer campgnId) {
        try {
            db.beginTransaction();
            MyCampgnsPhotoDbAdapter photos_db = new MyCampgnsPhotoDbAdapter(this.context);
            photos_db.delete("campaign_id=" + campgnId);
            this.delete("remote_id=" + campgnId);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();            
        }
    }
    
    public CreateCampaignResult getResult(Integer remoteId) {
        CreateCampaignResult mCampaignResult = null;
        Cursor c = this.fetchAll("remote_id=" + remoteId.toString(), "1");
        
        if ( c.moveToFirst() ) {     
        	HashMap<String, String> campgnParams = getResultParamsFromCursor(c);
        	
        	 MyCampgnsPhotoDbAdapter photos_db = new MyCampgnsPhotoDbAdapter(this.context);
            List<CampgnPhotos> mCampgnPhotosList = photos_db.getList(remoteId); 
            
            mCampaignResult = new CreateCampaignResult(campgnParams, mCampgnPhotosList);
        }
        
        c.close();
        
        return mCampaignResult;
    }
    
    public HashMap<String, String> getResultParamsFromCursor(Cursor c) {
        String [] columnNames = new String[] {"remote_id", "end_date",	"fb_link","created_at",
				"category_id","campaign_sub_type","start_date","is_active","description","donation_type",
				"video_link","pledge_for","campaign_type","updated_at","name","organization_id",
				"organization_name","supported_by","is_featured","goal","public_link"};
        
        HashMap<String, String> campgnParams = new HashMap<String, String>();
        Integer columnIndex;
        
        for ( String columnName: columnNames ) {
            columnIndex = c.getColumnIndex(columnName);
            campgnParams.put(columnName, c.getString(columnIndex));
        }
        
        return campgnParams;
    }

    public boolean update(long rowId, CreateCampaignResult mCampaignResult) {
    	ContentValues updateValues = createContentValues(mCampaignResult);
    	return super.update(rowId, updateValues);
    }
    
    public List<CreateCampaignResult> getList() {
        Cursor cursor;
        List<CreateCampaignResult> result;
        cursor = fetchAll(null, null);

        result =  cursorToResultList(cursor);
        cursor.close();

        return result;
    }

	private List<CreateCampaignResult> cursorToResultList(Cursor c) {
		List<CreateCampaignResult> result = new ArrayList<CreateCampaignResult>();

        Log.v("CHECKINFORGOOD", "MyCampaigns count: " + c.getCount());
        
        while(c.moveToNext()){
        	HashMap<String, String> campgnParams = getResultParamsFromCursor(c);
        	
        	int remoteIdColumn = c.getColumnIndex("remote_id");

        	MyCampgnsPhotoDbAdapter photos_db = new MyCampgnsPhotoDbAdapter(this.context);
        	List<CampgnPhotos> mCampgnPhotosList = photos_db.getList(c.getInt(remoteIdColumn)); 

        	result.add(new CreateCampaignResult(campgnParams, mCampgnPhotosList));
        }
        return result;
	}
    
}        