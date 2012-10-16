package com.checkinlibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.models.AppVersionResult;
import com.checkinlibrary.models.LoginResult;
import com.google.myjson.Gson;

import android.content.Context;
import android.util.Log;

public class AppVersionWebService implements WebServiceIface {

	public static AppVersionResult setAppVersion(CheckinLibraryActivity context,String[] appVersion) {
		
		AppVersionResult result=null;
		 WebService webService = new WebService(BASE_URL + "/mobile_user/app_versions.json?");
		 List<NameValuePair> params = new ArrayList<NameValuePair>(1);
	     params.add(new BasicNameValuePair("app_id",appVersion[0]));
	     params.add(new BasicNameValuePair("api_key",CheckinLibraryActivity.mAuthToken));
	     String response = webService.webPost("", params);
	     if(response != null ||response =="{}") {
	            Log.e("CHECKINFORGOOD", response.toString());
	       
	        try {
	            result = new Gson().fromJson(response, AppVersionResult.class);
	            Log.e("CHECKINFORGOOD", result.toString());
	        } catch (Exception e) {
	            Log.e("CHECKINFORGOOD", "LoginResult Error: " + e.getMessage());
	        }
	     }
	        return result;
		
	}

}
