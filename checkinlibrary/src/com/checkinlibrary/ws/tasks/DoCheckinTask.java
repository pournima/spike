package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.business.BusinessCheckinFragment;
import com.checkinlibrary.models.CheckinResult;
import com.checkinlibrary.ws.services.DoCheckinWebService;


public class DoCheckinTask extends AsyncTask<String, Integer, CheckinResult> {
	private BusinessCheckinFragment context;
	private CheckinLibraryActivity main_cxontext;

	public DoCheckinTask(CheckinLibraryActivity main_context,
			BusinessCheckinFragment checkin_context, String[] args) {
		this.context = checkin_context;
		this.main_cxontext = main_context;
	}

	protected void onPostExecute(CheckinResult result) {
		if (result != null)
			context.onAuthenticationResult(result.getSuccess(),
					result.getCheckin_amount(), result.getError_msg(),result.getCheckin_count());
		else {
			context.DisplayToast("Notification failed!");
		}
	}

	protected CheckinResult doInBackground(String... params) {
		// TODO Auto-generated method stub
		return DoCheckinWebService.docheckin(main_cxontext, params);
	}

}