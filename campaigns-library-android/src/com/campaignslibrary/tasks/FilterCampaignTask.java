package com.campaignslibrary.tasks;

import java.util.List;

import android.os.AsyncTask;

import com.campaignslibrary.CampaignsSearchFragment;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;
import com.checkinlibrary.CheckinLibraryActivity;

public class FilterCampaignTask extends AsyncTask<String, Integer, List<CreateCampaignResult>> {
	protected CheckinLibraryActivity context;

	public FilterCampaignTask(CheckinLibraryActivity context) {
		this.context = context;
	}

	protected List<CreateCampaignResult> doInBackground(String... create_params) {
		return CampaignWebService.filterCampaigns(create_params);
	}

	protected void onPostExecute(List<CreateCampaignResult> result) {
		CampaignsSearchFragment.addFilterCampaignsResult(context,result);
	}
}
