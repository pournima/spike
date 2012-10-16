package com.checkinlibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.checkinlibrary.checkin.CheckinResult;
import com.checkinlibrary.helpers.AppStatus;
import com.google.myjson.Gson;

public class DoCheckinWebService implements WebServiceIface {
	
	public static CheckinResult docheckin(Context context, String[] allparams) {
		AppStatus appStatus = new AppStatus();
		CheckinResult result = null;
		Log.e("CHECKINFORGOOD",
				"Network available: "
						+ Boolean.toString(isNetworkAvailable(context)));
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/do_checkin_simple.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(7);

		params.add(new BasicNameValuePair("business_id", allparams[0]));
		params.add(new BasicNameValuePair("organization_id", allparams[1]));
		params.add(new BasicNameValuePair("checkin_time", allparams[2]));
		params.add(new BasicNameValuePair("is_public", allparams[3]));
		params.add(new BasicNameValuePair("api_key",
				appStatus.getSharedStringValue(appStatus.AUTH_KEY)));
		params.add(new BasicNameValuePair("app_id", "heads_up"));
		
		if (0 != allparams[4].compareTo("NULL")) {
			params.add(new BasicNameValuePair("qrcode", allparams[4]));
		}
		if (0 != allparams[5].compareTo("NULL")) {
			params.add(new BasicNameValuePair("delay_sharing", allparams[5]));
		}

		String response = webService.webPost("", params);
		if (response != null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				result = new Gson().fromJson(response, CheckinResult.class);
				Log.e("CHECKINFORGOOD", result.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD",
						"DoCheckinResult Error: " + e.getMessage());
			}
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