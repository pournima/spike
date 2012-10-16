package com.campaignslibrary.tasks;

import java.util.List;

import android.os.AsyncTask;

import com.campaignslibrary.MyCampaignFragment;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;

public class MyCampaignTask extends AsyncTask<String, Integer, List<CreateCampaignResult>> {
	private MyCampaignFragment campgnContext;

	
	public MyCampaignTask(MyCampaignFragment context) {
		this.campgnContext = context;
	}

	protected List<CreateCampaignResult> doInBackground(String... my_campgn_params) {		
		return CampaignWebService.myCampaigns(my_campgn_params);
	}

	protected void onPostExecute(List<CreateCampaignResult> result) {
		campgnContext.getCampaignResult(result);
	}
}
