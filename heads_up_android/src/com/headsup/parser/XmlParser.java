package com.headsup.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Log;
import android.util.Xml;

import com.headsup.HeadsUpNativeActivity;
import com.headsup.helpers.XmlTags;
import com.headsup.models.Entry;

public class XmlParser extends XmlTags {
	HeadsUpNativeActivity context;	

	private static final String ns = null;
	public static XmlParser INSTANCE = new XmlParser();

	public ArrayList<Entry> parse(InputStream inputStream)
			throws XmlPullParserException, IOException {
		try {
			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(inputStream, null);
			parser.nextTag();
			return readRss(parser);
		} finally {
			inputStream.close();
		}
	}

	private ArrayList<Entry> readRss(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		ArrayList<Entry> entries = new ArrayList<Entry>();

		parser.require(XmlPullParser.START_TAG, ns, RSS);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(CHANNEL)) {
				entries = readXmlTag(parser);
			}
		}
		return entries;
	}

	private ArrayList<Entry> readXmlTag(XmlPullParser parser)
			throws XmlPullParserException, IOException {

		ArrayList<Entry> entries = new ArrayList<Entry>();
		parser.require(XmlPullParser.START_TAG, ns, CHANNEL);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String nameItem = parser.getName();
			if (nameItem.equals(ITEM)) {
				entries.add(readDetails(parser));
			} else {
				skip(parser);
			}
		}
		return entries;
	}

	private Entry readDetails(XmlPullParser parser)
			throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, ns, ITEM);
		String title = null;
		String description = null;
		String descriptionLink = null;
		String pub_date = null;
		String guid = null;
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}
			String name = parser.getName();
			if (name.equals(TITLE)) {
				title = readTitle(parser);
			} else if (name.equals(DESCRIPTION)) {
				
				descriptionLink= readDescription(parser);
/*
	 			String description1 = readDescription(parser);
				description = description1.replaceAll("<(.|\n)*?>", "");

				if (description1.toLowerCase().contains("<a href=")) {
					int start = description1.indexOf('=');
					int end = description1.lastIndexOf('"');
					descriptionLink = description1.substring(start + 2, end);

					if (descriptionLink.contains("target")
							&& !(descriptionLink.indexOf(">") != -1)) {
						int firstIndex = descriptionLink.indexOf('"');
						descriptionLink = descriptionLink.substring(0,
								firstIndex);
					} else {
					}

					if (descriptionLink.indexOf(">") != -1) {
						String sortout = descriptionLink.replaceAll(
								"<(.|\n)*?>", "");
						int startindex = sortout.indexOf("<a");
						String sort = sortout.substring(startindex);

						int firstIndex = sort.indexOf('"');
						int startup = firstIndex + 1;
						int secondIndex = sort.indexOf('"', firstIndex + 1);
						descriptionLink = sort.substring(startup, secondIndex);
					}
				} else {
					descriptionLink = "link not available";
				}
				*/
			} else if (name.equals(PUB_DATE)) {
				pub_date = readPubDate(parser);
			} else  if (name.equals(GUID)){
				guid = readGuid(parser);
				String[] uId = guid.split(" ");
				guid = uId[0];
				Log.i("",uId[0]);
			} else {
				skip(parser);
			}
		}
		return new Entry(title, description, descriptionLink, pub_date, guid);
	}

	private String readTitle(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, TITLE);
		String title = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, TITLE);
		return title;
	}

	private String readDescription(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, DESCRIPTION);
		String description = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, DESCRIPTION);
		return description;
	}

	private String readPubDate(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		parser.require(XmlPullParser.START_TAG, ns, PUB_DATE);
		String pub_date = readText(parser);
		parser.require(XmlPullParser.END_TAG, ns, PUB_DATE);
		return pub_date;
	}
	
	private String readGuid(XmlPullParser parser) throws IOException,
	XmlPullParserException {
	parser.require(XmlPullParser.START_TAG, ns, GUID);
	String guid = readText(parser);
	parser.require(XmlPullParser.END_TAG, ns, GUID);
	return guid;
}

	private String readText(XmlPullParser parser) throws IOException,
			XmlPullParserException {
		String result = "";
		if (parser.next() == XmlPullParser.TEXT) {
			result = parser.getText();
			parser.nextTag();
		}
		return result;
	}

	private void skip(XmlPullParser parser) throws XmlPullParserException,
			IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}
		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
			case XmlPullParser.END_TAG:
				depth--;
				break;
			case XmlPullParser.START_TAG:
				depth++;
				break;
			}
		}
	}
	
	public String parseArticleJson(String strResult, HeadsUpNativeActivity context) {
		String body = null;
		this.context = context;
		
		
		try {
			JSONObject jobj = new JSONObject(strResult);
			String nodes = jobj.getString("nodes");
			Log.i("Json Obj response", nodes);
			JSONArray jArray = new JSONArray(nodes);

			String node = jArray.getJSONObject(0).getString("node");
			Log.i("Json Array response", node);

			JSONObject jobjBody = new JSONObject(node);
			body = jobjBody.getString("body");
			Log.i("Json body response", body);
			
			context.removeDialog(0);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return body;

	}
}
