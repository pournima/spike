package com.headsup.safety;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;

public class SafetyFragment extends Fragment {
	Button btnAboutHeadsup;
	Button btnConcussionSignsSymptoms;
	Button btnHowtoTackle;
	Button btnSafetyChecklist;
	Button btnConcussionAwareness;
	Button btnEquipment;
	Button btnConditioning;
	Button btnHeatPreparedness;
	Button btnHydration;
	Button btnInjuryPrevention;
	Button btnNutrition;
	TextView txtMoneyRaisedHeader;
	HeadsUpNativeActivity context;
	AboutHeadsUpFootballFragment mAboutHeadsUpFootballFragment;
	SignsSymptomsFragment mSignsSymptomsFragment;
	HowToTackleFragment mHowToTackleFragment;
	SafetyChecklistsFragment mSafetyChecklistsFragment;
	ConcussionAwarenessFragment mConcussionAwarenessFragment;
	EquipmentFittingFragment mEquipmentFittingFragment;
	ConditioningFragment mConditioningFragment;
	HeatPreparednessFragment mHeatPreparednessFragment;
	HydrationFragment mHydrationFragment;
	InjuryPreventionFragment mInjuryPreventionFragment;
	NutritionFrament mNutritionFrament;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = (HeadsUpNativeActivity) this.getActivity();
		View v = inflater.inflate(R.layout.safety, container, false);
		
		initializePage(v);
		return v;
	}

	private void initializePage(View v) {
		btnAboutHeadsup = (Button)v.findViewById(R.id.btnAboutHeadsup);
		btnConcussionSignsSymptoms = (Button)v.findViewById(R.id.btnConcussionSignsSymptoms);
		btnHowtoTackle = (Button)v.findViewById(R.id.btnHowtoTackle);
		btnSafetyChecklist = (Button)v.findViewById(R.id.btnSafetyCheckList);
		btnConcussionAwareness = (Button)v.findViewById(R.id.btnConcussionAwareness);
		btnEquipment = (Button)v.findViewById(R.id.btnEquipment);
		btnConditioning = (Button)v.findViewById(R.id.btnConditioning);
		btnHeatPreparedness = (Button)v.findViewById(R.id.btnHeatPreparedness);
		btnHydration = (Button)v.findViewById(R.id.btnHydration);
		btnInjuryPrevention = (Button)v.findViewById(R.id.btnInjuryPrevention);
		btnNutrition = (Button) v.findViewById(R.id.btnNutrition);
		
		btnAboutHeadsup.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onAboutHeadsUpClick();
			}
		});
		
		btnConcussionSignsSymptoms.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onConcussionSignsSymptomsClick();	
			}
		});
		
		btnHowtoTackle.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onHowToTackleClick();	
			}
		});
		
		btnSafetyChecklist.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onSafetyCheckListClick();	
			}
		});
		
		btnConcussionAwareness.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onConcussionAwarenessClick();	
			}
		});
		
		btnEquipment.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onEquipmentClick();	
			}
		});
		
		btnConditioning.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onConditioningClick();	
			}
		});
		
		btnHeatPreparedness.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onHeatPrepardnessClick();	
			}
		});
		
		btnHydration.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onHydrationClick();	
			}
		});
		
		btnInjuryPrevention.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onInjuryPreventionClick();	
			}
		});
		
		
		btnNutrition.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				onNutritionClick();
			}
		});
	}



	public void onConcussionAwarenessClick() {
		if (mConcussionAwarenessFragment == null) {
			mConcussionAwarenessFragment = (ConcussionAwarenessFragment) (context).addFragment(
					R.id.linearLayout2, ConcussionAwarenessFragment.class.getName(),
					"concussion_awareness_fragment");
		} else {
			(context).attachFragment((Fragment) mConcussionAwarenessFragment);
		}
	}

	public void onConcussionSignsSymptomsClick() {	
		if (mSignsSymptomsFragment == null) {
			mSignsSymptomsFragment = (SignsSymptomsFragment) (context).addFragment(
					R.id.linearLayout2, SignsSymptomsFragment.class.getName(),
					"signs_symptoms_fragment");
		} else {
			(context).attachFragment((Fragment) mSignsSymptomsFragment);
		}
	}

	public void onEquipmentClick() {
		
		if (mEquipmentFittingFragment == null) {
			mEquipmentFittingFragment = (EquipmentFittingFragment) (context).addFragment(
					R.id.linearLayout2, EquipmentFittingFragment.class.getName(),
					"equipment_fitting_fragment");
		} else {
			(context).attachFragment((Fragment) mEquipmentFittingFragment);
		}
	}

	public void onInjuryPreventionClick() {
		if (mInjuryPreventionFragment == null) {
			mInjuryPreventionFragment = (InjuryPreventionFragment) (context).addFragment(
					R.id.linearLayout2, InjuryPreventionFragment.class.getName(),
					"equipment_fitting_fragment");
		} else {
			(context).attachFragment((Fragment) mInjuryPreventionFragment);
		}
	}

	public void onHydrationClick() {
		if (mHydrationFragment == null) {
			mHydrationFragment = (HydrationFragment) (context).addFragment(
					R.id.linearLayout2, HydrationFragment.class.getName(),
					"hydration_fragment");
		} else {
			(context).attachFragment((Fragment) mHydrationFragment);
		}
	}

	public void onNutritionClick() {
		
		if (mNutritionFrament == null) {
			mNutritionFrament = (NutritionFrament) (context).addFragment(
					R.id.linearLayout2, NutritionFrament.class.getName(),
					"nutrition_fragment");
		} else {
			(context).attachFragment((Fragment) mNutritionFrament);
		}
	}

	public void onConditioningClick() {
		
		if (mConditioningFragment == null) {
			mConditioningFragment = (ConditioningFragment) (context).addFragment(
					R.id.linearLayout2, ConditioningFragment.class.getName(),
					"conditioning_fragment");
		} else {
			(context).attachFragment((Fragment) mConditioningFragment);
		}
	}

	public void onSafetyCheckListClick() {
		
		if (mSafetyChecklistsFragment == null) {
			mSafetyChecklistsFragment = (SafetyChecklistsFragment) (context).addFragment(
					R.id.linearLayout2, SafetyChecklistsFragment.class.getName(),
					"safety_checklist_fragment");
		} else {
			(context).attachFragment((Fragment) mSafetyChecklistsFragment);
		}
	}

	public void onHeatPrepardnessClick() {
		
		if (mHeatPreparednessFragment == null) {
			mHeatPreparednessFragment = (HeatPreparednessFragment) (context).addFragment(
					R.id.linearLayout2, HeatPreparednessFragment.class.getName(),
					"heat_preparedness_fragment");
		} else {
			(context).attachFragment((Fragment) mHeatPreparednessFragment);
		}
	}

	public void onAboutHeadsUpClick() {
		
		if (mAboutHeadsUpFootballFragment == null) {
			mAboutHeadsUpFootballFragment = (AboutHeadsUpFootballFragment) (context).addFragment(
					R.id.linearLayout2, AboutHeadsUpFootballFragment.class.getName(),
					"about_headsup_fragment");
		} else {
			(context).attachFragment((Fragment) mAboutHeadsUpFootballFragment);
		}
		
	}

	public void onHowToTackleClick() {
		
		if (mHowToTackleFragment == null) {
			mHowToTackleFragment = (HowToTackleFragment) (context).addFragment(
					R.id.linearLayout2, HowToTackleFragment.class.getName(),
					"how_to_tackle_fragment");
		} else {
			(context).attachFragment((Fragment) mHowToTackleFragment);
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
}
