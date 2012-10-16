package com.checkinlibrary.account;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.facebook.FacebookShare;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.orgs.CauseResult;
import com.checkinlibrary.twitter.PrepareRequestTokenActivity;
import com.checkinlibrary.twitter.TwitterShare;
import com.checkinlibrary.ws.tasks.CreateAccountTask;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class SignupActivity extends Activity {
	private ProgressDialog mProgressDialog = null;
	private String mUsername;

	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private EditText mFirstnameEdit;
	private EditText mLastnameEdit;

	private int iActivityFlag = 0;

	static final int CREATE_ACC_ID = 0;
	static final int SOCIAL_SIGN_UP_ID = 1;

	public static AppStatus appStatus;
	Handler mhandler;

	public static boolean bIsFromAcctManager = false;
	LoginActivity loginContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		appStatus = AppStatus.getInstance(this);
		mhandler = new Handler();

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);

		loginContext = new LoginActivity();
		createNewTemplate();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void createNewTemplate() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.create_new_account,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		iActivityFlag = CREATE_ACC_ID;
		
		mUsernameEdit = (EditText) findViewById(R.id.EditLoginEmailCreate);
		mPasswordEdit = (EditText) findViewById(R.id.EditLoginPasswordCreate);
		mFirstnameEdit = (EditText) findViewById(R.id.EditLoginFirstnameCreate);
		mLastnameEdit = (EditText) findViewById(R.id.EditLoginLastnameCreate);
	}

	public void createNew(View button) {
		String[] args = new String[8];

		args[0] = mUsername = mUsernameEdit.getText().toString();
		args[1] = mPasswordEdit.getText().toString();
		args[2] = mFirstnameEdit.getText().toString();
		args[3] = mLastnameEdit.getText().toString();
		
		//mUsernameEdit = (EditText) findViewById(R.id.EditLoginEmailCreate);
		//mPasswordEdit = (EditText) findViewById(R.id.EditLoginPasswordCreate);
		//mFirstnameEdit = (EditText) findViewById(R.id.EditLoginFirstnameCreate);
		//mLastnameEdit = (EditText) findViewById(R.id.EditLoginLastnameCreate);

		if (SignupActivity.appStatus.isOnline()) {
			showDialog(0);
			Log.i("***HEADSUP", "create task called");
			new CreateAccountTask(this).execute(args);
		} else {
			Log.v("CreateAccount", "App is not online!");
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	public void onAuthenticationResult(AccountResult result) {

		String authToken = result.getApiKey();
		boolean success = ((authToken != null) && (authToken.length() > 0));
		Log.i("CHECKINFORGOOD", "onAuthenticationResult(" + success + ")");

		// Hide the progress dialog
		hideProgress();

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
		appStatus.saveSharedStringValue(appStatus.USER_NAME, mUsername);

		Log.v("LoginActivity",
				"####Checkin Amount from API: " + result.getCheckinAmount());
		addToPreferences(checkinAmount, checkinCount);
		if (success) {
			// Save our causes
			// Return home
			bIsFromAcctManager = true;
			/*
			 * final Intent homeIntent = new Intent(this,
			 * SettingsActivity.class); homeIntent.putExtra("LOGIN_FLAG", true);
			 * startActivity(homeIntent);
			 */

			List<CauseResult> causes = result.getOrganizations();

			if (causes != null) {
				Log.v("CHECKINFORGOOD",
						"Got login result with my organizations");
				MyOrganizationDBAdapter adapter = new MyOrganizationDBAdapter(
						(Context) this);

				adapter.refresh(causes);
			}
			
			SignupActivity.bIsFromAcctManager = true;
			Intent i = new Intent();
			setResult(MyConstants.LOGIN_REQ_CODE, i);
			finish();

			// get shared preferences
			//getSocialSharedPreferences(authToken);

			// finish();
		} else {
			Log.e("CHECKINFORGOOD",
					"onAuthenticationResult: failed to authenticate");
			if (result.getMessage() != null) {
				DisplayToast(result.getMessage());
			} else
				DisplayToast("Can't connect right now, try after some time!");
		}
		Log.e("CHECKINFORGOOD##########",
				"onAuthenticationResult:" + result.getMessage());
		// finish();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setTitle("Please Wait...");
		dialog.setMessage(getString(R.string.signupProgressMessage));

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

	private void hideProgress() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		removeDialog(0);
	}

	/*public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			final Intent intent;
			switch (iActivityFlag) {
			case CREATE_ACC_ID:
				initailizeLoginPage();
				break;
			case SOCIAL_SIGN_UP_ID:
				intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
				finish();
				iActivityFlag = SOCIAL_SIGN_UP_ID;
				break;
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			/*Intent intent= new Intent(this, LoginActivity.class);
			startActivity(intent);*/
			finish();		
			return true;
		}
		return super.onKeyDown(keyCode, event);
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
				Toast toast = Toast.makeText(SignupActivity.this, mesage, 8000);
				toast.show();
			}
		});
	}

	/*// ------------------Social login---Integration-------------------
	public void facebook_signup(View button) {
		Log.i("SocialSignupActivity", "facebook clicked");

		if (appStatus.isOnline()) {
			Log.e("LoginActivity", "Facebook Clicked ");
			Intent intent_ShareFB = new Intent(SignupActivity.this,
					FacebookShare.class);
			intent_ShareFB.putExtra("MSSSAGE", "");
			intent_ShareFB.putExtra(FacebookShare.FROM_WHERE_FLAG, true);
			bIsFromSocialLogin = true;
			startActivity(intent_ShareFB);
		} else {
			Log.v("LoginActivity", "App is not online!");
			
			 Intent intent= new
			 Intent(SignupActivity.this,NoConnectivityScreen.class);
			 startActivity(intent); 
			 finish();
			
		}
	}

	public void twitter_signup(View button) {
		Log.i("SocialSignupActivity", "twitter clicked");
		if (appStatus.isOnline()) {
			Intent intent_ShareTW = new Intent(SignupActivity.this,
					TwitterShare.class);
			intent_ShareTW.putExtra("MSSSAGE", "");
			intent_ShareTW.putExtra(PrepareRequestTokenActivity.IS_FROM_WHERE,
					true);
			bIsFromSocialLogin = true;
			startActivity(intent_ShareTW);

			Log.e("LoginActivity", "Twitter Clicked ");
		} else {
			Log.v("LoginActivity", "App is not online!");
			
			 Intent intent= new
			 Intent(SignupActivity.this,NoConnectivityScreen.class);
			 startActivity(intent); 
			 finish();
			 
		}
	}*/


/*	public void initailizeLoginPage() {
		iActivityFlag = SOCIAL_SIGN_UP_ID;
		bIsFromSocialLogin = false;
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.social_signup,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);
	}

	private void getSocialSharedPreferences(String mAuthToken) {
		// get social sharePreferences if any
		String args[] = new String[1];
		args[0] = mAuthToken;
		if (appStatus.isOnline()) {
			showDialog(0);

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
*/
	public void onAuthenticationResult(Boolean result) {
		// Hide the progress dialog
		hideProgress();

		if (result) {

		} else {
			message("Fail to get shared preferences!");
		}

		SignupActivity.bIsFromAcctManager = true;
		Intent i = new Intent();
		setResult(MyConstants.LOGIN_REQ_CODE, i);
		finish();
	}

}
