package com.checkinlibrary.offers;

import java.util.List; 

import com.checkinlibrary.ws.services.BusinessWebService;

import android.location.Location;

public class Searcher {
    String query;
    SearchType type;
    ViewOffersActivity context;
    
    public Searcher(String query, SearchType type, ViewOffersActivity context) {
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
       } 
       //else {
        //    return causeSearch(query);            
      //  }
       return null;
    }

//    private List<CauseResult> causeSearch(String query) {
//        List<CauseResult> result = OrganizationWebService.search(query);
//        return result;
//    }

    private List<BusinessResult> businessSearch(String query) {
        Location location = context.gps.getBestLocation();
        List<BusinessResult> result = BusinessWebService.search(query, location);

        return result;    
    }
}