package com.headsup.rules.signals;

import java.util.List;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.checkinlibrary.imageloader.ImageLoader;
import com.headsup.R;
import com.headsup.helpers.Constants;
import com.headsup.models.SignalsResult.SignalCategory;

public class SignalsImageAdapter extends BaseAdapter {
	private Context mContext;
	private ImageLoader mImageLoader;
	List<SignalCategory> mSignalList;
	Boolean typeConstructor;

	
	public SignalsImageAdapter(Context context, List<SignalCategory> result) {
		mContext = context;
		mSignalList = result;
		//typeConstructor=false;
	}
	
	@Override
	public int getCount() {
		return mSignalList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout v = (LinearLayout) inflater.inflate(R.layout.official_signal_grid_item, parent,false);
		mImageLoader = new ImageLoader(mContext);
		initializeAllObjects(v,position);
		return v;
	    }
	
	 private void initializeAllObjects(LinearLayout v, int position) {
		ImageView imgViewSignal = (ImageView)v.findViewById(R.id.imgViewSignalCatagory);
		TextView txtSignal = (TextView)v.findViewById(R.id.txtSignalCatagory);
		mImageLoader.DisplayImage(Constants.BASE_IMAGE_URL + mSignalList.get(position).image_thumbnail, imgViewSignal);	
		imgViewSignal.setScaleType(ScaleType.FIT_XY);
		imgViewSignal.setPadding(0, 15, 0, 0);
		//imgViewSignal.setBackgroundResource(mSignalsResult.getCatagories().get(position).image_thumbnail);
		txtSignal.setEllipsize(TruncateAt.MARQUEE);
		txtSignal.setText(mSignalList.get(position).getText_heading());	
		
	}

}
