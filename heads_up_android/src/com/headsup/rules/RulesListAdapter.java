package com.headsup.rules;

import com.headsup.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class RulesListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private static String[] rulesStringList;

	public RulesListAdapter(Context context,
			String[] rulesDataList) {
		rulesStringList = rulesDataList;
		mInflater = LayoutInflater.from(context);

	}

	@Override
	public int getCount() {
		return rulesStringList.length;

	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.rules_custom_text, null);
			holder = new ViewHolder();
			holder.txtRules = (TextView) convertView.findViewById(R.id.textViewRules);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.txtRules.setText(rulesStringList[position]);

		return convertView;
	}

	static class ViewHolder {
		TextView txtRules;

	}
}