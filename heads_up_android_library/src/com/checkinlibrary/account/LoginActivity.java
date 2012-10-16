package com.checkinlibrary.account;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.facebook.FacebookShare;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.orgs.CauseResult;
import com.checkinlibrary.twitter.PrepareRequestTokenActivity;
import com.checkinlibrary.twitter.TwitterShare;
import com.checkinlibrary.ws.tasks.ForgotPasswordTask;
import com.checkinlibrary.ws.tasks.LoginTask;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class LoginActivity extends Activity {
	private TextView mMessage;
	private LoginTask mAuthTask = null;
	private ProgressDialog mProgressDialog = null;

	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private TextView mForgotPwd;
	private TextView mTxtAlreadyHaveAcc;

	private int iActivityFlag = 0;

	static final int LOGIN_ID = 0;
	static final int FRGT_PWD_ID = 1;
	static final int GET_PREF = 2;
	static final int SOCIAL_LOGIN_ID = 3;

	public static AppStatus appStatus;
	Handler mhandler;

	private boolean bIsFromSocialLogin = false;
	private boolean bIsFromTwitter = false;
	public static boolean bISFromLoginActivity = false;
	private String mUsername;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		appStatus = AppStatus.getInstance(this);
		mhandler = new Handler();

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);

		initailizeSocialLoginPage();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (bIsFromSocialLogin) {
			String[] args = new String[8];
			if (!bIsFromTwitter) { // facebook
				String access_token = appStatus
						.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
				if (access_token != null) {
					// args[0] =
					// appStatus.getSharedStringValue(appStatus.FB_UID);
					// args[1] = "facebook";

					args[0] = appStatus.getSharedStringValue(appStatus.FB_UID);
					args[1] = "facebook";
					args[2] = appStatus
							.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
					args[3] = "no secret for fb";
					args[4] = Boolean.toString(appStatus
							.getSharedBoolValue(appStatus.FACEBOOK_ON));
					args[5] = appStatus
							.getSharedStringValue(appStatus.FB_EMAIL);
					args[6] = appStatus
							.getSharedStringValue(appStatus.FB_FIRST_NAME);
					args[7] = appStatus
							.getSharedStringValue(appStatus.FB_LAST_NAME);

					bIsFromTwitter = false;
					sendLogin(args);
				} else {
					Toast.makeText(this, "Oops. We didn't hear back from Facebook. Please sign in with your email address or try again in a few minutes.", Toast.LENGTH_LONG).show();
					bIsFromSocialLogin = false;
				}
			} else { // twitter
				if (appStatus.getSharedBoolValue(appStatus.TWITTER_ON)) {
					args[0] = appStatus.getSharedStringValue(appStatus.TW_UID);
					args[1] = "twitter";
					bIsFromTwitter = false;
					sendLogin(args);
				}
			}
		} else {
			// do nothing
		}
	}

	public void login(View button) {

		iActivityFlag = LOGIN_ID;

		String[] args = new String[2];

		String mPassword = mPasswordEdit.getText().toString();
		mUsername = mUsernameEdit.getText().toString();

		mMessage = (TextView) findViewById(R.id.loginMessage);
		mMessage.setText(getMessage(LOGIN_ID));

		args[0] = mUsername;
		args[1] = mPassword;

		sendLogin(args);
	}

	public void sendLogin(String[] args) {
		if (LoginActivity.appStatus.isOnline()) {
			showDialog(0);
			new LoginTask(this, bIsFromSocialLogin).execute(args);
			// bIsFromSocialLogin=false;
		} else {
			Log.v("LoginActivity", "App is not online!");
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	public void forgotPassword(View button) {
		mUsernameEdit = (EditText) findViewById(R.id.EditForgotPwdEmail);
		mUsername = mUsernameEdit.getText().toString();

		mMessage = (TextView) findViewById(R.id.forgotPwdMessage);
		mMessage.setText(getMessage(FRGT_PWD_ID));

		sendForgotPwd(mUsername);
	}

	public void sendForgotPwd(String email) {
		if (LoginActivity.appStatus.isOnline()) {
			showDialog(0);
			new ForgotPasswordTask(this).execute(email);
		} else {
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			Log.v("ForgotPassword", "App is not online!");
			finish();
		}
	}

	public void createNewTemplate(View button) {
		Intent intent = new Intent(this, SignupActivity.class);
		this.startActivityForResult(intent, MyConstants.LOGIN_REQ_CODE);
		// finish();
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == MyConstants.LOGIN_REQ_CODE) {
			SignupActivity.bIsFromAcctManager = false;
			setResult(MyConstants.LOGIN_REQ_CODE);
			finish();
		}
	}

	public void createNewTemplateforForgotPwd(View button) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.forgot_password,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		iActivityFlag = FRGT_PWD_ID;
	}

	public void onAuthenticationResult(AccountResult result) {

		String authToken = result.getApiKey();
		boolean success = ((authToken != null) && (authToken.length() > 0));
		Log.i("CHECKINFORGOOD", "onAuthenticationResult(" + success + ")");

		// Hide the progress dialog
		// hideProgress();

		bIsFromSocialLogin = false;

		String checkinAmount;

		if (result.getCheckinAmount() == null
				|| result.getCheckinAmount().toString().equals("null")) {

			checkinAmount = "0.00";

		} else {

			checkinAmount = String.format("%.2f", result.getCheckinAmount());
		}

		String checkinCount;
		checkinCount = String.valueOf(result.getCheckin_count());

		// save to sharepreferences
		appStatus.saveSharedStringValue(appStatus.AUTH_KEY, authToken);
		// appStatus.saveSharedStringValue(appStatus.USER_NAME, mUsername);
		
		if((result.getMobile_user() != null) && (result.getMobile_user().getmUserPref()!= null) &&
				(result.getMobile_user().getmUserPref().getEmail()!= null)){
			appStatus.saveSharedStringValue(appStatus.USER_NAME, result.getMobile_user().getmUserPref().getEmail());
		}

		Log.v("LoginActivity",
				"####Checkin Amount from API: " + result.getCheckinAmount());
		addToPreferences(checkinAmount, checkinCount);
		if (success) {
			// Save our causes
			List<CauseResult> causes = result.getOrganizations();
			Log.v("#####HeadsUp",
					"Total causes" + String.valueOf(causes.size()));
			if (causes != null) {
				Log.v("CHECKINFORGOOD",
						"Got login result with my organizations");
				MyOrganizationDBAdapter adapter = new MyOrganizationDBAdapter(
						(Context) this);
				adapter.refresh(causes);
			}

			// get shared preferences
			getSocialSharedPreferences(authToken);
		} else {
			Log.e("CHECKINFORGOOD",
					"onAuthenticationResult: failed to authenticate");
			hideProgress();
			if (result.getMessage() != null) {
				DisplayToast(result.getMessage());
			} else
				DisplayToast("Can't connect right now, try after some time!");
		}
		Log.e("CHECKINFORGOOD##########",
				"onAuthenticationResult:" + result.getMessage());
	}

	// For forgot password

	public void onAuthenticationResult(Boolean status, String message) {
		boolean success = status;
		Log.i("CHECKINFORGOOD", "onAuthenticationResult(" + success + ")");

		mAuthTask = null;

		// Hide the progress dialog hideProgress();
		hideProgress();

		if (message != null) {
			DisplayToast(message);

			if (status) {
				final Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
				iActivityFlag = LOGIN_ID;
				finish();
			}
		}
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Please Wait...");
		switch (id) {
		case LOGIN_ID:
			dialog.setMessage(getString(R.string.signInProgressMessage));
			break;
		case GET_PREF:
			dialog.setMessage(getString(R.string.sharePrefProgressMessage));
			break;
		case FRGT_PWD_ID:
			dialog.setMessage(getString(R.string.forgotPwdProgressMessage));
			break;
		}

		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				Log.i("CHECKINFORGOOD", "user cancelling authentication");
				if (mAuthTask != null) {
					mAuthTask.cancel(true);
				}

			}
		});

		mProgressDialog = dialog;
		return dialog;
	}

	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	private CharSequence getMessage(int iFlag) {

		CharSequence msg = null;
		switch (iFlag) {
		case LOGIN_ID:
			msg = "Log in for Checkinforgood.com";
			break;
		case FRGT_PWD_ID:
			msg = "Forgot Password for Checkinforgood.com";
			break;
		}
		return msg;
	}

	public void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		removeDialog(0);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			switch (iActivityFlag) {
			case FRGT_PWD_ID:
				initailizeLoginPage();
				break;
			case LOGIN_ID:
				initailizeSocialLoginPage();
				break;
			default:
				setResult(MyConstants.EXIT_REQ_CODE);
				finish();
				break;
			}
			return true;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void ShowMessageBox() {
		AlertDialog exitAlert = new AlertDialog.Builder(this).create();
		exitAlert.setTitle("Exit Application");
		exitAlert.setMessage("Are you sure you want to exit application?");

		exitAlert.setButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				bISFromLoginActivity = true;
				finish();
			}
		});
		exitAlert.setButton2("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		exitAlert.show();
	}

	public void addToPreferences(String strCheckinAmount, String strCheckinCount) {
		AppStatus appStatus = AppStatus.getInstance(this);
		appStatus.saveSharedStringValue(appStatus.CHECKIN_AMOUNT,
				strCheckinAmount);
		appStatus.saveSharedStringValue(appStatus.CHECKIN_COUNT,
				strCheckinCount);
	}

	public void message(String msg) {
		final String mesage = msg;
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(LoginActivity.this, mesage, 8000);
				toast.show();
			}
		});
	}

	private void getSocialSharedPreferences(String mAuthToken) {
		// get social sharePreferences if any
		String args[] = new String[1];
		args[0] = mAuthToken;
		if (appStatus.isOnline()) {
			// showDialog(0);
			new SocialSharingTask(this, SocialSharingTask.GET_PREF)
					.execute(args);
		} else {
			Log.v("CHECKINFORGOOD", "App is not online!");
			message("App is not online!");

			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	public void onAuthenticationResult(Boolean result) {
		// Hide the progress dialog
		hideProgress();

		if (result) {

		} else {
			message("Fail to get shared preferences!");
		}

		// Return home
		// Rahul 2 lines
//		final Intent homeIntent = new Intent(this, TabGroupActivity.class);//
//		homeIntent.putExtra("LOGIN_FLAG", true);
//		startActivity(homeIntent);//

		SignupActivity.bIsFromAcctManager = true;
		Intent i = new Intent();
		setResult(MyConstants.LOGIN_REQ_CODE, i);
		finish();
	}

	// ------------------Social login---Integration-------------------
	public void facebook_signin(View button) {
		Log.i("SocialSignupActivity", "facebook clicked");

		if (appStatus.isOnline()) {
			Log.e("LoginActivity", "Facebook Clicked ");
			Intent intent_ShareFB = new Intent(LoginActivity.this,
					FacebookShare.class);
			intent_ShareFB.putExtra("MSSSAGE", "");
			intent_ShareFB.putExtra(FacebookShare.FROM_WHERE_FLAG, true);
			bIsFromSocialLogin = true;
			startActivity(intent_ShareFB);
		} else {
			Log.v("LoginActivity", "App is not online!");

			Intent intent = new Intent(LoginActivity.this,
					NoConnectivityScreen.class);
			startActivity(intent);
			bISFromLoginActivity = true;
			finish();
		}
	}

	public void twitter_signin(View button) {
		Log.i("SocialSignupActivity", "twitter clicked");
		if (appStatus.isOnline()) {
			Intent intent_ShareTW = new Intent(LoginActivity.this,
					TwitterShare.class);
			intent_ShareTW.putExtra("MSSSAGE", "");
			intent_ShareTW.putExtra(PrepareRequestTokenActivity.IS_FROM_WHERE,
					true);
			bIsFromSocialLogin = true;
			startActivity(intent_ShareTW);

			Log.e("LoginActivity", "Twitter Clicked ");
		} else {
			Log.v("LoginActivity", "App is not online!");

			Intent intent = new Intent(LoginActivity.this,
					NoConnectivityScreen.class);
			startActivity(intent);
			bISFromLoginActivity = true;
			finish();

		}
	}

	public void initailizeLoginPage() {
		iActivityFlag = LOGIN_ID;
		bIsFromSocialLogin = false;
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.login,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		mUsernameEdit = (EditText) findViewById(R.id.EditLoginEmail);
		mPasswordEdit = (EditText) findViewById(R.id.EditLoginPassword);
		mForgotPwd = (TextView) findViewById(R.id.forgotPwd);

		mForgotPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createNewTemplateforForgotPwd(v);
			}
		});
	}

	public void initailizeSocialLoginPage() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.social_signin,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		mTxtAlreadyHaveAcc = (TextView) findViewById(R.id.txtSigninWithEmail);
		mTxtAlreadyHaveAcc.setText(Html
				.fromHtml(getString(R.string.social_have_account)));

		iActivityFlag = SOCIAL_LOGIN_ID;
		hideProgress();

		mTxtAlreadyHaveAcc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initailizeLoginPage();
			}
		});
	}
}
