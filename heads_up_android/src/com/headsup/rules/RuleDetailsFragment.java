package com.headsup.rules;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.headsup.R;

public class RuleDetailsFragment extends Fragment {

	WebView webViewRulesOfServices;
	/*	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.rulesofservice);		
	}
	*/
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.rulesofservice, container, false);
		setupButtons(v);
		return v;
	}
	
	private void setupButtons(View v) { 
		webViewRulesOfServices=(WebView)v.findViewById(R.id.webViewRules);
		webViewRulesOfServices.setBackgroundColor(0);
		
		if(RuleBookFragment.bIsFromNFLRuleBook) {
			showNFLRuleBook();
		} else {
			showTackleRuleBook();
		}
	}
	
	private void showNFLRuleBook() {
		
		switch (NFLRuleBookFragment.nflRuleItemPosition) {
		case NFLRuleBookFragment.GAME:
			webViewRulesOfServices.loadUrl("file:///android_asset/game.html");
			break;
		
		case NFLRuleBookFragment.TERMINOLOGY:
			webViewRulesOfServices.loadUrl("file:///android_asset/terminology.html");
			break;
		
		case NFLRuleBookFragment.ELIGIBILITY:
			webViewRulesOfServices.loadUrl("file:///android_asset/eligibility.html");
			break;
			
		case NFLRuleBookFragment.EQUIPMENT:
			webViewRulesOfServices.loadUrl("file:///android_asset/equipment.html");
			break;
		
		case NFLRuleBookFragment.FIELD:
			webViewRulesOfServices.loadUrl("file:///android_asset/field.html");
			break;
		
		case NFLRuleBookFragment.ROSTERS:
			webViewRulesOfServices.loadUrl("file:///android_asset/rosters.html");
			break;
			
		case NFLRuleBookFragment.TIMING_OVERTIME:
			webViewRulesOfServices.loadUrl("file:///android_asset/timing_overtime.html");
			break;
		
		case NFLRuleBookFragment.SCORING:
			webViewRulesOfServices.loadUrl("file:///android_asset/scoring.html");
			break;
			
		case NFLRuleBookFragment.COACHES:
			webViewRulesOfServices.loadUrl("file:///android_asset/coaches.html");
			break;		
			
		case NFLRuleBookFragment.LIVE_DEAD_BALL:
			webViewRulesOfServices.loadUrl("file:///android_asset/live-dead-ball.html");
			break;		
			
		case NFLRuleBookFragment.RUNNING:
			webViewRulesOfServices.loadUrl("file:///android_asset/running.html");
			break;		
			
		case NFLRuleBookFragment.PASSING:
			webViewRulesOfServices.loadUrl("file:///android_asset/passing.html");
			break;		
			
		case NFLRuleBookFragment.RECEIVING:
			webViewRulesOfServices.loadUrl("file:///android_asset/receiving.html");
			break;		
			
		case NFLRuleBookFragment.RUSHING:
			webViewRulesOfServices.loadUrl("file:///android_asset/rushing_passer.html");
			break;		
			
		case NFLRuleBookFragment.FLAG_PULLING:
			webViewRulesOfServices.loadUrl("file:///android_asset/flag-pulling.html");
			break;		
			
		case NFLRuleBookFragment.FORMATIONS:
			webViewRulesOfServices.loadUrl("file:///android_asset/formations.html");
			break;		
			
		case NFLRuleBookFragment.UNSPORTSMANLIKE_CONDUCT:
			webViewRulesOfServices.loadUrl("file:///android_asset/unsportsmanlike.html");
			break;		
			
		case NFLRuleBookFragment.PENALTIES:
			webViewRulesOfServices.loadUrl("file:///android_asset/penalties.html");
			break;		
		default:
			break;
		}
		
	}
	
	private void showTackleRuleBook() {
		
		switch (TackleRuleBookActivity.tackleRuleItemPosition) {
		case TackleRuleBookActivity.GAME_ELEMENT:
			webViewRulesOfServices.loadUrl("file:///android_asset/game_elements.html");
			break;
		
		case TackleRuleBookActivity.AREA_FIELDS:
			webViewRulesOfServices.loadUrl("file:///android_asset/area_of_the_field.html");
			break;
		
		case TackleRuleBookActivity.TEAM_DESIGNATIONS:
			webViewRulesOfServices.loadUrl("file:///android_asset/team_designations.html");
			break;
			
		case TackleRuleBookActivity.PLAYERS_DESIGNATION:
			webViewRulesOfServices.loadUrl("file:///android_asset/player_designations.html");
			break;
		
		case TackleRuleBookActivity.STATUS_BALLS:
			webViewRulesOfServices.loadUrl("file:///android_asset/status_of_the_ball.html");
			break;
		
		case TackleRuleBookActivity.PLAYERS_ACTION:
			webViewRulesOfServices.loadUrl("file:///android_asset/player_actions.html");
			break;
			
		case TackleRuleBookActivity.TYPES_FOULS:
			webViewRulesOfServices.loadUrl("file:///android_asset/types_of_fouls.html");
			break;
		
		case TackleRuleBookActivity.PENALALTY_ENFORCEMENT_SPOTS:
			webViewRulesOfServices.loadUrl("file:///android_asset/penalty_enforcement_spots.html");
			break;
			
		case TackleRuleBookActivity.OTHER_SPOTS:
			webViewRulesOfServices.loadUrl("file:///android_asset/other_spots.html");
			break;		
		default:
			break;
		}
		
	}
}