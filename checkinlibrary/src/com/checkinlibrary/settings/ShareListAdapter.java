package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.facebook.android.FacebookShare;
import com.checkinlibrary.twitter.TwitterShare;
import com.checkinlibrary.ws.tasks.SocialSharingTask;


public class ShareListAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> shareItems;
	CheckinLibraryActivity mContext;

	static int FACEBOOK=0;
	static int TWITTER=1;
	private ToggleButton tb;
	Handler mhandler;
    private DelinkShareFragment mDelinkShareFragment=null;

	public ShareListAdapter(Context context,  ArrayList<String>  shareItems,CheckinLibraryActivity mainContext) {
		this.context = context;
		this.shareItems = shareItems;
		this.mContext=mainContext;
	}

	@Override
	public int getCount() {
		if (shareItems != null)
			return shareItems.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int index) {
		return shareItems.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.share_list_item, parent, false);
		TextView tv = (TextView) v.findViewById(R.id.testViewFacebook);
		tv.setText(shareItems.get(position));
		
		RelativeLayout rlt=(RelativeLayout)v.findViewById(R.id.relativeLayoutshareItem);
		rlt.setId(position);

		tb=(ToggleButton)v.findViewById(R.id.toggleShare);
		tb.setId(position);

		mhandler = new Handler();

		if(position == 0) {
			boolean facebook_on = CheckinLibraryActivity.appStatus.
			                      getSharedBoolValue(CheckinLibraryActivity.appStatus.FACEBOOK_ON); 
			if(facebook_on) {
				tb.setChecked(true);
			} else {
				tb.setChecked(false);
			}
		} else if(position == 1) {
			boolean twitter_on = CheckinLibraryActivity.appStatus.
                                 getSharedBoolValue(CheckinLibraryActivity.appStatus.TWITTER_ON); 

			if(twitter_on) {
				tb.setChecked(true);
			} else {
				tb.setChecked(false);
			}
		}

		tb.setOnClickListener(new View.OnClickListener() 
		{
			public void onClick(View v) {
				if (((ToggleButton)v).isChecked()) {
					Log.i("ShareListAdapter","Toggle button is On");
					if(CheckAndchangeStatus(v.getId(),true)) {
						if(v.getId() == FACEBOOK) {
							onFacebookClick(v);
						} else if(v.getId() == TWITTER) {
							onTwitterClick(v);
						}
					}
				} else {
					Log.i("ShareListAdapter","Toggle button is Off");
					CheckAndchangeStatus(v.getId(),false);
				}
			}
		});

		rlt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(CheckinLibraryActivity.appStatus.isOnline()){
					checkAndGotoDelinkIfNeeded(v);
				}else{
					Log.v("ShareListAdapter", "App is not online!");
					Intent intent = new Intent(context, NoConnectivityScreen.class);
					context.startActivity(intent);
					mContext.finish();
				}
				
			}
		});
		return v;
	}
	//on FacebookStatusChanged store status to preferences
	public void onFacebookClick(View v) {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			Log.e("ShareListAdapter", "Facebook Clicked ");
			Intent intent_ShareFB = new Intent(context, FacebookShare.class);
			intent_ShareFB.putExtra("MSSSAGE","");
			context.startActivity(intent_ShareFB);
		}
		else {
			//message("Check internet connectivity!");
			Log.v("ShareListAdapter", "App is not online!");
			((ToggleButton)v).setChecked(false);
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			mContext.finish();
		}
	}

	//on TwitterStatusChanged store status to preferences
	public void onTwitterClick(View v) {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			Intent intent_ShareTW = new Intent(context, TwitterShare.class);
			context.startActivity(intent_ShareTW);
			intent_ShareTW.putExtra("MSSSAGE","");
			Log.e("ShareListAdapter", "Twitter Clicked ");
		}
		else {
			//message("Check internet connectivity!");
			Log.v("ShareListAdapter", "App is not online!");
			((ToggleButton)v).setChecked(false);
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			mContext.finish();
		}
	}

	private boolean CheckAndchangeStatus(int isFacebookOrTwitter,boolean bIsChecked) {
		boolean bReturnStatus=false;
		if(isFacebookOrTwitter == FACEBOOK) {
			if(bIsChecked) {
				String access_token = CheckinLibraryActivity.appStatus.
				getSharedStringValue(CheckinLibraryActivity.appStatus.FACEBOOK_TOKEN); 
				if(access_token == null) {
					bReturnStatus=true;
				} else {
					if(CheckinLibraryActivity.appStatus.getSharedBoolValue(CheckinLibraryActivity
							.appStatus.FACEBOOK_PERMISSIONS_ON))
					{
						CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.
								appStatus.FACEBOOK_ON,bIsChecked);
						updateSocialPreferences(bIsChecked, "facebook");	
					}else{
						bReturnStatus=true;
					}
				}
			} else {
				CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.
						appStatus.FACEBOOK_ON,bIsChecked);
				updateSocialPreferences(bIsChecked, "facebook");
			}
		} else if(isFacebookOrTwitter == TWITTER) {
			if(bIsChecked) {
				String twitter_token = CheckinLibraryActivity.appStatus.
				getSharedStringValue(CheckinLibraryActivity.appStatus.TWITTER_TOKEN); 
				String twitter_secret= CheckinLibraryActivity.appStatus.
				getSharedStringValue(CheckinLibraryActivity.appStatus.TWITTER_SECRET); 
				if((twitter_token == null) || (twitter_secret == null)) {
					bReturnStatus=true;
				} else { 
					CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.
							appStatus.TWITTER_ON,bIsChecked);
					updateSocialPreferences(bIsChecked, "twitter");
				} 
			} else {
				CheckinLibraryActivity.appStatus.saveSharedBoolValue(CheckinLibraryActivity.
						appStatus.TWITTER_ON,bIsChecked);
				updateSocialPreferences(bIsChecked, "twitter");
			}
		}
		return bReturnStatus;
	}
	 public void onAuthenticationResult(Boolean success) {
			if ( success ) {
				//this.removeDialog(0);
				message("Social preferences updated!");
			} else {
				message("Fail to update social preferences!");
			}
	 }
	 
	 public void message(String msg) {
			final String mesage = msg;
			mhandler.post(new Runnable() {
				@Override
				public void run() {
					Toast toast = Toast.makeText(context, mesage, 8000);
					toast.show();
				}
			});
		}
	 
	 private void updateSocialPreferences(Boolean status,String socialNetwork) {
			String args[]=new String[4];
			args[0]=CheckinLibraryActivity.mAuthToken;
			args[1]=socialNetwork;
			args[2]=Boolean.toString(status);
			args[3] = CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.APP_NAME);
			
			if(CheckinLibraryActivity.appStatus.isOnline()) {
			//	context.showDialog(0);
				new SocialSharingTask(this,SocialSharingTask.UPDATE_PREF).execute(args);
			}
			else {
				Log.v("CHECKINFORGOOD", "App is not online!");
				message("App is not online!");
				Intent intent= new Intent(context,NoConnectivityScreen.class);
				context.startActivity(intent);
				mContext.finish();
			}
		}

	 private void checkAndGotoDelinkIfNeeded(View v){
		 boolean bGoToDelink=true;

		 if(v.getId() == FACEBOOK){
			 String access_token = CheckinLibraryActivity.appStatus.
					 getSharedStringValue(CheckinLibraryActivity.appStatus.FACEBOOK_TOKEN); 
			 if(access_token == null) {
				 bGoToDelink=false;
			 }
		 }else if(v.getId() == TWITTER){
			 String twitter_token = CheckinLibraryActivity.appStatus.
					 getSharedStringValue(CheckinLibraryActivity.appStatus.TWITTER_TOKEN); 
			 String twitter_secret= CheckinLibraryActivity.appStatus.
					 getSharedStringValue(CheckinLibraryActivity.appStatus.TWITTER_SECRET); 
			 if((twitter_token == null) || (twitter_secret == null)) {
				 bGoToDelink=false;
			 }
		 }

		 if(bGoToDelink){  // go to Delink page
			 // go to dlink screen
			 Bundle bundle = new Bundle();
			 if(v.getId() == FACEBOOK)
				 bundle.putInt(DelinkShareFragment.STATUS_FLAG, FACEBOOK);
			 else if(v.getId() == TWITTER)
				 bundle.putInt(DelinkShareFragment.STATUS_FLAG, TWITTER);

			 if (mDelinkShareFragment == null) {
				 mDelinkShareFragment = (DelinkShareFragment) mContext.addFragment(
						 R.id.linearLayout2, DelinkShareFragment.class.getName(),
						 "delink_share_fragment",bundle);
			 } else {
				 mContext.attachFragment((Fragment) mDelinkShareFragment);
			 } 
		 }else{  // go to authenticate FB Or TW
			 if(CheckinLibraryActivity.appStatus.isOnline()) {
				 if(v.getId() == FACEBOOK) {
					 onFacebookClick(v);
				 } else if(v.getId() == TWITTER) {
					 onTwitterClick(v);
				 }
			 }
			 else {
				 //message("Check internet connectivity!");
				 Log.v("ShareListAdapter", "App is not online!");
				 ((ToggleButton)v).setChecked(false);
				 Intent intent= new Intent(context,NoConnectivityScreen.class);
				 context.startActivity(intent);
				 mContext.finish();
			 }
		 }
	 }
}
