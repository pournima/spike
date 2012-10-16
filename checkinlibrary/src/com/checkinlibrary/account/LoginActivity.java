package com.checkinlibrary.account;

import java.util.List;

import android.accounts.AccountAuthenticatorActivity;
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

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.facebook.android.FacebookShare;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.models.AccountResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.models.VideoResult;
import com.checkinlibrary.twitter.PrepareRequestTokenActivity;
import com.checkinlibrary.twitter.TwitterShare;
import com.checkinlibrary.ws.tasks.ForgotPasswordTask;
import com.checkinlibrary.ws.tasks.LoginTask;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class LoginActivity extends AccountAuthenticatorActivity {
	private TextView mMessage;
	private LoginTask mAuthTask = null;
	private ProgressDialog mProgressDialog = null;

	private EditText mUsernameEdit;
	private EditText mPasswordEdit;
	private TextView mForgotPwd;
	private TextView mTxtAlreadyHaveAcc;

	private int iActivityFlag=0;

	static final int LOGIN_ID         = 0;
	static final int FRGT_PWD_ID      = 1;
	static final int GET_PREF         = 2;
	static final int SOCIAL_LOGIN_ID  = 3;

	public static AppStatus appStatus;
	Handler mhandler;

	private boolean bIsFromSocialLogin=false;
	private boolean bIsFromTwitter=false;
	private String mUsername;
	CheckinLibraryActivity context;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_LEFT_ICON);

		appStatus = AppStatus.getInstance(this);
		mhandler = new Handler();
		
		getWindow().setFeatureDrawableResource(Window.FEATURE_LEFT_ICON,
				android.R.drawable.ic_dialog_alert);
		initailizeSocialLoginPage();
		context=new CheckinLibraryActivity();
		
	}

	@Override
	protected void onResume() {
		super.onResume();

		if(bIsFromSocialLogin){
			String[] args = new String[9];
			if(!bIsFromTwitter){ // facebook
				String access_token = appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN); 
				if(access_token != null){
					//if(appStatus.getSharedBoolValue(appStatus.FACEBOOK_ON)){
					args[0] = appStatus.getSharedStringValue(appStatus.FB_UID); 
					args[1] = "facebook";
					args[2] = appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN); 
					args[3] = "no secret for fb"; 
					args[4] = Boolean.toString(appStatus.getSharedBoolValue(appStatus.FACEBOOK_ON));
					args[5]=appStatus.getSharedStringValue(appStatus.FB_EMAIL);
					args[6]=appStatus.getSharedStringValue(appStatus.FB_FIRST_NAME);
					args[7]=appStatus.getSharedStringValue(appStatus.FB_LAST_NAME);
					args[8]=appStatus.getSharedStringValue(appStatus.APP_NAME);
					
					bIsFromTwitter=false;
					sendLogin(args);
				}
			}else{ //twitter
				if(appStatus.getSharedBoolValue(appStatus.TWITTER_ON)){
					args[0] = appStatus.getSharedStringValue(appStatus.TW_UID); 
					args[1] = "twitter";
					args[2] = appStatus.getSharedStringValue(appStatus.TWITTER_TOKEN); 
					args[3] = appStatus.getSharedStringValue(appStatus.TWITTER_SECRET);
					args[4] = Boolean.toString(appStatus.getSharedBoolValue(appStatus.TWITTER_ON));
					args[5]=appStatus.getSharedStringValue(appStatus.APP_NAME);
					
					bIsFromTwitter=false;
					sendLogin(args);
				}
			}
		}else{
			//do nothing
		}
	}

	public void login(View button) {

		iActivityFlag=LOGIN_ID;

		String[] args = new String[3];

		String mPassword = mPasswordEdit.getText().toString();
		mUsername = mUsernameEdit.getText().toString();

		mMessage = (TextView) findViewById(R.id.loginMessage);
		mMessage.setText(getMessage(LOGIN_ID));

		args[0]=mUsername;
		args[1]=mPassword;
		args[2]=appStatus.getSharedStringValue(appStatus.APP_NAME);
		sendLogin(args);
	}

	public void sendLogin(String[] args) {
		if(LoginActivity.appStatus.isOnline()) {
			showDialog(0);
			new LoginTask(this,bIsFromSocialLogin).execute(args);
			//bIsFromSocialLogin=false;
		} else {
			Log.v("LoginActivity", "App is not online!");
			Intent intent= new Intent(this,NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	public void forgotPassword(View button) {
		mUsernameEdit = (EditText) findViewById(R.id.EditForgotPwdEmail);
		mUsername = mUsernameEdit.getText().toString();

		mMessage = (TextView) findViewById(R.id.forgotPwdMessage);
		mMessage.setText(getMessage(FRGT_PWD_ID));
		String[] args = new String[2];
		args[0]=mUsername;
		args[1]=appStatus.getSharedStringValue(appStatus.APP_NAME);
		sendForgotPwd(args);
	}

	public void sendForgotPwd(String[] args) {
		if(LoginActivity.appStatus.isOnline()) {
			showDialog(0);
			new ForgotPasswordTask(this).execute(args);
		} else {
			Intent intent= new Intent(this,NoConnectivityScreen.class);
			this.startActivity(intent);
			Log.v("ForgotPassword", "App is not online!");
			finish();
		} 
	}

	public void createNewTemplate(View button) {
		Intent intent= new Intent(this,SignupActivity.class);
		intent.putExtra("is_from_social_login", bIsFromSocialLogin);
		intent.putExtra("is_from_twitter", bIsFromTwitter);
		this.startActivityForResult(intent, 111);
		//finish();
	}

	public void createNewTemplateforForgotPwd(View button) {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.forgot_password,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		iActivityFlag=FRGT_PWD_ID;
	}

	public void onAuthenticationResult(AccountResult result) {

		String authToken = result.getApiKey();
		boolean success = ((authToken != null) && (authToken.length() > 0));
		Log.i("CHECKINFORGOOD", "onAuthenticationResult(" + success + ")");

		// Hide the progress dialog
		//hideProgress();
		
		bIsFromSocialLogin=false;

		/*if(bIsFromSocialLogin){
			if((!result.getLogin()) && (!result.isIs_user_exist())){
				Intent intent= new Intent(this,SignupActivity.class);
				intent.putExtra("is_from_social_login", bIsFromSocialLogin);
				intent.putExtra("is_from_twitter", bIsFromTwitter);
				this.startActivity(intent);
				finish();
			}
			bIsFromSocialLogin=false;
		}*/
		
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
		//appStatus.saveSharedStringValue(appStatus.USER_NAME, mUsername);
		if((result.getMobile_user() != null) && (result.getMobile_user().getmUserPref()!= null) &&
				(result.getMobile_user().getmUserPref().getEmail()!= null)){
			appStatus.saveSharedStringValue(appStatus.USER_NAME, result.getMobile_user().getmUserPref().getEmail());
		}

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

			//get shared preferences
			getSocialSharedPreferences(authToken);
		} else {
			Log.e("CHECKINFORGOOD",
					"onAuthenticationResult: failed to authenticate");
			hideProgress();
			if(result.getMessage()!=null){
				DisplayToast(result.getMessage());
			}else DisplayToast("Can't connect right now, try after some time!");
		}
		Log.e("CHECKINFORGOOD##########",
				"onAuthenticationResult:"+ result.getMessage());
	}

	//For forgot password
	public void onAuthenticationResult(Boolean status, String message) {
		boolean success = status;
		Log.i("CHECKINFORGOOD", "onAuthenticationResult(" + success + ")");

		mAuthTask = null;

		// Hide the progress dialog
		hideProgress();

		if (message != null) {
			DisplayToast(message);

			if(status)
			{
				final Intent loginIntent = new Intent(this, LoginActivity.class);
				startActivity(loginIntent);
				iActivityFlag=LOGIN_ID;
				finish();
			}
		} 
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		ProgressDialog dialog = new ProgressDialog(this);
		/*dialog = ProgressDialog.show(this,null,null);
		dialog.setContentView(R.layout.loader);*/
		dialog.setTitle("Please Wait...");
		switch(id)
		{
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
		switch(iFlag)
		{
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
		Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
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
				ShowMessageBox();
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

		exitAlert.setButton("Yes", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {               
				dialog.dismiss();
				finish();
			}
		});
		exitAlert.setButton2("No", new DialogInterface.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();  
			}
		});
		exitAlert.show();
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
				Toast toast = Toast.makeText(LoginActivity.this, mesage, Toast.LENGTH_LONG);
				toast.show();
			}
		});
	}

	private void getSocialSharedPreferences(String mAuthToken){
		// get social sharePreferences if any 
		String args[]=new String[2];
		args[0]=mAuthToken;
		args[1]=appStatus.getSharedStringValue(appStatus.APP_NAME);
		
		if(appStatus.isOnline()) {
			//showDialog(0);
			new SocialSharingTask(this,SocialSharingTask.GET_PREF).execute(args);
		}
		else {
			Log.v("CHECKINFORGOOD", "App is not online!");
			message("App is not online!");
			Intent intent= new Intent(this,NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
		}
	}

	public void onAuthenticationResult(Boolean result) {
		

		if(result){

		}else{
			message("Fail to get shared preferences!");
		}
		
		// Return home
		Intent i= new Intent();
		i.putExtra("LOGIN_FLAG", true);
		setResult(1, i);
		//final Intent homeIntent = new Intent(this, CheckinLibraryActivity.class);
		//homeIntent.putExtra("LOGIN_FLAG", true);
		//startActivity(homeIntent);
		
		// Hide the progress dialog
				hideProgress();
		finish();
	}

	//------------------Social login---Integration------------------- 
	public void facebook_signin(View button) {
		Log.i("SocialSignupActivity", "facebook clicked");

		if(appStatus.isOnline()) {
			Log.e("LoginActivity", "Facebook Clicked ");
			Intent intent_ShareFB = new Intent(LoginActivity.this, FacebookShare.class);
			intent_ShareFB.putExtra("MSSSAGE","");
			intent_ShareFB.putExtra(FacebookShare.FROM_WHERE_FLAG, true);
			bIsFromSocialLogin=true;
			bIsFromTwitter=false;
			startActivity(intent_ShareFB);
		}
		else {
			Log.v("LoginActivity", "App is not online!");
			Intent intent= new Intent(LoginActivity.this,NoConnectivityScreen.class);
			startActivity(intent);
			finish();
		}
	}

	public void twitter_signin(View button) {
		Log.i("SocialSignupActivity", "twitter clicked");
		if(appStatus.isOnline()) {
			Intent intent_ShareTW = new Intent(LoginActivity.this, TwitterShare.class);
			intent_ShareTW.putExtra("MSSSAGE","");
			intent_ShareTW.putExtra(PrepareRequestTokenActivity.IS_FROM_WHERE, true);
			bIsFromSocialLogin=true;
			bIsFromTwitter=true;
			startActivity(intent_ShareTW);

			Log.e("LoginActivity", "Twitter Clicked ");
		}
		else {
			Log.v("LoginActivity", "App is not online!");
			Intent intent= new Intent(LoginActivity.this,NoConnectivityScreen.class);
			startActivity(intent);
			finish();
		}
	}

	public void initailizeLoginPage() {
		iActivityFlag=LOGIN_ID;
		bIsFromSocialLogin=false;
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.login,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		mUsernameEdit = (EditText) findViewById(R.id.EditLoginEmail);
		mPasswordEdit = (EditText) findViewById(R.id.EditLoginPassword);
		mForgotPwd=(TextView)findViewById(R.id.forgotPwd);

		mForgotPwd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createNewTemplateforForgotPwd(v);
			}
		});
	}
	
	private void initailizeSocialLoginPage() {
		LayoutInflater inflater = LayoutInflater.from(this);
		View creater = inflater.inflate(R.layout.social_signin,
				(ViewGroup) findViewById(R.id.linearLayout1));
		this.setContentView(creater);

		mTxtAlreadyHaveAcc=(TextView)findViewById(R.id.txtHaveAcc);
		mTxtAlreadyHaveAcc.setText(Html.fromHtml(getString(R.string.social_have_account)));

		iActivityFlag=SOCIAL_LOGIN_ID;

		mTxtAlreadyHaveAcc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initailizeLoginPage();
			}
		});	
	}
	
	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == 111 && resultCode == 2) {
			Intent intent= new Intent();
			intent.putExtra("LOGIN_FLAG", true);
			setResult(1, intent);
			finish();
		}
		else if(requestCode == 111 && resultCode == 0){
			Intent intent= new Intent();
			setResult(222, intent);
		}
	}

}
