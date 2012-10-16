package com.checkinlibrary.twitter;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;

public class TwitterShare extends Activity {
	public boolean isFromTwitterShare = false;

	private final Handler mTwitterHandler = new Handler();
	String msgToTweet;

	private boolean bIsFromSocialLogin;
	private static AppStatus appStatus;
	private String token;
	private String secret;

	boolean bIsActivityFinished = false;

	final Runnable mUpdateTwitterNotification = new Runnable() {
		public void run() {
			// Toast.makeText(getBaseContext(), "Tweet sent !",
			// Toast.LENGTH_LONG).show();
			isFromTwitterShare = true;
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_share_loading_screen);

		msgToTweet = getIntent().getStringExtra("MSSSAGE");
		bIsFromSocialLogin = getIntent().getBooleanExtra(
				PrepareRequestTokenActivity.IS_FROM_WHERE, false);
		appStatus = AppStatus.getInstance(this);

		token = appStatus.getSharedStringValue(appStatus.TWITTER_TOKEN);
		secret = appStatus.getSharedStringValue(appStatus.TWITTER_SECRET);

		if ((token == null) || (secret == null)) {
			this.gotoTwitterDialog();
		} else {
			MyTimer tim = new MyTimer(30000, 1000);
			tim.start();

			new AuthenticateTask(this, token, secret).execute();
		}

		// if (TwitterUtils.isAuthenticated(token, secret)) {
		// sendTweet();
		// } else {
		// Intent i = new Intent(getApplicationContext(),
		// PrepareRequestTokenActivity.class);
		// i.putExtra("tweet_msg", getTweetMsg());
		// i.putExtra(PrepareRequestTokenActivity.IS_FROM_WHERE,
		// bIsFromSocialLogin);
		// startActivity(i);
		// finish();
		// }
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private String getTweetMsg() {
		return msgToTweet;
	}

	public void sendTweet() {
		Thread t = new Thread() {
			public void run() {

				try {
					// TwitterUtils.sendTweet(getTweetMsg());
					mTwitterHandler.post(mUpdateTwitterNotification);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		};
		t.start();
	}

	private void clearCredentials() {
		// edit.remove("twitter_token");
		// edit.remove("twitter_secret");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void gotoTwitterDialog() {
		if (!bIsActivityFinished) {
			Intent i = new Intent(getApplicationContext(),
					PrepareRequestTokenActivity.class);
			i.putExtra("tweet_msg", getTweetMsg());
			i.putExtra(PrepareRequestTokenActivity.IS_FROM_WHERE,
					bIsFromSocialLogin);
			startActivity(i);
			bIsActivityFinished = true;
			finish();
		} else {
			finish();
		}
	}

	public class MyTimer extends CountDownTimer {
		public MyTimer(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			if (!bIsActivityFinished)
				Toast.makeText(getApplicationContext(),
						"Twitter is not responding, try after some time..",
						Toast.LENGTH_SHORT).show();
			bIsActivityFinished = true;
			finish();
		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO Auto-generated method stub

		}
	}

	public class AuthenticateTask extends AsyncTask<String, Void, Boolean> {
		private String token;
		private String secret;
		TwitterShare mContext;

		public AuthenticateTask(TwitterShare context, String token,
				String secret) {
			this.token = token;
			this.secret = secret;
			mContext = context;
		}

		protected Boolean doInBackground(String... urls) {
			return TwitterUtils.isAuthenticated(this.token, this.secret);
		}

		protected void onPostExecute(Boolean result) {
			if (!result)
				mContext.gotoTwitterDialog();
		}
	}
}