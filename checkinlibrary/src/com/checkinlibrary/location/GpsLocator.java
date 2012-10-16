package com.checkinlibrary.location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.widget.TextView;

import com.checkinlibrary.CheckinLibraryActivity;
import com.checkinlibrary.helpers.AppStatus;

public class GpsLocator extends Observable {
    private static GpsLocator ref;
    public CheckinLocationListener mlocListener;
    public LocationManager mlocManager;
    private static CheckinLibraryActivity context;
    private double lat;
    private double lon;
    private Location currentLocation;
    private Location bestLocation;
    private List<GpsFragment> observers;
    private Boolean notifying = false;
    private List<GpsFragment> removalList = null;
    AlertDialog alert;

    private GpsLocator(CheckinLibraryActivity context) {
        //this.context = context;
        this.observers = new ArrayList<GpsFragment>();
        this.removalList = new ArrayList<GpsFragment>();
    }

    public static GpsLocator getGpsLocator(CheckinLibraryActivity cxt) {
        if (ref == null)
            ref = new GpsLocator(cxt);
        context=cxt;
        return ref;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public void addObserver(GpsFragment fragment) {
        //Refuse to add the same fragment as an observer multiple times
        for (GpsFragment gpsFragment : observers)
            if (gpsFragment.equals(fragment)) return;
   
        observers.add(fragment);
    }

    public void removeObserver(GpsFragment fragment) {
        if (!notifying) {
            observers.remove(fragment);            
        } else {
            removalList.add(fragment);
        }
    }

    public void notifyObservers() {
        notifying = true;

        for (GpsFragment f : observers) {
            f.onLocationUpdated(currentLocation);
        }

        notifying = false;
        processRemovedObservers();
    }
    
    public void stopListening() {
        mlocManager.removeUpdates(mlocListener);
    }

    private void processRemovedObservers() {
        if (removalList != null) {
            for (GpsFragment f : removalList) {
                removeObserver(f);
            }            
        }
    }

  
    public void listen() {
    	mlocManager = (LocationManager) context
    			.getSystemService(Context.LOCATION_SERVICE);
    	mlocListener = new CheckinLocationListener(this);
    	
    	String bestProvider = getMostAccurateProvider();
    	
    	if (currentLocation == null) {
    		setCurrentLocation(mlocManager
    				.getLastKnownLocation(bestProvider));
    		setBestLocation(mlocManager
    				.getLastKnownLocation(bestProvider));

    	}

    	if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // &&
    		// !AppStatus.getInstance(context).isGPSOn()
    		showGpsSettings();
          
    	} else {
 //----------------requesting location updates from all Providers-------//
    		mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
    				0, (LocationListener) mlocListener);
    		
    		if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ){
    		mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
    				0, (LocationListener) mlocListener);
    		}
    		
    		mlocManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0,
    				0, (LocationListener) mlocListener);
    	}
    }
    
    public boolean isProviderEnabled(){
    	mlocManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    	return mlocManager.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }

    private void showGpsSettings() {
        createGpsDialog();
    }
    
    public void destroyGpsDialog() {
        if ( alert != null )
            alert.dismiss();
    }
    
    public void createGpsDialog () {
    	 final AlertDialog.Builder builder = new AlertDialog.Builder(context);
         builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(true)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {
             	   dialog.dismiss();
                    context.startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS),6186);
                    
             	  /* if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
              	      Intent myIntent = new Intent(
              	        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
              	      context.startActivityForResult(myIntent,6186);
             	   }*/
                    
                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(final DialogInterface dialog, final int id) {
                     dialog.dismiss();
                }
            });
         alert = builder.create();
         alert.show();
    }

    public void updateLocation(Location loc) {
        setCurrentLocation(loc);
        if ( GpsFix.isBestLocation(bestLocation, currentLocation) ) {
            setBestLocation(currentLocation);
        }
        
        this.setLat(loc.getLatitude());
        this.setLon(loc.getLongitude());
        notifyObservers();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
    
    public Location getCurrentLocation() {
        return currentLocation;
    }
    
    public Location getBestLocation() {
          return bestLocation;
      }
    
    //Added b/c emulator geo fix has bad timestamps
    public void setCurrentLocation(Location loc) {
        if ( loc != null ) {
            if ( "google_sdk".equals(Build.PRODUCT) ) {
                loc.setTime((new Date().getTime()));
            }
            //---------------save current location for fetching after location timer time out
            AppStatus appStatus=new AppStatus();
            appStatus.saveSharedStringValue(appStatus.LAT,String.valueOf(loc.getLatitude()));
            appStatus.saveSharedStringValue(appStatus.LONG,String.valueOf(loc.getLongitude()));
            appStatus.saveSharedStringValue(appStatus.ACCURACY,String.valueOf(loc.getAccuracy()));
            appStatus.saveSharedStringValue(appStatus.PROVIDER,String.valueOf(loc.getProvider()));
            //Log.i("Checkin",String.valueOf(loc.getAccuracy()));
            currentLocation = loc;
        }
    }

    //Added b/c emulator geo fix has bad timestamps
    public void setBestLocation(Location loc) {
        if ( loc != null ) {
            if ( "google_sdk".equals(Build.PRODUCT) ) {
                loc.setTime((new Date().getTime()));
            }
            
            bestLocation = loc;
            
            /*if(loc.getAccuracy() < 850){
            	bestLocation = loc;
            }else{
            	listen();
            }*/
        }
    }
    
//-------------- To Check best accuracy among network providers-------------//     
    
    public String getMostAccurateProvider() {
    	mlocManager = (LocationManager) context
    			.getSystemService(Context.LOCATION_SERVICE);
    	String provider = LocationManager.GPS_PROVIDER;
    	float fGpsAcc=10000, fNetAcc=10000, fPasAcc=10000;

    	if(mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) !=null)
    		fGpsAcc = mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER).getAccuracy();
    	if (mlocManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) && (mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null)){
    		fNetAcc = mlocManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER).getAccuracy();
    	}
    	if(mlocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER) !=null)
    		fPasAcc = mlocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER).getAccuracy();
    	
    	if((fGpsAcc <= fNetAcc) && (fGpsAcc <= fPasAcc)){
    		provider=LocationManager.GPS_PROVIDER;
    	}else if((fNetAcc <= fGpsAcc) && (fNetAcc <= fPasAcc)){
    		provider=LocationManager.NETWORK_PROVIDER;
		}else if((fPasAcc <= fNetAcc) && (fPasAcc <= fGpsAcc)){
			provider=LocationManager.PASSIVE_PROVIDER;
		}
    	/*Log.i("Checkin_PROVIDER",provider);
    	Log.i("Checkin_GPS_ACC",String.valueOf(fGpsAcc));
    	Log.i("Checkin_NET_ACC",String.valueOf(fNetAcc));
    	Log.i("Checkin_PAS_ACC",String.valueOf(fPasAcc));*/
		return provider; 	
    }
    
  //-----------------------------------------------------------------------------//	    
    
  //-----------To Show the current GPS providers locations and accuracy-----------//     
    
    public void showGPSDetailsDialog() {
    	TextView gps;
    	TextView network;
    	TextView passive;
    	TextView gpsAcc;
    	TextView netAcc;
    	TextView pasAcc;
    	mlocManager = (LocationManager) context
    			.getSystemService(Context.LOCATION_SERVICE);

    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	builder.setMessage("Your GPS seems to be disabled, do you want to enable it?").setCancelable(true);
    	builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {    	   
    		public void onClick(final DialogInterface dialog, final int id) {
    			dialog.dismiss();         
    		}
    	});
    	
    	/*
    	LayoutInflater inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    	View layout = inflater.inflate(R.layout.custom_dialog,(ViewGroup)context.findViewById(R.id.lnr_custom));

    	gps= (TextView)layout.findViewById(R.id.textViewgps);
    	network = (TextView)layout.findViewById(R.id.textViewnet);
    	passive= (TextView)layout.findViewById(R.id.textViewpas);
    	gpsAcc = (TextView)layout.findViewById(R.id.textViewgpsAcc);
    	netAcc = (TextView)layout.findViewById(R.id.textViewnetAcc);
    	pasAcc = (TextView)layout.findViewById(R.id.textViewpasAcc);

    	checkLocationProviders(LocationManager.GPS_PROVIDER, gps,gpsAcc, "GPS = ");
    	checkLocationProviders(LocationManager.NETWORK_PROVIDER, network,netAcc,"NETWORK = ");
    	checkLocationProviders(LocationManager.PASSIVE_PROVIDER, passive,pasAcc,"PASSIVE = ");

    	builder = new AlertDialog.Builder(context);
    	builder.setView(layout);
    	alert = builder.create();
    	alert.show();
    	*/
    }

    private void checkLocationProviders(String gpsProvider, TextView gps,TextView gpsAcc,String provider) {
    	String strNoProText = "Provider is Disabled!";
    	String strNoLocText = "No Location!";
    	if(mlocManager.isProviderEnabled(gpsProvider)){
    		if (mlocManager.getLastKnownLocation(gpsProvider) !=null) {
    			gps.setText(provider + String.valueOf(mlocManager.getLastKnownLocation(gpsProvider).getLatitude()) + "  " +
    					String.valueOf(mlocManager.getLastKnownLocation(gpsProvider).getLongitude()));
    			gpsAcc.setText(provider + String.valueOf(mlocManager.getLastKnownLocation(gpsProvider).getAccuracy()));
    		} else {
    			gps.setText(provider + strNoLocText);
    			gpsAcc.setText(provider + strNoLocText);
    		}

    	}else {
    		gps.setText(provider + strNoProText);
    		gpsAcc.setText(provider + strNoProText);
    	}
}
   
//-----------------------------------------------------------------------------//	   
    
}