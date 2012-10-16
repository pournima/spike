/*package com.checkinlibrary;



import java.util.List;

import android.content.Context;
import android.location.Location;

import com.checkinlibrary.offers.BusinessResult;
import com.checkinlibrary.orgs.CauseResult;
import com.checkinlibrary.ws.services.BusinessWebService;

public class Searcher {
    String query;
    SearchType type;
    Context context;
    
    public Searcher(String query, SearchType type, Context context) {
        this.query = query;
        this.type = type;
        this.context = context;
    }
    
    public static enum SearchType {
        BUSINESS, CAUSE, NONE_FOUND 
    }
    
    public List<? extends Object> search() {
        if ( type == Searcher.SearchType.BUSINESS ) {
            return businessSearch(query);
        } else {
           // return causeSearch(query);   
        	return null;
        }
    }

    private List<CauseResult> causeSearch(String query) {
        List<CauseResult> result = OrganizationWebService.search(query);
        return result;
    }

    private List<BusinessResult> businessSearch(String query) {
        Location location = context.gps.getBestLocation();
        List<BusinessResult> result = BusinessWebService.search(query, location);

        return result;    
    }
}*/