package com.checkinlibrary.location;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class CheckinLocationListener implements LocationListener {
    private static double lat;
    private static double lon;
    private GpsLocator locator;

    public CheckinLocationListener(GpsLocator locator) {
        this.locator = locator;
    }

    public void onLocationChanged(Location loc) {
        locator.updateLocation(loc);
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    public static double getLat() {
        return lat;
    }

    public static double getLon() {
        return lon;
    }
}