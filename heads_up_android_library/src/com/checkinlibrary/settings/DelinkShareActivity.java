package com.checkinlibrary.settings;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class DelinkShareActivity extends Activity {

	SocialSharingActivity context;
	ProgressDialog mProgressDialog;

	public static final String STATUS_FLAG = "FB_OT_TW";

	Button btnDlinkAcc;
	TextView txtListText;
	AppStatus appStatus;

	static int FACEBOOK = 0;
	static int TWITTER = 1;
	int iFbOrTwitter;
	String strProgressMessage;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.delink_sharing_settings);

		appStatus = AppStatus.getInstance(this);
		btnDlinkAcc = (Button) findViewById(R.id.deLinkButton);

		btnDlinkAcc.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (appStatus.isOnline()) {
					if (iFbOrTwitter == FACEBOOK) {
						onClearFbBtnClicked(v);
					} else if (iFbOrTwitter == TWITTER) {
						onClearTwBtnClicked(v);
					}
				} else {
					Intent intent = new Intent(DelinkShareActivity.this,
							NoConnectivityScreen.class);
					startActivity(intent);
					getParent().finish();
					Log.v("DelinkShareFragment", "App is not online!");
				}
			}
		});

		Bundle b = getIntent().getExtras();

		txtListText = (TextView) findViewById(R.id.testViewDelinkName);
		iFbOrTwitter = b.getInt(STATUS_FLAG, 0);

		if (iFbOrTwitter == FACEBOOK) {
			txtListText.setText("Facebook");
		} else if (iFbOrTwitter == TWITTER) {
			txtListText.setText("Twitter");
		}

	}

	/*
	 * @Override public View onCreateView(LayoutInflater inflater, ViewGroup
	 * container, Bundle savedInstanceState) {
	 * 
	 * context=(CheckinNativeActivity)this.getActivity(); View v =
	 * inflater.inflate(R.layout.delink_share_fragment, container, false);
	 * 
	 * btnDlinkAcc=(Button)v.findViewById(R.id.deLinkButton);
	 * 
	 * btnDlinkAcc.setOnClickListener(new View.OnClickListener() { public void
	 * onClick(View v) {
	 * 
	 * if (CheckinNativeActivity.appStatus.isOnline()) { if(iFbOrTwitter ==
	 * FACEBOOK){ onClearFbBtnClicked(v); }else if(iFbOrTwitter == TWITTER){
	 * onClearTwBtnClicked(v); } } else { Intent intent = new Intent(context,
	 * NoConnectivityScreen.class); context.startActivity(intent);
	 * context.finish(); Log.v("DelinkShareFragment", "App is not online!"); } }
	 * });
	 * 
	 * txtListText=(TextView)v.findViewById(R.id.testViewDelinkName);
	 * iFbOrTwitter=getArguments().getInt(DelinkShareFragment.STATUS_FLAG,0);
	 * 
	 * 
	 * if(iFbOrTwitter == FACEBOOK){ txtListText.setText("Facebook"); }else
	 * if(iFbOrTwitter == TWITTER){ txtListText.setText("Twitter"); }
	 * 
	 * return v;
	 * 
	 * }
	 */

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

	private void onClearFbBtnClicked(View view) {
		Log.i("FB", "Clicked");

		appStatus.clearSharedDataWithKey(appStatus.FACEBOOK_TOKEN);
		appStatus.clearSharedDataWithKey(appStatus.FACEBOOK_ON);

		postSocialPreferences("facebook");

	}

	private void onClearTwBtnClicked(View view) {
		Log.i("TW", "Clicked");

		appStatus.clearSharedDataWithKey(appStatus.TWITTER_TOKEN);
		appStatus.clearSharedDataWithKey(appStatus.TWITTER_SECRET);
		appStatus.clearSharedDataWithKey(appStatus.TWITTER_ON);

		postSocialPreferences("twitter");
	}

	private void postSocialPreferences(String social_network) {
		String args[] = new String[5];
		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		args[1] = social_network;// "facebook"; "twitter"
		args[2] = Boolean.toString(false);
		args[3] = null;
		args[4] = null;
		strProgressMessage = "Delinking account...";

		if (appStatus.isOnline()) {
			strProgressMessage = getString(R.string.delinkProgressMessage);
			showDialog(0);
			new SocialSharingTask(this, SocialSharingTask.POST_PREF)
					.execute(args);
		} else {
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			startActivity(intent);
			getParent().finish();
			Log.v("DelinkShareFragment", "App is not online!");
		}
	}

	public void onAuthenticationResult(Boolean success) {
		if (success) {
			removeDialog(0);
			DisplayToast("Preferences cleared!");

		} else {
			DisplayToast("Fail to clear preferences!");
		}
		finish();
		// context.popFragmentFromStack();
	}

	private void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		removeDialog(0);
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		final ProgressDialog dialog = new ProgressDialog(this.getParent());
		dialog.setTitle("Please Wait...");
		dialog.setMessage(strProgressMessage);
		dialog.setIndeterminate(true);
		dialog.setCancelable(true);
		dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			public void onCancel(DialogInterface dialog) {
				Log.i("LOCATOR", "user cancelling authentication");

			}
		});
		mProgressDialog = dialog;
		return dialog;
	}

}