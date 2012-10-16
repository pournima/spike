package com.checkinlibrary.offers;



import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkinlibrary.R;
import com.checkinlibrary.TabGroupActivity;

public class BusinessSearchActivity extends ViewOffersActivity {
    List<BusinessResult> mList;
    private String mQuery;
    static ViewOffersActivity context;
    static ListView mListView;
    List<BusinessResult> mBusinessResults;
    String query;
    @Override
    // Set up our tabs
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
		setContentView(R.layout.business_search);
        
		context = (BusinessSearchActivity)this;
		Bundle b = getIntent().getExtras();
		
		mBusinessResults = (List<BusinessResult>) b.getSerializable("search_result");
		query = b.getString("search_query");
    }
        
    @Override
    public void onResume() {
        super.onResume();
        TextView tv = (TextView)findViewById(R.id.textViewBusinessSearchQuery);
        tv.setText(query);
        mListView= (ListView)findViewById(android.R.id.list) ;
        addBusinessActivity();
    }

    public void setQuery(String query) {
        mQuery = query;
    }
    
    public void addBusinessActivity() {
        Log.v("CHECKINFORGOOD", "Adding business search fragment");
      
        if (mBusinessResults != null) {
        	OffersListAdapter mOffersListAdapter;
			
			if (mBusinessResults.size() > 0) {
				BusinessSearchActivity mBusinessSearchActivity = (BusinessSearchActivity)context ;
				
				mOffersListAdapter = new OffersListAdapter(this ,mBusinessResults);
				mListView.setAdapter(mOffersListAdapter);
				mListView.setOnItemClickListener(itemClick);
				mBusinessSearchActivity.setQuery(query);
				loadingMore=false;		
			}
		}
        ViewOffersActivity.isSearchedCalled = false;
    }
    
    public OnItemClickListener itemClick = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			if((mBusinessResults != null) && (mBusinessResults.size() > 0)){
				mSelectedBussResult=mBusinessResults.get(position);
				if(mSelectedBussResult != null){
					showDialog(0);
					getCommonCauses();
				}
			}
		}
	};
}