package com.headsup;

import java.util.List;

import android.location.Location;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.Searcher;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.ws.services.BusinessWebService;
import com.headsup.db.SignalsDbAdapter;
import com.headsup.models.SignalsResult.SignalCategory;

public class HeadsupSearcher extends Searcher{

	public HeadsupSearcher(String query, SearchType type,
			CheckinLibraryActivity context) {
		super(query, type, context);
		
	}
	public List<? extends Object> search() {
        if ( type == Searcher.SearchType.BUSINESS ) {
            return businessSearch(query);
        } if ( type == Searcher.SearchType.SIGNALS ) {
            return signalSearch(query);
        }
		return null; 
    }
    private List<BusinessResult> businessSearch(String query) {
        Location location = context.gps.getBestLocation();
        List<BusinessResult> result = BusinessWebService.search(query, location);

        return result;    
    }
    
    private List<SignalCategory> signalSearch(String query) {

        List<SignalCategory> result=null;

        SignalsDbAdapter signalsAdpt = new SignalsDbAdapter(context);
		
        result = signalsAdpt.getSearchList(query);
			
        return result;    
    }
}
