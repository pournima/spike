package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkinlibrary.R;
import com.checkinlibrary.R.color;

public class SettingsListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	ArrayList<String> settingItems;
	
	public static final int ABOUT = 0;
	public static final int CONTACT = 1;
	public static final int SHARING = 2;
	public static final int MYGOOD = 3;
	public static final int HOWTOCHECKIN = 4;
	public static final int PRIVACY = 5;
	public static final int TERMSOFUSE = 6;
	public static final int C4GABOUT = 7;

	public SettingsListAdapter(Context context, ArrayList<String> settingItems) {
		this.settingItems = settingItems;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return settingItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return settingItems.get(position);
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
			convertView = mInflater.inflate(R.layout.settings_list_item, null);
			holder = new ViewHolder();
			if(position!=7) {
			holder.text = (TextView) convertView.findViewById(R.id.txtSettings);
			holder.arrow = (ImageView) convertView.findViewById(R.id.imgArrow);
			} else {
				holder.text = (TextView) convertView.findViewById(R.id.txtSettings);
				holder.text.setTypeface(null, Typeface.NORMAL);
	            holder.text.setTextColor(color.gray);
	            holder.arrow = (ImageView) convertView.findViewById(R.id.imgArrow);
				holder.arrow.setVisibility(ImageView.INVISIBLE);
			}
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.text.setText(settingItems.get(position));
		
		
		return convertView;
	}

	static class ViewHolder {
		TextView text;
		ImageView arrow;
	}

}