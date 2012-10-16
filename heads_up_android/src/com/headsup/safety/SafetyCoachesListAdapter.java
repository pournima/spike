package com.headsup.safety;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headsup.R;

public class SafetyCoachesListAdapter extends BaseAdapter {
	String[] strArray;
	Context context;
	private LayoutInflater mInflater;

	public SafetyCoachesListAdapter(Context context,
			String[] lstCoaches) {
		// TODO Auto-generated constructor stub
		this.context = context;
		strArray = lstCoaches;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strArray.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return strArray[arg0];
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
			convertView = mInflater.inflate(R.layout.coaches_list_item, null);
			holder = new ViewHolder();
			
			holder.txtCoaches = (TextView) convertView
					.findViewById(R.id.txtCoaches);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txtCoaches.setText(strArray[position]);
		return convertView;
	}

	static class ViewHolder {
		ImageView imgCheckIcon;
		TextView txtCoaches;
}

}
