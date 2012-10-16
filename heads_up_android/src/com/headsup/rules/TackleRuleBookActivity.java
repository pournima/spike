package com.headsup.rules;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.headsup.R;

public class TackleRuleBookActivity extends Activity {

	ListView listViewRules;
	RulesListAdapter mRulesListAdapter;
	TextView mTextViewHeader;
	String[] rulesDataString = { "Game Element", "Areas of the Field",
			"Team Designations", "Players Designations", "Status of the Ball",
			"Players Action", "Types of Fouls", "Penalty Enforcement Spots",
			"Other Spots" };

	public static int tackleRuleItemPosition;
	public static final int GAME_ELEMENT = 0;
	public static final int AREA_FIELDS = 1;
	public static final int TEAM_DESIGNATIONS = 2;
	public static final int PLAYERS_DESIGNATION = 3;
	public static final int STATUS_BALLS = 4;
	public static final int PLAYERS_ACTION = 5;
	public static final int TYPES_FOULS = 6;
	public static final int PENALALTY_ENFORCEMENT_SPOTS = 7;
	public static final int OTHER_SPOTS = 8;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rules);

		listViewRules = (ListView) findViewById(android.R.id.list);

		mRulesListAdapter = new RulesListAdapter(TackleRuleBookActivity.this,
				rulesDataString);
		mRulesListAdapter.notifyDataSetChanged();
		listViewRules.setAdapter(mRulesListAdapter);
		
		mTextViewHeader = (TextView)findViewById(R.id.txtRulesTopMsg);
		mTextViewHeader.setText(getString(R.string.rule_book_tackle));

		//getRulesDataList();
		onClickRulesList();
		
		RuleBookFragment.bIsFromNFLRuleBook = false;

	}

	public void getRulesDataList() {

		

		mRulesListAdapter = new RulesListAdapter(TackleRuleBookActivity.this,
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
						// TODO Auto-generated method stub
						/*
						TabGroupActivity parentActivity = (TabGroupActivity) getParent();
						Intent rule_intent = new Intent(getParent(),
								RuleDetailsFragment.class);
						
						switch (position) {
						case GAME_ELEMENT:
							// Log.d("rule ---", "Game Element");
							tackleRuleItemPosition = GAME_ELEMENT;
							parentActivity.startChildActivity("game element",
									rule_intent);

							break;
						case AREA_FIELDS:
							tackleRuleItemPosition = AREA_FIELDS;
							parentActivity.startChildActivity("areas field",
									rule_intent);
							break;

						case TEAM_DESIGNATIONS:
							tackleRuleItemPosition = TEAM_DESIGNATIONS;
							parentActivity.startChildActivity(
									"team designation", rule_intent);
							break;

						case PLAYERS_DESIGNATION:
							tackleRuleItemPosition = PLAYERS_DESIGNATION;
							parentActivity.startChildActivity(
									"players designation", rule_intent);
							break;

						case STATUS_BALLS:
							tackleRuleItemPosition = STATUS_BALLS;
							parentActivity.startChildActivity("status balls",
									rule_intent);
							break;

						case PLAYERS_ACTION:
							tackleRuleItemPosition = PLAYERS_ACTION;
							parentActivity.startChildActivity("players action",
									rule_intent);
							break;

						case TYPES_FOULS:
							tackleRuleItemPosition = TYPES_FOULS;
							parentActivity.startChildActivity("type fouls",
									rule_intent);
							break;

						case PENALALTY_ENFORCEMENT_SPOTS:
							tackleRuleItemPosition = PENALALTY_ENFORCEMENT_SPOTS;
							parentActivity.startChildActivity(
									"penalaty enforcement", rule_intent);
							break;

						case OTHER_SPOTS:
							tackleRuleItemPosition = OTHER_SPOTS;
							parentActivity.startChildActivity("other spots",
									rule_intent);

							break;

						default:
							break;
						}*/
					}
				});
	
	}
				
	public void onResume() {
		super.onResume();
	}

}