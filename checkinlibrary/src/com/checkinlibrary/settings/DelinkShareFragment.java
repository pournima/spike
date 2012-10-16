package com.checkinlibrary.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.R;
import com.checkinlibrary.ws.tasks.SocialSharingTask;


@SuppressWarnings("unused")
public class DelinkShareFragment extends Fragment  {

	CheckinLibraryActivity context;
    ShareFragment mShareFragment;
    
    public static final String  STATUS_FLAG= "FB_OT_TW";
    
    Button btnDlinkAcc;
    TextView txtListText;
    
    static int FACEBOOK=0;
	static int TWITTER=1;
	int iFbOrTwitter;
   
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context=(CheckinLibraryActivity)this.getActivity();
        View v = inflater.inflate(R.layout.delink_share_fragment, container, false);  
        
        btnDlinkAcc=(Button)v.findViewById(R.id.deLinkButton);
        
        btnDlinkAcc.setOnClickListener(new View.OnClickListener() 
        {
        	public void onClick(View v) {

        		if (CheckinLibraryActivity.appStatus.isOnline()) {
        			if(iFbOrTwitter == FACEBOOK){
        				onClearFbBtnClicked(v);
        			}else if(iFbOrTwitter == TWITTER){
        				onClearTwBtnClicked(v);
        			}
        		} else {
        			Intent intent = new Intent(context, NoConnectivityScreen.class);
        			context.startActivity(intent);
        			context.finish();
        			Log.v("DelinkShareFragment", "App is not online!");
        		}
        	}
        });

        txtListText=(TextView)v.findViewById(R.id.testViewDelinkName);
        iFbOrTwitter=getArguments().getInt(DelinkShareFragment.STATUS_FLAG,0);
       

        if(iFbOrTwitter == FACEBOOK){
        	txtListText.setText("Facebook");
        }else if(iFbOrTwitter == TWITTER){
        	txtListText.setText("Twitter");
        }

        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
        ShareFragment.bIsFromDelink = true;

    }


    @Override
    public void onPause() {
        super.onPause();
        //Don't react to gps updates when the tab isn't active.
    }
    
    private void onClearFbBtnClicked(View view){
    	Log.i("FB", "Clicked");
    	
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_TOKEN);
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_ON);
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity.appStatus.FACEBOOK_PERMISSIONS_ON);
		
		postSocialPreferences("facebook");
    	
    }
    
    private void onClearTwBtnClicked(View view){
    	Log.i("TW", "Clicked");
    	
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity
				.appStatus.TWITTER_TOKEN);
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity
				.appStatus.TWITTER_SECRET);
    	CheckinLibraryActivity.appStatus.clearSharedDataWithKey(CheckinLibraryActivity
				.appStatus.TWITTER_ON);
		
		postSocialPreferences("twitter");
    }
    
    private void postSocialPreferences(String social_network) {
		String args[]=new String[6];
		args[0]=CheckinLibraryActivity.mAuthToken;
		args[1]=social_network;//"facebook"; "twitter"
		args[2]=Boolean.toString(false);
		args[3]=null;
		args[4]=null;
		args[5]=CheckinLibraryActivity.appStatus.getSharedStringValue(CheckinLibraryActivity.appStatus.APP_NAME);
		
		CheckinLibraryActivity.strProgressMessage="Delinking account...";			
		
		if (CheckinLibraryActivity.appStatus.isOnline()) {
			CheckinLibraryActivity.strProgressMessage=getString(R.string.delinkProgressMessage);
			context.showDialog(0);
			new SocialSharingTask(this,SocialSharingTask.POST_PREF).execute(args);
		} else {
			Intent intent = new Intent(context, NoConnectivityScreen.class);
			context.startActivity(intent);
			context.finish();
			Log.v("DelinkShareFragment", "App is not online!");
		}
	}
    
    public void onAuthenticationResult(Boolean success) {
		if ( success ) {
			context.removeDialog(0);
			DisplayToast("Preferences cleared!");
			
		} else {
			DisplayToast("Fail to clear preferences!");
		}
		
		context.popFragmentFromStack();
	}
    private void DisplayToast(String msg) {
		Toast.makeText(context, msg,Toast.LENGTH_SHORT).show();
		context.removeDialog(0);
	} 
}