package com.checkinlibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.checkinlibrary.account.CreateAccountResult;
import com.checkinlibrary.account.LoginResult;
import com.google.myjson.Gson;

public class LoginWebService implements WebServiceIface {
	public static LoginResult login(Context context, String... login_params) {
		LoginResult result = null;
		Log.e("CHECKINFORGOOD",
				"Network available: "
						+ Boolean.toString(isNetworkAvailable(context)));
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/sign_in.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("app_id", "heads_up"));
		params.add(new BasicNameValuePair("email", login_params[0]));
		params.add(new BasicNameValuePair("password", login_params[1]));

		String response = webService.webPost("", params);

		if (response != null) {
			Log.e("CHECKINFORGOOD", response.toString());
		}

		try {
			result = new Gson().fromJson(response, LoginResult.class);
			Log.e("CHECKINFORGOOD", result.toString());
		} catch (Exception e) {
			Log.e("CHECKINFORGOOD", "LoginResult Error: " + e.getMessage());
		}

		return result;
	}

	public static LoginResult createSocialLogin(Context context,
			String... login_params) {
		LoginResult result = null;
		Log.e("CHECKINFORGOOD",
				"Network available: "
						+ Boolean.toString(isNetworkAvailable(context)));
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/social_sign_in.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(9);
		params.add(new BasicNameValuePair("uid", login_params[0]));
		params.add(new BasicNameValuePair("social_network", login_params[1]));
		 params.add(new BasicNameValuePair("app_id", "heads_up"));
		params.add(new BasicNameValuePair("access_token", login_params[2]));
		params.add(new BasicNameValuePair("access_secret", login_params[3]));
		params.add(new BasicNameValuePair("active", login_params[4]));
		params.add(new BasicNameValuePair("email", login_params[5]));
		params.add(new BasicNameValuePair("first_name", login_params[6]));
		params.add(new BasicNameValuePair("last_name", login_params[7]));

		String response = webService.webPost("", params);

		if (response != null) {
			Log.e("CHECKINFORGOOD", response.toString());
		}

		try {
			result = new Gson().fromJson(response, LoginResult.class);
			Log.e("CHECKINFORGOOD", result.toString());
		} catch (Exception e) {
			Log.e("CHECKINFORGOOD", "LoginResult Error: " + e.getMessage());
		}

		return result;
	}

	public static LoginResult create(Context context, String[] create_params) {
		LoginResult result = null;
		Log.e("CHECKINFORGOOD",
				"Network available: "
						+ Boolean.toString(isNetworkAvailable(context)));
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/sign_up.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(5);
		params.add(new BasicNameValuePair("email", create_params[0]));
		params.add(new BasicNameValuePair("password", create_params[1]));
		params.add(new BasicNameValuePair("first_name", create_params[2]));
		params.add(new BasicNameValuePair("last_name", create_params[3]));
		params.add(new BasicNameValuePair("app_id", "heads_up"));

		String response = webService.webPost("", params);
		if (response != null)
			Log.e("CHECKINFORGOOD",
					"CREATE ACCOUNT RESULT: " + response.toString());

		try {
			result = new Gson().fromJson(response, LoginResult.class);

			Log.e("CHECKINFORGOOD", result.toString());
		} catch (Exception e) {
			Log.e("CHECKINFORGOOD", "LoginResult Error: " + e.getMessage());
		}

		return result;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			return true;
		}

		return false;
	}
}