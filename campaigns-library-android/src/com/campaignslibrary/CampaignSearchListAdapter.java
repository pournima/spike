package com.campaignslibrary;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CampaignSearchListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> cmpgnSearchArr;

	public CampaignSearchListAdapter(Context context, ArrayList<String> cmpgnSearchArrList) {
		mInflater = LayoutInflater.from(context);
		this.cmpgnSearchArr = cmpgnSearchArrList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cmpgnSearchArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cmpgnSearchArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.cmpgn_search_list_item, null);
			holder = new ViewHolder();
			holder.txtPreventionHeading = (TextView) convertView.findViewById(R.id.txtSearchHeading);
			holder.imgArrow = (ImageView) convertView.findViewById(R.id.imgArrow);
			
			holder.txtPreventionHeading.setText(cmpgnSearchArr.get(position));

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView imgPrevenationIcon;
		TextView txtPreventionHeading;
		ImageView imgArrow;
	}
}
