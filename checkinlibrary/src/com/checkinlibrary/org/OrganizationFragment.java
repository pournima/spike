package com.checkinlibrary.org;

import java.util.ArrayList;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.settings.HowItWorksFragment;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;

public class OrganizationFragment extends ListFragment {
	CheckinLibraryActivity context;
    CauseFragment mMyCausesFragment;
    CauseFragment mAllCausesFragment;
    ArrayList<String> organizationItems;
    HowItWorksFragment mHowItWorksFragment;
    private TextView textViewLink;

    final int MY_CAUSES=0;
    final int ALL_CAUSES=1;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
        initializeCauseTypeList();
        OrganizationListAdapter adapter = new OrganizationListAdapter(
                ((Context) this.getActivity()), organizationItems);
        this.setListAdapter((ListAdapter) adapter);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        context=(CheckinLibraryActivity)this.getActivity();
        initializeCauseTypeList();
        View fragmentView = inflater.inflate(
                R.layout.organization_fragment , null);
        initializeOnClickHandlers(fragmentView);
        return fragmentView;
    }

    public void onAllCausesClick(View v) {
        if (mAllCausesFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("supported", false);
            mAllCausesFragment = (CauseFragment) ((CheckinLibraryActivity) context).addFragment(R.id.linearLayout2,
                    CauseFragment.class.getName(), "all_cause_fragment", bundle);
        }  else {
            ((CheckinLibraryActivity) this.context).attachFragment((Fragment) mAllCausesFragment);
        }
    }

    public void onMyCausesClick(View v) {
        if (mMyCausesFragment == null) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("supported", true);
            mMyCausesFragment = (CauseFragment) ((CheckinLibraryActivity) context).addFragment(R.id.linearLayout2,
                    CauseFragment.class.getName(), "my_cause_fragment", bundle);
        } else {
            ((CheckinLibraryActivity) this.context).attachFragment((Fragment) mMyCausesFragment);
        }
    }    

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if(position == ALL_CAUSES) {
            onAllCausesClick(v);
        } else if(position == MY_CAUSES) {
            onMyCausesClick(v);
        }
    }

    private void goToHowItWorksFragment() {
        if (mHowItWorksFragment == null) {
            mHowItWorksFragment = (HowItWorksFragment) (context).addFragment(R.id.linearLayout2,
                    HowItWorksFragment.class.getName(), "how_it_works_fragment");    
        } else {
            (context).attachFragment((Fragment) mHowItWorksFragment);
        }
    }

    private void initializeCauseTypeList()
    {
        organizationItems =new ArrayList<String>();
        organizationItems.add("      Causes I'm supporting");
        organizationItems.add("      All Causes");
    }

    private void initializeOnClickHandlers(View v)
    {
        textViewLink=(TextView)v.findViewById(R.id.textViewLink);

        String strLink="<u><font color=\"#f38121\" >"+"Learn how Check-in for Good works"+"</font></u>";
        textViewLink.setText(Html.fromHtml(strLink));

        textViewLink.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                goToHowItWorksFragment();
            }
        });
    }

}