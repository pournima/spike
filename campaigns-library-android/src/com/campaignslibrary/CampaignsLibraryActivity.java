package com.campaignslibrary;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

public class CampaignsLibraryActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campaigns_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.campaigns_main, menu);
        return true;
    }

    
}
