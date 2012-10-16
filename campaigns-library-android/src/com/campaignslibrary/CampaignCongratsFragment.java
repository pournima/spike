package com.campaignslibrary;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campaignslibrary.dbAdapters.MyCampaignsDbAdapter;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.helpers.DayCalculator;
import com.campaignslibrary.imageloader.ImageLoader;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.tasks.UploadCampaignPhotoTask;
import com.campaignslibrary.ws.services.WebServiceIface;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;

public class CampaignCongratsFragment extends Fragment implements WebServiceIface {

	CheckinLibraryActivity context;
	final String TAG=getClass().getName();
	int iCampgnId;
	TextView txtLink, txtGoal, txtMycmpgnDaysRemaining, txtMycmpgnDesc, txtMycmpgnDescDtls, txtVCampCongtshareDaysRemain, txtVCampCongtTop;
	private ProgressBar raisedSoFar;
	
	private ImageView mImagViewAddPhoto;
	private LinearLayout lnrLtCampgnPhotos;		
	private CampaignsPhotoClass mCampaignsPhotoClass;
	public static AppStatus appStatus;
	Button btnSubmit;
	public ImageLoader imageLoader;
	
	Boolean isFromWhere;
	private RelativeLayout RelLtMyCmpgn, RelLtshare;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (CheckinLibraryActivity) this.getActivity();
		appStatus = AppStatus.getInstance(context);
		
		isFromWhere = this.getArguments().getBoolean("isFromWhere");
		
		mCampaignsPhotoClass=new CampaignsPhotoClass(context);
		
		iCampgnId=this.getArguments().getInt("campgn_id");
		imageLoader=new ImageLoader(context);
		View v = inflater.inflate(R.layout.campgn_congrats_fragment, container, false);
		initializeUiComponents(v);
		return v;
	}

	private void initializeUiComponents(View v) {
		RelLtMyCmpgn=(RelativeLayout)v.findViewById(R.id.rltLMyCmpgnCongtFragRaise);
		txtMycmpgnDaysRemaining=(TextView) v.findViewById(R.id.txtVMyCmpgnCongtDaysRemain);
		txtMycmpgnDesc=(TextView) v.findViewById(R.id.txtVMyCmpgnCampaignDescription);
		txtMycmpgnDescDtls=(TextView) v.findViewById(R.id.txtVMyCmpgnCampaignDescrDtls);
		
		RelLtshare=(RelativeLayout)v.findViewById(R.id.rltLCongtFragRaise);
		txtVCampCongtshareDaysRemain=(TextView) v.findViewById(R.id.txtVCampCongtDaysRemain);
		
		if(isFromWhere){
			txtGoal=(TextView) v.findViewById(R.id.txtVCampCongtGoal);
			raisedSoFar=(ProgressBar) v.findViewById(R.id.seekbar_raised_money);
			
			RelLtMyCmpgn.setVisibility(View.GONE);	
			txtMycmpgnDaysRemaining.setVisibility(View.GONE);
			txtMycmpgnDesc.setVisibility(View.GONE);
			txtMycmpgnDescDtls.setVisibility(View.GONE);
			
			RelLtshare.setVisibility(View.VISIBLE);
			txtVCampCongtshareDaysRemain.setVisibility(View.VISIBLE);
		}else {
			txtGoal=(TextView) v.findViewById(R.id.txtVMyCmpgnGoal);
			raisedSoFar=(ProgressBar) v.findViewById(R.id.MyCmpgn_seekbar_raised_money);
			txtVCampCongtTop=(TextView) v.findViewById(R.id.txtVCampCongtTop);
			
			RelLtMyCmpgn.setVisibility(View.VISIBLE);
			txtMycmpgnDaysRemaining.setVisibility(View.VISIBLE);
			txtMycmpgnDesc.setVisibility(View.VISIBLE);
			txtMycmpgnDescDtls.setVisibility(View.VISIBLE);
			
			RelLtshare.setVisibility(View.GONE);
			txtVCampCongtshareDaysRemain.setVisibility(View.GONE);
		}
		
		txtLink=(TextView) v.findViewById(R.id.txtVCampCongtlink);		
		mImagViewAddPhoto=(ImageView)v.findViewById(R.id.imgVCongtAddPhoto);
		lnrLtCampgnPhotos=(LinearLayout)v.findViewById(R.id.linearLCongratsPhotos);
		btnSubmit=(Button)v.findViewById(R.id.btnCampCongtSubmit);
		
		initializePage();
		mImagViewAddPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				mCampaignsPhotoClass.showPhotoOptions();
			}
		});
		
		btnSubmit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String jsonString = mCampaignsPhotoClass.getJsonFromPhotos();
				if(jsonString != null ){
					uploadPhotos(jsonString);
				} else {
					if (isFromWhere) { // true = from CreateCampaign, false = from MyCampaign
						context.mTabHost.setCurrentTab(3);
						context.mTabHost.setCurrentTab(2);
					}
				}
			}
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		
		if(Constants.INSTANCE.mFromPhotoSelection){
			displayPhotoToImageView(Constants.INSTANCE.SELECTED_PHOTO);
			Constants.INSTANCE.mSelectedBitmaps.add(Constants.INSTANCE.SELECTED_PHOTO);
		}
	}
	
	private void initializePage() {
		MyCampaignsDbAdapter adpt = new MyCampaignsDbAdapter(context);
		CreateCampaignResult mCampaignResult=adpt.getResult(iCampgnId);
		
		if(mCampaignResult != null && mCampaignResult.getCampaign() != null){
			
			if(mCampaignResult.getCampaign().getPublic_link()!= null){
				String pubLink=BASE_URL+mCampaignResult.getCampaign().getPublic_link();
				if(mCampaignResult.getCampaign().getIs_active())
					txtLink.setText(pubLink);
				else{
					txtLink.setText(getString(R.string.pubLink_campaigns_congt_frag_null));
				}
			}else{
				txtLink.setText(getString(R.string.pubLink_campaigns_congt_frag_null));
			}
			if(mCampaignResult.getCampaign().getGoal() > 0){
				int goalVal = mCampaignResult.getCampaign().getGoal();
				txtGoal.setText("Goal: $"+goalVal);			
					
				raisedSoFar.setMax(goalVal);
				raisedSoFar.setProgress((goalVal*20)/100);
			}
			if(mCampaignResult.getCampaign().getPhotos()!= null){
				for(int i=0;i<mCampaignResult.getCampaign().getPhotos().size();i++){
					String imgLink=mCampaignResult.getCampaign().getPhotos().get(i).getImage_link();
					if(imgLink != null){
						displayPhotoToImageViewFromLink(imgLink);
					}					
				}
			}
			if(mCampaignResult.getCampaign().getName() != null){
				if(!isFromWhere){
					txtVCampCongtTop.setText(mCampaignResult.getCampaign().getName());
				}
			}
			if(mCampaignResult.getCampaign().getDescription() != null){
				txtMycmpgnDescDtls.setText(mCampaignResult.getCampaign().getDescription());
			}
			
			int iDaysRemain=0;
			if(mCampaignResult.getCampaign().getCampaign_type().
					compareTo(Constants.INSTANCE.ONE_DAY_EVENT) == 0){
				iDaysRemain=DayCalculator.INSTANCE.daysCalc(mCampaignResult.getCampaign().getStart_date());
			}else{
				iDaysRemain=DayCalculator.INSTANCE.daysCalc(mCampaignResult.getCampaign().getEnd_date());
			}
			
			if(iDaysRemain > 0){
				txtMycmpgnDaysRemaining.setText(iDaysRemain+" days remaining");
				txtVCampCongtshareDaysRemain.setText(iDaysRemain+" days remaining");
			}else if(iDaysRemain == 0){
				txtMycmpgnDaysRemaining.setText("today last day");
				txtVCampCongtshareDaysRemain.setText(iDaysRemain+" days remaining");
			}else{
				txtMycmpgnDaysRemaining.setText("campaign ended");
				txtVCampCongtshareDaysRemain.setText(iDaysRemain+" days remaining");
			}
		}
	}

	/**
	 * This method use to display selected photo imageview
	 */
	private void displayPhotoToImageView(Bitmap mBitmap){
		ImageView mImgVPhoto = new ImageView(context);
		mImgVPhoto.setImageBitmap(mBitmap);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);
		Float density = this.getResources().getDisplayMetrics().density;
		lp.width=(int) (65*density);
		lp.height=(int) (70*density);
		lnrLtCampgnPhotos.addView(mImgVPhoto, lp);
		Constants.INSTANCE.mFromPhotoSelection=false;
	}
	
	/**
	 * This method use to display imageview from image url
	 * @param strImgUrl String imagelink to display on imageview
	 */
	private void displayPhotoToImageViewFromLink(String strImgUrl){
		String strUrl = BASE_URL + strImgUrl;
		ImageView mImgVPhoto = new ImageView(context);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);
		Float density = this.getResources().getDisplayMetrics().density;
		lp.width=(int) (65*density);
		lp.height=(int) (70*density);
		imageLoader.DisplayImage(strUrl, mImgVPhoto);
		lnrLtCampgnPhotos.addView(mImgVPhoto, lp);
	}
	
	/**
	 * This method use to upload all photos
	 */
	private void uploadPhotos(String jsonString){
		
		String[] args = new String[3];

		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY); 
		args[1] = String.valueOf(iCampgnId); // campaign id
		args[2] = jsonString;  // photos json

		if(appStatus.isOnline()) {
			new UploadCampaignPhotoTask(this,Constants.INSTANCE.CONGRATS_FRAG).execute(args);
		} else {
			Log.v(TAG, "App is not online!");
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
	}
	
	/**
	 * This method use to get response data from upload photo 
	 * api call
	 */
	public void getUploadPhotosResult(CreateCampaignResult result){
		if(result != null){
			if((result.getSuccess() != null) && (result.getSuccess())){
				mCampaignsPhotoClass.storeCampaignPhotos(result);
				if (isFromWhere) { // true = from CreateCampaign, false = from MyCampaign
					context.mTabHost.setCurrentTab(3);
					context.mTabHost.setCurrentTab(2);
				}
			}
		}else{
			Toast.makeText(context,"Failed to upload photos!", Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public static void updateCampaignsPhotos(CheckinLibraryActivity main_context, int iCampgnId){
		Bundle bundle=new Bundle();
		bundle.putBoolean("isFromWhere", true);
		bundle.putInt("campgn_id", iCampgnId);
		CampaignCongratsFragment mCampaignCongratsFragment = (CampaignCongratsFragment) 
				main_context.addFragment(R.id.linearLayout2,
						CampaignCongratsFragment.class.getName(),
						"congrats_campaign_fragment",bundle);
	}
}