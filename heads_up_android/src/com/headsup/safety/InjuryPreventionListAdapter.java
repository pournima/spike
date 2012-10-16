package com.headsup.safety;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headsup.R;
import com.headsup.models.Entry;

public class InjuryPreventionListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private ArrayList<Entry> injuryPreventionList;
	
	Entry obj=new Entry();
	

	public InjuryPreventionListAdapter(Context context,
			ArrayList<Entry> injuryPreventionList) {
		mInflater = LayoutInflater.from(context);
		this.injuryPreventionList = injuryPreventionList;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return injuryPreventionList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return injuryPreventionList.get(position);
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
			convertView = mInflater.inflate(
					R.layout.injury_prevention_list_item, null);
			holder = new ViewHolder();
			holder.imgPrevenationIcon = (ImageView) convertView
					.findViewById(R.id.imgPrevenationIcon);
			holder.imgPrevenationIcon.setVisibility(View.GONE);
			
			holder.txtPreventionHeading = (TextView) convertView
					.findViewById(R.id.txtPreventionHeading);
			holder.imgArrow = (ImageView) convertView
					.findViewById(R.id.imgArrow);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		obj = injuryPreventionList.get(position);

		holder.txtPreventionHeading.setText(obj.getTitle());
//		holder.imgPrevenationIcon
//				.setImageResource(injuryPreventionIcons[position]);

		return convertView;
	}

	static class ViewHolder {
		ImageView imgPrevenationIcon;
		TextView txtPreventionHeading;
		ImageView imgArrow;
	}
}
