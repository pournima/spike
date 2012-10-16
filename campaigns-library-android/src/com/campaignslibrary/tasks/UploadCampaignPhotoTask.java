package com.campaignslibrary.tasks;

import android.os.AsyncTask;

import com.campaignslibrary.CampaignCongratsFragment;
import com.campaignslibrary.CreateCampgnFragment;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;

public class UploadCampaignPhotoTask extends AsyncTask<String, Integer, CreateCampaignResult> {
	private CreateCampgnFragment createCampContext;
	private CampaignCongratsFragment congratsCampContext;
	
	private int iFromWhere; // from 0 =createcampfrag, 1= congrats  frag 

	public UploadCampaignPhotoTask(CreateCampgnFragment context,int iIsFrom) {
		this.createCampContext = context;
		this.iFromWhere=iIsFrom;
	}
	
	public UploadCampaignPhotoTask(CampaignCongratsFragment context,int iIsFrom) {
		this.congratsCampContext = context;
		this.iFromWhere=iIsFrom;
	}

	protected CreateCampaignResult doInBackground(String... create_params) {	
		return CampaignWebService.uploadCampaignPhotos(create_params);
	}

	protected void onPostExecute(CreateCampaignResult result) {
		if(this.iFromWhere == Constants.INSTANCE.CREATE_FRAG)
			createCampContext.getUploadPhotosResult(result);
		else if(this.iFromWhere ==  Constants.INSTANCE.CONGRATS_FRAG)
			congratsCampContext.getUploadPhotosResult(result);
	}
}
