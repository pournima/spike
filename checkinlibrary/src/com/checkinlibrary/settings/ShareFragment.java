package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;

public class ShareFragment extends ListFragment {

	CheckinLibraryActivity context;
	EditText editTextDelayedCheckin;
	String delayedCheckin;
	public static StringBuilder delayedCheckinHrs;
	public static boolean bIsFromDelink = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (CheckinLibraryActivity) this.getActivity();
		View v = inflater.inflate(R.layout.share_fragment, container, false);

		return v;

	}

	@Override
	public void onResume() {
		super.onResume();

		Log.e("CHECKINFORGOOD", "Setting the list adapters ");

		ArrayList<String> lst = new ArrayList<String>();
		lst.add("facebook");
		lst.add("twitter");

		editTextDelayedCheckin = (EditText) context
				.findViewById(R.id.editText_delayed_hrs);
		SeekBar seekBar = (SeekBar) context
				.findViewById(R.id.seekbar_delayed_checkin);

		seekBar.setMax(24);
		if (CheckinLibraryActivity.appStatus.getSharedLongValue("DELAY_TIME") != null) {
			if (bIsFromDelink) {
				seekBar.setProgress(0);
				bIsFromDelink = false;
			}
			seekBar.setProgress(CheckinLibraryActivity.appStatus
					.getSharedLongValue("DELAY_TIME").intValue());

			delayedCheckinHrs = new StringBuilder();
			delayedCheckinHrs.append(String.valueOf(seekBar.getProgress()));

			if (seekBar.getProgress() > 1) {
				delayedCheckinHrs.append(" hrs");
			} else {
				delayedCheckinHrs.append(" hr");
			}

			editTextDelayedCheckin.setText(delayedCheckinHrs);

		}

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			};

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				CheckinLibraryActivity.appStatus.saveSharedLongValue(
						"DELAY_TIME", Long.valueOf(progress));
				Log.v("ShareFragment", "delayTime: " + progress);

				delayedCheckinHrs = new StringBuilder();
				delayedCheckinHrs.append(String.valueOf(seekBar.getProgress()));

				if (progress > 1) {
					delayedCheckinHrs.append(" hrs");
				} else {
					delayedCheckinHrs.append(" hr");
				}
				editTextDelayedCheckin.setText(delayedCheckinHrs);

			}
		});

		ShareListAdapter adapter = new ShareListAdapter(this.getActivity(),
				lst, context);
		this.setListAdapter((ListAdapter) adapter);
	}

	@Override
	public void onPause() {
		super.onPause();
		// Don't react to gps updates when the tab isn't active.
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}