package com.headsup.settings;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.checkinlibrary.R;

public class AboutUsFragment extends Fragment {

	ImageView imgVideo;
	GestureDetector gestureDetector;

	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		initializeAboutData();
	}
*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.about, null);
		initializeAboutData(v);
		return v;
	}
	private void initializeAboutData(View v) {

		imgVideo = (ImageView)v.findViewById(R.id.imgVideo);

		imgVideo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=WKJFNGjEyuY");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
}
