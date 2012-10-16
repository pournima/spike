package com.checkinlibrary.ws.tasks;



import android.os.AsyncTask;

import com.checkinlibrary.account.ForgotPwdResult;
import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.ws.services.ForgotPwdWebService;

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