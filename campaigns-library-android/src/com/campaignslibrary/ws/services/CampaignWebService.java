package com.campaignslibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.campaignslibrary.CampaignsFragment;
import com.campaignslibrary.CreateCampgnFragment;
import com.campaignslibrary.models.CampaignDetailResult;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.LaunchCampaignResult;
import com.checkinlibrary.ws.services.WebService;
import com.checkinlibrary.ws.services.WebServiceIface;
import com.google.myjson.Gson;
import com.google.myjson.reflect.TypeToken;

public class CampaignWebService implements WebServiceIface {

	public static CreateCampaignResult createCampaign(CreateCampgnFragment context, String[] campaign_params) {
		CreateCampaignResult result = null;

		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/create_campaign.json");

		StringBuilder signed_value = new StringBuilder();

		for (int i = 0; i < campaign_params.length; i++) {
			signed_value.append(campaign_params[i]);
		}

		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>(15);

		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("campaign_sub_type",campaign_params[1]));
		params.add(new BasicNameValuePair("campaign_type", campaign_params[2]));
		params.add(new BasicNameValuePair("category_id", campaign_params[3]));
		params.add(new BasicNameValuePair("description", campaign_params[4]));
		params.add(new BasicNameValuePair("donation_type", campaign_params[5]));
		params.add(new BasicNameValuePair("end_date", campaign_params[6]));
		params.add(new BasicNameValuePair("fb_link", campaign_params[7]));
		params.add(new BasicNameValuePair("goal", campaign_params[8]));
		params.add(new BasicNameValuePair("name", campaign_params[9]));
		params.add(new BasicNameValuePair("organization_id", campaign_params[10]));
		params.add(new BasicNameValuePair("pledge_for", campaign_params[11]));
		params.add(new BasicNameValuePair("start_date", campaign_params[12]));
		params.add(new BasicNameValuePair("video_link", campaign_params[13]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));

		String response = webService.webPost("", params);
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				result = new Gson().fromJson(response, CreateCampaignResult.class);
				Log.e("CHECKINFORGOOD", result.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "Create campaign Error: " + e.getMessage());
			}
		}
		return result;
	}

	public static CreateCampaignResult uploadCampaignPhotos(String[] campaign_params) {
		CreateCampaignResult result = null;

		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/add_photos_to_campaign.json");

		StringBuilder signed_value = new StringBuilder();

		for (int i = 0; i < campaign_params.length-1; i++) {
			signed_value.append(campaign_params[i]);
		}

		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>(4);

		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("campaign_id",campaign_params[1]));
		params.add(new BasicNameValuePair("photos", campaign_params[2]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));

		String response = webService.webPost("", params);
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				result = new Gson().fromJson(response, CreateCampaignResult.class);
				Log.e("CHECKINFORGOOD", result.toString());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "upload campaign photos Error: " + e.getMessage());
			}
		}
		return result;
	}

	public static LaunchCampaignResult launchCampaign(String[] campaign_params) {
		LaunchCampaignResult result = null;

		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/launch_campaign.json");

		StringBuilder signed_value = new StringBuilder();

		for (int i = 0; i < campaign_params.length; i++) {
			signed_value.append(campaign_params[i]);
		}

		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>(4);

		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("campaign_id",campaign_params[1]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));

		String response = webService.webPost("", params);
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				result = new Gson().fromJson(response, LaunchCampaignResult.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "launch campaign Error: " + e.getMessage());
			}
		}
		return result;
	}
	
	public static CampaignDetailResult featuredCampaign(CampaignsFragment context, String[] campaign_params) {
		CampaignDetailResult result = null;
		
		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/current_campaigns");
		
		StringBuilder signed_value = new StringBuilder();
		for (int i = 0; i < campaign_params.length; i++) {
			signed_value.append(campaign_params[i]);
		}
		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));
		
		String response = webService.webPost("", params);		
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				result = new Gson().fromJson(response, CampaignDetailResult.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "launch campaign Error: " + e.getMessage());
			}
		}
		
		return result;
	}
	
	public static List<CreateCampaignResult> searchCampaign(String apiKey, String searchKey) {
		List<CreateCampaignResult> result = null;

		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/search_campaigns");

		StringBuilder signed_value = new StringBuilder();
		signed_value.append(apiKey);
		signed_value.append(searchKey);

		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);

		params.add(new BasicNameValuePair("api_key", apiKey));
		params.add(new BasicNameValuePair("search", searchKey));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));

		String response = webService.webPost("", params);		
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				java.lang.reflect.Type typeOfListOfMyCampgnResult = (new TypeToken<List<CreateCampaignResult>>() {}).getType();
				result = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfMyCampgnResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "launch campaign Error: " + e.getMessage());
			}
		}
		return result;
	}
	
	public static List<CreateCampaignResult> filterCampaigns(String[] campaign_params) {
		List<CreateCampaignResult> result = null;

		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/filter_campaigns");

		StringBuilder signed_value = new StringBuilder();

		for (int i = 0; i < campaign_params.length; i++) {
			signed_value.append(campaign_params[i]);
		}

		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);

		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("filter_by",campaign_params[1]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));

		String response = webService.webPost("", params);		
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				java.lang.reflect.Type typeOfListOfMyCampgnResult = (new TypeToken<List<CreateCampaignResult>>() {}).getType();
				result = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfMyCampgnResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "launch campaign Error: " + e.getMessage());
			}
		}
		return result;
	}
	
	public static List<CreateCampaignResult> myCampaigns(String[] campaign_params) {
		List<CreateCampaignResult> result = null;
		
		WebService webService = new WebService(BASE_URL + "/mobile_user/v2/my_campaigns");
		
		StringBuilder signed_value = new StringBuilder();
		for (int i = 0; i < campaign_params.length; i++) {
			signed_value.append(campaign_params[i]);
		}
		String encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("api_key", campaign_params[0]));
		params.add(new BasicNameValuePair("signed_key", encrypted_data));
		
		String response = webService.webPost("", params);		
		if(response!= null) {
			Log.e("CHECKINFORGOOD", response);
			try {
				java.lang.reflect.Type typeOfListOfMyCampgnResult = (new TypeToken<List<CreateCampaignResult>>() {}).getType();
				result = new Gson().fromJson(response, (java.lang.reflect.Type) typeOfListOfMyCampgnResult);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Log.e("CHECKINFORGOOD", "launch campaign Error: " + e.getMessage());
			}
		}
		
		return result;
	}
}