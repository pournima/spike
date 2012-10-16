package com.checkinlibrary;

import java.util.List;

import android.location.Location;

import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.ws.services.BusinessWebService;
import com.checkinlibrary.ws.services.OrganizationWebService;


public class Searcher {
    public String query;
    public SearchType type;
    public CheckinLibraryActivity context;
    
    public Searcher(String query, SearchType type, CheckinLibraryActivity context) {
        this.query = query;
        this.type = type;
        this.context = context;
    }
    
    public static enum SearchType {
        BUSINESS, CAUSE,SIGNALS,CAMPAIGN, NONE_FOUND 
    }
    
    public List<? extends Object> search() {
        if ( type == Searcher.SearchType.BUSINESS ) {
            return businessSearch(query);
        } if ( type == Searcher.SearchType.SIGNALS ) {
            return businessSearch(query);
        } else {
            return causeSearch(query);            
        }
    }

    private List<OrganizationResult> causeSearch(String query) {
        List<OrganizationResult> result = OrganizationWebService.search(query);
        return result;
    }

    private List<BusinessResult> businessSearch(String query) {
        Location location = context.gps.getBestLocation();
        List<BusinessResult> result = BusinessWebService.search(query, location);

        return result;    
    }
}