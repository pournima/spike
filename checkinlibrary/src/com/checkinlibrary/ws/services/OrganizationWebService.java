package com.checkinlibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.models.OrganizationResult;
import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;

public class OrganizationWebService implements WebServiceIface{
    
    public static List<OrganizationResult> getAll(Double[] locationParams, int page) {
        List<OrganizationResult> res = null;
        WebService webService = new WebService(BASE_URL + "/mobile_user/get_all_organizations.json");

        List<NameValuePair> params = new ArrayList<NameValuePair>(5);
        params.add(new BasicNameValuePair("latitude", Double.toString(locationParams[0])));
        params.add(new BasicNameValuePair("longitude", Double.toString(locationParams[1])));
        params.add(new BasicNameValuePair("api_key",CheckinLibraryActivity.mAuthToken));
        params.add(new BasicNameValuePair("page", String.valueOf(page)));
        params.add(new BasicNameValuePair("app_id", CheckinLibraryActivity.APP_NAME));
        
        String response = webService.webPost("", params);
        Log.v("CHECKINFORGOOD", "Allcause response: " + response);

        try {
            java.lang.reflect.Type typeOfListOfAllOrganizationResult = (new TypeToken<List<OrganizationResult>>() {}).getType();
            res = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfAllOrganizationResult);
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "AllOrganizationResult Error: " + e.getMessage());
        }

        return res;
    }

    public static List<OrganizationResult> getMy(Double[] locationParams) {
        List<OrganizationResult> res = null;
        WebService webService = new WebService(BASE_URL + "/mobile_user/get_supported_organizations.json");
        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("api_key", CheckinLibraryActivity.mAuthToken));
        params.add(new BasicNameValuePair("app_id", CheckinLibraryActivity.APP_NAME));

        String response = webService.webPost("", params);

        if (response == null) {
            Log.v("CHECKINFORGOOD",
                    "Null response from organization web service");
        } else {
            Log.e("CHECKINFORGOOD", "All causes result for lat " + locationParams[0] + " lon " + locationParams[1] + ": "
                    + response.toString());
        }

        try {
            java.lang.reflect.Type typeOfListOfResult = (new TypeToken<List<OrganizationResult>>() {}).getType();
            res = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfResult);

            for (OrganizationResult cause : res) {
                Log.e("CHECKINFORGOOD", "CAUSE LIST " + cause.getOrganization().getName());
            }
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "MyOrganizationResult Error: " + e.getMessage());
        }

        return res;
    }

    public static Boolean[] supportOrg(Integer orgId, Boolean isSupported) {
        Boolean res = false;
        WebService webService = new WebService(BASE_URL + "/mobile_user/support_org.json");

        List<NameValuePair> params = new ArrayList<NameValuePair>(4);
        params.add(new BasicNameValuePair("api_key", CheckinLibraryActivity.mAuthToken));
        params.add(new BasicNameValuePair("support", Boolean.toString(isSupported)));
        params.add(new BasicNameValuePair("organization_id", orgId.toString()));
        params.add(new BasicNameValuePair("app_id", CheckinLibraryActivity.APP_NAME));

        String response = webService.webPost("", params);
        Log.v("CHEcKINFOGOOD", "supportOrg response: " + response);

        try {
        	res = new Gson().fromJson(response, Boolean.class);
        	if(res == null)
        		res=false;
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "supportOrg Error: " + e.getMessage());
        }

        return new Boolean[] { isSupported, res };
    }

    public List<OrganizationResult> getList(Double[] locationParams, Boolean supported, int page) {
        List<OrganizationResult> result;
        
        if (supported) {
            result = getMy(locationParams);
        } else {
            result = getAll(locationParams,page);
        }
        
        return result;
    }

    public static List<OrganizationResult> search(CharSequence searchText) {
        List<OrganizationResult> res = null;
        WebService webService = new WebService(BASE_URL + "/mobile_user/search_orgs.json");

        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("api_key", CheckinLibraryActivity.mAuthToken));
        params.add(new BasicNameValuePair("search_text", searchText.toString()));
        params.add(new BasicNameValuePair("app_id", CheckinLibraryActivity.APP_NAME));
        String response = webService.webPost("", params);

        if (response == null) {
            Log.v("CHECKINFORGOOD",
                    "Null response from organization search web service");
        } else {
            Log.e("CHECKINFORGOOD", "Organization search response: " + response.toString());
        }

        try {
            java.lang.reflect.Type typeOfListOfAllOrganizationResult = (new TypeToken<List<OrganizationResult>>() {}).getType();
            res = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfAllOrganizationResult);
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "Cause search error: " + e.getMessage());
        }

        return res;
    }
}