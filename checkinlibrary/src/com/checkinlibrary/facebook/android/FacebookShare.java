package com.checkinlibrary.facebook.android;

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
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.facebook.android.Facebook.DialogListener;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.ws.services.WebService;
import com.checkinlibrary.ws.tasks.SocialSharingTask;
import com.google.myjson.Gson;


public class FacebookShare extends Activity {

	private Handler mRunOnUi = new Handler();
	
	public static final String appId="396355100423309";
	public static final String appSecret="d6dce2a60dfaac106c6a1d8282a08439";
	
	Facebook facebook = new Facebook(appId);
	public ProgressDialog mProgressDialog;
    String msgToPost;
    
    private static AppStatus appStatus;
    public static final String FROM_WHERE_FLAG="FROM_WHERE";
	private boolean bIsFromSocialLogin;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_share_loading_screen);
		
		TextView txtMsg=(TextView)findViewById(R.id.TextViewMainMsg);
		txtMsg.setText(getString(R.string.waiting_for_facebook));
		
		msgToPost=getIntent().getStringExtra("MSSSAGE");
		bIsFromSocialLogin=getIntent().getBooleanExtra(FacebookShare.FROM_WHERE_FLAG, false);
		appStatus = AppStatus.getInstance(this);
		
		String access_token = appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN); 
		FbDialog.bIsActivityFinished=false;
		
		Log.i("####checkin", "FB RESUME");
		
		if(access_token != null) {
			if(appStatus.getSharedBoolValue(appStatus.FACEBOOK_PERMISSIONS_ON))
			{
				facebook.setAccessToken(access_token);
			}
		}

		if(!facebook.isSessionValid()) {

			facebook.authorize(this, 	new String[] {"publish_stream", "read_stream",
			"offline_access","email"},Facebook.FORCE_DIALOG_AUTH, new DialogListener() {
				@Override
				public void onComplete(Bundle values) {
					appStatus.saveSharedStringValue(appStatus.FACEBOOK_TOKEN,facebook.getAccessToken());
					
					getFBId();
					
					if(getPermissionsFromFB()){
						appStatus.saveSharedBoolValue(appStatus.FACEBOOK_ON,true);	
						appStatus.saveSharedBoolValue(appStatus.FACEBOOK_PERMISSIONS_ON,true);
					}else{
						appStatus.saveSharedBoolValue(appStatus.FACEBOOK_ON,false);
						appStatus.saveSharedBoolValue(appStatus.FACEBOOK_PERMISSIONS_ON,false);
					}

					if(!bIsFromSocialLogin)
						postSocialPreferences(facebook.getAccessToken());
					else{
						finish();
						FbDialog.bIsActivityFinished=true;
					}
				}
				@Override
						public void onCancel() {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on cancel");
							FbDialog.bIsActivityFinished=true;
							finish();
						}

						@Override
						public void onError(DialogError e) {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on error");
							FbDialog.bIsActivityFinished=true;
							finish();
						}

						@Override
						public void onFacebookError(FacebookError e) {
							// TODO Auto-generated method stub
							Log.i("####checkin", "on FB error");
							FbDialog.bIsActivityFinished=true;
							finish();
						}
			});
		}
		else
		{
			//postToFacebook();
			getFBId();
			FbDialog.bIsActivityFinished=true;
			finish();
		}
	}

	public void getFriendList() {
		Bundle bundle = new Bundle();
		try {
			String response=facebook.request("me/friends",bundle);
			Log.i("friends response",response);
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
			String response=facebook.request("me",bundle);
			Log.i("friends response",response);
			JSONObject json = Util.parseJson(response);
			String facebookID = json.getString("id");
			String firstName = json.getString("first_name");
			String lastName = json.getString("last_name");
			String email = json.getString("email");
			Log.i("EMAIL ", json.getString("email"));
			
			appStatus.saveSharedStringValue(appStatus.FB_FIRST_NAME,firstName);
			appStatus.saveSharedStringValue(appStatus.FB_LAST_NAME,lastName);
			appStatus.saveSharedStringValue(appStatus.FB_UID,facebookID);
			appStatus.saveSharedStringValue(appStatus.FB_EMAIL,email);
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


	public boolean getPermissionsFromFB(){
		boolean bStatus=false;

		String uid=appStatus.getSharedStringValue(appStatus.FB_UID);
		String access_token=appStatus.getSharedStringValue(appStatus.FACEBOOK_TOKEN);;
		String path="https://graph.facebook.com/"+uid+"/"+"permissions?access_token="+access_token;
		String response;

		WebService webService = new WebService(path);
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		response = webService.webGET(path, params);
		
		FBPermissionsResult res=null;
		if(response != null)
			Log.i("facebook permissions",response);

		try {
			res = new Gson().fromJson(response,
					FBPermissionsResult.class);
			Log.e("CHECKINFORGOOD", res.toString());
			if((res != null) && (res.getData().size() > 0)){
				int installedSt=res.getData().get(0).getInstalled();
				int emailSt=res.getData().get(0).getEmail();
				int readStreamSt=res.getData().get(0).getRead_stream();
				int publishStreamSt=res.getData().get(0).getPublish_stream();

				if((installedSt == 1) && (emailSt == 1 ) && (publishStreamSt == 1))
					bStatus=true;
			}
		} catch (Exception e) {
			Log.e("CHECKINFORGOOD", "FBPermissionsResult Error: " + e.getMessage());
		}
		return bStatus;
	}
	
	public void postToFacebook() {
		AsyncFacebookRunner mAsyncFbRunner = new AsyncFacebookRunner(facebook);
		Bundle params = new Bundle();
		params.putString("name", "CheckinAndroid");
		params.putString("caption", "CheckinAndroid");
	    params.putString("link", MyConstants.BASE_WEB_URL);
		params.putString("description",msgToPost);
		mAsyncFbRunner.request("me/feed", params, "POST", new WallPostListener(),null);

		finish();
	}
	private final class WallPostListener extends BaseRequestListener {
		@SuppressWarnings("unused")
		public void onComplete(final String response) {
			mRunOnUi.post(new Runnable() {
				//   @Override
				public void run() {
					//          mProgress.cancel();
					Log.i("Posted to Facebook", response);
				}
			});
		}

		@Override
		public void onComplete(String response, Object state) {
			// TODO Auto-generated method stub
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
	public void onResume(){
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			FbDialog.bIsActivityFinished=true;
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void onAuthenticationResult(Boolean success) {
		if ( success ) {
			//this.removeDialog(0);
			DisplayToast("Facebook preferences posted!");
		} else {
			DisplayToast("Fail to post facebook preferences!");
		}
		
		finish();
	}

	private void postSocialPreferences(String accessToken) {
		String args[]=new String[6];
		args[0]=CheckinLibraryActivity.mAuthToken;
		args[1]="facebook";
		args[2]=Boolean.toString(appStatus.getSharedBoolValue(appStatus.FACEBOOK_ON));
		args[3]=accessToken;
		args[4]="no secret for fb";
		args[5] = appStatus.getSharedStringValue(appStatus.APP_NAME);
		
		if (appStatus.isOnline()) {
			//this.showDialog(0);
			new SocialSharingTask(this,SocialSharingTask.POST_PREF).execute(args);
		} else {
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			this.startActivity(intent);
			finish();
			Log.v("facebookShare", "App is not online!");
		}
	}

	private void DisplayToast(String msg) {
		//Toast.makeText(this, msg,Toast.LENGTH_SHORT).show();
		//this.removeDialog(0);
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
	public void onPause(){
		super.onPause();
	}
}
