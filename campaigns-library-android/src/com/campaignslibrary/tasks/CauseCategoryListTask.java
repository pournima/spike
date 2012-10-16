package com.campaignslibrary.tasks;

import java.util.List;

import android.os.AsyncTask;

import com.campaignslibrary.CreateCampgnFragment;
import com.campaignslibrary.models.CauseCategoryResult;
import com.campaignslibrary.ws.services.CampaignsOrgWebService;

public class CauseCategoryListTask extends AsyncTask<String, Integer, List<CauseCategoryResult>> {
    private CreateCampgnFragment fragment;
   
    public CauseCategoryListTask(CreateCampgnFragment myFragment) {
        this.fragment = myFragment;
    }

    protected List<CauseCategoryResult> doInBackground(String... locationParams) {
    	return CampaignsOrgWebService.getCauseCategoryList(locationParams);
    }

	protected void onPostExecute(List<CauseCategoryResult> result) {
			fragment.storeCauseCategoryListinDB(result);
	}
}
