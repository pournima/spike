package com.headsup.safety;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.headsup.R;
import com.headsup.safety.ParentsTabItems.VideoData;

public class SafetyCheckListAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	Context context;
	ArrayList<ParentsTabItems> mParentsTabItems;
	int[] imgBtnVidRes = { R.id.vid1, R.id.vid2, R.id.vid3, R.id.vid4,
			R.id.vid5, R.id.vid6, R.id.vid7 };

	public SafetyCheckListAdapter(Context context,
			ArrayList<ParentsTabItems> mPTabItems) {
		// TODO Auto-generated constructor stub
		this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mParentsTabItems = mPTabItems;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mParentsTabItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mParentsTabItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		final int iPosition = position;
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.safety_checklist_list_item, null);
			holder = new ViewHolder();
			holder.imgCheckIcon = (ImageView) convertView
					.findViewById(R.id.imgCheck);
			holder.imgShowHideVideo = (ImageView) convertView
					.findViewById(R.id.imgBtnShowVideo);
			holder.txtParentsTabDesc = (TextView) convertView
					.findViewById(R.id.txtParentsTabDesc);
			holder.txtParentsTabnHeading = (TextView) convertView
					.findViewById(R.id.txtHeader);
			holder.txtParentsTabContent = (TextView) convertView
					.findViewById(R.id.txtContent);
			holder.txtVideoLabel = (TextView) convertView
					.findViewById(R.id.txtWatchVideos);
			holder.tableVideos = (TableLayout) convertView
					.findViewById(R.id.tableLayoutVideos);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final ParentsTabItems mParentTabObj = (ParentsTabItems) getItem(position);

		if (mParentTabObj != null) {
			holder.imgShowHideVideo.setVisibility(mParentTabObj
					.getfImgShowHideVideo());

			holder.txtParentsTabnHeading.setVisibility(mParentTabObj
					.getfTxtParentsTabnHeading());
			holder.txtParentsTabnHeading.setText(mParentTabObj
					.getTxtParentsTabnHeading());

			holder.txtParentsTabContent.setVisibility(mParentTabObj
					.getfTxtParentsTabContent());
			holder.txtParentsTabContent.setText(mParentTabObj
					.getTxtParentsTabContent());

			holder.txtVideoLabel.setVisibility(mParentTabObj
					.getfTxtVideoLabel());
			holder.txtVideoLabel.setText(mParentTabObj.getTxtVideoLabel());

			holder.txtParentsTabDesc.setVisibility(mParentTabObj
					.getfTxtParentsTabDesc());
			holder.txtParentsTabDesc.setText(mParentTabObj
					.getTxtParentsTabDesc());

			holder.tableVideos.setVisibility(mParentTabObj.getfTableVideos());

			setVideosData(mParentTabObj, holder);
		}

		holder.imgShowHideVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if (iPosition == 4) {
					if (holder.txtParentsTabDesc.getVisibility() == TextView.GONE) {

						holder.txtParentsTabDesc
								.setVisibility(TextView.VISIBLE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.close_videos);
						holder.txtVideoLabel.setText("Close");
					} else if (holder.txtParentsTabDesc.getVisibility() == TextView.VISIBLE) {

						holder.txtParentsTabDesc.setVisibility(TextView.GONE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.watch_more_video);
						holder.txtVideoLabel.setText(mParentTabObj
								.getTxtVideoLabel());
					}

				} else if (iPosition == 3) {
					Toast.makeText(context,
							String.valueOf(holder.tableVideos.getVisibility()),
							Toast.LENGTH_SHORT).show();
					if (holder.tableVideos.getVisibility() == TableLayout.VISIBLE) {

						holder.tableVideos.setVisibility(TableLayout.GONE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.watch_more_video);
						holder.txtVideoLabel.setText(mParentTabObj
								.getTxtVideoLabel());
					} else if (holder.tableVideos.getVisibility() == TableLayout.GONE) {

						holder.tableVideos.setVisibility(TableLayout.VISIBLE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.close_videos);
						holder.txtVideoLabel.setText("Close");
					}
				} else if (iPosition == 2) {
					Toast.makeText(context,
							String.valueOf(holder.tableVideos.getVisibility()),
							Toast.LENGTH_SHORT).show();
					if (holder.tableVideos.getVisibility() == TableLayout.VISIBLE) {

						holder.tableVideos.setVisibility(TableLayout.GONE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.watch_more_video);
						holder.txtVideoLabel.setText(mParentTabObj
								.getTxtVideoLabel());
					} else if (holder.tableVideos.getVisibility() == TableLayout.GONE) {

						holder.tableVideos.setVisibility(TableLayout.VISIBLE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.close_videos);
						holder.txtVideoLabel.setText("Close");
					}
				} else if (iPosition == 1) {
					Toast.makeText(context,
							String.valueOf(holder.tableVideos.getVisibility()),
							Toast.LENGTH_SHORT).show();
					if (holder.tableVideos.getVisibility() == TableLayout.VISIBLE) {

						holder.tableVideos.setVisibility(TableLayout.GONE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.watch_more_video);
						holder.txtVideoLabel.setText(mParentTabObj
								.getTxtVideoLabel());
					} else if (holder.tableVideos.getVisibility() == TableLayout.GONE) {

						holder.tableVideos.setVisibility(TableLayout.VISIBLE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.close_videos);
						holder.txtVideoLabel.setText("Close");
					}
				} else {
					Toast.makeText(context,
							String.valueOf(holder.tableVideos.getVisibility()),
							Toast.LENGTH_SHORT).show();
					if (holder.tableVideos.getVisibility() == TableLayout.VISIBLE) {

						holder.tableVideos.setVisibility(TableLayout.GONE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.watch_more_video);
						holder.txtVideoLabel.setText(mParentTabObj
								.getTxtVideoLabel());
					} else if (holder.tableVideos.getVisibility() == TableLayout.GONE) {

						holder.tableVideos.setVisibility(TableLayout.VISIBLE);
						holder.imgShowHideVideo
								.setImageResource(R.drawable.close_videos);
						holder.txtVideoLabel.setText("Close");
					}
				}
			}
		});

		return convertView;
	}

	static class ViewHolder {
		ImageView imgShowHideVideo;
		ImageView imgCheckIcon;
		TextView txtParentsTabnHeading;
		TextView txtParentsTabContent;
		TextView txtVideoLabel;
		TextView txtParentsTabDesc;
		TableLayout tableVideos;

	}

	private void setVideosData(ParentsTabItems mParentTabObj, ViewHolder holder) {

		int iVideoCnt = mParentTabObj.getiVideosCount();

		if (iVideoCnt > 0) {
			if (iVideoCnt == 5) {
				ArrayList<VideoData> mVideoData = mParentTabObj
						.getTableVideos();
				for (int i = 0; i < iVideoCnt; i++) {
					VideoData vd = mVideoData.get(i);
					ImageButton imgBtn = (ImageButton) holder.tableVideos
							.getChildAt(i / 2).findViewById(imgBtnVidRes[i]);
					imgBtn.setBackgroundResource(vd.getiVideoCover());
					imgBtn.setTag(vd.getStrVideoLink());
					setImgBtnOnClickListener(imgBtn);
				}
				holder.tableVideos.getChildAt(2).findViewById(imgBtnVidRes[5])
						.setVisibility(ImageButton.INVISIBLE);
				holder.tableVideos.getChildAt(3)
						.setVisibility(TableLayout.GONE);

			} else if (iVideoCnt == 7) {
				ArrayList<VideoData> mVideoData = mParentTabObj
						.getTableVideos();
				for (int i = 0; i < iVideoCnt; i++) {
					VideoData vd = mVideoData.get(i);
					ImageButton imgBtn = (ImageButton) holder.tableVideos
							.getChildAt(i / 2).findViewById(imgBtnVidRes[i]);
					imgBtn.setBackgroundResource(vd.getiVideoCover());
					imgBtn.setTag(vd.getStrVideoLink());
					setImgBtnOnClickListener(imgBtn);
				}
			} else if (iVideoCnt == 1) {
				ArrayList<VideoData> mVideoData = mParentTabObj
						.getTableVideos();
				for (int i = 0; i < iVideoCnt; i++) {
					VideoData vd = mVideoData.get(i);
					ImageButton imgBtn = (ImageButton) holder.tableVideos
							.getChildAt(i / 2).findViewById(imgBtnVidRes[i]);
					imgBtn.setBackgroundResource(vd.getiVideoCover());
					imgBtn.setTag(vd.getStrVideoLink());
					setImgBtnOnClickListener(imgBtn);
				}
				holder.tableVideos.getChildAt(0).findViewById(imgBtnVidRes[1])
						.setVisibility(ImageButton.INVISIBLE);
				holder.tableVideos.getChildAt(1)
						.setVisibility(TableLayout.GONE);
				holder.tableVideos.getChildAt(2)
						.setVisibility(TableLayout.GONE);
				holder.tableVideos.getChildAt(3)
						.setVisibility(TableLayout.GONE);
			}
		}
	}

	private void setImgBtnOnClickListener(ImageButton imgBtn) {
		imgBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i("CLICK", String.valueOf(v.getTag()));
				Uri uri = Uri.parse((String) v.getTag());
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				context.startActivity(intent);
			}
		});
	}

}
