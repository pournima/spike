package com.checkinlibrary.ws.tasks;

import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.models.LoginResult;
import com.checkinlibrary.ws.services.LoginWebService;

import android.os.AsyncTask;

public class LoginTask extends AsyncTask<String, Integer, LoginResult> {
	private LoginActivity context;
	 private boolean bIsFromSocialLogin=false;
	 
	public LoginTask(LoginActivity context,Boolean bIsFromSCL) {
		this.context = context;
		this.bIsFromSocialLogin=bIsFromSCL;
	}

	protected LoginResult doInBackground(String... login_params) {
		if(bIsFromSocialLogin){
			return LoginWebService.createSocialLogin(context, login_params);
		}
		else{
			return LoginWebService.login(context, login_params);
		}
	}

	protected void onPostExecute(LoginResult result) {
		if(result != null)
			context.onAuthenticationResult(result);
		else {
			context.DisplayToast("Can't connect right now, try after some time!");
		}
	}
}
