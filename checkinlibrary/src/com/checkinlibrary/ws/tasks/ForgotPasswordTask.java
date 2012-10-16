package com.checkinlibrary.ws.tasks;

import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.models.ForgotPwdResult;
import com.checkinlibrary.ws.services.ForgotPwdWebService;

import android.os.AsyncTask;
import android.util.Log;

public class ForgotPasswordTask extends AsyncTask<String, Integer, ForgotPwdResult> {
	private LoginActivity context;

	public ForgotPasswordTask(LoginActivity context) {
		this.context = context;
	}
	protected void onPostExecute(ForgotPwdResult result) {
		if(result != null)
			context.onAuthenticationResult(result.getStatus(), result.getMessage());
		else {
			context.DisplayToast("Notification failed!");
		}
	}

	protected ForgotPwdResult doInBackground(String... forgotPwd_params) {
		// TODO Auto-generated method stub
		return ForgotPwdWebService.forgotpwd(context, forgotPwd_params);
	}

}