package com.headsup.safety;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.headsup.R;

public class HeatPreparednessVideoListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	// private ArrayList<Entry> heatPreparednessList;
	private ArrayList<String> heatPreparednessList;
	private String strActivityFlag;
	Context context;
	String strFirstVideo;

	// Entry obj = new Entry();
	String obj;

	private int[] videoImages;

	public HeatPreparednessVideoListAdapter(Context context,
			ArrayList<String> heatPreparednessList,
			int[] equipmentFittingIcons, String strActivityFlag) {
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.heatPreparednessList = heatPreparednessList;
		this.videoImages = equipmentFittingIcons;
		this.strActivityFlag = strActivityFlag;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return heatPreparednessList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return heatPreparednessList.get(position);
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
					R.layout.heatpreparedness_video_list_item, null);
			holder = new ViewHolder();
			holder.imgHeatPreparednessVideo = (ImageView) convertView
					.findViewById(R.id.imgHeatPreparednessVideo);

			// holder.imgHeatPreparednessVideo.setVisibility(View.GONE);

			holder.txtHeatPreparednessDetil = (TextView) convertView
					.findViewById(R.id.txtHeatPreparednessDetails);

			// convertView.setBackgroundResource(R.drawable.headsup_videobg);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		obj = heatPreparednessList.get(position);

		holder.txtHeatPreparednessDetil.setText(obj);

		holder.imgHeatPreparednessVideo.setImageResource(videoImages[position]);

		switch (position) {
		case 0:
			if (strActivityFlag.equals("EquipmentFitting"))
				strFirstVideo = "http://www.youtube.com/watch?v=i53UeBlowm8&feature=plcp";
			else if (strActivityFlag.equals("ConcussionAwareness"))
				strFirstVideo = "http://www.youtube.com/watch?v=yWJno5dUKBg";
			else if (strActivityFlag.equals("HeatPreparedness"))
				strFirstVideo = "http://www.youtube.com/watch?v=Irw21MjEEbQ";

			holder.imgHeatPreparednessVideo
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Uri uri = Uri.parse(strFirstVideo);
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(intent);
						}
					});
			break;
		case 1:
			holder.imgHeatPreparednessVideo
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Uri uri = Uri.parse("http://www.youtube.com/watch?v=r_ojB2waDpU&feature=plcp");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(intent);
						}
					});
			break;
		case 2:
			holder.imgHeatPreparednessVideo
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Uri uri = Uri
									.parse("http://www.youtube.com/watch?v=abHXcfr_82g&feature=plcp");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(intent);
						}
					});
			break;
		case 3:
			holder.imgHeatPreparednessVideo
					.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Uri uri = Uri
									.parse("http://www.youtube.com/watch?v=_i4NkxI6mwc");
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							context.startActivity(intent);
						}
					});
			break;
		default:
			break;
		}
		return convertView;
	}

	static class ViewHolder {
		ImageView imgHeatPreparednessVideo;
		TextView txtHeatPreparednessDetil;
	}
}
