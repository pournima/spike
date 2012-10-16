package com.headsup.safety;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.headsup.R;

public class HeadsUpFootballActivity extends Activity {

	ListView listViewConcussion;
	ConcussionAwarenessListAdapter mConcussionAwarenessListAdapter;

	public static final int ACTION_PLAN = 0;
	public static final int VIDEO_SERIES = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.concussion);

		displayConcussionList();
		onClickConcussionList();
	}

	private void displayConcussionList() {

		String[] concussionAwarenessListName = { "Action Plan", "Video Series",
				"Prevention & Preparation", "What Athletes Need to Know",
				"Articles & latest News" };
		String[] concussionAwarenessListData = { "Sign & Symptoms and more",
				"How to minimize and treat possible concussions",
				"Resources to help be ready for concussion",
				"Tools for every athlete",
				"Stay up-to-date with latest information" };

		listViewConcussion = (ListView) findViewById(android.R.id.list);
		mConcussionAwarenessListAdapter = new ConcussionAwarenessListAdapter(
				HeadsUpFootballActivity.this, concussionAwarenessListName,
				concussionAwarenessListData);
		listViewConcussion.setAdapter(mConcussionAwarenessListAdapter);
	}

	public void onClickConcussionList() {

		listViewConcussion
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						// TODO Auto-generated method stub

						switch (position) {

						case ACTION_PLAN:
							/*
							TabGroupActivity parentActivity = (TabGroupActivity) getParent();
							Intent action_intent = new Intent(getParent(),
									SignsSymptomsFragment.class);
							parentActivity.startChildActivity("Action Plan",
									action_intent);
							*/
							break;
						default:
							break;
						}

					}
				});
	}
}
