package com.campaignslibrary;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.campaignslibrary.calender.SimpleCalendarViewFragment;
import com.campaignslibrary.dbAdapters.CauseCategoryDBAdapter;
import com.campaignslibrary.dbAdapters.MyCampaignsDbAdapter;
import com.campaignslibrary.helpers.Constants;
import com.campaignslibrary.models.CauseCategoryResult;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.tasks.CampaignTask;
import com.campaignslibrary.tasks.CauseCategoryListTask;
import com.campaignslibrary.tasks.UploadCampaignPhotoTask;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.models.OrganizationResult;

public class CreateCampgnFragment extends Fragment {

	CheckinLibraryActivity context;
	private EditText edtName, edtDesc;
	private EditText edtSupport;
	private EditText edtCategory;
	private EditText edtStDate;
	private EditText edtEnDate;
	private EditText edtGoal;
	private EditText edtVideo;
	private EditText edtFbLike;

	private Button btnDonation, btnPledge, btnMile,btnDay, btnHour, btnMinute, btnSubmit;

	private ImageView mImagViewAddPhoto;

	private String strCatName;
	private String strSubCatName;
	private int iSubCatPosition;
	private Resources resources;

	public static AppStatus appStatus;
	private CampgnCauseListFragment mCampgnCauseListFragment;
	private OrganizationResult mSelectedCauseResult;
	private CauseCategoryResult mSelectedCauseCatResult;

	View fragmentView;
	private String[] donationType = {"donation","pledge"};
	private String[] pledgeFor    = {"mile","day","hour","minute"};

	String strDonationType;
	String strPledgeType;

	private LinearLayout lnrLtCampgnPhotos, lnrLtCampgnPledges;		
	private TextView txtCampgnPledgePer;
	private SimpleCalendarViewFragment mSimpleCalendarViewFragment = null;
	private Boolean bIsStartDateSelected = false;
	final String TAG=getClass().getName();

	String[] oneDayArr;
	String[] onGoingArr;
	String[] raiseAwareArr;

	CampaignshareFragment mCampaignshareFragment=null;
	CampaignsPhotoClass mCampaignsPhotoClass;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (CheckinLibraryActivity) this.getActivity();
		fragmentView = inflater.inflate(R.layout.create_campgn_fragment,
				null);

		appStatus = AppStatus.getInstance(context);

		mCampaignsPhotoClass=new CampaignsPhotoClass(context);

		resources = getResources();
		oneDayArr = resources.getStringArray(R.array.campgn_oneday_event_arr);
		onGoingArr = resources.getStringArray(R.array.campgn_ongoing_arr);
		raiseAwareArr = resources.getStringArray(R.array.campgn_raise_awareness_arr);

		strCatName = this.getArguments().getString("campgn_cat");
		iSubCatPosition = this.getArguments().getInt("campgn_sub_cat");
		strSubCatName = getSubCatName();

		initialization(fragmentView);
		return fragmentView;
	}

	@Override
	public void onResume() {
		super.onResume();

		if(Constants.INSTANCE.mFromPhotoSelection){
			displayPhotoToImageView(Constants.INSTANCE.SELECTED_PHOTO);
			Constants.INSTANCE.mSelectedBitmaps.add(Constants.INSTANCE.SELECTED_PHOTO);
		}else{
			reInitializeImageViews();
		}
		setSelectedItemsData();
	}

	/**
	 * This method initialises ui components
	 * @param v View for given fragment
	 */
	private void initialization(View v) {		

		edtName = (EditText) v.findViewById(R.id.edtCrtCampgnName);
		edtDesc = (EditText) v.findViewById(R.id.edtCrtCampgnDesc);
		edtSupport = (EditText) v.findViewById(R.id.edtCrtCampgnCauseSprt);
		edtCategory = (EditText) v.findViewById(R.id.edtCrtCampgnCat);		

		edtStDate = (EditText) v.findViewById(R.id.edtCrtCampgnStrtDate);
		edtEnDate = (EditText) v.findViewById(R.id.edtCrtCampgnEndDate);
		edtGoal = (EditText) v.findViewById(R.id.edtCrtCampgnGoal);
		edtVideo = (EditText) v.findViewById(R.id.edtCrtCampgnVideoLink);
		edtFbLike = (EditText) v.findViewById(R.id.edtCrtCampgnFbLink);

		btnDonation = (Button) v.findViewById(R.id.btn_campgn_donation);
		btnPledge = (Button) v.findViewById(R.id.btn_campgn_pledges);

		btnMile = (Button) v.findViewById(R.id.btn_campgn_mile);
		btnDay = (Button) v.findViewById(R.id.btn_campgn_day);
		btnHour = (Button) v.findViewById(R.id.btn_campgn_hour);
		btnMinute = (Button) v.findViewById(R.id.btn_campgn_min);

		btnSubmit = (Button) v.findViewById(R.id.btn_submit_campgn);
		mImagViewAddPhoto=(ImageView)v.findViewById(R.id.imgVCampgnAddPhoto);
		lnrLtCampgnPhotos=(LinearLayout)v.findViewById(R.id.linearLCreateCampPhotos);

		lnrLtCampgnPledges = (LinearLayout)v.findViewById(R.id.lnrL_campgn_pledges);
		txtCampgnPledgePer = (TextView)v.findViewById(R.id.txt_campgn_pledgePer);

		lnrLtCampgnPledges.setVisibility(View.GONE);
		txtCampgnPledgePer.setVisibility(View.GONE);

		if (strCatName.equalsIgnoreCase(Constants.INSTANCE.ONE_DAY_EVENT)) {
			if (strSubCatName.equalsIgnoreCase(oneDayArr[0])) {
				edtEnDate.setVisibility(View.GONE);
				edtVideo.setVisibility(View.GONE);			
			} else if (strSubCatName.equalsIgnoreCase(oneDayArr[1])) {
				edtEnDate.setVisibility(View.GONE);
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
			} else if (strSubCatName.equalsIgnoreCase(oneDayArr[2])) {
				edtEnDate.setVisibility(View.GONE);
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
			} else if (strSubCatName.equalsIgnoreCase(oneDayArr[3])) {
				edtEnDate.setVisibility(View.GONE);
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
			}
		} else if (strCatName.equalsIgnoreCase(Constants.INSTANCE.ONGOING_ACT)) {
			if (strSubCatName.equalsIgnoreCase(onGoingArr[0])) {				
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
				btnDonation.setVisibility(View.GONE);
			} else if (strSubCatName.equalsIgnoreCase(onGoingArr[1])) {
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
				btnDonation.setVisibility(View.GONE);
				edtGoal.setVisibility(View.GONE);
			} else if (strSubCatName.equalsIgnoreCase(onGoingArr[2])) {
				edtVideo.setVisibility(View.GONE);
				btnPledge.setVisibility(View.GONE);
				btnDonation.setVisibility(View.GONE);
			}
		} else if (strCatName.equalsIgnoreCase(Constants.INSTANCE.RAISE_AWARENESS)) {
			if (strSubCatName.equalsIgnoreCase(raiseAwareArr[0])) {
				btnPledge.setVisibility(View.GONE);
				btnDonation.setVisibility(View.GONE);
			} else if (strSubCatName.equalsIgnoreCase(raiseAwareArr[1])) {
				btnPledge.setVisibility(View.GONE);
				btnDonation.setVisibility(View.GONE);
				edtVideo.setVisibility(View.GONE);
			}
		}

		initializeOnClickHandlersForEditText(edtSupport);		
		initializeOnClickHandlersForEditText(edtCategory);
		initializeOnClickHandlersForEditText(edtStDate);
		initializeOnClickHandlersForEditText(edtEnDate);

		initializeOnClickHandlersForButtons(btnDonation);
		initializeOnClickHandlersForButtons(btnPledge);
		initializeOnClickHandlersForButtons(btnMile);
		initializeOnClickHandlersForButtons(btnDay);
		initializeOnClickHandlersForButtons(btnHour);
		initializeOnClickHandlersForButtons(btnMinute);
		initializeOnClickHandlersForButtons(btnSubmit);

		//OnClick listner for images
		initializeOnClickHandlersForButtons(mImagViewAddPhoto);		
	}

	/**
	 * This method initialises onclick listener for edit text 
	 * @param v UI component
	 */
	private void initializeOnClickHandlersForEditText(View v) {

		v.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (v.getId() == R.id.edtCrtCampgnCauseSprt) {
					showCausesList();
				} else if (v.getId() == R.id.edtCrtCampgnCat) {
					getCauseCatList();
				} else if (v.getId() == R.id.edtCrtCampgnStrtDate) {
					bIsStartDateSelected = true;
					getDateFromCalender();
				} else if (v.getId() == R.id.edtCrtCampgnEndDate) {
					bIsStartDateSelected = false;
					getDateFromCalender();
				} 
			}
		});
	}

	/**
	 * This method initialises onclick listener for UI components
	 * @param v UI component
	 */
	private void initializeOnClickHandlersForButtons(View v) {

		v.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (v.getId() == R.id.btn_campgn_donation) {
					askForDonationInitialization();
					strDonationType=donationType[0];
				} else if (v.getId() == R.id.btn_campgn_pledges) {
					askForPledgesInitialization();
					strDonationType=donationType[1];
				} else if (v.getId() == R.id.btn_campgn_mile) {
					strPledgeType=pledgeFor[0];
				} else if (v.getId() == R.id.btn_campgn_day) {
					strPledgeType=pledgeFor[1];
				} else if (v.getId() == R.id.btn_campgn_hour) {
					strPledgeType=pledgeFor[2];
				} else if (v.getId() == R.id.btn_campgn_min) {
					strPledgeType=pledgeFor[3];
				} else if (v.getId() == R.id.btn_submit_campgn) {
					onBtnSubmitClick();
				} else if (v.getId() == R.id.imgVCampgnAddPhoto) {
					mCampaignsPhotoClass.showPhotoOptions();
				} else {
				}
			}
		});
	}

	/**
	 * This method initialises onclick listener for btnSubmit
	 * to call create campaign api
	 */
	private void onBtnSubmitClick(){

		String[] args = new String[14];
		String categoryID="",organizationId="";
		if(mSelectedCauseCatResult != null)
			categoryID=String.valueOf(mSelectedCauseCatResult.getCategory().getId());
		if(mSelectedCauseResult != null)
			organizationId=String.valueOf(mSelectedCauseResult.getOrganization().getId()); 

		args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY); 
		args[1] = getSubCatName();                      // sub campaign page
		args[2] = strCatName;                           // campaign type
		args[3] = categoryID;                           // category_id
		args[4] = edtDesc.getText().toString();         // description
		args[5] = (strDonationType != null)?strDonationType:"";                      // donation_type  (donation/pledge)
		args[6] = edtEnDate.getText().toString();       // endDate
		args[7] = edtFbLike.getText().toString();       // fb_link
		args[8] = edtGoal.getText().toString();         // Goal
		args[9] = edtName.getText().toString();         // name
		args[10] = organizationId;                      // organization ID 
		args[11] = (strPledgeType!= null)?strPledgeType:"";                       // pledge for (mile/day/hour/minute)
		args[12] = edtStDate.getText().toString();      // start Date
		args[13] = edtVideo.getText().toString();       // video link

		if(appStatus.isOnline()) {
			CheckinLibraryActivity.strProgressMessage = getString(R.string.createCampgnProgressMessage);
			context.showDialog(0);
			new CampaignTask(this).execute(args);
		} else {
			Log.v(TAG, "App is not online!");
			Intent intent= new Intent(context,NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
		}
		//goToShareCampaignFragment(56);
	}

	/**
	 * This method use to get response data from create campaign 
	 * api call
	 */
	public void getCreateCampaignResult(CreateCampaignResult result){
		if(result != null){
			if(result.getSuccess() != null){
				if(result.getSuccess()){
					if(result.getCampaign() != null){
						context.removeDialog(0);
						goToShareCampaignFragment(result.getCampaign().getId());
						storeCampaignInMycampaignDB(result);
						uploadPhotos(result.getCampaign().getId());
					}else{
						context.removeDialog(0);
					}
				}else{
					context.removeDialog(0);
					Toast.makeText(context, (result.getError()!=null)?
							result.getError():"Faild to create campaign", Toast.LENGTH_SHORT).show();
				}
			}else{
				context.removeDialog(0);
			}
		}else{
			context.removeDialog(0);
		}
	}

	/**
	 * This method use to store created campaign in DB 
	 * 
	 */
	private void storeCampaignInMycampaignDB(CreateCampaignResult result) {
		MyCampaignsDbAdapter adpt = new MyCampaignsDbAdapter(context);
		if(result != null && result.getCampaign() != null){
			try {
				adpt.beginTransaction();
				Log.i("CHECKINFORGOOD", "Id for camp res " + result.getCampaign().getName()+
						String.valueOf(result.getCampaign().getId()));
				adpt.createAssociated(result);
				adpt.succeedTransaction();
			} finally {
				adpt.endTransaction();                
			}  
		}
	}

	/**
	 * This method returns campaign sub category name
	 * @return String subcategory name
	 */
	private String getSubCatName(){

		String subCatName = "";
		if(strCatName.equalsIgnoreCase(Constants.INSTANCE.ONE_DAY_EVENT)){
			if(oneDayArr != null && oneDayArr.length>iSubCatPosition){
				subCatName = oneDayArr[iSubCatPosition];
			}			
		}else if(strCatName.equalsIgnoreCase(Constants.INSTANCE.ONGOING_ACT)){
			if(onGoingArr != null && onGoingArr.length>iSubCatPosition){
				subCatName = onGoingArr[iSubCatPosition];
			}
		}else if(strCatName.equalsIgnoreCase(Constants.INSTANCE.RAISE_AWARENESS)){
			if(raiseAwareArr != null && raiseAwareArr.length>iSubCatPosition){
				subCatName = raiseAwareArr[iSubCatPosition];
			}
		}
		return subCatName;
	}

	/**
	 * This method shows causes list
	 */
	private void showCausesList(){
		mCampgnCauseListFragment=null;
		if (mCampgnCauseListFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("campgn_list_type",false);

			mCampgnCauseListFragment = (CampgnCauseListFragment) context.addFragment(
					R.id.linearLayout2, CampgnCauseListFragment.class.getName(),
					"campgn_cat_cause_list", bundle);
		} else {
			(context).attachFragment((Fragment) mCampgnCauseListFragment);
		}	
	}

	/**
	 * This method shows causes category list
	 */
	private void showCausesCatogoryList(){
		mCampgnCauseListFragment=null;
		if (mCampgnCauseListFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("campgn_list_type",true);

			mCampgnCauseListFragment = (CampgnCauseListFragment) context.addFragment(
					R.id.linearLayout2, CampgnCauseListFragment.class.getName(),
					"campgn_cat_cause_list", bundle);
		} else {
			(context).attachFragment((Fragment) mCampgnCauseListFragment);
		}	
	}

	/**
	 * This method sets selected items from cause list to edittext
	 * 
	 */
	public void setSelectedItemsData(){
		if(mCampgnCauseListFragment != null){
			if(mCampgnCauseListFragment.bIsItemClick){
				if(mCampgnCauseListFragment.bIsFromCampgnCat){
					mSelectedCauseCatResult=mCampgnCauseListFragment.mSelectedCauseCatResult;
					if(mSelectedCauseCatResult != null){
						edtCategory.setText(mSelectedCauseCatResult.getCategory().getName());
					}
				}else{
					mSelectedCauseResult=mCampgnCauseListFragment.mSelectedCauseResult;
					if(mSelectedCauseResult != null){
						edtSupport.setText(mSelectedCauseResult.getOrganization().getName());	
					}
				}
				mCampgnCauseListFragment.bIsItemClick=false;
			}
		}
		if(mSimpleCalendarViewFragment != null){
			if(mSimpleCalendarViewFragment.bIsDateSelected){
				if(mSimpleCalendarViewFragment.strSelectedDate != null){			
					if(bIsStartDateSelected)
						edtStDate.setText(mSimpleCalendarViewFragment.strSelectedDate);
					else
						edtEnDate.setText(mSimpleCalendarViewFragment.strSelectedDate);
				}
			}
			mSimpleCalendarViewFragment.bIsDateSelected = false;
		}
	}

	/**
	 * This method sets images back to imageView
	 * 
	 */
	private void reInitializeImageViews(){
		//int iCnt=lnrLtCampgnPhotos.getChildCount();
		lnrLtCampgnPhotos.removeAllViews();
		//iCnt=lnrLtCampgnPhotos.getChildCount();
		for(int i=0;i<Constants.INSTANCE.mSelectedBitmaps.size();i++){
			displayPhotoToImageView(Constants.INSTANCE.mSelectedBitmaps.get(i));
		}
	}

	/**
	 * This method checks if cause category is present in DB if not 
	 * call the web api and fetches list
	 */ 
	private void getCauseCatList(){
		CauseCategoryDBAdapter adpt = new CauseCategoryDBAdapter(context);
		List<CauseCategoryResult> mCategoryRes = new ArrayList<CauseCategoryResult>();
		mCategoryRes = adpt.getList();

		if((mCategoryRes == null) || ((mCategoryRes != null) && (mCategoryRes.size() < 1)) ){
			// if no causes category list is present in DB then call api
			String[] args = new String[1];

			args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY); 
			if(appStatus.isOnline()) {
				CheckinLibraryActivity.strProgressMessage = getString(R.string.causeCatListMessage);
				context.showDialog(0);
				new CauseCategoryListTask(this).execute(args);
			} else {
				Log.v(TAG, "App is not online!");
				Intent intent= new Intent(context,NoConnectivityScreen.class);
				context.startActivity(intent);
				context.finish();
			}
		}else{
			showCausesCatogoryList();
		}
	}

	/**
	 * This method use to cache cause category list in DB 
	 * @param  list  list of cause categories
	 */
	public void storeCauseCategoryListinDB(List<CauseCategoryResult> list) {
		if (list != null && list.size() > 0) {
			CauseCategoryDBAdapter adapter = new CauseCategoryDBAdapter(context);
			adapter.refresh(list);
			context.removeDialog(0);
			showCausesCatogoryList();
		} else {
			context.removeDialog(0);
			Toast.makeText(context, "Faild to fetch causes category list",Toast.LENGTH_SHORT).show();
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
	 * This method use to upload all photos
	 * @param int iCampaignId
	 */
	private void uploadPhotos(int iCampaignId) {

		String jsonString = mCampaignsPhotoClass.getJsonFromPhotos();
		if (jsonString == null) {
			//context.removeDialog(0);
			//goToShareCampaignFragment(iCampaignId);
		} else {
			String[] args = new String[3];

			args[0] = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
			args[1] = String.valueOf(iCampaignId); // campaign id
			args[2] = jsonString; // photos json

			if (appStatus.isOnline()) {
				new UploadCampaignPhotoTask(this,
						Constants.INSTANCE.CREATE_FRAG).execute(args);
			} else {
				Log.v(TAG, "App is not online!");
				Intent intent = new Intent(context, NoConnectivityScreen.class);
				context.startActivity(intent);
				context.finish();
			}
		}
	}

	/**
	 * This method use to get response data from upload photo 
	 * api call
	 */
	public void getUploadPhotosResult(CreateCampaignResult result){
		context.removeDialog(0);
		if(result != null){
			if((result.getSuccess() != null) && (result.getSuccess())){
				mCampaignsPhotoClass.storeCampaignPhotos(result);
				// open share campaign fragment
				// goToShareCampaignFragment(result.getCampaign().getId());
				
				Fragment fragment = context.getLastFragment();
				if(fragment.getTag() == "congrats_campaign_fragment"){
					if(!context.isOnPause){
						context.popFragmentFromStack();
						CampaignCongratsFragment.updateCampaignsPhotos(context,result.getCampaign().getId());
					}
				}
			}
		}else{
			Toast.makeText(context,"Failed to upload photos!", Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * This method initialises SimpleCalenderViewFragment
	 */
	private void getDateFromCalender(){
		mSimpleCalendarViewFragment=null;
		if (mSimpleCalendarViewFragment == null) {
			Bundle bundle = new Bundle();

			mSimpleCalendarViewFragment = (SimpleCalendarViewFragment) ((CheckinLibraryActivity) context)
					.addFragment(R.id.linearLayout2,
							SimpleCalendarViewFragment.class.getName(),
							"simple_calender_fragment",bundle);
		} else {
			((CheckinLibraryActivity) this.context)
			.attachFragment((Fragment) mSimpleCalendarViewFragment);
		}
		Log.i("SimpleCalendarViewFragment", "getDateFromCalender");
	}

	/**
	 * This method use to open share campaign fragment
	 */
	private void goToShareCampaignFragment(int iCampaignId){
		if (mCampaignshareFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putInt("campgn_id", iCampaignId);

			mCampaignshareFragment = (CampaignshareFragment) ((CheckinLibraryActivity) context)
					.addFragment(R.id.linearLayout2,
							CampaignshareFragment.class.getName(),
							"share_campaign_fragment",bundle);
		} else {
			((CheckinLibraryActivity) this.context)
			.attachFragment((Fragment) mCampaignshareFragment);
		}
	}

	/**
	 * This method use to initialize pledges /mile,/min,/hour,/day in campaign flow
	 * api call
	 */
	private void askForPledgesInitialization(){
		lnrLtCampgnPledges.setVisibility(View.VISIBLE);
		txtCampgnPledgePer.setVisibility(View.VISIBLE);
	}

	/**
	 * This method use to initialize pledges /mile,/min,/hour,/day in campaign flow
	 * api call
	 */
	private void askForDonationInitialization(){
		lnrLtCampgnPledges.setVisibility(View.GONE);
		txtCampgnPledgePer.setVisibility(View.GONE);
	}
}
