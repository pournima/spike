package com.headsup.task;

import android.os.AsyncTask;

import com.headsup.models.SignalsResult;
import com.headsup.rules.signals.SignalsFragment;
import com.headsup.ws.services.SignalsWebService;


public class SignalTask extends AsyncTask<String, Integer, SignalsResult>{

	private SignalsFragment mSignalContext;
	

	public SignalTask(SignalsFragment context) {
		this.mSignalContext=context;
	}



	@Override
	protected SignalsResult doInBackground(String... params) {
		
			return SignalsWebService.getSignalsData(params);
		
	}

	protected void onPostExecute(SignalsResult result) {
		
		mSignalContext.onAuthenticationResult(result);
	}
}
