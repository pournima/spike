package com.checkinlibrary.settings;


import android.content.Context;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.checkinlibrary.R;


public class SettingsListLayout extends RelativeLayout {
    TextView txtName;
    ImageView imgNextArrow;
    int iIndex;
    Context context;
    int total;

    public SettingsListLayout(Context context, String causes,int iIndex,int total) {
        super(context);
        this.context = context;
        this.iIndex=iIndex;
        this.total=total;
        // Styles - List - Padding Item
        int padding_in_dp = 10;  // 10 dps
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_in_dp * scale + 0.5f);
        this.setPadding(0, padding_in_px, 0, padding_in_px);
        this.setBackgroundResource(R.drawable.list_bg);
        addNextArrow(); 
        addTextName(causes);
    }
    
    private void addNextArrow() {
        RelativeLayout.LayoutParams lp_imgNext = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        lp_imgNext.addRule(RelativeLayout.ALIGN_PARENT_RIGHT); 
        imgNextArrow=new ImageView(context);
        // Styles - List - Arrow
        imgNextArrow.setImageResource(R.drawable.nextarrow);
        imgNextArrow.setPadding(0, 5, 15, 0);
        addView(imgNextArrow, lp_imgNext);
    }

    private void addTextName(String name) {
        RelativeLayout.LayoutParams lp_txtName = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        lp_txtName.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        txtName = new TextView(context);
        txtName.setText(name);
        // Styles - List - Text
        txtName.setTextAppearance(context,R.style.medtext);
        txtName.setTypeface(null, Typeface.BOLD);
        txtName.setTextSize(16);
        txtName.setPadding(15, 0, 0, 0);
        if(iIndex==total){
        	// Styles - List - Non linked items
            txtName.setTypeface(null, Typeface.NORMAL);
            txtName.setTextColor(R.color.gray);
            imgNextArrow.setVisibility(EditText.INVISIBLE);
        }
        addView(txtName, lp_txtName);
    }

    public void setName(String value) {
        txtName.setText(value);
    }
}