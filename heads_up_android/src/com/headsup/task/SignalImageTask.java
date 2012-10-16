package com.headsup.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.headsup.rules.signals.SignalsFragment;

public class SignalImageTask extends AsyncTask<String, Void, String>{

	private SignalsFragment mSignalContext;
	Context context;
	ImageView mImage;
	
	  public SignalImageTask(SignalsFragment context,ImageView mImage) {
	        this.mImage = mImage;
	        this.mSignalContext=context;
	    }
	
	@Override
	protected String doInBackground(String... arg0) {

		try{
			mSignalContext.mImageLoader.DisplayImage(arg0[0], mImage);
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
	}

	protected void onPostExecute(String result) {
	//	mSignalContext.onSignalImageResult(result);
	}
}
