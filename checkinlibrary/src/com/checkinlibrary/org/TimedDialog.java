package com.checkinlibrary.org;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

class TimedDialog extends DialogFragment {
    private String dialogText;
    
    static TimedDialog newInstance(String title) {
        TimedDialog dialog = new TimedDialog();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        Log.v("CHECKINFORGOOD", "Bundling up title " + title);
        args.putString("title", title);
        dialog.setArguments(args);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = android.R.style.Theme_Light;
        Bundle args = getArguments();
        dialogText = args.getString("title");
        setStyle(style, theme);
    }
    
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(dialogText);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        return dialog;
    }
}