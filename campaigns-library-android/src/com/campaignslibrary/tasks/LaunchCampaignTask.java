package com.campaignslibrary.tasks;

import android.os.AsyncTask;

import com.campaignslibrary.CampaignshareFragment;
import com.campaignslibrary.models.LaunchCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;

public class LaunchCampaignTask extends AsyncTask<String, Integer, LaunchCampaignResult> {
	private CampaignshareFragment shareCampContext;
	
	public LaunchCampaignTask(CampaignshareFragment context) {
		this.shareCampContext = context;
	}

	protected LaunchCampaignResult doInBackground(String... create_params) {
		return CampaignWebService.launchCampaign(create_params);
	}

	protected void onPostExecute(LaunchCampaignResult result) {
		shareCampContext.getLaunchCampaignResult(result);
	}
}
