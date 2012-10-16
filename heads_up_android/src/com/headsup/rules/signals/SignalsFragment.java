package com.headsup.rules.signals;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils.TruncateAt;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.imageloader.ImageLoader;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.db.SignalsDbAdapter;
import com.headsup.helpers.Constants;
import com.headsup.models.SignalsResult;
import com.headsup.models.SignalsResult.SignalCategory;
import com.headsup.task.SignalTask;
import com.headsup.ws.services.HmacSHAClass;


public class SignalsFragment extends Fragment {
	
	AppStatus appStatus;
	String mAuthToken;
	String strDeviceDensity;
	String encrypted_data;
	
	HeadsUpNativeActivity context;
	SignalsCategoryFragment mSelectedSignalTypeFragment;

	LinearLayout lnrSignalsList;
	LinearLayout lnrLiveBallList;
	LinearLayout lnrDeadBallList;
	LinearLayout lnrConductList;
	ImageButton btnSort;
	Button btnScoring;
	Button btnDeadBall;
	Button btnLiveBall;
	Button btnConduct;
	LinearLayout lnrSignalScoring;
	
	SignalsResult mSigScoreList;
	SignalsResult mSigLBallList;
	SignalsResult mSigDBallList;
	SignalsResult mSigConductList;
	
	public ImageLoader mImageLoader;
	LinearLayout lnrHViewLayout;
	ImageView mImageView;
	TextView mTextView;
	Animation slideUp,slideDown;
	
	TextView mTextSignalDivider;
	LinearLayout lnrOfficialSignalHeader;
	Boolean flagPopupImage;
	
	private final int BTN_SCORING = 1, BTN_DBALL= 2,BTN_LBALL= 3,BTN_CNT = 4;
	

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (HeadsUpNativeActivity) this.getActivity();
	}
	
	final String TAG=getClass().getName();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.official_signal, container, false);
		mImageLoader = new ImageLoader(context);

		appStatus = AppStatus.getInstance(context);
		mAuthToken = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		
		initializeWidgets(v);
		getEncryptData();
		getSignalImages();
		
		return v;
	}

	public String getDeviceDensity() {

		String strPxList =null;
		switch (getResources().getDisplayMetrics().densityDpi) {
		case DisplayMetrics.DENSITY_LOW:
			strPxList = "ldpi";
			break;
		case DisplayMetrics.DENSITY_MEDIUM:
			strPxList = "mdpi";
			break;
		case DisplayMetrics.DENSITY_HIGH:
			strPxList = "hdpi";
			break;
		case 320:
			strPxList="xhdpi";
			break;
		default:
			strPxList = "mdpi";
			break;
		}
		return strPxList;
	}
	public void getEncryptData(){
		
		strDeviceDensity=getDeviceDensity();
		
		String[] signals_parms = {mAuthToken,strDeviceDensity};
		StringBuilder signed_value = new StringBuilder();
		
		for (int i = 0; i < signals_parms.length; i++) {
			signed_value.append(signals_parms[i]);
		}
		encrypted_data = HmacSHAClass.hash_hmac(signed_value.toString());
	}
	
	private void getSignalImages() {	


		SignalsDbAdapter mSignalsDbAdapter = new SignalsDbAdapter(context);
		if(mSignalsDbAdapter.getCount() < 1){
			if(CheckinLibraryActivity.appStatus.isOnline()) {
				context.showDialog(0);
				new SignalTask(this).execute(mAuthToken,strDeviceDensity,encrypted_data);
			} else {
				Intent intent= new Intent(context,NoConnectivityScreen.class);
				this.startActivity(intent);
				context.finish();
			}
		}else {
			fetchSignalDatafromDB();
		}
		
	}
	
	public void onAuthenticationResult(SignalsResult result) {
		context.dismissDialog(0);
		if(result != null){
			storeSignalsDatainDB(result);
		}else
		{
			Toast toast = Toast.makeText(context, "No Signal found ..", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	private void storeSignalsDatainDB(SignalsResult result) {
		
		if(result != null){
			
			SignalsDbAdapter mSignalsDbAdapter = new SignalsDbAdapter(context);
		//	Log.i("Signal Fragment", "########is from refresh "+String.valueOf(CheckinLibraryActivity.isFromRefresh));
		
			try {
				mSignalsDbAdapter.beginTransaction();

				mSignalsDbAdapter.deleteAllAssociated(); // Delete all data (Temperorily)
				for(int i=0;i<result.getCatagories().size();i++) {
					mSignalsDbAdapter.create(result.getCatagories().get(i));
				}

				mSignalsDbAdapter.succeedTransaction();
			} finally {
				mSignalsDbAdapter.endTransaction();                
			}
			
		}
		fetchSignalDatafromDB();
		
	}

	private void fetchSignalDatafromDB() {
		mSigScoreList = fetchResultForCategory(BTN_SCORING);
		mSigDBallList = fetchResultForCategory(BTN_DBALL);
		mSigLBallList = fetchResultForCategory(BTN_LBALL);
		mSigConductList = fetchResultForCategory(BTN_CNT);
		
		initializeScrollers(lnrSignalScoring, mSigScoreList,BTN_SCORING);
		initializeScrollers(lnrDeadBallList, mSigDBallList,BTN_DBALL);
		initializeScrollers(lnrLiveBallList, mSigLBallList,BTN_LBALL);
		initializeScrollers(lnrConductList, mSigConductList, BTN_CNT);
				
		
	}
	
	private SignalsResult fetchResultForCategory(int iCategory) {
		SignalsDbAdapter signalsAdpt = new SignalsDbAdapter(context);
		SignalsResult mSignalsResult = null;
		
			mSignalsResult = signalsAdpt.getList(iCategory);
			
		
		return mSignalsResult;
	}

	private void initializeScrollers(LinearLayout mLinearLayout,SignalsResult mSignalList,int iCategory) {
		if(mSignalList != null && mSignalList.getCatagories() != null) {
			for(int i=0; i<mSignalList.getCatagories().size(); i++) {
				mLinearLayout.addView(createHorizontalScrollerLayout(mSignalList.getCatagories().get(i),i,iCategory));
				
			}
		}
	}
	
	
	private View createHorizontalScrollerLayout(SignalCategory signalsResult,int iPosition, int iCategory) {
		Float density = this.getResources().getDisplayMetrics().density;
		lnrHViewLayout = new LinearLayout(context);
		lnrHViewLayout.setOrientation(LinearLayout.VERTICAL);
		mImageView = new ImageView(context);
		mTextView = new TextView(context);
		mTextView.setPadding(10, 0, 10, 0);
		mTextView.setText(signalsResult.getText_heading());
		mTextView.setTextColor(getResources().getColor(R.color.black));
		
		mTextView.setLines(2);
		mTextView.setEllipsize(TruncateAt.MARQUEE);
		//mTextView.setMaxWidth(180);
		//mTextView.setMinHeight(180);
		//mTextView.setMinHeight(30);
		//mTextView.setMaxHeight(30);
		mTextView.setMinWidth((int) (density * 160));
		mTextView.setMaxWidth((int) (density * 160));
		mTextView.setMinHeight((int) (density * 35));
		mTextView.setMaxHeight((int) (density * 35));
		mTextView.setGravity(Gravity.CENTER);
		//LinearLayout.LayoutParams lpText = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 35);
		//mTextView.setLayoutParams(lpText);
		mTextView.setBackgroundResource(R.drawable.signal_mainscreen_txt_bg);
		mImageView.setPadding(10, 30, 10, 0);
		mImageView.setBackgroundResource(R.drawable.signal_mainscreen_inner_bg);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		mImageView.setLayoutParams(lp);
		lp.gravity=Gravity.CENTER;
		mImageView.setScaleType(ScaleType.FIT_XY);
		
		//Log.i(TAG, "URL" + signalsResult.image_thumbnail);
		String finalURL=Constants.BASE_IMAGE_URL+signalsResult.image_thumbnail;
		Log.i(TAG, "Final URL" + finalURL);
		
		mImageLoader.DisplayImage(finalURL, mImageView);
		
		lnrHViewLayout.addView(mImageView);
		lnrHViewLayout.addView(mTextView);
		mImageView.setTag(signalsResult.image);
		
		setOnClickListenerForImage(mImageView,signalsResult.getText_heading(),signalsResult.getText_desc());
		//new SignalImageTask(this, mImageView).execute(finalURL);
		return lnrHViewLayout;

	}
	
	private void setOnClickListenerForImage(final ImageView mImageView, final String heading, final String desc) {
		mImageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("SIGNALS ","Position:" + String.valueOf(v.getId()));
				Log.i("SIGNALS ","Category:" +String.valueOf(v.getTag()));
				String imageUrl=Constants.BASE_IMAGE_URL+String.valueOf(mImageView.getTag());
				Log.i("SIGNALS ","image url:" +String.valueOf(v.getTag()) +" url -- "+imageUrl);
				
				if(CheckinLibraryActivity.appStatus.isOnline()) {
					if(flagPopupImage.equals(false)){
						flagPopupImage=true;
						lnrSignalsList.setVisibility(LinearLayout.GONE);
						loadPhoto(v,imageUrl,heading,desc);
					}
				} else {
					Toast toast = Toast.makeText(context, "Please connect to cellular data or Wi-fi", Toast.LENGTH_LONG);
					toast.show();
					/*Intent intent= new Intent(context,NoConnectivityScreen.class);
					context.startActivity(intent);
					context.finish();*/
				}
				
				
			}
		});

	}

	private void initializeWidgets(View v) {
		mTextSignalDivider=(TextView) v.findViewById(R.id.signalDivider);
		lnrOfficialSignalHeader=(LinearLayout)v.findViewById(R.id.lnrOfficialSignalHeader);
		btnSort = (ImageButton)v.findViewById(R.id.btnSort);
		btnScoring = (Button)v.findViewById(R.id.btnScoring);
		btnDeadBall = (Button)v.findViewById(R.id.btnDeadBall);
		btnLiveBall = (Button)v.findViewById(R.id.btnLiveBall);
		btnConduct = (Button)v.findViewById(R.id.btnConduct);
		lnrSignalsList = (LinearLayout)v.findViewById(R.id.lnrOfficialSignalList);
		lnrSignalScoring = (LinearLayout)v.findViewById(R.id.lnrSignalScoring);
		lnrLiveBallList = (LinearLayout)v.findViewById(R.id.lnrSignalLiveball);
		lnrDeadBallList = (LinearLayout)v.findViewById(R.id.lnrSignalDeadball);
		lnrConductList = (LinearLayout)v.findViewById(R.id.lnrSignalConduct);
		
		onImageButtonClick();
		
		btnScoring.setTag(BTN_SCORING);onButtonClick(btnScoring,getString(R.string.signals_scoring));
		btnDeadBall.setTag(BTN_DBALL);onButtonClick(btnDeadBall,getString(R.string.signals_dead_ball));
		btnLiveBall.setTag(BTN_LBALL);onButtonClick(btnLiveBall,getString(R.string.signals_live_ball));
		btnConduct.setTag(BTN_CNT);onButtonClick(btnConduct,getString(R.string.signals_conduct));
		flagPopupImage=false;
	}

	private void onButtonClick(Button btn, final String strText) {
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//EnumButton mEnumButton	=(EnumButton) v.getTag();

					Bundle data = new Bundle();
					data.putString("header", strText);
					data.putString("catagory", String.valueOf(v.getTag()));	
					data.putInt("category_id", (Integer)v.getTag());
						mSelectedSignalTypeFragment = (SignalsCategoryFragment) (context).addFragment(
								R.id.linearLayout2, SignalsCategoryFragment.class.getName(),
								"selected_signal_type_fragmnet", data);

			}
		});
	}

	private void onImageButtonClick() {
		btnSort.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
								
				if(lnrSignalsList.getVisibility() == LinearLayout.GONE){
						lnrSignalsList.bringToFront();
						mTextSignalDivider.bringToFront();
						lnrOfficialSignalHeader.bringToFront();
						lnrSignalsList.setVisibility(LinearLayout.VISIBLE);
						slideDown = AnimationUtils.loadAnimation(context, R.drawable.btn_slide_down_pressed);
						lnrSignalsList.startAnimation(slideDown);
				} else {
						lnrSignalsList.setVisibility(LinearLayout.GONE);
						slideUp= AnimationUtils.loadAnimation(context, R.drawable.btn_slide_up_pressed);
						lnrSignalsList.startAnimation(slideUp);
				}			
			}
		});
	}

	public void loadPhoto(View v,String imageURL, String heading, String desc) {
		final Dialog custom = new Dialog(context,R.style.Dialog);
		custom.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		custom.setContentView(getLayoutInflater(null).inflate(R.layout.image_popup_view, (ViewGroup) v.findViewById(R.id.lnrImagePopup)));
		ImageView image = (ImageView) custom.findViewById(R.id.fullimage);
		TextView txtHeader = (TextView) custom.findViewById(R.id.txtSignalPopUpHeader);
		TextView txtDesc = (TextView) custom.findViewById(R.id.txtSignalPopUpDesc);
		custom.getWindow().setBackgroundDrawable(new ColorDrawable(color.transparent));
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		param.gravity=Gravity.CENTER;
		image.setLayoutParams(param);
		
		mImageLoader.DisplayImage(imageURL, image);
		
		txtHeader.setText(heading);
		txtDesc.setText(desc);
		
		Log.i(TAG, imageURL);
		
		LinearLayout lnrImagePopup=(LinearLayout) custom.findViewById(R.id.lnrImagePopup);
		lnrImagePopup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				custom.cancel();
				flagPopupImage=false;
			}
		});
		
		
		Button btnClose = (Button) custom.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				custom.cancel();
				flagPopupImage=false;
				Log.i("#######", "Clicked");
			}
		});
		custom.show();
	
	}

}
