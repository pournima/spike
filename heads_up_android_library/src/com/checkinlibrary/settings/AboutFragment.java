package com.checkinlibrary.settings;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.checkinlibrary.R;

public class AboutFragment extends Activity {

	ImageView imgVideo;
	GestureDetector gestureDetector;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);

		initializeAboutData();
	}

	private void initializeAboutData() {

		imgVideo = (ImageView) findViewById(R.id.imgVideo);

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
