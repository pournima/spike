package com.headsup.safety;

import com.headsup.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ConcussionAwarenessListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private String[] concussionAwarenessListName;
	private String[] concussionAwarenessListData;
	
	public ConcussionAwarenessListAdapter(Context context,
			String[] concussionAwarenessListName,String[] concussionAwarenessListData) {
		mInflater = LayoutInflater.from(context);
		this.concussionAwarenessListName = concussionAwarenessListName;
		this.concussionAwarenessListData=concussionAwarenessListData;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return concussionAwarenessListName.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return concussionAwarenessListName[position];
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
					R.layout.concussion_list_item, null);
			holder = new ViewHolder();

			holder.txtConcussionName = (TextView) convertView
					.findViewById(R.id.concussionName);
			holder.txtConcussionData=(TextView) convertView
					.findViewById(R.id.concussionData);
			holder.imgArrow = (ImageView) convertView
					.findViewById(R.id.imgArrow);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtConcussionName.setText(concussionAwarenessListName[position]);
		holder.txtConcussionData.setText(concussionAwarenessListData[position]);
		
		return convertView;
	}

	static class ViewHolder {
		TextView txtConcussionName;
		TextView txtConcussionData;
		ImageView imgArrow;
	}

}
