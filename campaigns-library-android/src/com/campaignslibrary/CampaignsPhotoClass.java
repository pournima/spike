package com.campaignslibrary;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import com.campaignslibrary.dbAdapters.MyCampgnsPhotoDbAdapter;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.CreateCampaignResult.CampgnPhotos;
import com.checkinlibrary.CheckinLibraryActivity;
import com.google.myjson.Gson;

public class CampaignsPhotoClass {
	CheckinLibraryActivity context;
	
	public CampaignsPhotoClass(CheckinLibraryActivity context) {
		this.context=context;
	}
	
	/**
	 * This method use to add photo i.e open gallery or camera to take photo
	 */
	public void showPhotoOptions(){
		AlertDialog exitAlert = new AlertDialog.Builder(context).create();
		exitAlert.setTitle("Select Photo");

		exitAlert.setButton("Camera", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				takePhotoFromCamera();
			}
		});
		exitAlert.setButton2("Gallery", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				takePhotoFromGallery();
			}
		});
		exitAlert.show();
	}
	
	/**
	 * This method use to open gallery to take photo
	 */
	private void takePhotoFromGallery(){
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		context.startActivityForResult(Intent.createChooser(intent,"Select Photo "), 
				Constants.INSTANCE.SELECT_PHOTO_FROM_GALLERY);
	}

	/**
	 * This method use to add open Camera to take photo
	 */
	private void takePhotoFromCamera(){
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE); 
		context.startActivityForResult(cameraIntent, Constants.INSTANCE.SELECT_PHOTO_FROM_CAMERA); 
	}

	/**
	 * This method use to create json for upload photos  
	 * api call
	 * @return JSONString converted final json string
	 */
	public String getJsonFromPhotos() {
		int iLength = 0;
		Gson gson = new Gson();
		List<photoResult> photos = new ArrayList<photoResult>();
		
		if(Constants.INSTANCE.mSelectedBitmaps == null ){
			return null;
		}
		try {			
			iLength = Constants.INSTANCE.mSelectedBitmaps.size();
			if(iLength < 1){
				return null;
			}

			for (int i = 0; i < iLength; i++) {
				Bitmap bitmap = Constants.INSTANCE.mSelectedBitmaps.get(i);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
				byte[] byteArrayImage = baos.toByteArray();

				String encodedImage = Base64.encodeToString(byteArrayImage,
						Base64.NO_WRAP);

				photoResult ph = new photoResult();
				String filename = String.valueOf(Calendar.getInstance()
						.getTimeInMillis()+(i+1));
				ph.photo_file_name = filename + ".jpeg";// "photo"+String.valueOf(i+1);
				ph.encoded_photo = encodedImage;
				photos.add(ph);
			}
		} catch (Exception e) {
			Log.i("getJsonFromPhotos", "Exception while create json for photos "+e);
		}
		// create json for photos///////
		String JSONString = gson.toJson(photos);
		return JSONString;
	}
	//-----------class to create json for photos-----------
	public class photoResult {
		String photo_file_name;
		String encoded_photo;
	}

	/**
	 * This method use to store uploaded photo in DB 
	 * 
	 */
	public void storeCampaignPhotos(CreateCampaignResult result) {
		MyCampgnsPhotoDbAdapter adpt = new MyCampgnsPhotoDbAdapter(context);
		if(result != null && result.getCampaign() != null && result.getCampaign().getPhotos()!= null){
			try {
				adpt.beginTransaction();
			
				for(int i=0;i<result.getCampaign().getPhotos().size();i++){
					CampgnPhotos mCampgnPhotos=result.getCampaign().getPhotos().get(i);
					adpt.create(result.getCampaign().getId(),mCampgnPhotos.getImage_file_name()
							, mCampgnPhotos.getImage_link(),mCampgnPhotos.getId());
					
					Log.i("CHECKINFORGOOD", "Id for photo res " + mCampgnPhotos.getImage_file_name()+
							String.valueOf(mCampgnPhotos.getId()));

				}
				adpt.succeedTransaction();
			} finally {
				adpt.endTransaction();                
			}  
		}
	}
}
