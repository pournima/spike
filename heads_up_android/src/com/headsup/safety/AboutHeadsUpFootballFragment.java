package com.headsup.safety;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;

public class AboutHeadsUpFootballFragment extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.about_headsup_football, container,
				false);

		TextView videoLink = (TextView) v
				.findViewById(R.id.about_hedsup_football_video);

		videoLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {

				if (HeadsUpNativeActivity.appStatus.isOnline()) {
					Uri uri = Uri
							.parse("http://www.youtube.com/watch?v=WKJFNGjEyuY");
					Intent intent = new Intent(Intent.ACTION_VIEW, uri);
					startActivity(intent);
				}
			}
		});
		return v;

	}
	
	@Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //Don't react to gps updates when the tab isn't active.
    }

	/*@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_headsup_football);

		TextView videoLink = (TextView) findViewById(R.id.about_hedsup_football_video);

		videoLink.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=WKJFNGjEyuY");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}*/
}
