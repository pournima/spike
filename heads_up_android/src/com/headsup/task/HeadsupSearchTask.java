package com.headsup.task;

import java.util.List;

import android.os.AsyncTask;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.Searcher.SearchType;
import com.headsup.HeadsupSearcher;

public class HeadsupSearchTask extends AsyncTask<String, Integer, List<? extends Object>> {
    private CheckinLibraryActivity context;
    private SearchType type;
    private String query;

    public HeadsupSearchTask(CheckinLibraryActivity context, SearchType type) {
        this.context = context;
        this.type = type;
    }

    protected void onPostExecute(List<? extends Object> result) {
        context.onSearchCompleted(result, type, query);
    }

    protected List<? extends Object> doInBackground(String... params) {
        query = params[0];
        HeadsupSearcher search = new HeadsupSearcher(query, type, context);
        List<? extends Object> result = search.search();
        return result;
    }

}
