package com.checkinlibrary.org;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.models.OrganizationResult;
import com.checkinlibrary.ws.services.OrganizationWebService;


public class CauseListAdapter extends BaseAdapter implements Filterable {
	Context context;
	List<OrganizationResult> causes;
	CheckinLibraryActivity main_context;
	CauseProfileFragment mCausesProfileFragment;

	public CauseListAdapter(Context context, List<OrganizationResult> causes) {
		this.context = context;
		this.causes = causes;
		main_context=(CheckinLibraryActivity)context;
	}

	@Override
	public int getCount() {
		if (causes != null)
			return causes.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int index) {
		return causes.get(index);
	}

	@Override
	public long getItemId(int position) {
		return causes.get(position).getOrganization().getId();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout v = (RelativeLayout) inflater.inflate(R.layout.cause_list_item, parent, false);
		TextView tv = (TextView) v.findViewById(R.id.allCauseName);
		final OrganizationResult result = (OrganizationResult) getItem(position);
		tv.setText(result.getOrganization().getName());

		final ImageView imageView;
		imageView=(ImageView)v.findViewById(R.id.imagecheckBox);  
		
		updateSupportedImage(imageView, result.getOrganization().getSupported());
		setSupportedClickHandler(imageView, !result.getOrganization().getSupported(), position);

		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				itemClick(imageView, !result.getOrganization().getSupported(), position);	
			}
			
		});
		return v;
	}

	private void setSupportedClickHandler(ImageView view, Boolean isSupported, Integer position) {
		final int closurePosition = position; //Ah, Java
		final Boolean closureSupported = isSupported;

		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				itemClick((ImageView) view,closureSupported, closurePosition);
			}
		});        
	}

	private void itemClick(ImageView view, Boolean isSupported, Integer position) {	
		OrganizationResult mOrganizationResult=(OrganizationResult)getItem(position);
		if (mCausesProfileFragment == null) {
			Bundle bundle = new Bundle();
			bundle.putSerializable("selected_cause",(Serializable) mOrganizationResult);

			mCausesProfileFragment = (CauseProfileFragment) main_context.addFragment(
					R.id.linearLayout2, CauseProfileFragment.class.getName(),
					"cause_profile_fragment", bundle);
		} else {
			(main_context).attachFragment((Fragment) mCausesProfileFragment);
		}
	}
	
	private void updateSupportedImage(ImageView view, Boolean isSupported) {
		//Toggle our image
		if ( isSupported ) {
			view.setImageResource(R.drawable.btn_check_on);                
		} else {
			view.setImageResource(R.drawable.btn_check_off);
		}        
	}

	@Override
	public Filter getFilter() {
		return new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				Log.d("CHECKINFORGOOD", "**** PUBLISHING RESULTS for: " + constraint);

				for (OrganizationResult cause : (List<OrganizationResult>) results.values) {
					Log.e("CHECKINFORGOOD", "PUBLISHING RESULT VALUES: " + cause.getOrganization().getName());
				}

				CauseListAdapter.this.causes = (List<OrganizationResult>) results.values;
				CauseListAdapter.this.notifyDataSetChanged();
			}

			@Override
			protected FilterResults performFiltering(CharSequence constraint) {
				Log.d("CHECKINFORGOOD", "**** PERFORM FILTERING for: " + constraint);
				List<OrganizationResult> filteredResults = OrganizationWebService.search(constraint);

				FilterResults results = new FilterResults();
				results.values = filteredResults;

				return results;
			}
		};
	}
}
