package com.checkinlibrary.checkin;



import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.checkinlibrary.helpers.AppStatus;
import com.checkinlibrary.location.GpsLocator;
import com.checkinlibrary.offers.BusinessResult.CheckinTimeResult;

public class CheckinAlgorithm {
    
    Context context;
    public String mMessage;
    public boolean IsUpdateReq;  // true=update false=insert
    public boolean IsFacbookOrTwitterSettingNeeded;
    AppStatus appstatus;

    public CheckinAlgorithm(Context ctx){
    	this.context=ctx;
    	appstatus = AppStatus.getInstance(context);
    }
    
    public Boolean checkCheckinAlgorithm(String distance, int businessId,boolean bIsPublicCheckin, List<CheckinTimeResult> checkins) {
        IsUpdateReq=false;
        IsFacbookOrTwitterSettingNeeded=false;
        if( (checkTodaysCheckinCount(businessId)) && (checkValidDistance(distance)) 
                && (hasActivePrefs(bIsPublicCheckin)) && (isAtValidTime(checkins))) {
            return true;
        } else {
            return false;
        }
    }
    
 public Boolean checkCheckinAlgorithm(int businessId,boolean bIsPublicCheckin, List<CheckinTimeResult> checkins) {
        
        if( (checkTodaysCheckinCount(businessId)) && (hasActivePrefs(bIsPublicCheckin)) && (isAtValidTime(checkins))) {
            return true;
        } else {
            return false;
        }
    }

 private Boolean checkValidDistance(String distance) {
	 Double dDistance=Double.valueOf(distance);
	 BigDecimal mDistance=BigDecimal.valueOf(dDistance);

	 //----------------------------------------------------------------
	 GpsLocator gps = GpsLocator.getGpsLocator(context);

	 Location currentLocation=gps.getBestLocation();
	 float accuracy = 0;
	 if(currentLocation != null) {
		 accuracy = currentLocation.getAccuracy();
		 Log.i("CheckInAndroid",String.valueOf(accuracy) );
	 } else {

		 String strAccuracy=appstatus.getSharedStringValue(appstatus.ACCURACY);
		 if(strAccuracy!=null) {

			 accuracy = Float.valueOf(strAccuracy);
			 Log.i("CheckInAndroid",strAccuracy );

		 }
	 }
	 Log.v("CheckInAndroid","Distance = " + distance );
	 accuracy = accuracy + 500;
	 float accuracyInKms = accuracy/1000;
	 Log.v("CheckInAndroid","Accuracy in Kms = " + accuracyInKms );
	 //------------------------------------------------------------------
	 int diffrence=mDistance.compareTo(BigDecimal.valueOf(accuracyInKms));

	 if(diffrence > 0) {
		 mMessage="Your GPS shows you as too far from the business location to check-in.";
		 return false;
	 } else {
		 return true;
	 }
 }

    private Boolean checkTodaysCheckinCount(int iBusinessId) {
       /* MyCheckinsDbAdapter mycheckin_db = new MyCheckinsDbAdapter((CheckinNativeActivity)context);    
        String mycheckinDay = mycheckin_db.getList(iBusinessId); 

        if(mycheckinDay != null) {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter= new SimpleDateFormat("yyyy/MM/dd");
            String strDate = formatter.format(currentDate.getTime());
            Log.i("Date",strDate);

            if(mycheckinDay.compareTo(strDate) == 0) {
                //todays one checkin done,
                mMessage="You can checkin only once per day for an offer.";
                return false;
            } else {
                IsUpdateReq=true;
                return true;
            }
        } else {
            IsUpdateReq=false;
            return true;
        }*/
    	return true;
    }
    private boolean hasActivePrefs(boolean isPublicCheckin) {
        if(isPublicCheckin) {
            boolean twitterOn = appstatus.getSharedBoolValue(appstatus.TWITTER_ON);
            boolean facebookOn = appstatus.getSharedBoolValue(appstatus.FACEBOOK_ON);
         
            // if both twitter and facebook not set
            if((!twitterOn) && (!facebookOn)) {
                IsFacbookOrTwitterSettingNeeded=true;
                mMessage="Set up Facebook or Twitter for Public Check-ins.";
                return false;    
            }
        }
        return true;
    }
    
	private Boolean isAtValidTime(List<CheckinTimeResult> checkins) {

		String[] weekdayName = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
		int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		String weekDay = weekdayName[day-1];
		Boolean isInTime = false;

		for (int i = 0; i < checkins.size(); i++) {
			if (weekDay.compareTo(checkins.get(i).day) == 0) {
				String strStartTime = checkins.get(i).start_time;
				String strEndTime = checkins.get(i).end_time;

				strStartTime = strStartTime.substring(0,
						strStartTime.length() - 1);
				strEndTime = strEndTime.substring(0, strEndTime.length() - 1);

				SimpleDateFormat d1 = new SimpleDateFormat(
						"yyyy-MM-dd'T'HH:mm:ss");
				try {

					int startTimeHours = d1.parse(strStartTime).getHours();
					int startTimeMin = d1.parse(strStartTime).getMinutes();

					int endTimeHours = d1.parse(strEndTime).getHours();
					int endTimeMin = d1.parse(strEndTime).getMinutes();

					int todayTimeHours = Calendar.getInstance().getTime()
							.getHours();
					int todayTimeMin = Calendar.getInstance().getTime()
							.getMinutes();

					Log.i("start Time", Calendar.getInstance().getTime()
							.toString());
					if ((todayTimeHours > startTimeHours) && (todayTimeHours < endTimeHours)) {
						isInTime = true;
						break;
					}else if (todayTimeHours == startTimeHours) {
						if (todayTimeMin >= startTimeMin) {
							isInTime = true;
							break;
						}
					}else if (todayTimeHours == endTimeHours){
						if(todayTimeMin < endTimeMin){
							isInTime = true;
							break;
						}
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		 mMessage="We're sorry. You aren't allowed to check in at this location at this time of day.";
		return isInTime;
	}
}
