package com.headsup.offers;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.checkinlibrary.models.BusinessResult;
import com.headsup.R;

public class OffersListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	Context context;
	public List<BusinessResult> businesses;

	public OffersListAdapter(Context context, List<BusinessResult> result) {
		mInflater = LayoutInflater.from(context);
		this.businesses = result;
		
	}

	@Override
	public int getCount() {
		if (businesses != null)
			return businesses.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return businesses.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.business_list_item, null);
			holder = new ViewHolder();
			holder.textViewName = (TextView) convertView
					.findViewById(R.id.businessName);
			holder.textViewAddress = (TextView) convertView
					.findViewById(R.id.businessAddress);
			holder.textViewDistance = (TextView) convertView
					.findViewById(R.id.businessDistance);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textViewName.setText(businesses.get(position).getName());
		
		holder.textViewAddress.setText(businesses.get(position).getAddress());
		
		BigDecimal distance = new BigDecimal(businesses.get(position).getDistance()).setScale(2, BigDecimal.ROUND_CEILING);
        distance = new BigDecimal(distance.floatValue()/1.609344f);  //Convert from km to miles
        String strDst=new DecimalFormat("#.#").format(distance);  
        holder.textViewDistance.setText(strDst);
		return convertView;
	}

	static class ViewHolder {
		TextView textViewName;
		TextView textViewAddress;
		TextView textViewDistance;
	}

}
