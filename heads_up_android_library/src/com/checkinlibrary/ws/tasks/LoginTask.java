package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;

import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.account.LoginResult;
import com.checkinlibrary.ws.services.LoginWebService;

public class LoginTask extends AsyncTask<String, Integer, LoginResult> {
	private LoginActivity context;
	private boolean bIsFromSocialLogin = false;

	public LoginTask(LoginActivity context, Boolean bIsFromSCL) {
		this.context = context;
		this.bIsFromSocialLogin = bIsFromSCL;
	}

	protected LoginResult doInBackground(String... login_params) {
		if (bIsFromSocialLogin) {
			return LoginWebService.createSocialLogin(context, login_params);
		} else {
			return LoginWebService.login(context, login_params);			
		}
	}

	protected void onPostExecute(LoginResult result) {
		if (result != null)
			context.onAuthenticationResult(result);
		else {
			context.DisplayToast("Oops. We didn't hear back from Facebook. Please sign in with your email address or try again in a few minutes.");
			context.initailizeSocialLoginPage();
		}
	}
}
