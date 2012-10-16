package com.headsup.rules;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.rules.signals.SignalsFragment;

public class RuleBookFragment extends Fragment {
	ListView listViewRules;
	RuleBookListAdapter mRuleBookListAdapter;
	TextView mTextViewHeader;
	public static boolean bIsFromNFLRuleBook = false;
	String[] ruleBookDataString = { "NFL FLAG Rules", "Officials Signals", "Youth Football Rulebook" };

	public static final int NFL_RULE_BOOK = 0;
	public static final int OFFICIAL_SIGNALS = 1;
	public static final int TACKLE_RULE_BOOK = 2;

	ImageView btnRuleBook;
	HeadsUpNativeActivity context;

	NFLRuleBookFragment mNFLRuleBookFragment;
	SignalsFragment mRulesSignalsFragment;

	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (HeadsUpNativeActivity) this.getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.rules, container, false);
		setupButtons(v);
		return v;
	}

	private void setupButtons(View v) {      
		listViewRules = (ListView)v.findViewById(android.R.id.list);

		mRuleBookListAdapter = new RuleBookListAdapter(((Context) this.getActivity()),
				ruleBookDataString);


		mRuleBookListAdapter.notifyDataSetChanged();
		listViewRules.setAdapter(mRuleBookListAdapter);

		mTextViewHeader = (TextView)v.findViewById(R.id.txtRulesTopMsg);
		mTextViewHeader.setText(getString(R.string.rule_book));

		btnRuleBook = (ImageView)v.findViewById(R.id.imgBtnRuleBook);

		onRuleBookItemClick();
	}

	@Override
	public void onResume() {
		super.onResume();
	}


	public void onRuleBookItemClick() {

		listViewRules
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				switch (position) {

				case NFL_RULE_BOOK:
					if (mNFLRuleBookFragment == null) {
						mNFLRuleBookFragment = (NFLRuleBookFragment) (context).addFragment(
								R.id.linearLayout2, NFLRuleBookFragment.class.getName(),
								"NFLRuleBookFragment");
					} else {
						(context).attachFragment((Fragment) mNFLRuleBookFragment);
					}			
					break;

				case OFFICIAL_SIGNALS:
					if (mRulesSignalsFragment == null) {
						mRulesSignalsFragment = (SignalsFragment) (context).addFragment(
								R.id.linearLayout2, SignalsFragment.class.getName(),
								"officail_signals_fragment");
					} else {
						(context).attachFragment((Fragment) mRulesSignalsFragment);
					}
					break;

				case TACKLE_RULE_BOOK:
					break;

				default:
					break;
				}
			}
		});

		btnRuleBook.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent browserIntent = new Intent(
						Intent.ACTION_VIEW,
						Uri.parse("http://www.amazon.com/Youth-Football-Rulebook-2012-ebook/dp/B008PEXJCO/ref=sr_1_8?s=books&ie=UTF8&qid=1344605387&sr=1-8"));
				startActivity(browserIntent);
			}
		});
	}

}
