package com.checkinlibrary.org;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.checkinlibrary.R;


public class TimedCauseNotFoundRunnable implements Runnable, com.checkinlibrary.org.TimedDialogRunnable {
    private Fragment caller;
    private TimedDialogAdapter adapter;

    public TimedCauseNotFoundRunnable(Fragment caller) {
        this.caller = caller;
    }
    
    @Override
    public void run() {
        Activity context = this.caller.getActivity();
        adapter.stopDialogAndItsCallback();
        View hideMe = context.findViewById(R.id.textViewCausesTopMsg);
        if ( hideMe != null ) hideMe.setVisibility(View.GONE);
        TextView tv = new TextView(context);
        tv.setText(getSearchSpan(), BufferType.SPANNABLE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setTextColor(context.getResources().getColor(R.color.black));
        LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.setMargins(20, 10, 20, 10);
        tv.setTextSize(13);
        tv.setLayoutParams(lp);
        LinearLayout causesLayout = (LinearLayout) context.findViewById(R.id.linearAllCause);
        if ( causesLayout != null || hideMe != null ) {
        	causesLayout.removeViewAt(1);
        	causesLayout.addView(tv, 1);
        }
    }
    
    private SpannableStringBuilder getSearchSpan() {
        final Activity context = caller.getActivity();
        CharSequence sequence = Html.fromHtml(context.getString(R.string.cause_location_not_found));
        SpannableStringBuilder strBuilder = new SpannableStringBuilder(sequence);
        UnderlineSpan [] underlines = strBuilder.getSpans(0, strBuilder.length(), UnderlineSpan.class);

        for(UnderlineSpan span : underlines) {
            int start = strBuilder.getSpanStart(span);
            int end = strBuilder.getSpanEnd(span);
            int flags = strBuilder.getSpanFlags(span);
   
            ClickableSpan searchLauncher = new ClickableSpan() {
                public void onClick(View view) {
                    FragmentTransaction ft = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                    ft.detach(caller);
                    ft.commit();
                    context.onSearchRequested();
                }
            };
            
            strBuilder.setSpan(searchLauncher, start, end, flags);
        }
        
        return strBuilder;
    }

    @Override
    public void setDialogAdapter(TimedDialogAdapter td) {
        this.adapter = td;
    }            
}