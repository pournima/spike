package com.campaignslibrary;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CmpgnDonationListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<String> cmpgnDonArr;

	public CmpgnDonationListAdapter(Context context, ArrayList<String> injuryPreventionList) {
		mInflater = LayoutInflater.from(context);
		this.cmpgnDonArr = injuryPreventionList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cmpgnDonArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cmpgnDonArr.get(position);
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
			convertView = mInflater.inflate(R.layout.cmpgn_donation_list_item, null);
			holder = new ViewHolder();
			
			holder.imgMainIcon = (ImageView) convertView.findViewById(R.id.imgList);
			holder.txtHeading = (TextView) convertView.findViewById(R.id.txtDonateHeading);			
			holder.txtSubHeading = (TextView) convertView.findViewById(R.id.txtDonateSubHeading);
			holder.txtdesc = (TextView) convertView.findViewById(R.id.txtDesc);
			holder.imgArrow = (ImageView) convertView.findViewById(R.id.imgArrow);
			
			holder.txtHeading.setText(cmpgnDonArr.get(position));

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	static class ViewHolder {
		ImageView imgMainIcon;
		TextView txtHeading;
		TextView txtSubHeading;
		TextView txtdesc;
		ImageView imgArrow;
	}
}
