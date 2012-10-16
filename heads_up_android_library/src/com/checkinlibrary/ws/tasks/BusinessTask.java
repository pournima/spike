package com.checkinlibrary.ws.tasks;



import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.checkinlibrary.offers.BusinessResult;
import com.checkinlibrary.offers.ViewOffersActivity;
import com.checkinlibrary.ws.services.BusinessWebService;

public class BusinessTask extends AsyncTask<Double, Integer, List<BusinessResult>> {
	private ViewOffersActivity context;
	private Boolean isSupported;
	//private CauseListAdapter subContext;
	private Boolean isFromBusinessFagment;
	private int currentPageNo;

	public BusinessTask(ViewOffersActivity context, Boolean isSupported, int page) {
		this.context = context;
		this.isSupported = isSupported;
		this.currentPageNo = page;
		isFromBusinessFagment=true;
	}
	
	/*public BusinessTask(ViewOffersActivity context) {
		this.context = context;
		this.isSupported = true;
		isFromBusinessFagment=false;
		this.currentPageNo=1;
	}
*/
	

	protected List<BusinessResult> doInBackground(Double... create_params) {
		return BusinessWebService.getNearby(context, create_params, isSupported,currentPageNo);
	}

	protected void onPostExecute(List<BusinessResult> result) {
	
		if(isFromBusinessFagment) {
			//if(result != null)
				context.updateLocationView(result);
			/*else {
//				context.DisplayToast("Creating account failed!");//RR..
				context.DisplayToast("Failed to load businesses!");
				context.removeDialog(0);
			}*/
		} else{
			//subContext.updateBusIntoDB(result);
		}
		
	}
}
