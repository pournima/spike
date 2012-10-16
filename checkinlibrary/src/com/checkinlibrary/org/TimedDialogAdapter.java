package com.checkinlibrary.org;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

public class TimedDialogAdapter {
    private Handler handler;
    private TimedDialogRunnable runnable;
    private FragmentActivity context;
    private TimedDialog dialogFragment;
    
    public TimedDialogAdapter(TimedDialogRunnable runnable, FragmentActivity context) {
        this.runnable = runnable;
        runnable.setDialogAdapter(this);
        this.context = context;
    }
    
    public void showDialog(long secondsDisplayed, String title) {
        //Custom handler dismisses the dialog when called.
        handler = new Handler();
        handler.removeCallbacks((Runnable)runnable);
        handler.postDelayed((Runnable)runnable, secondsDisplayed*1000);
        showDialogFragment(title);
    }
    
    private void showDialogFragment(String title) {
        FragmentTransaction ft = context.getSupportFragmentManager().beginTransaction();
        Fragment prev = context.getSupportFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
            ft.remove(prev);
        }
            
        ft.addToBackStack(null);

        dialogFragment = TimedDialog.newInstance(title);
        dialogFragment.show(ft, "dialog");
        Log.v("CHECKINFORGOOD", "Show the dialog");
    }
    
    public Boolean canSee() {
        return this.dialogFragment.isVisible();
    }
    
    public void stopDialogAndItsCallback() {
        this.stopDialog();
        handler.removeCallbacks((Runnable)runnable);
    }
    
    private void stopDialog() {
    	Log.v("CHECKINFORGOOD", "Dialog dismissed");
    	try{
    		if(dialogFragment!=null){
    		dialogFragment.dismiss();
    		}
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}