package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;

import com.checkinlibrary.checkin.CheckinActivity;
import com.checkinlibrary.checkin.CheckinResult;
import com.checkinlibrary.ws.services.DoCheckinWebService;

public class DoCheckinTask extends AsyncTask<String, Integer, CheckinResult> {
	private CheckinActivity context;

	public DoCheckinTask(CheckinActivity checkin_context,
			String[] args) {
		this.context = checkin_context;
	}

	protected void onPostExecute(CheckinResult result) {
		if (result != null)
			context.onAuthenticationResult(result.getSuccess(),
					result.getCheckin_amount(), result.getError_msg(),
					result.getCheckin_count());
		else {
			context.DisplayToast("Notification failed!");
		}
	}

	protected CheckinResult doInBackground(String... params) {
		// TODO Auto-generated method stub
		return DoCheckinWebService.docheckin(context, params);
	}

}