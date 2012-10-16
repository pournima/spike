package com.headsup.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import android.util.Log;
import com.checkinlibrary.ws.services.WebService;
import com.checkinlibrary.ws.services.WebServiceIface;
import com.google.myjson.Gson;
import com.headsup.helpers.Constants;
import com.headsup.models.SignalsResult;


public class SignalsWebService implements WebServiceIface {

	public static SignalsResult getSignalsData(String... signals_parms) {
		
		WebService webService = new WebService(Constants.BASE_IMAGE_URL +"/catagories/get_data.json");
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);

		params.add(new BasicNameValuePair("api_key",signals_parms[0] ));
		params.add(new BasicNameValuePair("device",signals_parms[1]));
		params.add(new BasicNameValuePair("signed_key", signals_parms[2]));
		
		String response = webService.webPost("", params);
		SignalsResult result=null;
			
		 if(response != null)
	            Log.e("CHECKINFORGOOD", response.toString());

	        try {
	            result = new Gson().fromJson(response, SignalsResult.class);
	            Log.e("CHECKINFORGOOD", result.toString());
	        } catch (Exception e) {
	            Log.e("CHECKINFORGOOD", "Signal Result Error: " + e.getMessage());
	        }
		return result;
	}
}
