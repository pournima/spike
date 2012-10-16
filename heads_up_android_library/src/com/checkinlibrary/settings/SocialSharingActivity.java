package com.checkinlibrary.settings;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.checkinlibrary.R;
import com.checkinlibrary.helpers.AppStatus;

public class SocialSharingActivity extends Activity {
	
	Long delayTime;
    EditText editTextDelayedCheckin;
    String delayedCheckin;
    AppStatus appStatus;
    ListView mListView;
    ShareListAdapter mShareListAdapter;
    public static StringBuilder delayedCheckinHrs;
    public static boolean bIsFromFBTwitter = false;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.social_share);
		
	}
	
	  @Override
	    public void onResume() {
	        super.onResume();
	        
	        Log.e("CHECKINFORGOOD", "Setting the list adapters ");
	        mListView =(ListView)findViewById(android.R.id.list);
	        ArrayList<String> lst = new ArrayList<String>();
	        lst.add("Facebook");
	        lst.add("Twitter");
	        mShareListAdapter =new ShareListAdapter(SocialSharingActivity.this,lst,this);	       
	        
	        editTextDelayedCheckin = (EditText)findViewById(R.id.editText_delayed_hrs);
	        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar_delayed_checkin);
	        seekBar.setMax(24);
	        if(AppStatus.getInstance(this).getSharedLongValue("DELAY_TIME")!=null){
	        	
	        	seekBar.setProgress(AppStatus.getInstance(this).getSharedLongValue("DELAY_TIME").intValue());
	        	
	        	delayedCheckinHrs = new StringBuilder();
	        	delayedCheckinHrs.append(String.valueOf(seekBar.getProgress()));
	        	
	        	if(seekBar.getProgress() >1){
					delayedCheckinHrs.append(" hrs");
				}else{
					delayedCheckinHrs.append(" hr");
				}
	        	
	        	editTextDelayedCheckin.setText(delayedCheckinHrs);
	        	        	
	        }
	        delayTime = new Long(seekBar.getProgress());
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
					AppStatus.getInstance(getParent()).saveSharedLongValue("DELAY_TIME",Long.valueOf(progress));
					Log.v("ShareFragment", "delayTime: " +progress);
					
					
					delayedCheckinHrs = new StringBuilder();
					delayedCheckinHrs.append(String.valueOf(seekBar.getProgress()));
					
					if(progress >1){
						delayedCheckinHrs.append(" hrs");
					}else{
						delayedCheckinHrs.append(" hr");
					}
					editTextDelayedCheckin.setText(delayedCheckinHrs);
					
				}
			});
	        
	        mListView.setAdapter(mShareListAdapter);
	        
	    }


	    @Override
	    public void onPause() {
	        super.onPause();
	        //Don't react to gps updates when the tab isn't active.
	    }
	    
	    @Override
	    protected void onStop() {
	    	// TODO Auto-generated method stub
	    	super.onStop();
	    }
	    
	    @Override
	    protected void onDestroy() {
	    	// TODO Auto-generated method stub
	    	super.onDestroy();
	    }
	    
	    @Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    	// TODO Auto-generated method stub
	    	super.onActivityResult(requestCode, resultCode, data);
	    	Log.i("SocialSharingActivity", "in on activity result");
	    }

}
