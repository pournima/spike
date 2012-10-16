package com.headsup.safety;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkinlibrary.NoConnectivityScreen;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.parser.XmlParser;
import com.headsup.task.XmlDescriptionTask;

public class XmlDescriptionFragment extends Fragment {

	HeadsUpNativeActivity context;
	TextView txtDescription;
	TextView txtDescriptionTitle;
	XmlParser parser;
	ProgressDialog progressBar;
	
	String title;
	String description;
	String link;
	String guid;
	
	protected View v;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (HeadsUpNativeActivity) this.getActivity();
		v = inflater.inflate(R.layout.xml_description, container, false);

		guid=getArguments().getString("Guid");
		title=getArguments().getString("Title");

		context.showDialog(0);
		if (HeadsUpNativeActivity.appStatus.isOnline()) {
			new XmlDescriptionTask(this, guid).execute("");
		}else {

			Log.v("XmlDescriptionActivity", "App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			this.startActivity(intent);
			context.finish();
		}				
		return v;
	}
	
	public void onResume() {
		super.onResume();
	}

	public void displayArticle(String result) {

		txtDescriptionTitle = (TextView) v.findViewById(R.id.txtXmlDescriptionTitle);
		txtDescription = (TextView) v.findViewById(R.id.txtXmlDescription);
		txtDescription.setMovementMethod(LinkMovementMethod.getInstance());
		txtDescriptionTitle.setText(title); //String.valueOf(getArguments().getString("Title"))
		
		parser = new XmlParser();
		String strArticle = parser.parseArticleJson(result, context);
		if (strArticle != null) {
			txtDescription.setText(Html.fromHtml(strArticle));
		} else {
			txtDescription.setText("No Description Available!");
		}
		
		context.removeDialog(0);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
