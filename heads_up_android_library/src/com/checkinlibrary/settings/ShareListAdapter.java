package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.facebook.FacebookShare;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.twitter.TwitterShare;
import com.checkinlibrary.ws.tasks.SocialSharingTask;

public class ShareListAdapter extends BaseAdapter {
	Context context;
	ArrayList<String> shareItems;
	SocialSharingActivity mContext;
	AppStatus appStatus;
	
	public static Boolean fromFbSettingPage = false;

	static int FACEBOOK = 0;
	static int TWITTER = 1;
	// private ToggleButton tb;
	Handler mhandler;
	private DelinkShareActivity mDelinkShareFragment = null;
	private LayoutInflater mInflater;

	public ShareListAdapter(Context context, ArrayList<String> shareItems,
			SocialSharingActivity mainContext) {
		this.context = context;
		this.shareItems = shareItems;
		this.mContext = mainContext;
		appStatus = AppStatus.getInstance(mainContext);
		mInflater = LayoutInflater.from(context);

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

	@SuppressLint("ParserError")
	@Override
	/*
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * LayoutInflater inflater = (LayoutInflater) context
	 * .getSystemService(Context.LAYOUT_INFLATER_SERVICE); View v =
	 * inflater.inflate(R.layout.share_list_item, parent, false); TextView tv =
	 * (TextView)findViewById(R.id.testViewFacebook);
	 * tv.setText(shareItems.get(position));
	 * 
	 * RelativeLayout rlt = (RelativeLayout) v
	 * .findViewById(R.id.relativeLayoutshareItem); rlt.setId(position);
	 * 
	 * tb = (ToggleButton)findViewById(R.id.toggleShare); tb.setId(position);
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
//		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.share_list_item, null);
			holder = new ViewHolder();
			holder.name = (TextView) convertView
					.findViewById(R.id.testViewFacebook);
			holder.toggleBtn = (ToggleButton) convertView
					.findViewById(R.id.toggleShare);
			holder.rLayout = (RelativeLayout) convertView
					.findViewById(R.id.relativeLayoutshareItem);

			convertView.setTag(holder);

			if (position == 0) {
				boolean facebook_on = appStatus
						.getSharedBoolValue(appStatus.FACEBOOK_ON);
				if (facebook_on) {
					holder.toggleBtn.setChecked(true);
				} else {
					holder.toggleBtn.setChecked(false);
				}
			} else if (position == 1) {
				boolean twitter_on = appStatus
						.getSharedBoolValue(appStatus.TWITTER_ON);

				if (twitter_on) {
					holder.toggleBtn.setChecked(true);
				} else {
					holder.toggleBtn.setChecked(false);
				}
			}

			holder.toggleBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					if (((ToggleButton) v).isChecked()) {
						Log.i("ShareListAdapter", "Toggle button is On");
						if (CheckAndchangeStatus(v.getId(), true)) {
							if (v.getId() == FACEBOOK) {
								onFacebookClick(v);
							} else if (v.getId() == TWITTER) {
								onTwitterClick(v);
							}
						}
					} else {
						Log.i("ShareListAdapter", "Toggle button is Off");
						CheckAndchangeStatus(v.getId(), false);
					}
				}
			});

			holder.rLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					checkAndGotoDelinkIfNeeded(v);
				}
			});

//		} 
//		else {
//			holder = (ViewHolder) convertView.getTag();
//		}

		mhandler = new Handler();
		holder.name.setText(shareItems.get(position));
		holder.toggleBtn.setId(position);
		holder.rLayout.setId(position);
		return convertView;
	}

	// on FacebookStatusChanged store status to preferences
	public void onFacebookClick(View v) {

		if (appStatus.isOnline()) {
			fromFbSettingPage = true;
			
			Log.e("ShareListAdapter", "Facebook Clicked ");
			Intent intent_ShareFB = new Intent(context, FacebookShare.class);
			intent_ShareFB.putExtra("MSSSAGE", "");
			context.startActivity(intent_ShareFB);
//			mContext.startActivityForResult(intent_ShareFB, 100);
//			((Activity) context).finish();
		} else {
			// message("Check internet connectivity!");
			Log.v("ShareListAdapter", "App is not online!");
			((ToggleButton) v).setChecked(false);
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			mContext.getParent().finish();
		}
	}

	// on TwitterStatusChanged store status to preferences
	public void onTwitterClick(View v) {
		if (appStatus.isOnline()) {
			Intent intent_ShareTW = new Intent(context, TwitterShare.class);
			context.startActivity(intent_ShareTW);
			intent_ShareTW.putExtra("MSSSAGE", "");
			Log.e("ShareListAdapter", "Twitter Clicked ");
		} else {
			// message("Check internet connectivity!");
			Log.v("ShareListAdapter", "App is not online!");
			((ToggleButton) v).setChecked(false);
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			mContext.getParent().finish();
		}
	}

	private boolean CheckAndchangeStatus(int isFacebookOrTwitter,
			boolean bIsChecked) {
		boolean bReturnStatus = false;
		if (isFacebookOrTwitter == FACEBOOK) {
			if (bIsChecked) {
				String access_token = appStatus
						.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
				if (access_token == null) {
					bReturnStatus = true;
				} else {

					if (appStatus
							.getSharedBoolValue(appStatus.FACEBOOK_PERMISSIONS_ON)) {
						appStatus.saveSharedBoolValue(appStatus.FACEBOOK_ON,
								bIsChecked);
						updateSocialPreferences(bIsChecked, "facebook");
					} else {
						bReturnStatus = true;
					}

				}
			} else {
				appStatus
						.saveSharedBoolValue(appStatus.FACEBOOK_ON, bIsChecked);
				updateSocialPreferences(bIsChecked, "facebook");
			}
		} else if (isFacebookOrTwitter == TWITTER) {
			if (bIsChecked) {
				String twitter_token = appStatus
						.getSharedStringValue(appStatus.TWITTER_TOKEN);
				String twitter_secret = appStatus
						.getSharedStringValue(appStatus.TWITTER_SECRET);
				if ((twitter_token == null) || (twitter_secret == null)) {
					bReturnStatus = true;
				} else {
					appStatus.saveSharedBoolValue(appStatus.TWITTER_ON,
							bIsChecked);
					updateSocialPreferences(bIsChecked, "twitter");
				}
			} else {
				appStatus.saveSharedBoolValue(appStatus.TWITTER_ON, bIsChecked);
				updateSocialPreferences(bIsChecked, "twitter");
			}
		}
		return bReturnStatus;
	}

	public void onAuthenticationResult(Boolean success) {
		if (success) {
			// this.removeDialog(0);
			message("Social preferences updated!");
		} else {
			message("Fail to update social preferences!");
		}
//		finish();//RR..
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

	private void updateSocialPreferences(Boolean status, String socialNetwork) {
		String args[] = new String[3];
		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		args[1] = socialNetwork;
		args[2] = Boolean.toString(status);

		if (appStatus.isOnline()) {
			// context.showDialog(0);
			new SocialSharingTask(this, SocialSharingTask.UPDATE_PREF)
					.execute(args);
		} else {
			Log.v("CHECKINFORGOOD", "App is not online!");
			message("App is not online!");
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			mContext.getParent().finish();
		}
	}

	private void checkAndGotoDelinkIfNeeded(View v) {
		boolean bGoToDelink = true;

		if (v.getId() == FACEBOOK) {
			String access_token = appStatus
					.getSharedStringValue(appStatus.FACEBOOK_TOKEN);
			if (access_token == null) {
				bGoToDelink = false;
			}
		} else if (v.getId() == TWITTER) {
			String twitter_token = appStatus
					.getSharedStringValue(appStatus.TWITTER_TOKEN);
			String twitter_secret = appStatus
					.getSharedStringValue(appStatus.TWITTER_SECRET);
			if ((twitter_token == null) || (twitter_secret == null)) {
				bGoToDelink = false;
			}
		}

		if (bGoToDelink) { // go to Delink page
			// go to dlink screen
			Bundle bundle = new Bundle();
			if (v.getId() == FACEBOOK)
				bundle.putInt(DelinkShareActivity.STATUS_FLAG, FACEBOOK);
			else if (v.getId() == TWITTER)
				bundle.putInt(DelinkShareActivity.STATUS_FLAG, TWITTER);

			TabGroupActivity parentActivity = (TabGroupActivity) mContext
					.getParent();
			Intent intent = new Intent(mContext.getParent(),
					DelinkShareActivity.class);
			intent.putExtras(bundle);
			parentActivity.startChildActivity("Delink Share", intent);

			/*
			 * if (mDelinkShareFragment == null) { mDelinkShareFragment =
			 * (DelinkShareFragment) mContext .addFragment(R.id.linearLayout2,
			 * DelinkShareFragment.class.getName(), "delink_share_fragment",
			 * bundle); } else { mContext.attachFragment((Fragment)
			 * mDelinkShareFragment); }
			 */

		} else { // go to authenticate FB Or TW
			if (appStatus.isOnline()) {
				if (v.getId() == FACEBOOK) {
					onFacebookClick(v);
				} else if (v.getId() == TWITTER) {
					onTwitterClick(v);
				}
			} else {
				// message("Check internet connectivity!");
				Log.v("ShareListAdapter", "App is not online!");
				((ToggleButton) v).setChecked(false);
				Intent intent = new Intent(context, NoConnectivityScreen.class);
				context.startActivity(intent);
				mContext.getParent().finish();
			}
		}
	}

	static class ViewHolder {
		TextView name;
		ToggleButton toggleBtn;
		RelativeLayout rLayout;
	}
}
