package com.checkinlibrary.account;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.checkinlibrary.CheckinLibraryActivity;

public class AuthTokenTask extends AsyncTask<String, Integer, String> {
    private CheckinLibraryActivity context;

    public AuthTokenTask(CheckinLibraryActivity context) {
        this.context = context;
        Log.e("CHECKINFORGOOD", "Network available: " + Boolean.toString(isNetworkAvailable(context)));
    }

    protected String doInBackground(String... login_params) {
        String authToken = "";
        final AccountManager accountManager = AccountManager.get(context);
        Account[] accounts = accountManager
        .getAccountsByType("checkinforgood.com");

        try {
            authToken = accountManager.blockingGetAuthToken(accounts[0],
                    "checkinforgood.com", true);
        } catch (Exception e) {
            Log.e("CHECKINFORGOOD",
                    e.getClass().toString() + " " + e.getMessage());
        }
        return authToken;
    }

    protected void onPostExecute(String result) {
       // CheckinNativeActivity.mAuthToken = result;

        // Refresh fragment tab if authenticated

        if (!TextUtils.isEmpty(result)) {
            Fragment fragment = context.getSupportFragmentManager()
            .findFragmentByTag("business_tab");
            if (fragment != null && !fragment.isDetached()) {
                FragmentTransaction ft = context.getSupportFragmentManager()
                .beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
            }
        } else {
            final Intent intent = new Intent(context, LoginActivity.class);
            intent.setAction("android.intent.action.VIEW");
            context.startActivity(intent);
            context.finish();
        }
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } 
        return false;
    }
}