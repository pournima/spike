package com.checkinlibrary.org;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.db.BusinessDbAdapter;
import com.checkinlibrary.db.MyOrganizationDBAdapter;
import com.checkinlibrary.helpers.MyConstants;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.CauseProfileResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.ws.tasks.BusinessTask;
import com.checkinlibrary.ws.tasks.causeProfileImageTask;


public class CauseProfileFragment extends Fragment {

	CheckinLibraryActivity main_context;
	Context context;
	TextView causesprofileTitleTextView,causesprofileUser_countTextView,causesprofileBusiness_countTextView,causesprofilemissionDescTextVIew;
	Button btnSupport;
	ImageView imgCauseLogo;
	OrganizationResult mOrganizationResult;
	TextView txtTapCauseMsg,causesprofilemissionTitleTextVIew;
	static BlockingQueue<SupportParams> queue = new LinkedBlockingQueue<SupportParams>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		main_context = (CheckinLibraryActivity) this.getActivity();
		View v = inflater.inflate(R.layout.causeprofile, container, false);

		mOrganizationResult = (OrganizationResult) this.getArguments().getSerializable(
				"selected_cause");

		initializePage(v);
		//getCausesprofileData(false);
		initializeCauseProfileData();
		return v;
	}

	void initializePage(View v) {

		causesprofileTitleTextView = (TextView) v.findViewById(R.id.txtHeading);
		causesprofileUser_countTextView = (TextView) v.findViewById(R.id.txtNoOfUsers);
		causesprofileBusiness_countTextView = (TextView) v.findViewById(R.id.txtNoOfBusiness);
		causesprofilemissionDescTextVIew = (TextView) v.findViewById(R.id.txtContentDescriptionCausesprofile);
		imgCauseLogo = (ImageView)v.findViewById(R.id.imgCausesProfile);
		btnSupport = (Button) v.findViewById(R.id.btnSupport);
		txtTapCauseMsg=(TextView)v.findViewById(R.id.txtSupportMsg);
		setSupportedClickHandler(btnSupport, !mOrganizationResult.getOrganization().getSupported());
		causesprofilemissionTitleTextVIew = (TextView) v.findViewById(R.id.txtTitleDescriptionCausesprofile);

		float scale = main_context.getResources().getDisplayMetrics().density;
		imgCauseLogo.getLayoutParams().height = (int) (80.0*scale);
		imgCauseLogo.getLayoutParams().width = (int) (80.0*scale);
		imgCauseLogo.setScaleType(ImageView.ScaleType.FIT_XY);

		if(mOrganizationResult != null){
			updateBtnText(mOrganizationResult.getOrganization().getSupported());
		}

		Thread readThread = new Thread(new SupportQueueReader(queue));
		readThread.start();
	}

	private void setSupportedClickHandler(final Button btnView, Boolean isSupported) {
		final Boolean closureSupported = isSupported;

		btnView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {				
				supportBtnClicked(closureSupported,btnView);			
			}
		});        
	}

	public void supportBtnClicked(Boolean closureSupported,Button view){
		updateBtnText(closureSupported);
		btnSupport.setEnabled(false);
		if (CheckinLibraryActivity.appStatus.isOnline()) {

			if(closureSupported)
				CheckinLibraryActivity.strProgressMessage = getString(R.string.supportProgressMessage);
			else
				CheckinLibraryActivity.strProgressMessage = getString(R.string.unSupportProgressMessage);

			//main_context.showDialog(0);
			mOrganizationResult.setSupported(closureSupported, main_context);
			SupportParams params = new SupportParams(main_context, this,closureSupported);
			Thread writeThread = new Thread(new SupportQueueAdder(queue, params));
			writeThread.start();
		} else {
			Log.v("SupportCause", "App is not online!");
			Intent intent= new Intent(main_context,NoConnectivityScreen.class);
			main_context.startActivity(intent);
			main_context.finish();
		}
	}

	//Dont remove this code might needed //
	/*public void getCausesprofileData(boolean bIsFromUpdate) {

		if (CheckinNativeActivity.appStatus.isOnline()) {

			String args[] = new String[2];
			args[0] = CheckinNativeActivity.mAuthToken;
			args[1] = Integer.toString(mOrganizationResult.getOrganization().getId());

			if(!bIsFromUpdate){
				CheckinNativeActivity.strProgressMessage = getString(R.string.causesprofileProgressMessage);
				//main_context.showDialog(0);
			}
			new CauseProfileTask(main_context, this, args).execute(args);
		} else {
			main_context.message("Check internet connectivity!");
			Log.v("CauseProfile", "App is not online!");
			Intent intent = new Intent(main_context, NoConnectivityScreen.class);
			startActivity(intent);
			main_context.finish();
		}
	}*/

	public void DisplayToast(String msg) {
		Toast.makeText(main_context, msg, Toast.LENGTH_SHORT).show();
		main_context.removeDialog(0);
	}

	public void onCausesprofileResult(CauseProfileResult result) {
		causesprofileTitleTextView.setText(result.getName());

		if (Integer.parseInt(result.getUsers_count()) == 1) {

			causesprofileUser_countTextView.setText(result.getUsers_count()
					+ " User");
		} else {

			causesprofileUser_countTextView.setText(result.getUsers_count()
					+ " Users");
		}

		if (Integer.parseInt(result.getBusinesses_count()) == 1) {

			causesprofileBusiness_countTextView.setText(result
					.getBusinesses_count() + " Business");
		} else {

			causesprofileBusiness_countTextView.setText(result
					.getBusinesses_count() + " Businesses");
		}
		causesprofilemissionDescTextVIew.setText(result.getDescription());
		causesprofilemissionTitleTextVIew.setText(result.getTitle());
		main_context.removeDialog(0);

		if (result.getImage_url().compareTo("/system/logos/no-logo.gif") != 0) {
			new causeProfileImageTask(this, imgCauseLogo).execute(result
					.getImage_url());
		}
	}

	private void updateBtnText(Boolean isSupported) {
		if(isSupported){
			btnSupport.setBackgroundResource(R.drawable.btn_supporting);
			txtTapCauseMsg.setVisibility(TextView.VISIBLE);
		}else{
			btnSupport.setBackgroundResource(R.drawable.unsupport);
			txtTapCauseMsg.setVisibility(TextView.INVISIBLE);
		}    
	}

	public void updateSupport(Boolean[] ret) {
		if((ret!= null) && (ret.length > 0)) {
			if ( ret[1] ) {
				//getCausesprofileData(true);
				//Update our view/model
				updateBtnText(ret[0]);
				mOrganizationResult.setSupported(ret[0],main_context);
				setSupportedClickHandler(btnSupport, !ret[0]);
				//Add or remove from myCause
				updateMyCause(ret[0],mOrganizationResult);
			} else {
				//Set the image back, since the update failed.
				updateBtnText(!ret[0]);
				mOrganizationResult.setSupported(!ret[0],main_context);
				btnSupport.setEnabled(true);
				main_context.removeDialog(0);
			}
		}
	}

	private void updateMyCause(Boolean bIsSupported,OrganizationResult result) {
		MyOrganizationDBAdapter adpt = new MyOrganizationDBAdapter(context);
		if(bIsSupported) { // if supported then insert into myCauses table
			
			String title="",image_link="",about_us="";
			if(result.getOrganization().getCause_profile() != null){
				title = result.getOrganization().getCause_profile().getTitle();
                image_link = result.getOrganization().getCause_profile().getImage_link();
                about_us=result.getOrganization().getCause_profile().getAbout_us();
			}
	    		
			adpt.create((long)result.getOrganization().getId(), result.getOrganization().getName(),result.getOrganization().getUsers_count(),result.getOrganization().getSupport_count(),title,
					image_link,about_us);
		} else { //if stop supported then delete from myCauses table
			adpt.delete("remote_id="+String.valueOf(result.getOrganization().getId()));
		}
		//fetch businesses supporting my causes from api and refresh that table 
		updateBusSuppMyCauses();
	}

	private void updateBusSuppMyCauses() {
		if(CheckinLibraryActivity.appStatus.isOnline()) {
			GpsLocator gps = GpsLocator.getGpsLocator(main_context);
			Location currentLocation=gps.getBestLocation();
			double latitude = 0,longitude = 0;
			if(currentLocation != null) {
				new BusinessTask(this).execute(currentLocation.getLatitude(), currentLocation.getLongitude());
			} else {
				String lat=CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.LAT);
				String lon=CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.LONG);
				if((lat!= null) && (lon!=null)) {
					latitude=Double.valueOf(lat);
					longitude=Double.valueOf(lon);
				}
				new BusinessTask(this).execute(latitude, longitude);
			}
		} else {
			Log.v("CHECKINFORGOOD", "App is not online!");
			Intent intent= new Intent(main_context,NoConnectivityScreen.class);
			main_context.startActivity(intent);
			main_context.finish();
		}
		btnSupport.setEnabled(true);
	}

	public void updateBusIntoDB(List<BusinessResult> result) {
		if (result != null) {
			BusinessDbAdapter adpt = new BusinessDbAdapter(main_context,
					MyConstants.TABLE_MY_ORGS_BUS,
					MyConstants.TABLE_MY_BUS_ORGS,
					MyConstants.TABLE_MY_ORGS_CHECKIN_TIMES);
			adpt.deleteAllAssociated();

			try {
				adpt.beginTransaction();

				for (int i = 0; i < result.size(); i++) {
					Log.v("CHECKINFORGOOD",
							"Id for test res " + result.get(i).getName()
							+ String.valueOf(result.get(i).getId()));
					adpt.createAssociated(result.get(i));
				}

				adpt.succeedTransaction();
			} finally {
				adpt.endTransaction();
			}
		}
		btnSupport.setEnabled(true);
	}

	public void onCausesImageResult(Bitmap bitmap) {
		if(bitmap != null){
			imgCauseLogo.setImageBitmap(bitmap);
		}
	}

	private void initializeCauseProfileData(){
		if(mOrganizationResult != null){
			causesprofileTitleTextView.setText(mOrganizationResult.getOrganization().getName());

			if (mOrganizationResult.getOrganization().getUsers_count() == 1) {

				causesprofileUser_countTextView.setText(mOrganizationResult.getOrganization().getUsers_count()
						+ " User");
			} else {

				causesprofileUser_countTextView.setText(mOrganizationResult.getOrganization().getUsers_count()
						+ " Users");
			}

			if (mOrganizationResult.getOrganization().getSupport_count() == 1) {

				causesprofileBusiness_countTextView.setText(mOrganizationResult.getOrganization()
						.getSupport_count() + " Business");
			} else {

				causesprofileBusiness_countTextView.setText(mOrganizationResult
						.getOrganization().getSupport_count() + " Businesses");
			}

			if(mOrganizationResult.getOrganization().getCause_profile() != null){
				causesprofilemissionDescTextVIew.setText(mOrganizationResult.getOrganization().getCause_profile().getAbout_us());
				causesprofilemissionTitleTextVIew.setText(mOrganizationResult.getOrganization().getCause_profile().getTitle());
				main_context.removeDialog(0);

				if (mOrganizationResult.getOrganization().getCause_profile().getImage_link().compareTo("/system/logos/no-logo.gif") != 0) {
					new causeProfileImageTask(this, imgCauseLogo).execute(mOrganizationResult
							.getOrganization().getCause_profile().getImage_link());
				}
			}
		}
	}
}
