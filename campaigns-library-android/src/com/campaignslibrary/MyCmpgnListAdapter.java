package com.campaignslibrary;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.campaignslibrary.imageloader.ImageLoader;
import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.WebServiceIface;
import com.checkinlibrary.CheckinLibraryActivity;

public class MyCmpgnListAdapter extends BaseAdapter implements WebServiceIface{
	private LayoutInflater mInflater;
	private List<CreateCampaignResult> mMyCampgnList = null;
	
	Context context;
	private CampaignCongratsFragment mCampaignCongratsFragment = null;
	
	private CampaignDetailsFragment mCampaignDetailsFragment = null;
	
	public ImageLoader imageLoader;
	
	private Boolean isFromSearch = false;

	public MyCmpgnListAdapter(Context context, List<CreateCampaignResult> myCampgnList, Boolean isFromSearch) {
		mInflater = LayoutInflater.from(context);
		this.mMyCampgnList = myCampgnList;
		this.context = context;
		imageLoader=new ImageLoader(context);
		this.isFromSearch = isFromSearch;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.mMyCampgnList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return this.mMyCampgnList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;

		convertView = mInflater.inflate(R.layout.my_cmpgn_list_item, null);
		holder = new ViewHolder();

		holder.imgMyCmpgn = (ImageView) convertView.findViewById(R.id.imgMyCmpgn);
		holder.txtRaisedSoFar = (TextView) convertView.findViewById(R.id.txtRaisedSoFar);				
		holder.progressRaisedSoFar = (ProgressBar) convertView.findViewById(R.id.progress_raised_money);
		holder.progressRaisedSoFar.setMax(100);
		holder.progressRaisedSoFar.setProgress(40);
		holder.btnViewDtls = (Button) convertView.findViewById(R.id.btnViewDetails);
		holder.txtCmpgnName = (TextView) convertView.findViewById(R.id.txtMyCmpgnName);

		if (this.mMyCampgnList.get(position).getCampaign() != null) {
			if(this.mMyCampgnList.get(position).getCampaign().getGoal() > 0){
				holder.txtRaisedSoFar.setText("$"+ this.mMyCampgnList.get(position).getCampaign()
						.getGoal() + " Raised so far");
			}
			if(this.mMyCampgnList.get(position).getCampaign().getName() != null){
				holder.txtCmpgnName.setText(this.mMyCampgnList.get(position).getCampaign().getName());
			}
			if(mMyCampgnList.get(position).getCampaign().getPhotos() != null && 
					mMyCampgnList.get(position).getCampaign().getPhotos().size() >0){
				String imgLink = mMyCampgnList.get(position).getCampaign().getPhotos().get(0).getImage_link();
				if(imgLink != null){					
					displayPhotoToImageViewFromLink(imgLink, holder.imgMyCmpgn);
				}
			}			
		}

		convertView.setTag(holder);

		holder.btnViewDtls.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v) {
				if(isFromSearch){
					goToCmpgnDtlsPage(mMyCampgnList.get(position));
				}else{
					goToCongratsCampaignFragment(mMyCampgnList.get(position).getCampaign().getId());
				}
			}
		});
		return convertView;
	}

	static class ViewHolder {
		ImageView imgMyCmpgn;
		TextView txtRaisedSoFar;
		TextView txtCmpgnName;
		ProgressBar progressRaisedSoFar;
		Button btnViewDtls;
	}
	
	/**
	 * This method use to open congrats campaign fragment
	 */
	private void goToCongratsCampaignFragment(int iCampgnId){
		mCampaignCongratsFragment=null;
		if (mCampaignCongratsFragment == null) {
			Bundle bundle=new Bundle();
			bundle.putBoolean("isFromWhere", false);
			bundle.putInt("campgn_id", iCampgnId);
			mCampaignCongratsFragment = (CampaignCongratsFragment) ((CheckinLibraryActivity) context)
					.addFragment(R.id.linearLayout2,
							CampaignCongratsFragment.class.getName(),
							"congrats_campaign_fragment",bundle);
		} else {
			((CheckinLibraryActivity) this.context)
					.attachFragment((Fragment) mCampaignCongratsFragment);
		}
	}
	
	/**
	 * This method use to display imageview from image url
	 * @param strImgUrl String imagelink to display on imageview
	 */
	private void displayPhotoToImageViewFromLink(String strImgUrl, ImageView imgView){
		String strUrl = BASE_URL + strImgUrl;		
		imageLoader.DisplayImage(strUrl, imgView);
	}
	
	/**
	 * This method handles the photo click event of ending soon
	 */
	private void goToCmpgnDtlsPage(CreateCampaignResult mCreateCampaignResult){
		mCampaignDetailsFragment=null;
		if (mCampaignDetailsFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_campaign", mCreateCampaignResult);
			
			mCampaignDetailsFragment = (CampaignDetailsFragment) ((CheckinLibraryActivity) this.context).addFragment(
					R.id.linearLayout2, CampaignDetailsFragment.class.getName(),
					"campgn_details_fragment", bundle);
		} else {
			((CheckinLibraryActivity) this.context).attachFragment((Fragment) mCampaignDetailsFragment);
		}
	}
}
