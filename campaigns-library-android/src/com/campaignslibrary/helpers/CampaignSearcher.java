package com.campaignslibrary.helpers;

import java.util.List;

import com.campaignslibrary.models.CreateCampaignResult;
import com.campaignslibrary.ws.services.CampaignWebService;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.Searcher;
import com.checkinlibrary.helpers.AppStatus;

public class CampaignSearcher extends Searcher{
	 public static AppStatus appStatus;
	    
	public CampaignSearcher(String query, SearchType type,
			CheckinLibraryActivity context) {
		super(query, type, context);
		 appStatus = AppStatus.getInstance(context);
	}
	public List<? extends Object> search() {
        if ( type == Searcher.SearchType.CAMPAIGN ) {
            return campaignSearch(query);
        }
		return null; 
    }
    
	private List<CreateCampaignResult> campaignSearch(String query) {
		List<CreateCampaignResult> listResult;
		String apiKey = appStatus.getSharedStringValue(appStatus.AUTH_KEY);
		listResult = CampaignWebService.searchCampaign(apiKey, query);
		return listResult;
	}
}
