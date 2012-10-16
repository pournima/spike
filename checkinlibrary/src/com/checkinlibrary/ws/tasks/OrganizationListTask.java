package com.checkinlibrary.ws.tasks;

import java.util.List;

import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.org.CauseFragment;
import com.checkinlibrary.ws.services.OrganizationWebService;

import android.os.AsyncTask;

public class OrganizationListTask extends AsyncTask<Double, Integer, List<OrganizationResult>> {
    private CauseFragment fragment;
    int page;
    boolean bIsSupported = false;

    public OrganizationListTask(CauseFragment myFragment,int page, boolean bIsSupported) {
        this.fragment = myFragment;
        this.page = page;
        this.bIsSupported = bIsSupported;
    }

    protected List<OrganizationResult> doInBackground(Double... locationParams) {
    	if(!this.bIsSupported){
    	return OrganizationWebService.getAll(locationParams,page);
    	} else {
    		return OrganizationWebService.getMy(locationParams);
		}
    }

	protected void onPostExecute(List<OrganizationResult> result) {
		// If for some freakish reason, the db is slower than the network, and
		// it is still running,
		// show its results.
		if (result != null) {
			if (!this.bIsSupported) {
				fragment.storeAllCausesInDB(result);
			} else {
				fragment.storeMyCausesinDB(result);
			}
		}
	}
}
