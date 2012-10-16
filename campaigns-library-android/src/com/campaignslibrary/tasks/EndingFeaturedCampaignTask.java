package com.campaignslibrary.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.campaignslibrary.CampaignsFragment;
import com.campaignslibrary.models.CampaignDetailResult;
import com.campaignslibrary.ws.services.CampaignWebService;

public class EndingFeaturedCampaignTask extends AsyncTask<String, Integer, CampaignDetailResult> {
	private CampaignsFragment campgnContext;

	public EndingFeaturedCampaignTask(CampaignsFragment context) {
		this.campgnContext = context;
	}

	protected CampaignDetailResult doInBackground(String... create_params) {		
		return CampaignWebService.featuredCampaign(campgnContext, create_params);
	}

	protected void onPostExecute(CampaignDetailResult result) {
		Log.i("EndingFeaturedCampaignTask", "onPostExecute");
		campgnContext.getCampaignResult(result);
	}
}
