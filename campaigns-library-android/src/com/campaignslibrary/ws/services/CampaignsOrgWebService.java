package com.campaignslibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.campaignslibrary.models.CauseCategoryResult;
import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;

public class CampaignsOrgWebService implements WebServiceIface{ 
    public static List<CauseCategoryResult> getCauseCategoryList(String[] locationParams) {
        List<CauseCategoryResult> res = null;
        WebService webService = new WebService(BASE_URL + "/mobile_user/v2/categories_list.json");
        
		String encrypted_data = HmacSHAClass.hash_hmac(locationParams[0]);
	
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("api_key", locationParams[0]));
        params.add(new BasicNameValuePair("signed_key", encrypted_data));

        String response = webService.webPost("", params);

        if (response == null) {
        	Log.v("CHECKINFORGOOD",
        			"Null response from cause category web service");
        } else {
        	Log.e("CHECKINFORGOOD", "cause category list result : "+ response.toString());
        }
        
        try {
            java.lang.reflect.Type typeOfListOfResult = (new TypeToken<List<CauseCategoryResult>>() {}).getType();
            res = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfResult);
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "MyOrganizationResult Error: " + e.getMessage());
        }
		return res;
    }

}