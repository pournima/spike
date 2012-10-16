package com.checkinlibrary.org;

import java.util.Date;

import android.content.Context;
import android.util.Log;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.db.OrganizationDbAdapter;


public class OrganizationCacheManager {
    //12 hours in milliseconds
    private static final long timeout = 12 * 60 * 60 * 1000;
    Context context;
    public OrganizationCacheManager(Context context) {
    	this.context = context;
    }
    
	public Boolean cacheExpired() {
		Long now = new Date().getTime();
		if (getCacheFromPrefs() < (now - timeout)) {
			OrganizationDbAdapter adapter = new OrganizationDbAdapter(context);
			if (adapter.getCount() > 0) {
				setCacheToPrefs(new Date().getTime());
			}
			return true;
		} else {
			return false;
		}
	}
    
    public long getCacheFromPrefs() {
    	Log.i("CHECKIN", String.valueOf(CheckinLibraryActivity.appStatus.
                              getSharedLongValue(CheckinLibraryActivity.appStatus.ALL_CAUSE_CACHE_UPDATED)));
        return CheckinLibraryActivity.appStatus.
                              getSharedLongValue(CheckinLibraryActivity.appStatus.ALL_CAUSE_CACHE_UPDATED);  
    }

    public void setCacheToPrefs(long catcheTime) {
    	CheckinLibraryActivity.appStatus.saveSharedLongValue(CheckinLibraryActivity.appStatus
    			.ALL_CAUSE_CACHE_UPDATED,catcheTime);
    }
}