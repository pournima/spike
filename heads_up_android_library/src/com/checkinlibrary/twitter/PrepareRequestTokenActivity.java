package com.checkinlibrary.twitter;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.OAuthProvider;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import twitter4j.IDs;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.http.AccessToken;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class PrepareRequestTokenActivity extends Activity {

	TwitterShare mTwitterShare;
	final String TAG = getClass().getName();

	private OAuthConsumer consumer;
	private OAuthProvider provider;
	private ProgressDialog mProgressDialog;
	private boolean isFromTwitterShare;

	private boolean bIsFromSocialLogin;
	private static AppStatus appStatus;

	public static final String IS_FROM_WHERE = "FROM_WHERE";
	private boolean bIsFromBrowser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_share_loading_screen);

		isFromTwitterShare = true;
		bIsFromSocialLogin = getIntent().getBooleanExtra(IS_FROM_WHERE, false);
		appStatus = AppStatus.getInstance(this);
		bIsFromBrowser = false;

		try {
			this.consumer = new CommonsHttpOAuthConsumer(
					Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET);
			this.provider = new CommonsHttpOAuthProvider(Constants.REQUEST_URL,
					Constants.ACCESS_URL, Constants.AUTHORIZE_URL);
		} catch (Exception e) {
			Log.e(TAG, "Error creating consumer / provider", e);
		}

		Log.i(TAG, "Starting task to retrieve request token.");
		new OAuthRequestTokenTask(this, consumer, provider).execute();
	}

	/**
	 * Called when the OAuthRequestTokenTask finishes (user has authorized the
	 * request token). The callback URL will be intercepted here.
	 */
	@Override
	public void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		final Uri uri = intent.getData();
		bIsFromBrowser = true;
		if (uri != null
				&& uri.getScheme().equals(Constants.OAUTH_CALLBACK_SCHEME)) {
			Log.i(TAG, "Callback received : " + uri);
			Log.i(TAG, "Retrieving Access Token");

			// isFromTwitterShare = true;
			new RetrieveAccessTokenTask(this, consumer, provider).execute(uri);
			// finish();//RR..
		}
	}

	public class RetrieveAccessTokenTask extends AsyncTask<Uri, Void, Void> {

		private Context context;
		private OAuthProvider provider;
		private OAuthConsumer consumer;

		public RetrieveAccessTokenTask(Context context, OAuthConsumer consumer,
				OAuthProvider provider) {
			this.context = context;
			this.consumer = consumer;
			this.provider = provider;
		}

		/**
		 * Retrieve the oauth_verifier, and store the oauth and
		 * oauth_token_secret for future API calls.
		 */
		@Override
		protected Void doInBackground(Uri... params) {
			final Uri uri = params[0];
			final String oauth_verifier = uri
					.getQueryParameter(OAuth.OAUTH_VERIFIER);

			try {
				provider.retrieveAccessToken(consumer, oauth_verifier);

				String token = consumer.getToken();
				String secret = consumer.getTokenSecret();

				appStatus.saveSharedStringValue(appStatus.TWITTER_TOKEN, token);
				appStatus.saveSharedStringValue(appStatus.TWITTER_SECRET,
						secret);
				appStatus.saveSharedBoolValue(appStatus.TWITTER_ON, true);

				getFollowerList();
				if (!bIsFromSocialLogin) {
					postSocialPreferences(token, secret);
				} else {
					finish();
				}
				Log.i(TAG, "OAuth - Access Token Retrieved");

			} catch (Exception e) {
				Log.e(TAG, "OAuth - Access Token Retrieval Error", e);
				finish();
			}

			return null;
		}

		private void getFollowerList() {
			String token = consumer.getToken();
			String secret = consumer.getTokenSecret();
			AccessToken a = new AccessToken(token, secret);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(Constants.CONSUMER_KEY,
					Constants.CONSUMER_SECRET);
			twitter.setOAuthAccessToken(a);
			try {
				// IDs obj=twitter.getFriendsIDs(); // following
				IDs obj = twitter.getFollowersIDs();
				Log.i("Twitter followers count : ",
						String.valueOf(obj.getIDs().length));
				long userID = twitter.getId();

				User user = twitter.showUser((int) userID);
				String strUserName = user.getName();

				String[] parts = strUserName.split(" ");
				String firstName = "", lastName = "";
				if ((parts != null) && (parts.length > 0)) {
					firstName = parts[0];
					lastName = parts[parts.length - 1];
				}
				Log.i("Twitter UserID : ", String.valueOf(userID));
				Log.i("Twitter UserName : ", strUserName);
				appStatus.saveSharedStringValue(appStatus.TW_FIRST_NAME,
						firstName);
				appStatus.saveSharedStringValue(appStatus.TW_LAST_NAME,
						lastName);
				appStatus.saveSharedStringValue(appStatus.TW_UID,
						String.valueOf(userID));

			} catch (TwitterException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				finish();
			}
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onAuthenticationResult(Boolean success) {
		// removeDialog(0);
		if (success) {
			DisplayToast("Twitter preferences updated!");
		} else {
			DisplayToast("Fail to update twitter preferences!");
		}
		// startActivity(new Intent(this,TwitterShare.class));
		finish();
	}

	private void postSocialPreferences(String accessToken, String accessSceret) {
		String args[] = new String[5];
		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		args[1] = "twitter";
		args[2] = Boolean.toString(true);
		args[3] = accessToken;
		args[4] = accessSceret;
		if (appStatus.isOnline()) {
			// showDialog(0);
			new SocialSharingTask(this, SocialSharingTask.POST_PREF)
					.execute(args);
		} else {
			Log.v("TwitterShare", "App is not online!");
			DisplayToast("App is not online!");
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	private void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
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
	protected void onResume() {
		super.onResume();
		Log.i("######## Twitter", "In on resume of prepareRequestTokenActivity");
		if (isFromTwitterShare) {
			isFromTwitterShare = false;

		} else {
			if (!bIsFromBrowser)
				finish();
			else
				bIsFromBrowser = false;
		}
	}
}
