package com.checkinlibrary.ws.tasks;

import com.checkinlibrary.account.SignupActivity;
import com.checkinlibrary.models.LoginResult;
import com.checkinlibrary.ws.services.LoginWebService;

import android.os.AsyncTask;

public class CreateAccountTask extends
AsyncTask<String, Integer, LoginResult> {
	private SignupActivity context;
    private boolean bIsFromSocialLogin=false;
    
	public CreateAccountTask(SignupActivity context,Boolean bIsFromSCL) {
		this.context = context;
		this.bIsFromSocialLogin=bIsFromSCL;
	}
	
	protected LoginResult doInBackground(String... create_params) {
		if(bIsFromSocialLogin){
			return LoginWebService.createSocialSignup(context, create_params);
		}else{
			return LoginWebService.create(context, create_params);	
		}
	}

	protected void onPostExecute(LoginResult result) {
		if(result != null)
			context.onAuthenticationResult(result);
		else {
			context.DisplayToast("Creating account failed!");
		}
	}
}
