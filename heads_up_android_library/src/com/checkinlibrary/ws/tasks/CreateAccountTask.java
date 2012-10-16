package com.checkinlibrary.ws.tasks;

import android.os.AsyncTask;

import com.checkinlibrary.account.LoginResult;
import com.checkinlibrary.account.SignupActivity;
import com.checkinlibrary.ws.services.LoginWebService;

public class CreateAccountTask extends
		AsyncTask<String, Integer, LoginResult> {
	private SignupActivity context;

	public CreateAccountTask(SignupActivity context) {
		this.context = context;
	}

	protected LoginResult doInBackground(String... create_params) {
			return LoginWebService.create(context, create_params);
	}

	protected void onPostExecute(LoginResult result) {
		if (result != null)
			context.onAuthenticationResult(result);
		else {
			context.DisplayToast("Creating account failed!");
		}
	}
}
