package com.campaignslibrary.tasks;

import android.os.AsyncTask;

import com.campaignslibrary.CreateCampgnFragment;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;

public class CampaignTask extends AsyncTask<String, Integer, CreateCampaignResult> {
	private CreateCampgnFragment createCampContext;

	public CampaignTask(CreateCampgnFragment context) {
		this.createCampContext = context;
	}

	protected CreateCampaignResult doInBackground(String... create_params) {
		return CampaignWebService.createCampaign(createCampContext, create_params);
	}

	protected void onPostExecute(CreateCampaignResult result) {
		createCampContext.getCreateCampaignResult(result);
	}
}
