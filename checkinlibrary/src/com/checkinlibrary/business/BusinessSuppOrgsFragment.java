package com.checkinlibrary.business;

import java.io.Serializable;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.models.BusinessResult;
import com.checkinlibrary.models.OrganizationResult;


@SuppressWarnings("unused")
public class BusinessSuppOrgsFragment extends ListFragment  {

	CheckinLibraryActivity context;
    List<OrganizationResult> mCommonCauses;
    BusinessCheckinFragment mBusinessCheckinFragment;
    BusinessResult mBusinessResult;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=(CheckinLibraryActivity)this.getActivity();
        View v = inflater.inflate(R.layout.business_supp_orgs_fragment, container, false);  
        mCommonCauses=(List<OrganizationResult>) this.getArguments().getSerializable("common_causes");    
        mBusinessResult=(BusinessResult) this.getArguments().getSerializable("selected_business");
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        mCommonCauses=(List<OrganizationResult>) this.getArguments().getSerializable("common_causes");
        mBusinessResult=(BusinessResult) this.getArguments().getSerializable("selected_business");
        BusinessSuppOrgsListAdapter adapter = new BusinessSuppOrgsListAdapter(context, mCommonCauses);
        this.setListAdapter((ListAdapter) adapter);
    }
    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Go to checkin screen
        OrganizationResult mSelectedOrganizationResult=mCommonCauses.get(position);
        
        if(mSelectedOrganizationResult != null) {
            Log.i("BusinessSuppOrganization list clicked", mSelectedOrganizationResult.getOrganization().getName());
            gotoCheckinPage(mSelectedOrganizationResult);
        }
    }
    void gotoCheckinPage(OrganizationResult mOrganizationResult) {
            Log.i("gotoCheckinPage OrgId", String.valueOf(mOrganizationResult.getOrganization().getId()));
            Log.i("gotoCheckinPage BusId", String.valueOf(mBusinessResult.getId()));
            
                if (mBusinessCheckinFragment == null) {
                    Bundle bundle = new Bundle();                   
                    bundle.putSerializable("selected_cause", (Serializable) mOrganizationResult);
                    bundle.putSerializable("selected_business", (Serializable) mBusinessResult);
                
                    mBusinessCheckinFragment = (BusinessCheckinFragment) context.addFragment(R.id.linearLayout2,
                            BusinessCheckinFragment.class.getName(), "checkin_fragment", bundle);
                } else {
                    (context).attachFragment((Fragment) mBusinessCheckinFragment,"checkin_fragment");
                }
    }

    @Override
    public void onPause() {
        super.onPause();
        //Don't react to gps updates when the tab isn't active.
    }

}