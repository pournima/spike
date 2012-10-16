package com.checkinlibrary.ws.services;



import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.settings.SocialSharingResult;

public class SocialSharingWebService implements WebServiceIface {

	public static boolean doSharePreferences(String[] allparams) {
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/sharing_preferences.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(6);

		params.add(new BasicNameValuePair("api_key",allparams[0]));
		params.add(new BasicNameValuePair("social_network", allparams[1]));
		params.add(new BasicNameValuePair("active", allparams[2]));
		params.add(new BasicNameValuePair("access_token", allparams[3]));
		params.add(new BasicNameValuePair("access_secret",allparams[4]));
		params.add(new BasicNameValuePair("app_id", "heads_up"));

		String response = webService.webPost("", params);
		Boolean result=false;
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			JSONObject jsonResult;
			try {
				jsonResult = new JSONObject(response);
				result=jsonResult.getBoolean("success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static boolean doUpdatePreferences(String[] allparams) {
		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/update_sharing_preferences.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(4);

		params.add(new BasicNameValuePair("api_key",allparams[0]));
		params.add(new BasicNameValuePair("social_network", allparams[1]));
		params.add(new BasicNameValuePair("active", allparams[2]));
		params.add(new BasicNameValuePair("app_id", "heads_up"));
		
		String response = webService.webPost("", params);
		Boolean result=false;
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			JSONObject jsonResult;
			try {
				jsonResult = new JSONObject(response);
				result=jsonResult.getBoolean("success");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}

	public static boolean getSharedPreferences(String[] allparams) {

		WebService webService = new WebService(BASE_URL
				+ "/mobile_user/get_shared_preferences.json");

		List<NameValuePair> params = new ArrayList<NameValuePair>(2);

		params.add(new BasicNameValuePair("api_key",allparams[0]));
		params.add(new BasicNameValuePair("app_id", "heads_up"));

		String response = webService.webPost("", params);
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response); 
			JSONObject jsonResult;
			try {
				jsonResult = new JSONObject(response);
				SocialSharingResult res = new SocialSharingResult(jsonResult.getString("facebook_social_preference"), 
						jsonResult.getString("twitter_social_preference"));

				writeTosharedPreferences(res);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	private static void writeTosharedPreferences(SocialSharingResult socialObject){
		AppStatus appStatus=new AppStatus();
		if(socialObject.facebookObject.getExist()) {
			if(socialObject.facebookObject.getAccess_token() != null)
				appStatus.saveSharedStringValue(appStatus.FACEBOOK_TOKEN,socialObject.facebookObject.getAccess_token());
	            appStatus.saveSharedBoolValue(appStatus.FACEBOOK_ON,socialObject.facebookObject.getActive());
		}
		
		if(socialObject.twitterObject.getExist()) {
			if(( socialObject.twitterObject.getAccess_token()!= null) && 
					(socialObject.twitterObject.getAccess_secret() != null)){
				appStatus.saveSharedStringValue(appStatus.TWITTER_TOKEN, socialObject.twitterObject.getAccess_token());
				appStatus.saveSharedStringValue(appStatus.TWITTER_SECRET, socialObject.twitterObject.getAccess_secret());
			}
			appStatus.saveSharedBoolValue(appStatus.TWITTER_ON, socialObject.twitterObject.getActive());
		}
	}
}
