package com.checkinlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class NoConnectivityScreen extends Activity {

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.no_connectivity);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();

		}
		return super.onKeyDown(keyCode, event);
	}

}
