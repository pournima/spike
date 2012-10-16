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
import android.util.Log;

import com.checkinlibrary.helpers.AppStatus;

public class GpsLocator extends Observable {
	private static GpsLocator ref;
	private CheckinLocationListener mlocListener;
	private LocationManager mlocManager;
	//private static CheckinNativeActivity context;
	private double lat;
	private double lon;
	private Location currentLocation;
	private Location bestLocation;
	private List<GpsFragment> observers;
	private Boolean notifying = false;
	private List<GpsFragment> removalList = null;
	AlertDialog alert;
	public static Context context;

	private GpsLocator(Context context) {
		// this.context = context;
		this.observers = new ArrayList<GpsFragment>();
		this.removalList = new ArrayList<GpsFragment>();
	}

	public static GpsLocator getGpsLocator(Context cxt) {
		if (ref == null)
			ref = new GpsLocator(cxt);
		context = cxt;
		return ref;
	}

	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	public void addObserver(GpsFragment fragment) {
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

		if (currentLocation == null) {

			setCurrentLocation(mlocManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			setBestLocation(mlocManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER));
			if (mlocManager.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {

				setCurrentLocation(mlocManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
				setBestLocation(mlocManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));

				Log.i("#######LOC_NETWORK",
						String.valueOf(mlocManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)));

				if (mlocManager
						.getLastKnownLocation(LocationManager.GPS_PROVIDER) == null) {

					setCurrentLocation(mlocManager
							.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));
					setBestLocation(mlocManager
							.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER));

					Log.i("#######LOC_PASSIVE",
							String.valueOf(mlocManager
									.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)));
				}

				Log.i("#######LOC", String.valueOf(mlocManager
						.getProvider(LocationManager.PASSIVE_PROVIDER)));
			}
		}

		if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { // &&
																			// !AppStatus.getInstance(context).isGPSOn()
			showGpsSettings();

		} else {
			mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
					0, (LocationListener) mlocListener);
		}
	}

	public boolean isProviderEnabled() {
		mlocManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		return mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	private void showGpsSettings() {

		createGpsDialog();

	}

	public void destroyGpsDialog() {

		if (alert != null)
			alert.dismiss();
	}

	public void createGpsDialog() {

		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(
				"Your GPS seems to be disabled, do you want to enable it?")
				.setCancelable(true)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(final DialogInterface dialog,
									final int id) {
								dialog.dismiss();
								context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

								/*
								 * if
								 * (!mlocManager.isProviderEnabled(LocationManager
								 * .GPS_PROVIDER)) { Intent myIntent = new
								 * Intent(
								 * Settings.ACTION_LOCATION_SOURCE_SETTINGS);
								 * context
								 * .startActivityForResult(myIntent,6186); }
								 */

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(final DialogInterface dialog,
							final int id) {
						dialog.dismiss();
					}
				});
		alert = builder.create();
		alert.show();
	}

	public void updateLocation(Location loc) {
		setCurrentLocation(loc);
		if (GpsFix.isBestLocation(bestLocation, currentLocation)) {
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

	// Added b/c emulator geo fix has bad timestamps
	public void setCurrentLocation(Location loc) {
		if (loc != null) {
			if ("google_sdk".equals(Build.PRODUCT)) {
				loc.setTime((new Date().getTime()));
			}
			// ---------------save current location for fetching after location
			// timer time out
			AppStatus appStatus = new AppStatus();
			appStatus.saveSharedStringValue(appStatus.LAT,
					String.valueOf(loc.getLatitude()));
			appStatus.saveSharedStringValue(appStatus.LONG,
					String.valueOf(loc.getLongitude()));
			appStatus.saveSharedStringValue(appStatus.ACCURACY,
					String.valueOf(loc.getAccuracy()));
			appStatus.saveSharedStringValue(appStatus.PROVIDER,
					String.valueOf(loc.getProvider()));
			// Log.i("Checkin",String.valueOf(loc.getAccuracy()));
			currentLocation = loc;
		}
	}

	// Added b/c emulator geo fix has bad timestamps
	public void setBestLocation(Location loc) {
		if (loc != null) {
			if ("google_sdk".equals(Build.PRODUCT)) {
				loc.setTime((new Date().getTime()));
			}

			bestLocation = loc;
		}
	}
}