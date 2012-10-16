package com.headsup.offers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.business.BusinessFragment;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.helpers.Constants;

public class OffersHeadsUpFragment extends Fragment {
	TextView txtLink;
	Button btnViewBusinesses;
	HeadsUpNativeActivity context;
	BusinessFragment mBusinessFragment;
	TextView imgPlayVideo,txtSupportText;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (HeadsUpNativeActivity) this.getActivity();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.offers_intro, container, false);
		
		btnViewBusinesses=(Button) v.findViewById(R.id.btnViewBusinesses);
		txtLink=(TextView)v.findViewById(R.id.txtlink);		
		imgPlayVideo=(TextView)v.findViewById(R.id.imgPlayVideo);
		txtSupportText=(TextView)v.findViewById(R.id.txtSupportText);
		
		onViewBusinessButtonClick();
		return v;
	}

	public void onViewBusinessButtonClick(){

		btnViewBusinesses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.listenForLocation();
				if(mBusinessFragment == null){
					mBusinessFragment = (BusinessFragment) context.
							addFragment(R.id.linearLayout2,
									BusinessFragment.class.getName(),
									"business_tab");
				}
				else {

					(context).attachFragment((Fragment) mBusinessFragment,"business_tab");
				}

			}
		});
		
		imgPlayVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onVideoLinkClick();
			}	
		});
		
		
		txtSupportText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onVideoLinkClick();
			}	
		});
		
		txtLink.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				onLinkClick();
			}	
		});
		
		
	}
	
	 @Override
	    public void onResume() {
	        super.onResume();

	        HeadsUpNativeActivity.textViewRaisedMoney.setText(HeadsUpNativeActivity.appStatus.setCheckinAmount());
	 }
	 
	public void onLinkClick()

	 {
		 if(HeadsUpNativeActivity.appStatus.isOnline())
		 {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.CHECKIN_FOR_GOODS_VIDEO));
			this.startActivity(browserIntent);
		 }
		 else
		 {
				Log.v("Conditioning Activity", "App is not online!");
				Intent intent = new Intent(context, NoConnectivityScreen.class);
				this.startActivity(intent);
				context.finish();
		 }
	 }
	 
	 public void onVideoLinkClick()
	 {
			Uri uri = Uri
					.parse(Constants.CHECK_IN_VIDEO);
			Intent intent = new Intent(Intent.ACTION_VIEW, uri);
			startActivity(intent);
	 }
}
