package com.headsup.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.settings.HtmlPageFragment;
import com.headsup.HeadsUpNativeActivity;

public class HtmlHeadsupPageFragment extends HtmlPageFragment {
	private WebView webViewTermsOfService;
//	ProgressDialog mProgressDialog;
//	String strProgressMessage;
	HeadsUpNativeActivity context;
	//AppStatus appStatus;

	
	  @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		  context = (HeadsUpNativeActivity) this.getActivity();
		  View v = inflater.inflate(R.layout.termsofservice, container, false);    
		  
		  webViewTermsOfService = (WebView)v.findViewById(R.id.webViewTermsOfUse);

			if (HeadsUpNativeActivity.appStatus.isOnline()) {
				
				//strProgressMessage = getString(R.string.defaultProgressMessage);
				setSettingLinks();
			} else {
				Intent intent = new Intent(context,
						NoConnectivityScreen.class);
				startActivity(intent);
				Log.v("TERMSOfUSE", "You are not online!!!!");
				context.finish();
			}
			
			return v;
	  }
		
	  @Override
	  public void setSettingLinks(){
		  switch (SettingsFragment.itemPosition) {
			case SettingsFragment.TERMSOFUSE:
				webViewTermsOfService
						.loadUrl("file:///android_asset/terms_service.html");
				break;
			case SettingsFragment.PRIVACY:
				webViewTermsOfService
						.loadUrl("file:///android_asset/privacy_policy.html");
				break;
			case SettingsFragment.CONTACT:
				webViewTermsOfService
						.loadUrl("file:///android_asset/index.html");
				break;

			default:
				break;
		  }
		}	  
	
}
