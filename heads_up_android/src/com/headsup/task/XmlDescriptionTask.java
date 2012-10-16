package com.headsup.task;

import android.os.AsyncTask;

import com.checkinlibrary.ws.services.WebService;
import com.headsup.safety.XmlDescriptionFragment;

public class XmlDescriptionTask extends AsyncTask<String, Integer, String> {

	String guid;
	XmlDescriptionFragment mXmlDescriptionActivity;
	public static final String ARTICLE_URL = "http://usafootball.com/nodeInXml/";
	
	public XmlDescriptionTask(XmlDescriptionFragment context, String guid) {
		this.mXmlDescriptionActivity = context;
		this.guid = guid;
	}

	@Override
	protected String doInBackground(String... params) {
		WebService webService = new WebService(ARTICLE_URL
				+ guid);
		String articleResult = webService.webGET(ARTICLE_URL
				+ guid, null);
		return articleResult;
	}

	protected void onPostExecute(String result) {
		
		mXmlDescriptionActivity.displayArticle(result);

	}
}
