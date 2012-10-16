package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.models.CauseProfileResult;
import com.checkinlibrary.org.CauseProfileFragment;
import com.checkinlibrary.ws.services.CauseProfileWebService;

public class CauseProfileTask extends
		AsyncTask<String, Integer, CauseProfileResult> {
	private CauseProfileFragment context;
	private CheckinLibraryActivity main_cxontext;

	public CauseProfileTask(CheckinLibraryActivity main_context,
			CauseProfileFragment checkin_context, String[] args) {
		this.context = checkin_context;
		this.main_cxontext = main_context;
	}

	protected void onPostExecute(CauseProfileResult result) {
		if (result != null) {
			context.onCausesprofileResult(result);
		} else {
			context.DisplayToast("Notification failed!");
		}
	}

	protected CauseProfileResult doInBackground(String... params) {
		// TODO Auto-generated method stub
		return CauseProfileWebService.getCauseProfile(main_cxontext, params);
	}

}