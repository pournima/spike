package com.headsup.safety;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.headsup.R;

public class HowToTackleFragment extends Fragment {

	ImageView imgHowToTackle;
	ImageView imgTeachingTackling;
	ImageView imgBreakingPosition;
	ImageView imgHitPosition;
	ImageView imgBuzzToHitDrill;
	ImageView imgAirBuddyDrill;
	ImageView imgRipTackleDrill;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.how_to_tackle, container,false);
	
		initializeHowToTackleVideos(v);
		return v;
	}

	private void initializeHowToTackleVideos(View v) {
		// TODO Auto-generated method stub
		imgHowToTackle = (ImageView) v.findViewById(R.id.imgHowToTackle);
		imgHowToTackle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=KExA8grY1NE");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgTeachingTackling = (ImageView) v.findViewById(R.id.imgTeachingTackling);
		imgTeachingTackling.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=YfIqqKFfINY&feature=youtu.be");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgBreakingPosition = (ImageView) v.findViewById(R.id.imgBreakdownPosition);
		imgBreakingPosition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=SQaBJ-1IGtw&feature=plcp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgHitPosition = (ImageView) v.findViewById(R.id.imgTackleHitPosition);
		imgHitPosition.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=scoLo7xZWwc&feature=plcp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgBuzzToHitDrill = (ImageView) v.findViewById(R.id.imgBuzzToHitDrill);
		imgBuzzToHitDrill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=mmrnuVcgpfo&feature=plcp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgAirBuddyDrill = (ImageView) v.findViewById(R.id.imgAirBuddyDrill);
		imgAirBuddyDrill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=67bucnKrONg&feature=plcp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});

		imgRipTackleDrill = (ImageView) v.findViewById(R.id.imgRipTackleDrill);
		imgRipTackleDrill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri
						.parse("http://www.youtube.com/watch?v=Wdq4ZupQQes&feature=plcp");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);
			}
		});
	}
}
