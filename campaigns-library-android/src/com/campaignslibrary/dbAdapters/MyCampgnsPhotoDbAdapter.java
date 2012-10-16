package com.campaignslibrary.dbAdapters;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.CreateCampaignResult.CampgnPhotos;
import com.checkinlibrary.db.DbAdapter;

public class MyCampgnsPhotoDbAdapter extends DbAdapter {
    public MyCampgnsPhotoDbAdapter(Context context) {
        super(context);
        setDbName();
        setDbColumns();
    }
    
    protected void setDbName() {
        this.dbName = "MyCampaignsPhotos";
    }
    
    protected void setDbColumns() {
        this.dbColumns = new String[] {"_id", "image_file_name", "image_link","campaign_id","photo_id"};
    }    
    
    ContentValues createContentValues(Integer campaignId, String imgName,String imgLink,Integer photoId) {
        ContentValues values = new ContentValues();
        values.put("campaign_id", campaignId);
        values.put("image_file_name", imgName);
        values.put("image_link", imgLink);
        values.put("photo_id", photoId);
        return values;
    }

    public long create(Integer campaignId, String imgName,String imgLink,Integer photoId) {
        ContentValues initialValues = createContentValues(campaignId, imgName,imgLink,photoId);
        return super.create(initialValues);
    }

    public boolean update(long rowId, Integer campaignId, String imgName,String imgLink,Integer photoId) {
        ContentValues updateValues = createContentValues(campaignId, imgName,imgLink,photoId);
        return super.update(rowId, updateValues);
    }
    
    public List<CampgnPhotos> getList(Integer campaignId) {       
        Cursor myCampPhotosC = fetchAll("campaign_id=" + campaignId.toString(), "1");
        List<CampgnPhotos> result;
        
        result =  cursorToResultList(myCampPhotosC);
        myCampPhotosC.close();
        db.close();
        
        return result;
    }
    
    private List<CampgnPhotos> cursorToResultList (Cursor c) {        
		List<CampgnPhotos> result = new ArrayList<CampgnPhotos>();

		Log.v("CHECKINFORGOOD", "Photos count: " + c.getCount());

		if ( c.moveToFirst() ) {
			int nameColumn = c.getColumnIndex("image_file_name"); 
			int linkColumn = c.getColumnIndex("image_link");
			int campIdColumn = c.getColumnIndex("campaign_id");
			int photoIdColumn = c.getColumnIndex("photo_id");
			
			CreateCampaignResult cmpResult=new CreateCampaignResult();
			do {
				result.add(cmpResult.new CampgnPhotos(c.getString(nameColumn),c.getString(linkColumn),
						c.getInt(photoIdColumn)));
			} while ( c.moveToNext() );
			}
		return result;
	}

}            
