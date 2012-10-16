package checkinforgood.com;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.campaignslibrary.MyCampaignFragment;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.settings.HowItWorksFragment;
import com.checkinlibrary.settings.HtmlPageFragment;
import com.checkinlibrary.settings.MyGoodFragment;
import com.checkinlibrary.settings.SettingFragment;
import com.checkinlibrary.settings.SettingsListAdapter;
import com.checkinlibrary.settings.ShareFragment;


public class CheckinSettingFragment extends SettingFragment {

	MyCampaignFragment mMyCampaignFragment;

	final int SHARE_FRG = 0;
	final int MY_CAMP_FRG = 1;
	final int MY_GOOD = 2;
	final int HOW_IT_WORKS_FRG = 3;
	final int CONTACTS_FRG = 4;
	final int PRIPOL_FRG = 5;
	final int TERMS_FRG = 6;
	
	int iCampgnId;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {

		switch (position) {
		case SHARE_FRG:
			onShareSettingsClick();
			break;
		case MY_CAMP_FRG:
			onMyCampaignsClick();
			break;
		case MY_GOOD:
			onMyGoodClicked();
			break;
		case HOW_IT_WORKS_FRG:
			onHowItWorksSettingsClick();
			break;
		case CONTACTS_FRG:
			itemPosition = CONTACTS_FRG;
			onHtmlPageClick();
			break;
		case PRIPOL_FRG:
			itemPosition = PRIPOL_FRG;
			onHtmlPageClick();
			break;
		case TERMS_FRG:
			itemPosition = TERMS_FRG;
			onHtmlPageClick();
			break;
		}
	}

	private void onMyCampaignsClick() {
		mMyCampaignFragment = null;
		if (mMyCampaignFragment == null) {
			mMyCampaignFragment = (MyCampaignFragment) ((CheckinNativeActivity) context)
					.addFragment(R.id.linearLayout2, MyCampaignFragment.class.getName(),
							"my_campaign_fragment", new Bundle());
		} else {
			((CheckinNativeActivity) context).attachFragment((Fragment) mMyCampaignFragment);
		}
	}
	
	@Override
	public void initializeCauseTypeList() {

		settingItems = new ArrayList<String>();
		settingItems.add("Sharing Settings");
		settingItems.add("My Campaigns");
		settingItems.add("My Good");
		settingItems.add("How Check-in for Good Works");
		settingItems.add("Support & Contact Us");
		settingItems.add("Privacy Policy");
		settingItems.add("Terms Of Service");

		// --------------Extract the username -----------
		String mUserName = CheckinLibraryActivity.appStatus
				.getSharedStringValue(CheckinLibraryActivity.appStatus.USER_NAME);
		if (mUserName != null) {
			Log.i("ACCOUNT", mUserName);
			settingItems.add(mUserName);
		}
	}
}