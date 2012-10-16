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
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.models.AccountResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.models.VideoResult;
import com.checkinlibrary.ws.tasks.CreateAccountTask;


public class SignupActivity extends Activity {
	private ProgressDialog mProgressDialog = null;
	private String mUsername;

	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private EditText mFirstnameEdit;
	private EditText mLastnameEdit;

	public static AppStatus appStatus;
	Handler mhandler;

	private boolean bIsFromSocialLogin;
	private boolean bIsFromTwitter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		appStatus = AppStatus.getInstance(this);
		mhandler = new Handler();

		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);

		bIsFromSocialLogin=getIntent().getBooleanExtra("is_from_social_login",false);
		bIsFromTwitter=getIntent().getBooleanExtra("is_from_twitter",false);
	
		createNewTemplate();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if(bIsFromSocialLogin){
			String access_token = appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN); 
			if(access_token != null){
			//if(appStatus.getSharedBoolValue(appStatus.FACEBOOK_ON)){
				//call social_signup api with fb
				bIsFromTwitter=false;
				//prefillSignupPage();
			}else if(appStatus.getSharedBoolValue(appStatus.TWITTER_ON)){
				//call social_signup api with tw
				bIsFromTwitter=true;
				prefillSignupPage();
			}
		}else{
			//do nothing
		}
	}

	public void createNewTemplate() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.create_new_account,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);
		
		mUsernameEdit = (EditText) findViewById(R.id.EditLoginEmailCreate);
		mPasswordEdit = (EditText) findViewById(R.id.EditLoginPasswordCreate);
		mFirstnameEdit = (EditText) findViewById(R.id.EditLoginFirstnameCreate);
		mLastnameEdit = (EditText) findViewById(R.id.EditLoginLastnameCreate);
	}

	public void createNew(View button) {
		String[] args = new String[10];

		args[0] = mUsername = mUsernameEdit.getText().toString();
		args[1] = mPasswordEdit.getText().toString();
		args[2] = mFirstnameEdit.getText().toString();
		args[3] = mLastnameEdit.getText().toString();
		args[4] = appStatus.getSharedStringValue(appStatus.APP_NAME);
		
		if(bIsFromSocialLogin){
			if(bIsFromTwitter){ // twitter
				args[5] = appStatus.getSharedStringValue(appStatus.TWITTER_TOKEN); 
				args[6] = appStatus.getSharedStringValue(appStatus.TWITTER_SECRET);
				args[7] = appStatus.getSharedStringValue(appStatus.TW_UID); 
				args[8] = "twitter";
				args[9] = Boolean.toString(appStatus.getSharedBoolValue(appStatus.TWITTER_ON));
				
			}else{  //facebook
				args[5] = appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN); 
				args[6] = "no secret for fb"; 
				args[7] = appStatus.getSharedStringValue(appStatus.FB_UID); 
				args[8] = "facebook";
				args[9] = Boolean.toString(appStatus.getSharedBoolValue(appStatus.FACEBOOK_ON));
				
			}
		}

		if(SignupActivity.appStatus.isOnline()) {
			showDialog(0);
			new CreateAccountTask(this,bIsFromSocialLogin).execute(args);
		} else {
			Log.v("CreateAccount", "App is not online!");
			Intent intent= new Intent(this,NoConnectivityScreen.class);
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

		if(result.getCheckinAmount()==null || result.getCheckinAmount().toString().equals("null")){

			checkinAmount = "0.00";

		}else{

			checkinAmount=String.format("%.2f", result.getCheckinAmount());
		}

		String checkinCount;
		checkinCount=String.valueOf(result.getCheckin_count());

		//save to sharepreferences
		appStatus.saveSharedStringValue(appStatus.AUTH_KEY, authToken);
		appStatus.saveSharedStringValue(appStatus.USER_NAME, mUsername);


		Log.v("LoginActivity", "####Checkin Amount from API: " +result.getCheckinAmount());
		addToPreferences(checkinAmount,checkinCount,result.getVideo_links());
		if (success) {
			//Save our causes
			List<OrganizationResult> causes = result.getOrganizations();
			if ( causes != null ) {
				Log.v("CHECKINFORGOOD", "Got login result with my organizations");
				MyOrganizationDBAdapter adapter=new MyOrganizationDBAdapter((Context)this);
				adapter.refresh(causes);
			}
			// Return home
			Intent intent= new Intent();
			intent.putExtra("LOGIN_FLAG", true);
			setResult(2, intent);
			/*
			final Intent homeIntent = new Intent(this, CheckinLibraryActivity.class);
			homeIntent.putExtra("LOGIN_FLAG", true);
			startActivity(homeIntent);
			 */
			bIsFromSocialLogin=false;
			finish();
		} else {
			Log.e("CHECKINFORGOOD",
					"onAuthenticationResult: failed to authenticate");
			if(result.getMessage()!=null){
				DisplayToast(result.getMessage());
			}else DisplayToast("Can't connect right now, try after some time!");
		}
		Log.e("CHECKINFORGOOD##########",
				"onAuthenticationResult:"+ result.getMessage());
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
		Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
		removeDialog(0);
	}    

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {

//			Intent intent= new Intent(this, LoginActivity.class);
//			startActivity(intent);
			finish();		
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void addToPreferences(String strCheckinAmount,String strCheckinCount,VideoResult mVideoResult) {
		AppStatus appStatus=AppStatus.getInstance(this);
		appStatus.saveSharedStringValue(appStatus.CHECKIN_AMOUNT, strCheckinAmount);
		appStatus.saveSharedStringValue(appStatus.CHECKIN_COUNT, strCheckinCount);

		//store video links in -----------------//
		if(mVideoResult != null){
			if(mVideoResult.getApp_user_link() != null){
				appStatus.saveSharedStringValue(appStatus.APP_USER_LINK, mVideoResult.getApp_user_link().getLink());	
				appStatus.saveSharedStringValue(appStatus.APP_USER_COVER, mVideoResult.getApp_user_link().getCover());
			}

			if(mVideoResult.getGrow_business_link() != null){
				appStatus.saveSharedStringValue(appStatus.GROW_BUS_LINK, mVideoResult.getGrow_business_link().getLink());	
				appStatus.saveSharedStringValue(appStatus.GROW_BUS_COVER, mVideoResult.getGrow_business_link().getCover());
			}

			if(mVideoResult.getOverview_link() != null){
				appStatus.saveSharedStringValue(appStatus.OVERVIEW_LINK, mVideoResult.getOverview_link().getLink());	
				appStatus.saveSharedStringValue(appStatus.OVERVIEW_COVER, mVideoResult.getOverview_link().getCover());
			}
		}
	}
	
	public void message(String msg) {
		final String mesage = msg;
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				Toast toast = Toast.makeText(SignupActivity.this, mesage, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}
	
	private void prefillSignupPage(){
		if(!bIsFromTwitter){
			mFirstnameEdit.setText(appStatus.getSharedStringValue(appStatus.FB_FIRST_NAME));
			mLastnameEdit.setText(appStatus.getSharedStringValue(appStatus.FB_LAST_NAME));
			mUsernameEdit.setText(appStatus.getSharedStringValue(appStatus.FB_EMAIL));
		}else{
			mFirstnameEdit.setText(appStatus.getSharedStringValue(appStatus.TW_FIRST_NAME));
			mLastnameEdit.setText(appStatus.getSharedStringValue(appStatus.TW_LAST_NAME));
		}
	}

	
}
