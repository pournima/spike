package com.checkinlibrary.checkin;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.offers.BusinessResult;
import com.checkinlibrary.offers.BusinessResult.CheckinTimeResult;
import com.checkinlibrary.offers.ViewOffersActivity;
import com.checkinlibrary.orgs.CauseResult;
import com.checkinlibrary.settings.SocialSharingActivity;
import com.checkinlibrary.ws.tasks.DoCheckinTask;

@SuppressWarnings("unused")
public class CheckinActivity extends Activity {

	private TextView textViewName;
	private TextView textViewAddress;
	private TextView textViewOffer;
	private TextView textViewDivider2;
	private TextView textViewCheckinTimes;
	private Button btnCheckin;
	private Button btnPublic;
	private Button btnPrivate;
	private Button btnMap;
	private Boolean bIsPublicCheckin;
	private LinearLayout mlinearlayoutPubOrPri;
	private CheckinAlgorithm mCheckinAlgorithm;
	private BusinessResult mBusinessResult;
	ThankyouActivity mThankyouFragment;
	SocialSharingActivity mSocialSharingActivity;
	CauseResult mCauseResult;
	private ScrollView mScrollView;
	private RelativeLayout mRelativeLayoutQRSnap;
	private Button btnQRSnap;
	private ImageView mImageViewSnapNow;
	private TextView mTextViewSnap;
	private TextView mTextViewSnap1;
	private TextView mTextViewSnap2;
	AppStatus appStatus;
	ProgressDialog mProgressDialog;
	String strProgressMessage;
	public static boolean bIsFromCheckin =false; 
	TextView txtViewTopRaisedMoney;
	private ScrollView mScrollViewSnap;
    
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkin);

		
		Bundle b = getIntent().getExtras();
	
		mCauseResult = (CauseResult) b.getSerializable("selected_cause");
		mBusinessResult = (BusinessResult) b.getSerializable("selected_business");
		initializePage();
		mScrollView.setVisibility(EditText.VISIBLE);
		//mRelativeLayoutQRSnap.setVisibility(EditText.GONE);
		mScrollViewSnap.setVisibility(ScrollView.GONE);
		mImageViewSnapNow.setVisibility(EditText.VISIBLE);
		mTextViewSnap1.setVisibility(EditText.VISIBLE);
		mTextViewSnap.setVisibility(EditText.VISIBLE);
		mTextViewSnap2.setVisibility(EditText.GONE);
		txtViewTopRaisedMoney=(TextView)findViewById(R.id.textViewTopRaisedMoney);
	}

	@Override
	public void onResume() {
		super.onResume();

		Bundle b = getIntent().getExtras();
		
		appStatus =AppStatus.getInstance(getParent());
		mCauseResult = (CauseResult) b.getSerializable("selected_cause");
		mBusinessResult = (BusinessResult) b.getSerializable("selected_business");
		txtViewTopRaisedMoney.setText(appStatus.setCheckinAmount());
		
		if (mBusinessResult != null) {
			textViewName.setText(mBusinessResult.getName());
			textViewAddress.setText(mBusinessResult.getAddress() + "\n");
			if (mBusinessResult.getPromotionalOffer().length() == 0) {
				textViewOffer.setVisibility(EditText.GONE);
				textViewDivider2.setVisibility(EditText.GONE);
				textViewCheckinTimes.setVisibility(EditText.GONE);
			} else {
				textViewOffer.setVisibility(EditText.VISIBLE);
				textViewDivider2.setVisibility(EditText.VISIBLE);
				textViewCheckinTimes.setVisibility(EditText.VISIBLE);
				textViewOffer.setText(mBusinessResult.getPromotionalOffer());

				List<CheckinTimeResult> mCheckinTimes = mBusinessResult
						.getCheckinTimes();
				if (mCheckinTimes.size() > 0) {
					String strCheckinTime = "";
					for (CheckinTimeResult mCheckinResult : mCheckinTimes) {
						strCheckinTime += getFormatedCheckinTime(mCheckinResult)
								+ ", ";
					}
					strCheckinTime = strCheckinTime.substring(0,
							strCheckinTime.length() - 2);
					textViewCheckinTimes.setText(Html.fromHtml("<b>"
							+ "Check-in times:" + "</b>" + "<br>" + strCheckinTime));
				} else {
					textViewCheckinTimes.setVisibility(EditText.GONE);
				}
			}

			BigDecimal pub_amount = mBusinessResult.getCheckinAmounts().get(
					"public_amount");
			BigDecimal pri_amount = mBusinessResult.getCheckinAmounts().get(
					"private_amount");

			btnPublic.setText(Html.fromHtml("<b>" + "YES" + "</b>" + " | "
					+ "Raise $"
					+ new DecimalFormat("###.00").format(pub_amount)));
			btnPrivate.setText(Html.fromHtml("<b>" + "NO" + "</b>" + " | "
					+ "Raise $"
					+ new DecimalFormat("###.00").format(pri_amount)));
		}

		
		
		// after Qrcode scanning go to direct do_chekin
		if (appStatus.mFromQrCode) {
			do_checkin();
		}
		
		mScrollView.setVisibility(EditText.VISIBLE);
		//mRelativeLayoutQRSnap.setVisibility(EditText.GONE);
		mScrollViewSnap.setVisibility(ScrollView.GONE);
		mImageViewSnapNow.setVisibility(EditText.VISIBLE);
		mTextViewSnap1.setVisibility(EditText.VISIBLE);
		mTextViewSnap.setVisibility(EditText.VISIBLE);
		mTextViewSnap2.setVisibility(EditText.GONE);
		
		appStatus.saveSharedBoolValue(appStatus.IS_APP_RESUMED, false);
	}

	void initializePage() {
		textViewName = (TextView)findViewById(R.id.textViewBusName);
		textViewAddress = (TextView) findViewById(R.id.textViewBusAddress);
		textViewOffer = (TextView) findViewById(R.id.textViewBusOffer);
		textViewDivider2 = (TextView) findViewById(R.id.divider_bottom2);
		textViewCheckinTimes = (TextView) findViewById(R.id.textViewBusCheckinTimes);
		btnCheckin = (Button) findViewById(R.id.btnBusCheckin);
		btnPublic = (Button) findViewById(R.id.btnBusPublic);
		btnPrivate = (Button) findViewById(R.id.btnBusPrivate);
		btnMap = (Button) findViewById(R.id.btnBusMap);
		mlinearlayoutPubOrPri = (LinearLayout) findViewById(R.id.linearPubOrPri);
		mImageViewSnapNow = (ImageView) findViewById(R.id.imgSnapnow);
		mTextViewSnap = (TextView) findViewById(R.id.txtViewsnap);
		mTextViewSnap1 = (TextView) findViewById(R.id.txtViewsnap1);
		mTextViewSnap2 = (TextView) findViewById(R.id.txtViewsnap2);
		bIsPublicCheckin = true;
		btnPublic.setSelected(true);
		mCheckinAlgorithm = new CheckinAlgorithm(this);
		mScrollView = (ScrollView) findViewById(R.id.scrollbarVideo);
		mRelativeLayoutQRSnap = (RelativeLayout)findViewById(R.id.linearQRCodeSnap);
		btnQRSnap = (Button) findViewById(R.id.btnSnap);
		mScrollViewSnap = (ScrollView)findViewById(R.id.scrollbarSnap);

		btnMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mapBtnClicked();
			}
		});

		btnCheckin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				checkinBtnClicked();
			}
		});

		btnPublic.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				publicCheckinBtnClicked();
			}
		});

		btnPrivate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				privateCheckinBtnClicked();
			}
		});
		btnQRSnap.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				snapBtnClicked();

			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

	public void do_checkin() {
		if (appStatus.isOnline()) {

			String args[] = new String[6];
			args[0] = Integer.toString(mBusinessResult.getId());
			args[1] = Integer.toString(mCauseResult.getId());
			String checkin_time = getCurrentTimeStamp();
			args[2] = checkin_time;
			args[3] = Boolean.toString(bIsPublicCheckin);

			if (appStatus.mFromQrCode) {
				String scanResult = appStatus.mQrCodeResult;
				Log.v("DoCheckin", scanResult);
				args[4] = scanResult;
			} else {
				args[4] = "NULL"; // Added for qrcode not needed in
									// do_checkin_simple api
			}
			if (AppStatus.getInstance(this).getSharedLongValue("DELAY_TIME") != null) {

				args[5] = AppStatus.getInstance(this)
						.getSharedLongValue("DELAY_TIME").toString();
				Log.v("DoCheckin", args[5]);

			} else {

				args[5] = "0";
			}
			strProgressMessage=getString(R.string.checkinProgressMessage);
			showDialog(0);
			new DoCheckinTask(this, args).execute(args);
		} else {
			//context.message("Check internet connectivity!");
			Log.v("DoCheckin", "App is not online!");
			Intent intent = new Intent(this, NoConnectivityScreen.class);
			startActivity(intent);
			getParent().finish();
		}
	}

	public void onAuthenticationResult(Boolean success, Double checkinAmount,
			String errorMsg, int checkinCount) {
		Log.i("Checkin", "onAuthenticationResult(" + String.valueOf(success)
				+ ")");

		removeDialog(0);
		if (success) {
			updateCheckinInfo(checkinAmount, checkinCount);
			showThankyouPage();

		} else {
			if (appStatus.mFromQrCode) {
				mScrollView.setVisibility(EditText.GONE);
				//mRelativeLayoutQRSnap.setVisibility(EditText.VISIBLE);
				mScrollViewSnap.setVisibility(ScrollView.VISIBLE);
				mImageViewSnapNow.setVisibility(EditText.GONE);
				mTextViewSnap1.setVisibility(EditText.INVISIBLE);
				mTextViewSnap.setVisibility(EditText.INVISIBLE);
				mTextViewSnap2.setVisibility(EditText.VISIBLE);

			} else {
				DisplayToast(errorMsg);
			}

		}
		appStatus.mFromQrCode = false;
	}

	public void DisplayToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
		removeDialog(0);
	}

	public String getCurrentTimeStamp() {
		String strTimeStamp = null;
		java.util.Date date = new java.util.Date();
		strTimeStamp = (new Timestamp(date.getTime())).toString();
		Log.i("TimeStamp", strTimeStamp);
		return strTimeStamp;
	}

	public void checkinBtnClicked() {
		Log.i("Checkin", "btn checkin clicked");
		TextView txtPoweredBy = (TextView) findViewById(R.id.txtPoweredBy);
		//txtPoweredBy.setVisibility(EditText.GONE);
		if (mBusinessResult.needsSnap()) {
			Log.i("CHECKINFORGOOD", "QRCODE");
			if (mCheckinAlgorithm.checkCheckinAlgorithm(
					mBusinessResult.getId(), bIsPublicCheckin,
					mBusinessResult.getCheckinTimes())) {
				mScrollView.setVisibility(EditText.GONE);
				//mRelativeLayoutQRSnap.setVisibility(EditText.VISIBLE);
				mScrollViewSnap.setVisibility(ScrollView.VISIBLE);
				
			} else {
				if (mCheckinAlgorithm.IsFacbookOrTwitterSettingNeeded) {
					ShowMessageBox("", mCheckinAlgorithm.mMessage, "SETTINGS");
				} else {
					ShowMessageBox("", mCheckinAlgorithm.mMessage, "OK");
				}
			}
		} else { // direct checkin
			if (mCheckinAlgorithm.checkCheckinAlgorithm(
					mBusinessResult.getDistance(), mBusinessResult.getId(),
					bIsPublicCheckin, mBusinessResult.getCheckinTimes())) {
				Log.i("CHECKINFORGOOD", "Direct checkin");
				do_checkin();
			} else {
				if (mCheckinAlgorithm.IsFacbookOrTwitterSettingNeeded) {
					ShowMessageBox("", mCheckinAlgorithm.mMessage, "SETTINGS");
				} else {
					ShowMessageBox("", mCheckinAlgorithm.mMessage, "OK");
				}
			}
		}
	}

	public void publicCheckinBtnClicked() {
		Log.i("Checkin", "btn public checkin clicked");
		bIsPublicCheckin = true;
		btnPrivate.setSelected(false);
		btnPublic.setSelected(true);

	}

	public void privateCheckinBtnClicked() {
		Log.i("Checkin", "btn private checkin clicked");
		bIsPublicCheckin = false;
		btnPublic.setSelected(false);
		btnPrivate.setSelected(true);
	}

	public void mapBtnClicked() {
		Intent intent;

		try {
			Log.i("Checkin",
					"map button clicked for google.navigation:"
							+ URLEncoder.encode(
									this.mBusinessResult.getAddress(), "UTF-8"));
			Double[] latLng = mBusinessResult.getLatLng();
			String latLngStr = latLng[0].toString() + ","
					+ latLng[1].toString();
			Uri uri = Uri.parse("http://maps.google.com/maps?saddr="
					+ latLngStr + "&daddr=" + latLngStr);
			intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
			intent.setClassName("com.google.android.apps.maps",
					"com.google.android.maps.MapsActivity");
			startActivity(intent);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void ShowMessageBox(String mTitle, String mMsg,
			final String posivitiButtonText) {
		AlertDialog.Builder mBuilder = new AlertDialog.Builder(this.getParent());
		mBuilder.setTitle(mTitle);
		mBuilder.setMessage(mMsg);

		mBuilder.setPositiveButton(posivitiButtonText,
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						if (posivitiButtonText.compareTo("OK") == 0) {
							/*context.mTabManager.onTabChanged(context.mTabHost
									.getCurrentTabTag());*/
							bIsFromCheckin=true;
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							parentActivity.mIdList.clear();				
							Intent intent = new Intent(getParent(),
									ViewOffersActivity.class);
							parentActivity.startChildActivity("View Offers", intent);
							
						} else { // go to facebook or twitter setting
							goToFacebookOrTwitterSettings();
							mCheckinAlgorithm.IsFacbookOrTwitterSettingNeeded = false;
						}
					}
				});

		if (mCheckinAlgorithm.IsFacbookOrTwitterSettingNeeded) {
			mBuilder.setNegativeButton("NO THANKS",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							mCheckinAlgorithm.IsFacbookOrTwitterSettingNeeded = false;
							privateCheckinBtnClicked();
						}
					});
		}

		AlertDialog mAlert = mBuilder.create();
		mAlert.show();
	}

	public void showThankyouPage() {

		Bundle bundle = new Bundle();
		bundle.putString("business_name", mBusinessResult.getName());
		bundle.putString("organization_name", mCauseResult.getName());

		Intent intent = new Intent(getParent(),ThankyouActivity.class);
		intent.putExtras(bundle);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("ThankYou", intent);

	}

	
	private void goToFacebookOrTwitterSettings() {
		
		Intent intent = new Intent(getParent(),SocialSharingActivity.class);
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		parentActivity.startChildActivity("Social Share", intent);
		/*if (mShareFragment == null) {
			//context.mTabHost.setCurrentTab(2);	
			//FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
			//Fragment oldFragment = mTabManager.mLastTab.fragment;
			//ft.remove(getFragmentManager().findFragmentByTag("setting_tab"));
			//ft.detach();
			mShareFragment = (ShareFragment) (context).addFragment(
					R.id.linearLayout2, ShareFragment.class.getName(),
					"share_fragment");
		} else {
			//context.mTabHost.setCurrentTab(2);
			(context).attachFragment((Fragment) mShareFragment);
		}*/
		
	}

	private void updateCheckinInfo(Double raisedMoney, int checkinCount) {
		appStatus.saveSharedStringValue(
				appStatus.CHECKIN_AMOUNT,
				String.format("%.2f", raisedMoney));
		setCheckinAmount();
	
		appStatus.saveSharedStringValue(
				appStatus.CHECKIN_COUNT,
				String.valueOf(checkinCount));
	}

	public void setCheckinAmount() {
		String checkin_amount = appStatus
				.getSharedStringValue(appStatus.CHECKIN_AMOUNT);
		String checkin_msg = null;
		if (checkin_amount == null || checkin_amount.equals("null")) {

			checkin_msg = "You've raised: $0.00";
		} else {
			Log.v("CheckinActivity", "Checkin_Amount: " + checkin_amount);
			checkin_msg = "You've raised: $" + checkin_amount;

		}

		/*if (textViewRaisedMoney != null) {

			textViewRaisedMoney.setText(checkin_msg);
		} else {

			textViewRaisedMoney = (TextView) findViewById(R.id.textViewTopRaisedMoney);
			textViewRaisedMoney.setText(checkin_msg);
		}*/

	}
	
	private String getFormatedCheckinTime(CheckinTimeResult mCheckinResult) {
		String strCheckinTime = "";
		String strDay = mCheckinResult.day;
		String strStartTime = mCheckinResult.start_time;
		String strEndTime = mCheckinResult.end_time;

		strStartTime = strStartTime.substring(0, strStartTime.length() - 1);
		strEndTime = strEndTime.substring(0, strEndTime.length() - 1);

		SimpleDateFormat d1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		try {
			Date startDate = d1.parse(strStartTime);
			Date endDate = d1.parse(strEndTime);

			strStartTime = parseTime(startDate);
			strEndTime = parseTime(endDate);

			strCheckinTime = strDay + ": " + strStartTime + "-" + strEndTime;
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return strCheckinTime;
	}

	private String parseTime(Date date) {
		String strTime = "";
		String strHour = String.valueOf(date.getHours());
		String strMin = String.valueOf(date.getMinutes());

		if (date.getHours() > 12) {
			strTime += String.valueOf(date.getHours() - 12) + ":" + strMin
					+ "pm";
		} else {
			strTime += String.valueOf(date.getHours()) + ":" + strMin + "am";
		}
		return strTime;
	}

	public void snapBtnClicked() {
		
		TabGroupActivity parentActivity = (TabGroupActivity) getParent();
		
	    Intent intent = new Intent(getParent(),com.google.zxing.client.android.CaptureActivity.class);
		intent.setPackage("com.google.zxing.client.android");
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		parentActivity.startActivityForResult(intent,appStatus.REQUEST_CODE);
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
