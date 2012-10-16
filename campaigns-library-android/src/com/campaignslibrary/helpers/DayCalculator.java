package com.campaignslibrary.helpers;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.provider.Settings.System;

public class DayCalculator {

	public static DayCalculator INSTANCE = new DayCalculator();

	public DayCalculator() {
		// TODO Auto-generated constructor stub
	}

	public int daysCalc(String EndDt) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String StartDt = sdf.format(Calendar.getInstance().getTime());
		StartDt+="Z";
		
		Calendar calSt = Calendar.getInstance();
		Calendar calEnd = Calendar.getInstance();
		int yearStart = 0,monthStart = 0,dayStart = 0,yearEnd = 0,monthEnd = 0,dayEnd = 0;

		String strYearStart = StartDt.substring(0, 4);yearStart = Integer.parseInt(strYearStart);
		String strMonthStart = StartDt.substring(5, 7);strMonthStart = strMonthStart.substring(0);
		monthStart = Integer.parseInt(strMonthStart);
		String strDayStart = StartDt.substring(8,10);strDayStart = strDayStart.substring(0);
		dayStart = Integer.parseInt(strDayStart);
		
		String strYearEnd = EndDt.substring(0, 4);yearEnd = Integer.parseInt(strYearEnd);
		String strMonthEnd = EndDt.substring(5, 7);strMonthEnd = strMonthEnd.substring(0);
		monthEnd = Integer.parseInt(strMonthEnd);
		String strDayEnd = EndDt.substring(8,10);strDayEnd = strDayEnd.substring(0);
		dayEnd = Integer.parseInt(strDayEnd);
		
		calSt.set(yearStart, monthStart, dayStart);
		calEnd.set(yearEnd, monthEnd, dayEnd);
		
		long msStartDay = calSt.getTimeInMillis();
		long msEndDay = calEnd.getTimeInMillis();
		long differ = msEndDay - msStartDay;
		long diffDays = differ / (24 * 60 * 60 * 1000);

		return (int)diffDays;

	}
}
