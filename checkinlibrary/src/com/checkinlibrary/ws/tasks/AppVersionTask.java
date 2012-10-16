package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;
import android.util.Log;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.models.AppVersionResult;
import com.checkinlibrary.ws.services.AppVersionWebService;

public class AppVersionTask extends AsyncTask<String, Integer, AppVersionResult> {

	private CheckinLibraryActivity context;
	
	public AppVersionTask(CheckinLibraryActivity context) {
		this.context = context;
	}
	
	@Override
	protected AppVersionResult doInBackground(String... arg0) {
		
		return AppVersionWebService.setAppVersion(context, arg0);
	}

	protected void onPostExecute(AppVersionResult result) {
		if(result != null){				
			context.getAppVersion(result);
		}
		else {
			Log.e("app version call --", "fail");
		}
		
	}

}
