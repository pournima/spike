package com.campaignslibrary.helpers;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;

public class Constants {

	public static Constants INSTANCE = new Constants();

	public final String ONE_DAY_EVENT        = "One Day Event";
	public final String ONGOING_ACT          = "Ongoing Activity";	
	public final String RAISE_AWARENESS      = "Raise Awareness";
	
	public final int REQUEST_CODE = 123;
	public final int SELECT_PHOTO_FROM_GALLERY = 111;
	public final int SELECT_PHOTO_FROM_CAMERA  = 112;
    public Bitmap SELECTED_PHOTO;
   
    public Boolean mFromPhotoSelection=false;
    public List<Bitmap> mSelectedBitmaps=new ArrayList<Bitmap>();
    
    //upload photos flags
  	public final int CREATE_FRAG   = 0;
  	public final int CONGRATS_FRAG = 1;
    
  	
  	//------------CampaignsFragmet Constants--------------
  	public final int SWIPE_MIN_DISTANCE = 80;
  	public final int SWIPE_MAX_OFF_PATH = 300;
  	public final int SWIPE_THRESHOLD_VELOCITY = 150;
}
