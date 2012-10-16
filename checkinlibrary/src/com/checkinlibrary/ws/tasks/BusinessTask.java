package com.checkinlibrary.ws.tasks;

import java.util.List;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.business.BusinessFragment;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.org.CauseProfileFragment;
import com.checkinlibrary.ws.services.BusinessWebService;


public class BusinessTask extends AsyncTask<Double, Integer, List<BusinessResult>> {
	
	private CheckinLibraryActivity context;
	private Boolean isSupported;
	private CauseProfileFragment subContext;
	private Boolean isFromBusinessFagment;
	private int currentPageNo;

	public BusinessTask(CheckinLibraryActivity context, Boolean isSupported, int page) {
		this.context = context;
		this.isSupported = isSupported;
		this.currentPageNo = page;
		isFromBusinessFagment=true;
	}
	
	public BusinessTask(CauseProfileFragment context) {
		this.subContext = context;
		this.isSupported = true;
		isFromBusinessFagment=false;
		this.currentPageNo=1;
	}
	
	protected List<BusinessResult> doInBackground(Double... create_params) {
		if(isFromBusinessFagment){
			Fragment fragment= context.getLastFragment();
			if(fragment.getTag() == "business_tab"){
				return BusinessWebService.getNearby(context, create_params, isSupported,currentPageNo);
			}else{
				BusinessFragment businessFragment = (BusinessFragment) context
						.getSupportFragmentManager().findFragmentByTag("business_tab");
				if(businessFragment != null)
					businessFragment.hideProgress();
				return null;
			}
		}else {
			return BusinessWebService.getNearby(context, create_params, isSupported,currentPageNo);
		}
	}

	protected void onPostExecute(List<BusinessResult> result) {

		if(isFromBusinessFagment) {
			BusinessFragment fragment = (BusinessFragment) context
					.getSupportFragmentManager().findFragmentByTag("business_tab");

			if(!context.isOnPause) {
				if (fragment != null) {
					if (fragment.getView() != null) {
						fragment.updateLocationView(result);
					}
				}
			}else{
				if (fragment != null) 
					fragment.hideProgress();
			}
		} else{
			
			subContext.updateBusIntoDB(result);
			
		}
	}
}
