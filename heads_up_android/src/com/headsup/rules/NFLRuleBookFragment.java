package com.headsup.rules;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;

public class NFLRuleBookFragment extends Fragment {

	ListView listViewRules;
	RulesListAdapter mRulesListAdapter;
	TextView mTextViewHeader;
	String[] rulesDataString = { "Game", "Terminology", "Eligibility",
			"Equipment", "Field", "Rosters", "Timing and Overtime", "Scoring",
			"Coaches", "Live Ball/Dead Ball", "Running", "Passing",
			"Receiving", "Rushing the Passer", "Flag Pulling", "Formations",
			"Unsportsmanlike Conduct", "Penalties" };

	public static int nflRuleItemPosition;
	public static final int GAME = 0;
	public static final int TERMINOLOGY = 1;
	public static final int ELIGIBILITY = 2;
	public static final int EQUIPMENT = 3;
	public static final int FIELD = 4;
	public static final int ROSTERS = 5;
	public static final int TIMING_OVERTIME = 6;
	public static final int SCORING = 7;
	public static final int COACHES = 8;
	public static final int LIVE_DEAD_BALL = 9;
	public static final int RUNNING = 10;
	public static final int PASSING = 11;
	public static final int RECEIVING = 12;
	public static final int RUSHING = 13;
	public static final int FLAG_PULLING = 14;
	public static final int FORMATIONS = 15;
	public static final int UNSPORTSMANLIKE_CONDUCT = 16;
	public static final int PENALTIES = 17;
	
	ImageView btnRuleBook;
	
	HeadsUpNativeActivity context;
	RuleDetailsFragment mRuleDetailsFragment;
	
	/*
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.nfl_rules);
	}
	*/
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (HeadsUpNativeActivity) this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.nfl_rules, container, false);
		setupButtons(v);
		return v;
	}
	private void setupButtons(View v) { 
		listViewRules = (ListView)v.findViewById(android.R.id.list);

		mRulesListAdapter = new RulesListAdapter(((Context) this.getActivity()),
				rulesDataString);
		mRulesListAdapter.notifyDataSetChanged();
		listViewRules.setAdapter(mRulesListAdapter);

		mTextViewHeader = (TextView)v.findViewById(R.id.txtRulesTopMsg);
		mTextViewHeader.setText(getString(R.string.rule_book_nfl));
		
		btnRuleBook = (ImageView)v.findViewById(R.id.imgBtnRuleBook);
		btnRuleBook.setVisibility(View.GONE);

		onClickRulesList();

		RuleBookFragment.bIsFromNFLRuleBook = true;
	}
	
	
	public void getRulesDataList() {

		mRulesListAdapter = new RulesListAdapter(((Context) this.getActivity()),
				rulesDataString);
		mRulesListAdapter.notifyDataSetChanged();
		listViewRules.setAdapter(mRulesListAdapter);
	}

	public void onClickRulesList() {

		listViewRules
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
					/*				
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						Intent rule_intent = new Intent(getParent(),
								RuleDetailsFragment.class);
					*/
						switch (position) {
						case GAME:
							// Log.d("rule ---", "Game Element");
							nflRuleItemPosition = GAME;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("game",
									rule_intent);	*/

							break;
						case TERMINOLOGY:
							nflRuleItemPosition = TERMINOLOGY;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("terminology",
									rule_intent);	*/
							break;

						case ELIGIBILITY:
							nflRuleItemPosition = ELIGIBILITY;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("eligibilty",
									rule_intent);	*/
							break;

						case EQUIPMENT:
							nflRuleItemPosition = EQUIPMENT;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("equipment",
									rule_intent);	*/
							break;

						case FIELD:
							nflRuleItemPosition = FIELD;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("field",
									rule_intent);	*/
							break;

						case ROSTERS:
							nflRuleItemPosition = ROSTERS;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("rosters",
									rule_intent);	*/
							break;

						case TIMING_OVERTIME:
							nflRuleItemPosition = TIMING_OVERTIME;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity(
									"timing overtime", rule_intent);	*/
							break;

						case SCORING:
							nflRuleItemPosition = SCORING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("scoring",
									rule_intent);	*/
							break;

						case COACHES:
							nflRuleItemPosition = COACHES;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("other spots",
									rule_intent);	*/

							break;

						case LIVE_DEAD_BALL:
							nflRuleItemPosition = LIVE_DEAD_BALL;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity(
									"live and dead ball", rule_intent);	*/

							break;

						case RUNNING:
							nflRuleItemPosition = RUNNING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("running",
									rule_intent);	*/

							break;

						case PASSING:
							nflRuleItemPosition = PASSING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("passing",
									rule_intent);	*/

							break;

						case RECEIVING:
							nflRuleItemPosition = RECEIVING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("receiving",
									rule_intent);	*/

							break;

						case RUSHING:
							nflRuleItemPosition = RUSHING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("rushing",
									rule_intent);	*/

							break;

						case FLAG_PULLING:
							nflRuleItemPosition = FLAG_PULLING;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("flag pulling",
									rule_intent);	*/

							break;

						case FORMATIONS:
							nflRuleItemPosition = FORMATIONS;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("formations",
									rule_intent);	*/

							break;

						case UNSPORTSMANLIKE_CONDUCT:
							nflRuleItemPosition = UNSPORTSMANLIKE_CONDUCT;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity(
									"unsportsmanlike", rule_intent);	*/

							break;

						case PENALTIES:
							nflRuleItemPosition = PENALTIES;
							gotoRuleDetail();
						/*	parentActivity.startChildActivity("penalties",
									rule_intent);	*/

							break;

						default:
							break;
						}
					}
				});
	}

	public void gotoRuleDetail()
	{
		if (mRuleDetailsFragment == null) {
			mRuleDetailsFragment = (RuleDetailsFragment) context.
					addFragment(com.headsup.R.id.linearLayout2,
							RuleDetailsFragment.class.getName(),
							"RuleDetailsFragment");
		} else {
			(context).attachFragment((Fragment) mRuleDetailsFragment);
		}
		
	}
	public void onResume() {
		super.onResume();
	}

}