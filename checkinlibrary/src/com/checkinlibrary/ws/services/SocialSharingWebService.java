package com.checkinlibrary.ws.services;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.models.SocialSharingResult;


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
		params.add(new BasicNameValuePair("app_id", allparams[5]));

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
		params.add(new BasicNameValuePair("app_id",allparams[3]));

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
		params.add(new BasicNameValuePair("app_id", allparams[1]));

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

	public static boolean checkFBTokenExpiry(String[] allparams) {

		WebService webService = new WebService(allparams[0]);
		List<NameValuePair> params = new ArrayList<NameValuePair>(0);
		String response = webService.webPost("", params);
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			String fbAccessToken=parseUrlStream(response);
			if(fbAccessToken != null){
				CheckinLibraryActivity.appStatus.saveSharedStringValue(CheckinLibraryActivity.appStatus.FACEBOOK_TOKEN,fbAccessToken);
				updateExpiredToken(false, fbAccessToken);
				return true;
			}else{
				CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_TOKEN);
		    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_ON);
		    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_PERMISSIONS_ON);
				updateExpiredToken(true, fbAccessToken);
			}
		}
		return false;
	}
	
	private static String parseUrlStream(String urlString){
		String pair[]=urlString.split("&"); 
		if(pair.length > 0){
			pair = pair[0].split("=",2);
			String key;
			try {
				key = URLDecoder.decode(pair[0], "UTF-8");

				String value = "";
				if (pair.length > 1) {
					value = URLDecoder.decode(pair[1], "UTF-8");
					return value;
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private static void updateExpiredToken(Boolean bTokenExpired,String fbAccessToken){
		String args[]=new String[6];
		args[0]=CheckinLibraryActivity.mAuthToken;
		args[1]="facebook";
		if(bTokenExpired){
			args[2]=Boolean.toString(false);
			args[3]=null;
			args[4]=null;
		}else{
			args[2]=Boolean.toString(CheckinLibraryActivity.
					appStatus.getSharedBoolValue(CheckinLibraryActivity.appStatus.FACEBOOK_ON));
			args[3]=fbAccessToken;
			args[4]="no secret for fb";
		}
		args[5]=CheckinLibraryActivity.appStatus.getSharedStringValue(
				CheckinLibraryActivity.appStatus.APP_NAME);
		doSharePreferences(args);
	}
}
