package com.checkinlibrary.ws.tasks;

import com.checkinlibrary.models.VideoResult;
import com.checkinlibrary.settings.HowItWorksFragment;
import com.checkinlibrary.ws.services.HowItWorksWebService;

import android.os.AsyncTask;

public class HowItWorksTask extends AsyncTask<String, Integer, VideoResult> {
	private HowItWorksFragment subContext;

	public HowItWorksTask(HowItWorksFragment context) {
		this.subContext = context;
	}

	protected VideoResult doInBackground(String... create_params) {
		return HowItWorksWebService.getVideoLinks(create_params);
	}

	protected void onPostExecute(VideoResult result) {
		if(result != null){
			subContext.onVideosResult(result);
		}
	}
}
