package com.campaignslibrary.tasks;

import java.util.List;

import android.os.AsyncTask;

import com.campaignslibrary.helpers.CampaignSearcher;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.Searcher.SearchType;

public class CampaignSearchTask extends AsyncTask<String, Integer, List<? extends Object>> {
    private CheckinLibraryActivity context;
    private SearchType type;
    private String query;

    public CampaignSearchTask(CheckinLibraryActivity context, SearchType type) {
        this.context = context;
        this.type = type;
    }

    protected void onPostExecute(List<? extends Object> result) {
        context.onSearchCompleted(result, type, query);
    }

    protected List<? extends Object> doInBackground(String... params) {
        query = params[0];
        CampaignSearcher search = new CampaignSearcher(query, type, context);
        List<? extends Object> result = search.search();
        return result;
    }

}
