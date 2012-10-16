package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.org.CauseProfileFragment;
import com.checkinlibrary.ws.services.OrganizationWebService;


public class SupportCauseTask extends AsyncTask<Integer, Integer, Boolean[]> {
	private CauseProfileFragment mCauseProfileFragment;
	private Integer orgId;
	private Boolean isSupported;
	CheckinLibraryActivity context;

	public SupportCauseTask(CheckinLibraryActivity main_context,CauseProfileFragment mCauseProFrag,Boolean bSupported) {
		this.isSupported = bSupported;
		this.mCauseProfileFragment = mCauseProFrag;
		this.context=main_context;
	}

	protected Boolean[] doInBackground(Integer... orgId) {
		this.orgId = orgId[0];
		return OrganizationWebService.supportOrg(this.orgId, isSupported);
	}

	protected void onPostExecute(Boolean [] success) {
		mCauseProfileFragment.updateSupport(success);
	}
}