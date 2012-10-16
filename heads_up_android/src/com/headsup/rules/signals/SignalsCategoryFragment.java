/**
 * 
 */
package com.headsup.rules.signals;

import java.util.List;

import android.R.color;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.NoConnectivityScreen;
import com.checkinlibrary.imageloader.ImageLoader;
import com.headsup.HeadsUpNativeActivity;
import com.headsup.R;
import com.headsup.db.SignalsDbAdapter;
import com.headsup.helpers.Constants;
import com.headsup.models.SignalsResult;
import com.headsup.models.SignalsResult.SignalCategory;


public class SignalsCategoryFragment extends Fragment {
	
	HeadsUpNativeActivity context;
	LinearLayout lnrSignalsList;
	Button btnSort;
	Button btnScoring;
	Button btnDeadBall;
	Button btnLiveBall;
	Button btnConduct;
	public static GridView gridViewsignal;
	TextView txtSignalTypeHeader;
	ImageLoader mImageLoader;
	static int iCurrentCategory;
	Context cntxt;
	ImageView mImageView;
	
	Animation slideUp,slideDown;
	TextView mTextSignalDivider;
	LinearLayout lnrOfficialSignalHeader;	
	Boolean flagPopupImage;
	
	public final int BTN_SORT = 0, BTN_SCORING = 1, BTN_DBALL= 2,BTN_LBALL= 3,BTN_CNT = 4;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.context = (HeadsUpNativeActivity) this.getActivity();
	}
	
	public SignalsCategoryFragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v;
		v = inflater.inflate(R.layout.official_signal_type, container, false);
		iCurrentCategory = this.getArguments().getInt("category_id");
		initializeWidgets(v);
		
		onGridImageClicked(v);
		return v;
	}
	
	private void initializeWidgets(View v) {
		flagPopupImage=false;
		mTextSignalDivider=(TextView) v.findViewById(R.id.signalDivider);
		lnrOfficialSignalHeader=(LinearLayout)v.findViewById(R.id.lnrOfficialSignalHeader);
		btnSort = (Button)v.findViewById(R.id.btnSort);
		lnrSignalsList = (LinearLayout)v.findViewById(R.id.lnrOfficialSignalList);
		
		txtSignalTypeHeader = (TextView)v.findViewById(R.id.txtSignalTypeHeader);
		
		gridViewsignal = (GridView)v.findViewById(R.id.signalGridView);
		
		btnScoring = (Button)v.findViewById(R.id.btnScoring);
		btnDeadBall = (Button)v.findViewById(R.id.btnDeadBall);
		btnLiveBall = (Button)v.findViewById(R.id.btnLiveBall);
		btnConduct = (Button)v.findViewById(R.id.btnConduct);
		
		txtSignalTypeHeader.setText(this.getArguments().getString("header"));
		
		btnSort.setTag(BTN_SORT);onButtonClick(btnSort,"");
		btnScoring.setTag(BTN_SCORING);onButtonClick(btnScoring,getString(R.string.signals_scoring));
		btnDeadBall.setTag(BTN_DBALL);onButtonClick(btnDeadBall,getString(R.string.signals_dead_ball));
		btnLiveBall.setTag(BTN_LBALL);onButtonClick(btnLiveBall,getString(R.string.signals_live_ball));
		btnConduct.setTag(BTN_CNT);onButtonClick(btnConduct,getString(R.string.signals_conduct));

		gridViewsignal.setAdapter(new SignalsImageAdapter(context,fetchResultForCategory(this.getArguments().getInt("category_id"))));
		
	}
	
	public void onButtonClick(Button btn, final String strText) {
	
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Log.i("HeadsUp", String.valueOf(v.getTag()));
				switch (Integer.parseInt(String.valueOf(v.getTag()))) {
				case BTN_SORT:
					if(lnrSignalsList.getVisibility() == LinearLayout.GONE){
						lnrSignalsList.bringToFront();
						mTextSignalDivider.bringToFront();
						lnrOfficialSignalHeader.bringToFront();
						lnrSignalsList.setVisibility(LinearLayout.VISIBLE);
						slideDown = AnimationUtils.loadAnimation(context, R.drawable.btn_slide_down_pressed);
						lnrSignalsList.startAnimation(slideDown);
					} else {
							lnrSignalsList.setVisibility(LinearLayout.GONE);
							slideUp= AnimationUtils.loadAnimation(context, R.drawable.btn_slide_up_pressed);
							lnrSignalsList.startAnimation(slideUp);
					}	
					iCurrentCategory = BTN_SORT;
					break;
				case BTN_SCORING:
					txtSignalTypeHeader.setText(strText);
					gridViewsignal.invalidate();
					gridViewsignal.setAdapter(new SignalsImageAdapter(context, fetchResultForCategory(BTN_SCORING)));
					lnrSignalsList.setVisibility(LinearLayout.GONE);
					iCurrentCategory = BTN_SCORING;
					onGridImageClicked(v);
					
					break;
				case BTN_DBALL:
					txtSignalTypeHeader.setText(strText);
					gridViewsignal.invalidate();
					gridViewsignal.setAdapter(new SignalsImageAdapter(context, fetchResultForCategory(BTN_DBALL)));
					lnrSignalsList.setVisibility(LinearLayout.GONE);
					iCurrentCategory = BTN_DBALL;
					onGridImageClicked(v);
					break;
				case BTN_LBALL:
					txtSignalTypeHeader.setText(strText);
					gridViewsignal.invalidate();
					gridViewsignal.setAdapter(new SignalsImageAdapter(context,fetchResultForCategory(BTN_LBALL)));
					lnrSignalsList.setVisibility(LinearLayout.GONE);
					iCurrentCategory = BTN_LBALL;
					onGridImageClicked(v);
					break;
				case BTN_CNT:
					txtSignalTypeHeader.setText(strText);
					gridViewsignal.invalidate();
					gridViewsignal.setAdapter(new SignalsImageAdapter(context, fetchResultForCategory(BTN_CNT)));
					lnrSignalsList.setVisibility(LinearLayout.GONE);
					iCurrentCategory = BTN_CNT;
					onGridImageClicked(v);
					break;
				default:
					break;
				}
			}
		});
	}
	
	private List<SignalCategory> fetchResultForCategory(int iCategory) {
		SignalsDbAdapter signalsAdpt = new SignalsDbAdapter(context);
		SignalsResult mSignalsResult = null;
		
			mSignalsResult = signalsAdpt.getList(iCategory);
			
		
		return mSignalsResult.getCatagories();
	}
	
	public void onGridImageClicked(View v) {
		final List<SignalCategory> mResult =fetchResultForCategory(iCurrentCategory);
		gridViewsignal.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				if(CheckinLibraryActivity.appStatus.isOnline()) {
					if(flagPopupImage.equals(false)){
						flagPopupImage=true;
						lnrSignalsList.setVisibility(LinearLayout.GONE);
						loadPhoto(v, mResult.get(position));
					}
					
				} else {
					Intent intent= new Intent(context,NoConnectivityScreen.class);
					context.startActivity(intent);
					context.finish();
				}
				
	        }
		});
	}
	
	public void loadPhoto(View v, SignalCategory signalCategory) {
		mImageLoader = new ImageLoader(context);
		final Dialog custom = new Dialog(context,R.style.Dialog);
		custom.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		custom.setContentView(getLayoutInflater(null).inflate(R.layout.image_popup_view, (ViewGroup) v.findViewById(R.id.lnrImagePopup)));
		ImageView image = (ImageView) custom.findViewById(R.id.fullimage);
		//ImageView imageShadow=(ImageView) custom.findViewById(R.id.fullimageShadow);
		TextView txtHeader = (TextView) custom.findViewById(R.id.txtSignalPopUpHeader);
		TextView txtDesc = (TextView) custom.findViewById(R.id.txtSignalPopUpDesc);
		custom.getWindow().setBackgroundDrawable(new ColorDrawable(color.transparent));
		Log.i("#######HEADSUP", "Grid Image URL =  " + signalCategory);
		/*image.setImageDrawable(tempImageView.getDrawable());
		image.setLayoutParams(new LinearLayout.LayoutParams(*/

        String imageUrl=Constants.BASE_IMAGE_URL + signalCategory.getImage_link_large();
		Log.i("SIGNALS ","image url:" +imageUrl);
		
		mImageLoader.DisplayImage(imageUrl, image);
//		image.setLayoutParams(new LinearLayout.LayoutParams(
//				360, 360));
		LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		param.gravity=Gravity.CENTER;
		image.setLayoutParams(param);
		
		txtHeader.setText(signalCategory.getText_heading());
		txtDesc.setText(signalCategory.getText_desc());

		LinearLayout lnrImagePopup=(LinearLayout) custom.findViewById(R.id.lnrImagePopup);
		lnrImagePopup.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				custom.cancel();
				flagPopupImage=false;
			}
		});
		
		Button btnClose = (Button) custom.findViewById(R.id.btnClose);
		btnClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				custom.cancel();
				flagPopupImage=false;
				Log.i("#######", "Clicked");

			}
		});
		custom.show();
	}

}
