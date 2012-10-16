package com.checkinlibrary.ws.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.checkinlibrary.models.ForgotPwdResult;
import com.google.myjson.Gson;

public class ForgotPwdWebService implements WebServiceIface {
    public static ForgotPwdResult forgotpwd(Context context, String... forgotpwd_params) {
        ForgotPwdResult result = null;
        Log.e("CHECKINFORGOOD",
                "Network available: "
                + Boolean.toString(isNetworkAvailable(context)));
        WebService webService = new WebService(BASE_URL
                + "/mobile_user/send_reset_password_token.json");

        List<NameValuePair> params = new ArrayList<NameValuePair>(2);
        params.add(new BasicNameValuePair("email", forgotpwd_params[0]));
        params.add(new BasicNameValuePair("app_id", forgotpwd_params[1]));
        
        String response = webService.webPost("", params);
        if(response != null)
            Log.e("CHECKINFORGOOD", response.toString());

        try {
            result = new Gson().fromJson(response, ForgotPwdResult.class);
            Log.e("CHECKINFORGOOD", result.toString());
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD", "ForgotPasswordResult Error: " + e.getMessage());
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