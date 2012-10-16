package com.checkinlibrary.location;

import android.location.Location;
import android.util.Log;

import java.util.Date;

public class GpsFix {
	Integer waitPeriod;
	Integer stalePeriod;
	Long quittingTime;
	Long expireTime;
	Integer accuracyInMeters;
	Location bestFix;
	private static final float DEFAULT_ACCURACY = 550;
	private static Integer ACCURACY_LEEWAY = 300;
	private static int TIME_CHANGE_INTERVAL = 15 * 1000; // 15 seconds

	public enum GpsFixStatus {
		TIMED_OUT, OK_FIX, TRY_AGAIN
	}

	public GpsFix(Integer waitPeriod, Integer accuracyInMeters,
			Integer stalePeriod) {
		this.waitPeriod = waitPeriod;
		this.accuracyInMeters = accuracyInMeters;
		this.stalePeriod = stalePeriod;

		Log.i("CHECKINFORGOOD", "Trying to get Gps Fix with waitPeriod: "
				+ Integer.toString(waitPeriod) + " accuracyInMeters: "
				+ Integer.toString(accuracyInMeters) + " stalePeriod: "
				+ Integer.toString(stalePeriod));
	}

	// Start with last good fix from GPS locator, and return immediately if no
	// calls needed.
	public GpsFixStatus getFix(Location location) {
		Long now = new Date().getTime();
		quittingTime = now + (waitPeriod * 1000);
		expireTime = now - (stalePeriod * 1000);

		if (isLocationGood(location)) {
			return GpsFixStatus.OK_FIX;
		} else {
			return GpsFixStatus.TRY_AGAIN;
		}
	}

	public GpsFixStatus isAcceptableFix(Location location) {
		Long now = new Date().getTime();

		if (isLocationGood(location)) {
			return GpsFixStatus.OK_FIX;
		} else {
			if (now >= quittingTime) {
				return GpsFixStatus.TIMED_OUT;
			} else {
				return GpsFixStatus.TRY_AGAIN;
			}
		}
	}

	private Boolean isLocationGood(Location location) {
		Float accuracy;

		if (location == null) {
			return false;
		}

		if (location.hasAccuracy()) {
			accuracy = location.getAccuracy();
		} else {
			accuracy = DEFAULT_ACCURACY;
			location.setAccuracy(DEFAULT_ACCURACY);
		}

		Log.i("CHECKINFORGOOD",
				"isLocationGood needs accuracy " + accuracyInMeters
						+ " and has accuracy: " + accuracy.toString()
						+ " expireTime: "
						+ new Date(expireTime).toLocaleString()
						+ " locationTime: "
						+ new Date(location.getTime()).toLocaleString());

		return (accuracy <= (location.getAccuracy() + ACCURACY_LEEWAY))
				&& (location.getTime() >= expireTime);
	}

	// Thanks to Google for this algorithm. We store the best location
	// systemwide, but
	// we're really concerned about
	public static boolean isBestLocation(Location currentBestLocation,
			Location newLocation) {

		if (currentBestLocation == null) {
			return true;
		}

		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TIME_CHANGE_INTERVAL;
		boolean isSignificantlyOlder = timeDelta < (-1 * TIME_CHANGE_INTERVAL);
		boolean isNewer = timeDelta > 0;

		// Log.i("CHECKINFORGOOD", "NEW TIME:" +new
		// Date(newLocation.getTime()).toLocaleString()+ " OLD TIME: " + new
		// Date(currentBestLocation.getTime()).toLocaleString());
		// Log.i("CHECKINFORGOOD", "isSignificantlyNewer: " +
		// isSignificantlyNewer + " isSignificantlyOlder: " +
		// isSignificantlyOlder);

		if (isSignificantlyNewer) {
			return true;
		} else if (isSignificantlyOlder) {
			return false;
		}

		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;

		// Log.i("CHECKINFORGOOD", "isSignificantlyNewer: " +
		// isSignificantlyNewer + " isSignificantlyOlder: " +
		// isSignificantlyOlder + " isMoreAccurate: " + isMoreAccurate +
		// "isLessAccurate: " + isLessAccurate);

		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		}

		return false;
	}
}
