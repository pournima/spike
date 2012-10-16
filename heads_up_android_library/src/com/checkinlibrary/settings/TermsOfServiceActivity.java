package com.checkinlibrary.settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;

public class TermsOfServiceActivity extends Activity {
	private WebView webViewTermsOfService;
	ProgressDialog mProgressDialog;
	String strProgressMessage;
	AppStatus appStatus;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.termsofservice);

		appStatus = AppStatus.getInstance(this);
		webViewTermsOfService = (WebView) findViewById(R.id.webViewTermsOfUse);

		if (appStatus.isOnline()) {
			
			strProgressMessage = getString(R.string.defaultProgressMessage);
			showDialog(0);
			switch (SettingsFragment.itemPosition) {
			case SettingsListAdapter.TERMSOFUSE:
				webViewTermsOfService
						.loadUrl("file:///android_asset/terms_service.html");
				break;
			case SettingsListAdapter.PRIVACY:
				webViewTermsOfService
						.loadUrl("file:///android_asset/privacy_policy.html");
				break;
			case SettingsListAdapter.CONTACT:
				webViewTermsOfService
						.loadUrl("file:///android_asset/index.html");
				break;

			default:
				break;
			}
		} else {
			Intent intent = new Intent(TermsOfServiceActivity.this,
					NoConnectivityScreen.class);
			startActivity(intent);
			Log.v("TERMSOfUSE", "You are not online!!!!");
			getParent().finish();
		}
	}
}
