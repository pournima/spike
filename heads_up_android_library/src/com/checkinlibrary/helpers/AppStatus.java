package com.checkinlibrary.helpers;



import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class AppStatus {
	private static AppStatus instance = new AppStatus();
	ConnectivityManager connectivityManager;
	static Context context;
	boolean connected = false;
	public static final String FILE_NAME = "appdata";

	public final String AUTH_KEY = "auth_key";
	public final String TWITTER_ON = "twitter_on";
	public final String FACEBOOK_ON = "facebook_on";
	public final String ALL_CAUSE_CACHE_UPDATED = "allCauseCacheUpdated";
	public final String MY_CAUSE_CACHE_UPDATED = "myCauseCacheUpdated";
	public final String FACEBOOK_TOKEN = "facebook_token";
	public final String TWITTER_TOKEN = "twitter_token";
	public final String TWITTER_SECRET = "twitter_secret";
	public final String CHECKIN_AMOUNT = "checkin_amount";
	public final String USER_NAME = "userName";
	public final int REQUEST_CODE = 123;
	public Boolean mFromQrCode=false;
	public String mQrCodeResult;
	public final String LAT = "latitude";
	public final String LONG = "longitude";
	public final String ACCURACY = "accuracy";
	public final String PROVIDER = "provider";
	public Boolean GPS_STATUS_ON = false;
	public final int TOTAL_CHECKINS = 0;
	public final String CHECKIN_COUNT = "checkin_count";
	
	public final String FB_FIRST_NAME  = "fb_first_name";
	public final String FB_LAST_NAME   = "fb_last_name";
	public final String FB_UID         = "fb_uid";
	public final String FB_EMAIL       = "fb_email";
	
	public final String TW_FIRST_NAME  = "tw_first_name";
	public final String TW_LAST_NAME   = "tw_last_name";
	public final String TW_UID         = "tw_uid";
	
	public final String APP_NAME = "app_name";
	public String moneyRaised;
	
	public final String FACEBOOK_PERMISSIONS_ON = "fb_permissions_on";
	
	public final String SAFETY_TAB_CLICKED =  "safety_tab_click";
	public final String RULES_TAB_CLICKED =   "rules_tab_click";
	public final String CHECKIN_TAB_CLICKED = "checkin_tab_click";
	public final String SETTINGS_TAB_CLICKED= "settings_tab_click";
	
	 public final String IS_APP_RESUMED = "is_app_resumed";

	public static AppStatus getInstance(Context ctx) {
		context = ctx;
		return instance;
	}

	public Boolean isGPSOn() {

		if (GPS_STATUS_ON) {

			return true;

		} else {

			return false;
		}

	}

	public Boolean isOnline() {

		try {
			connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);

			NetworkInfo networkInfo = connectivityManager
					.getActiveNetworkInfo();
			connected = networkInfo != null && networkInfo.isAvailable()
					&& networkInfo.isConnected();
			return connected;

		} catch (Exception e) {
			Log.v("AppStatus", e.toString());
		}
		return connected;
	}

	public Boolean isRegistered() {
		try {
			String auth_key = getSharedStringValue(AUTH_KEY);
			if (auth_key == null)
				return false;
			else
				return true;
		} catch (Exception e) {
			Log.d("AppStatus", e.toString());
		}

		return false;
	}

	public String getSharedStringValue(String key) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		String value = sp.getString(key, null);
		return value;
	}

	public boolean saveSharedStringValue(String key, String value) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Editor edit = sp.edit();
		edit.putString(key, value);
		return edit.commit();
	}

	public Boolean getSharedBoolValue(String key) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Boolean value = sp.getBoolean(key, false);
		return value;
	}

	public boolean saveSharedBoolValue(String key, Boolean value) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Editor edit = sp.edit();
		edit.putBoolean(key, value);
		return edit.commit();
	}

	public Long getSharedLongValue(String key) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Long value = sp.getLong(key, 0);
		return value;
	}

	public boolean saveSharedLongValue(String key, Long value) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Editor edit = sp.edit();
		edit.putLong(key, value);
		return edit.commit();
	}

	public boolean clearSharedData() {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Editor edit = sp.edit();
		edit.clear();
		return edit.commit();
	}

	public boolean clearSharedDataWithKey(String key) {
		SharedPreferences sp = context.getSharedPreferences("FILE_NAME", 0);
		Editor edit = sp.edit();
		edit.remove(key);
		return edit.commit();
	}
	
	public String setCheckinAmount() {
		String checkin_amount = getSharedStringValue(CHECKIN_AMOUNT);
		String strCheckinMsg = null;
		if (checkin_amount == null || checkin_amount.equals("null")) {

			strCheckinMsg = "You've raised: $0.00";
		} else {
			Log.v("CheckinActivity", "Checkin_Amount: " + checkin_amount);
			strCheckinMsg = "You've raised: $" + checkin_amount;

		}

		/*if (textViewRaisedMoney != null) {

			textViewRaisedMoney.setText(checkin_msg);
		} else {

			textViewRaisedMoney = (TextView) findViewById(R.id.textViewTopRaisedMoney);
			textViewRaisedMoney.setText(checkin_msg);
		}*/
		return strCheckinMsg;

	}


}
