package com.headsup.safety;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;

public class XmlWebDescriptionFragment extends Fragment {

	HeadsUpNativeActivity context;
	WebView webView;
	String link;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (HeadsUpNativeActivity) this.getActivity();
		View v = inflater.inflate(R.layout.rulesofservice, container, false);
		
		link=getArguments().getString("DescriptionLink");
		
		webView = (WebView) v.findViewById(R.id.webViewRules);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(link);
		return v;
	}
}
