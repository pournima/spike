package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;
import android.util.Log;

import com.checkinlibrary.ws.services.SocialSharingWebService;


public class FacebookExpiryTask extends AsyncTask<String, Integer, Boolean> {
	
	public FacebookExpiryTask() {
	}

	protected void onPostExecute(Boolean result) {
		Log.i("FB access token expired : ", result.toString());
	}

	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		return SocialSharingWebService.checkFBTokenExpiry(params);
	}

}