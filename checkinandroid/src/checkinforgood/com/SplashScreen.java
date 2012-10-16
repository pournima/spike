package checkinforgood.com;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.account.LoginActivity;
import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.helpers.MyConstants;


public class SplashScreen extends Activity
{
	private ProgressDialog loading;
	Handler mhandler;
	AppStatus appStatus;
	private String authKey;
	boolean bIsFinished=false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);

		appStatus = AppStatus.getInstance(this);
		mhandler = new Handler();
		loading = new ProgressDialog(this);
		loading.setMessage("Loading...");
		loading.setCancelable(true);
		authKey=appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		appStatus.saveSharedStringValue(appStatus.APP_NAME, "");
		MyConstants.BASE_WEB_URL=Config.WEB_SERVICE_BASE;
		Log.i("CheckinNativeActivity","WEB BASE URL : "+ Config.WEB_SERVICE_BASE);
		startApp();
	}

	void startApp() {
		//showLoading(true, "Loading", "In Process please wait...");

		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {	

				try {
					synchronized (this) {
						wait(2000);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				if(appStatus.isOnline()) {
					Log.v("SPLASH_SCREEN", "App is online");
					if(true == appStatus.isRegistered()) {
						Log.v("SPLASH_SCREEN", "App is registered!");		
						Log.v("auth_key", authKey+ " key");
						Intent i = new Intent(SplashScreen.this,CheckinNativeActivity.class);
						i.putExtra("LOGIN_FLAG", false);
						if(!bIsFinished)
							startActivity(i);
						finish();
					} else{
						Log.v("SPLASH_SCREEN", "You are not registered!!!!");
						Intent intent_login = new Intent(SplashScreen.this, LoginActivity.class);
						if(!bIsFinished)
							startActivityForResult(intent_login, 1);   
						//finish();
					}
				} else{
					Intent intent= new Intent(SplashScreen.this,NoConnectivityScreen.class);
					startActivity(intent);
					Log.v("SPLASH_SCREEN", "You are not online!!!!");
					//message("Please check you internet connection!!");
					finish();
				}
				//showLoading(false, "", "");
			}
		});
		t.start();
	}
	
	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == 1) {
			//SignupActivity.bIsFromAcctManager = false;
			final Intent homeIntent = new Intent(this,
					CheckinNativeActivity.class);
			homeIntent.putExtra("LOGIN_FLAG", true);
			startActivity(homeIntent);
			finish();
		} else if (resultCode == 222 || (requestCode==101 && resultCode==0)) {
			//ShowMessageBox();
			finish();
		}
	}
	
	void showLoading(final boolean show,final String title,final String msg)
	{
		mhandler.post(new Runnable() {
			@Override
			public void run() {
				if (show) {
					if (loading != null)
					{
						loading.setTitle(title);
						loading.setMessage(msg);
						loading.show();
					}
				} else {
					loading.cancel();
					loading.dismiss();
				}

			}
		});
	}
	void message(String msg) 
	{
		final String mesage = msg;
		mhandler.post(new Runnable() 
		{
			@Override
			public void run() {
				Toast toast = Toast.makeText(SplashScreen.this, mesage, 8000);
				toast.show();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			bIsFinished=true;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
