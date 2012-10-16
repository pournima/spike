package com.campaignslibrary;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.helpers.DayCalculator;
import com.campaignslibrary.imageloader.ImageLoader;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.models.CreateCampaignResult.CampgnPhotos;
import com.campaignslibrary.ws.services.WebServiceIface;
import com.checkinlibrary.CheckinLibraryActivity;

public class CampaignDetailsFragment extends Fragment implements WebServiceIface{
	CheckinLibraryActivity context;
	private Button btnMakePledge, btnLike, btnTweet, btnShare;	
	CreateCampaignResult mCreateCampaignResult;
	private LinearLayout.LayoutParams lp;
	private LinearLayout lnrLtCampgnPhotos;
	private ImageLoader imageLoader;
	private TextView txtCmpgnGoal,txtCmpgnDtlsHeadingName,txtCmpgnDtlsTitleName,txtCmpgnDtlsSubTitle,
					 txtName,txtCmpgnName,txtCmpgnRaisedSoFar,txtCmpgnDaysRemaining,txtCmpgnDesc;	
	private ImageView imgTitle;
	private CampaignsDonationFragment mCampaignsDonationFragment;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		context = (CheckinLibraryActivity) this.getActivity();

		View fragmentView = inflater.inflate(R.layout.campgn_details_fragment, null);
		
		imageLoader=new ImageLoader(context);
		
		mCreateCampaignResult = (CreateCampaignResult) this.getArguments().getSerializable("selected_campaign");

		initAllComponents(fragmentView);
		initializeComponentsWithValues();
		initializeOnClickHandlers(fragmentView);
		
		getCmpgnPhotos(mCreateCampaignResult);
		
		return fragmentView;
	}

	/**
	 * This method initialises onclick listener for ui components
	 * @param v View for given fragment
	 */
	private void initializeOnClickHandlers(View v) {
		btnMakePledge.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCampaignsDonationFragment == null) {
					mCampaignsDonationFragment = (CampaignsDonationFragment) ((CheckinLibraryActivity) context)
							.addFragment(R.id.linearLayout2, CampaignsDonationFragment.class.getName(),
									"campaign_donation_fragment", new Bundle());
				} else {
					((CheckinLibraryActivity) context).attachFragment((Fragment) mCampaignsDonationFragment);
				}
			}
		});

		btnLike.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Like", Toast.LENGTH_SHORT).show();
			}
		});

		btnTweet.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Tweet", Toast.LENGTH_SHORT).show();
			}
		});

		btnShare.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * This method initialises entire UI
	 */
	private void initAllComponents(View v) {
		
		txtCmpgnDtlsHeadingName = (TextView) v.findViewById(R.id.txtHeading);
		txtCmpgnDtlsTitleName = (TextView) v.findViewById(R.id.txtTitle);
		txtCmpgnDtlsSubTitle = (TextView) v.findViewById(R.id.txtSubTitle);
		txtName = (TextView) v.findViewById(R.id.txtDescCmpgnDtlsName);
		txtCmpgnName = (TextView) v.findViewById(R.id.txtDescCmpgnDtlsCmpgnName);
		txtCmpgnRaisedSoFar = (TextView) v.findViewById(R.id.txtCmpgnDetlsRaisedSoFar);
		txtCmpgnGoal = (TextView) v.findViewById(R.id.txtCmpgnDetlsGoal);
		txtCmpgnDaysRemaining = (TextView) v.findViewById(R.id.txtCmpgnDetlsDaysRemain);
		txtCmpgnDesc=(TextView) v.findViewById(R.id.txtDescCmpgnDtlsCmpgnDesc);
		
		btnLike = (Button) v.findViewById(R.id.btnLike);
		btnTweet = (Button) v.findViewById(R.id.btnTweet);
		btnShare = (Button) v.findViewById(R.id.btnShare);
		imgTitle = (ImageView) v.findViewById(R.id.imgCmpgnDetailsTitle);
		btnMakePledge = (Button) v.findViewById(R.id.btnMakePledge);		
		
		ProgressBar progressBar = (ProgressBar) v.findViewById(R.id.progress_raised_money);
		progressBar.setMax(12);
		progressBar.setProgress((12*20)/100);
		
		lnrLtCampgnPhotos=(LinearLayout)v.findViewById(R.id.lnrLHoriScrollPhotos);	
		
		lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 0, 0, 0);		
		Float density = this.getResources().getDisplayMetrics().density;
		lp.width=(int) (70*density);
		lp.height=(int) (70*density);		
	}
	
	/**
	 * This method use to set values getting from api to the UI 
	 */	
	private void initializeComponentsWithValues(){
		if(mCreateCampaignResult != null && mCreateCampaignResult.getCampaign()!= null){
			try{
			txtCmpgnDtlsHeadingName.setText(mCreateCampaignResult.getCampaign().getSupported_by());
			txtCmpgnDtlsTitleName.setText(mCreateCampaignResult.getCampaign().getName());
			txtCmpgnRaisedSoFar.setText("$498 Raised so far");
			
			int iDaysRemain=0;
			if(mCreateCampaignResult.getCampaign().getCampaign_type().
					compareTo(Constants.INSTANCE.ONE_DAY_EVENT) == 0){
				iDaysRemain=DayCalculator.INSTANCE.daysCalc(mCreateCampaignResult.getCampaign().getStart_date());
			}else{
				iDaysRemain=DayCalculator.INSTANCE.daysCalc(mCreateCampaignResult.getCampaign().getEnd_date());
			}
			
			txtCmpgnGoal.setText("Goal: $"+mCreateCampaignResult.getCampaign().getGoal());
			
			if(iDaysRemain > 0){
				txtCmpgnDaysRemaining.setText(iDaysRemain+" days remaining");
				txtCmpgnDtlsSubTitle.setText(iDaysRemain+" days left to make a pledge");
			}else if(iDaysRemain == 0){
				txtCmpgnDaysRemaining.setText("today last day");
				txtCmpgnDtlsSubTitle.setText("today last day to make a pledge\n");
			}else{
				txtCmpgnDaysRemaining.setText("campaign ended");
				txtCmpgnDtlsSubTitle.setText("campaign ended");
			}
			
			
			txtName.setText(mCreateCampaignResult.getCampaign().getSupported_by());		
			txtCmpgnName.setText(mCreateCampaignResult.getCampaign().getOrganization().getName());
			txtCmpgnDesc.setText(mCreateCampaignResult.getCampaign().getDescription());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * This method use to get photo links from api
	 */	
	private void getCmpgnPhotos(CreateCampaignResult mCreateCampaignResult){
		List<CampgnPhotos> photoLink = mCreateCampaignResult.getCampaign().getPhotos();		
		for (int i = 0; i < photoLink.size(); i++) {
			String photoUrl = photoLink.get(i).getImage_link();
			if(photoUrl != null){
				displayPhotos(photoUrl,i);
			}
		}
	}
	
	/**
	 * This method use to display photo imageview
	 */
	private void displayPhotos(String photoUrl,int iIndex){		
		ImageView mImgVPhoto = new ImageView(context);
		String strUrl = BASE_URL + photoUrl;
		imageLoader.DisplayImage(strUrl, mImgVPhoto);		
		lnrLtCampgnPhotos.addView(mImgVPhoto, lp);
		if(iIndex == 0){
			imageLoader.DisplayImage(strUrl, imgTitle);	
		}
	}
}