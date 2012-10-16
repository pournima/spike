package com.checkinlibrary.business;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.R;
import com.checkinlibrary.models.BusinessResult;
import com.google.zxing.client.android.encode.EncodeActivity;


public class ThankyouFragment extends Fragment {

	CheckinLibraryActivity context;
	private BusinessResult mBusinessResult;
	private String mBusinessName;
	private String mOrganizationName;
	TextView textView_name;
	ImageView imgBarcode;
	TextView txtThankYou;
	TextView txtOffers;
	TextView txtOffersDetails;
	TextView txtBarcode;
	View v;
	BusinessFragment mBusinessFragment;
	Button btnFindBusiness;
	
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		this.context = (CheckinLibraryActivity) this.getActivity();

		mBusinessResult = (BusinessResult) this.getArguments().getSerializable("selected_business");
		mBusinessName = this.getArguments().getString("business_name");
		mOrganizationName = this.getArguments().getString("organization_name");


		if (0 == (getString(R.string.app_name)).compareTo("USA Football")) {
			Log.d("BarcodeNumber","-  "+ mBusinessResult.getBarcodeNumber());
			if (mBusinessResult != null && mBusinessResult.getBarcodeNumber().length() > 0){
				v = inflater.inflate(R.layout.thankyou_barcode, container, false);
				imgBarcode = (ImageView) v.findViewById(R.id.imgBarcode);
				txtOffers = (TextView) v.findViewById(R.id.txt_offer);
				txtOffersDetails = (TextView) v.findViewById(R.id.txtOfferDetails);
				txtBarcode = (TextView) v.findViewById(R.id.txtBarCode);
	
				txtOffers.setText(mBusinessResult.getPromotionalOffer());
				txtBarcode.setText(mBusinessResult.getBarcodeNumber());
	
				String strOffer = "Offer Details: ";
				strOffer = strOffer + mBusinessResult.getPromotionalOffer();
	
				final SpannableStringBuilder sb = new SpannableStringBuilder(strOffer);
	
				final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
				sb.setSpan(bss, 0, 14, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold 
	
				txtOffersDetails.setText(sb);
			}else{
				v = inflater.inflate(R.layout.thankyou, container, false);
			}
		}else{

			v = inflater.inflate(R.layout.thankyou, container, false);
		}

		txtThankYou = (TextView) v.findViewById(R.id.txtThankYou);
		btnFindBusiness=(Button)v.findViewById(R.id.btnFindMoreBusinesses);

		/*if (0 == (getString(R.string.app_name)).compareTo("USA Football")) {
			btnFindBusiness.setVisibility(Button.VISIBLE);
			onFindMoreBusinessesClick();
		}else{
			btnFindBusiness.setVisibility(Button.GONE);
		}*/

		txtThankYou.setText("Thank you for checking in at \n" + mBusinessName + " on behalf of " + mOrganizationName);
		if (0 == (getString(R.string.app_name)).compareTo("USA Football")) {
			if (mBusinessResult != null && mBusinessResult.getBarcodeNumber().length() > 0) {
				String barcodeNumber = mBusinessResult.getBarcodeNumber();
				Intent intent = new Intent("com.google.zxing.client.android.ENCODE");
				intent.putExtra("ENCODE_FORMAT", "CODE_128");  
				intent.putExtra("ENCODE_DATA", barcodeNumber);
				startActivity(intent);
			}
		}
		
		return v;
	}

	public void onResume() {
		super.onResume();
		if(EncodeActivity.bmpBarcode != null){
			imgBarcode.setImageBitmap(EncodeActivity.bmpBarcode);
			EncodeActivity.bmpBarcode = null;
		}
	}

	
	public void onFindMoreBusinessesClick(){
		
		btnFindBusiness.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

//				context.mTabManager.onTabChanged(context.mTabHost
//						.getCurrentTabTag());
//				
//				if(mBusinessFragment == null){
//					mBusinessFragment = (BusinessFragment) context.
//							addFragment(R.id.linearLayout2,
//									BusinessFragment.class.getName(),
//									"business_tab");
//				}
//				else {
//
//					(context).attachFragment((Fragment) mBusinessFragment,"business_tab");
//				}
				context.popFragmentFromStack();
				context.popFragmentFromStack();
			}
		});
	}
	@Override
	public void onPause() { 
		super.onPause();
	}

}