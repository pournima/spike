package com.checkinlibrary.facebook;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.facebook.Facebook.DialogListener;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.settings.SocialSharingActivity;
import com.checkinlibrary.twitter.TwitterShare.MyTimer;
import com.checkinlibrary.ws.services.WebService;
import com.checkinlibrary.ws.tasks.SocialSharingTask;
import com.google.myjson.Gson;

public class FacebookShare extends Activity {

	private Handler mRunOnUi = new Handler();
	private static final String appId = "396355100423309";
	// private static final String appId="237627916296942";
	private static final String appSecret = "d6dce2a60dfaac106c6a1d8282a08439";
	Facebook facebook = new Facebook(appId);
	public ProgressDialog mProgressDialog;
	String msgToPost;

	private static AppStatus appStatus;
	public static final String FROM_WHERE_FLAG = "FROM_WHERE";
	private boolean bIsFromSocialLogin;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.social_share_loading_screen);

		TextView txtMsg = (TextView) findViewById(R.id.TextViewMainMsg);
		txtMsg.setText(getString(R.string.waiting_for_facebook));

		msgToPost = getIntent().getStringExtra("MSSSAGE");
		bIsFromSocialLogin = getIntent().getBooleanExtra(
				FacebookShare.FROM_WHERE_FLAG, false);
		appStatus = AppStatus.getInstance(this);

		String access_token = appStatus
				.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
		FbDialog.bIsActivityEnabled = false;

		if (access_token != null) {
			if (appStatus.getSharedBoolValue(appStatus.FACEBOOK_PERMISSIONS_ON)) {
				facebook.setAccessToken(access_token);
			}
		}

		if (!facebook.isSessionValid()) {
			/*MyTimer tim = new MyTimer(45000, 1000);
			tim.start();*/
			facebook.authorize(this, new String[] { "publish_stream",
					"read_stream", "offline_access", "email" },
					Facebook.FORCE_DIALOG_AUTH, new DialogListener() {
						@Override
						public void onComplete(Bundle values) {
							appStatus.saveSharedStringValue(
									appStatus.FACEBOOK_TOKEN,
									facebook.getAccessToken());
							// appStatus.saveSharedBoolValue(appStatus.FACEBOOK_ON,true);

							getFBId();

							if (getPermissionsFromFB()) {
								appStatus.saveSharedBoolValue(
										appStatus.FACEBOOK_ON, true);
								appStatus
										.saveSharedBoolValue(
												appStatus.FACEBOOK_PERMISSIONS_ON,
												true);
							} else {
								appStatus.saveSharedBoolValue(
										appStatus.FACEBOOK_ON, false);
								appStatus.saveSharedBoolValue(
										appStatus.FACEBOOK_PERMISSIONS_ON,
										false);
							}

							if (!bIsFromSocialLogin)
								postSocialPreferences(facebook.getAccessToken());
							else {
								finish();
								FbDialog.bIsActivityEnabled = true;
							}

							// FbDialog.bIsActivityEnabled = true;
							// finish();

							// goToPrevious();
						}

						@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on cancel");
							SocialSharingActivity.bIsFromFBTwitter = true;
							FbDialog.bIsActivityEnabled = false;
							finish();
							// goToPrevious();
						}

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on error");
							SocialSharingActivity.bIsFromFBTwitter = true;
							FbDialog.bIsActivityEnabled = false;
							finish();
							// goToPrevious();
						}

						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on FB error");
							SocialSharingActivity.bIsFromFBTwitter = true;
							FbDialog.bIsActivityEnabled = false;
							finish();
							// goToPrevious();
						}
					});
		} else {
			// postToFacebook();
			getFBId();
			SocialSharingActivity.bIsFromFBTwitter = true;
			FbDialog.bIsActivityEnabled = true;
			finish();
			// goToPrevious();
		}
	}

	public void getFriendList() {
		Bundle bundle = new Bundle();
		try {
			String response = facebook.request("me/friends", bundle);
			Log.i("friends response", response);
			JSONObject json = Util.parseJson(response);
			final JSONArray friends = json.getJSONArray("data");
			Log.i("friends count", String.valueOf(friends.length()));
			FacebookShare.this.runOnUiThread(new Runnable() {
				public void run() {
					// Do stuff here with your friends array,
					// which is an array of JSONObjects.
				}
			});
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getFBId() {
		Bundle bundle = new Bundle();
		try {
			String response = facebook.request("me", bundle);
			Log.i("friends response", response);
			JSONObject json = Util.parseJson(response);
			String facebookID = json.getString("id");
			String firstName = json.getString("first_name");
			String lastName = json.getString("last_name");
			String email = json.getString("email");
			Log.i("EMAIL ", json.getString("email"));

			appStatus.saveSharedStringValue(appStatus.FB_FIRST_NAME, firstName);
			appStatus.saveSharedStringValue(appStatus.FB_LAST_NAME, lastName);
			appStatus.saveSharedStringValue(appStatus.FB_UID, facebookID);
			appStatus.saveSharedStringValue(appStatus.FB_EMAIL, email);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FacebookError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void postToFacebook() {
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(facebook);
		Bundle params = new Bundle();
		params.putString("name", "CheckinAndroid");
		params.putString("caption", "CheckinAndroid");
		params.putString("link", "http://stage.checkinforgood.com");
		params.putString("description", msgToPost);
		mAsyncFbRunner.request("me/feed", params, "POST",
				new WallPostListener(), null);

		SocialSharingActivity.bIsFromFBTwitter = true;
		finish();
	}

	private final class WallPostListener extends BaseRequestListener {
		@SuppressWarnings("unused")
		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				// @Override
				public void run() {
					// mProgress.cancel();
					Log.i("Posted to Facebook", response);
				}
			});
		}

		@Override
		public void onComplete(String response, Object state) {
			Log.i("Posted to Facebook", response);
		}

		@Override
		public void onFacebookError(FacebookError e, Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onFileNotFoundException(FileNotFoundException e,
				Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onIOException(IOException e, Object state) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onMalformedURLException(MalformedURLException e,
				Object state) {
			// TODO Auto-generated method stub

		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		facebook.authorizeCallback(requestCode, resultCode, data);

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			// goToPrevious();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onAuthenticationResult(Boolean success) {
		if (success) {
			// this.removeDialog(0);
			DisplayToast("Facebook preferences posted!");
		} else {
			DisplayToast("Fail to post facebook preferences!");
		}
		finish();
//		goToPrevious();
	}

	public void goToPrevious() {
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
//		parentActivity.mIdList.clear();
		Intent intent = new Intent(getParent(), SocialSharingActivity.class);
		parentActivity.startChildActivity("View Offers", intent);
	}

	private void postSocialPreferences(String accessToken) {
		String args[] = new String[5];
		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		args[1] = "facebook";
		args[2] = Boolean.toString(true);
		args[3] = accessToken;
		args[4] = "no secret for fb";

		if (appStatus.isOnline()) {
			// this.showDialog(0);
			new SocialSharingTask(this, SocialSharingTask.POST_PREF)
					.execute(args);
		} else {
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			getParent().finish();
			Log.v("facebookShare", "App is not online!");
		}
	}

	private void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		this.removeDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Please Wait...");
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.i("CHECKINFORGOOD", "user cancelling authentication");

			}
		});
		mProgressDialog = dialog;
		return dialog;
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	/*
	 * public void goToPrevious(){ TabGroupActivity parentActivity =
	 * (TabGroupActivity) getParent(); parentActivity.mIdList.clear(); Intent
	 * intent = new Intent(getParent(), SocialSharingActivity.class);
	 * parentActivity.startChildActivity("View Offers", intent); }
	 */

	public boolean getPermissionsFromFB() {
		boolean bStatus = false;

		String uid = appStatus.getSharedStringValue(appStatus.FB_UID);
		String access_token = appStatus
				.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
		
		String path = "https://graph.facebook.com/" + uid + "/"
				+ "permissions?access_token=" + access_token;
		String response;

		WebService webService = new WebService(path);
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		response = webService.webGET(path, params);

		FBPermissionsResult res = null;
		if (response != null)
			Log.i("facebook permissions", response);

		try {
			res = new Gson().fromJson(response, FBPermissionsResult.class);
			Log.e("Heads Up", res.toString());
			if ((res != null) && (res.getData().size() > 0)) {
				int installedSt = res.getData().get(0).getInstalled();
				int emailSt = res.getData().get(0).getEmail();
				int readStreamSt = res.getData().get(0).getRead_stream();
				int publishStreamSt = res.getData().get(0).getPublish_stream();

				if ((installedSt == 1) && (emailSt == 1)
						&& (publishStreamSt == 1))
					bStatus = true;
			}
		} catch (Exception e) {
			Log.e("CHECKINFORGOOD",
					"FBPermissionsResult Error: " + e.getMessage());
		}
		return bStatus;
	}
	
	public class MyTimer extends CountDownTimer {
		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			if (FbDialog.bIsActivityEnabled){
				SocialSharingActivity.bIsFromFBTwitter = true;
				FbDialog.bIsActivityEnabled = false;
				finish();
			}
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}
	}
}
