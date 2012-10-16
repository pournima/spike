package com.checkinlibrary.ws.tasks;

import java.io.InputStream;

import com.checkinlibrary.org.CauseProfileFragment;
import com.checkinlibrary.ws.services.WebServiceIface;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

public class causeProfileImageTask extends AsyncTask<String, Void, Bitmap> implements WebServiceIface {
    ImageView bmImage;
	private CauseProfileFragment context;
	
    public causeProfileImageTask(CauseProfileFragment checkin_context,ImageView bmImage) {
        this.bmImage = bmImage;
    	this.context = checkin_context;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = BASE_URL+urls[0];
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
			context.onCausesImageResult(result);
    }
}
