package com.campaignslibrary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;

public class CampaignCauseListAdapter extends BaseAdapter{
	Context context;
	String[] strList;
	CheckinLibraryActivity main_context;

	public CampaignCauseListAdapter(Context context,String[] list) {
		this.context = context;
		main_context=(CheckinLibraryActivity)context;
		this.strList=list;
	}

	@Override
	public int getCount() {
		if(strList != null)
			return strList.length;
		else
			return 0;
	}

	@Override
	public Object getItem(int index) {
		if(strList != null)
			return strList[index];
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.cause_list_item, parent, false);
		TextView tv = (TextView) v.findViewById(R.id.allCauseName);
		final String result = (String) getItem(position);
		tv.setText(result);

		final ImageView imageView;
		imageView=(ImageView)v.findViewById(R.id.imagecheckBox);  
		imageView.setVisibility(ImageView.GONE);
		
		final ImageView imageViewNextArrow;
		imageViewNextArrow=(ImageView)v.findViewById(R.id.imageNextArrow);
		imageViewNextArrow.setVisibility(ImageView.GONE);
		
		return v;
	}
}
