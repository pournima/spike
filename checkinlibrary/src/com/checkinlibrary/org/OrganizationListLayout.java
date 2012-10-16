package com.checkinlibrary.org;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.checkinlibrary.R;


public class OrganizationListLayout extends RelativeLayout {
    TextView txtName;
    ImageView imgFlag;
    ImageView imgNextArrow;
    Context context;
    int index;

    private int[] img_resources={R.drawable.my_cause,R.drawable.all_cause};

    public OrganizationListLayout(Context context, String causes,int iImgResIndex) {
        super(context);
        this.context = context;
        this.index=iImgResIndex;
        // Styles - List - Padding Item
        int padding_in_dp = 10;  // 10 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        this.setPadding(0, padding_in_px, 0, padding_in_px);
        this.setBackgroundResource(R.drawable.list_bg);
        addNextArrow(); 
        addFlag(img_resources[iImgResIndex]);
        addTextName(causes);
    }
    private void addNextArrow() {
        RelativeLayout.LayoutParams lp_imgNext = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        lp_imgNext.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
        imgNextArrow=new ImageView(context);
        imgNextArrow.setId(3);
        // Styles - List - Arrow
        imgNextArrow.setImageResource(R.drawable.nextarrow);
        imgNextArrow.setPadding(0, 5, 15, 0);
        addView(imgNextArrow, lp_imgNext);
    }

    private void addTextName(String name) {
        RelativeLayout.LayoutParams lp_txtName = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        
        lp_txtName.addRule(RelativeLayout.RIGHT_OF,imgFlag.getId());
        txtName = new TextView(context);
        txtName.setText(name);
        txtName.setId(2);
        // Styles - List - Text
        txtName.setTextAppearance(context,R.style.medtext);
        txtName.setTypeface(null, Typeface.BOLD);
        txtName.setTextSize(16);
        txtName.setPadding(0, 3, 0, 0);
        addView(txtName, lp_txtName);
    }

    private void addFlag(int iImgResId) {
        RelativeLayout.LayoutParams lp_img_flag = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp_img_flag.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        // Styles - List - Icon
        imgFlag=new ImageView(context);
        imgFlag.setImageResource(iImgResId);
        imgFlag.setId(1);
        imgFlag.setPadding(15, 0, 0, 0);
        addView(imgFlag, lp_img_flag);
    }

    public void setName(String value) {
        txtName.setText(value);
    }

    public void setFlagResource(int value) {
        imgFlag.setImageResource(img_resources[value]);
    }
}