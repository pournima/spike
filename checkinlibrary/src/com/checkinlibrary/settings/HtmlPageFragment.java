package com.checkinlibrary.settings;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class HtmlPageFragment extends Fragment {

	private WebView webView;
	ProgressDialog mProgressDialog;
	String strProgressMessage;
	CheckinLibraryActivity context;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.contactus_fragment, container, false);    
		webView = (WebView)v.findViewById(R.id.webViewContactUs);
		context=(CheckinLibraryActivity)this.getActivity();
		
		if (CheckinLibraryActivity.appStatus.isOnline()) {
			setSettingLinks();
			
		}else{
        	Intent intent = new Intent(context,	NoConnectivityScreen.class);
        	context.startActivity(intent);
        	Log.v("ContactUsFragment", "You are not online!!!!");
        	context.finish();
        }
		return v;
	}
	
	
	public void setSettingLinks(){
		switch (SettingFragment.itemPosition) {
		case SettingFragment.TERMS_FRG:
			webView
					.loadUrl("http://checkinforgood.com/page/app_pages/terms_of_service");
			break;
		case SettingFragment.PRIPOL_FRG:
			webView
					.loadUrl("http://checkinforgood.com/page/app_pages/privacy_policy");
			break;
		case SettingFragment.CONTACTS_FRG:
			webView
					.loadUrl("http://checkinforgood.com/page/app_pages/contact_us");
			break;
		default:
			break;
		}
		webView.setBackgroundColor(0);
	}
}
