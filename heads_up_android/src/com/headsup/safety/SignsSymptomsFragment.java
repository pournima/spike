package com.headsup.safety;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.headsup.R;

public class SignsSymptomsFragment extends Fragment {

	WebView webViewSignSymptoms;
	
	@SuppressLint("SetJavaScriptEnabled")

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.signs_symptoms, container,false);
		

		webViewSignSymptoms = (WebView) v.findViewById(R.id.webViewSignSymptoms);
		webViewSignSymptoms.getSettings().setJavaScriptEnabled(true);
		webViewSignSymptoms.loadUrl("file:///android_asset/sign_symptoms.html");	
		return v;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
