package org.beatific.flow.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	private final static String DEFAULT_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String format(Date date) {
		return format(date, DEFAULT_FORMAT);
	}
	
	public static String format(Date date, String format) {
		SimpleDateFormat simpleFormat = new SimpleDateFormat(format);
		return simpleFormat.format(date);
	}
	
	public static String today(String format) {
		return format(new Date(), format);
	}
	
	public static String today() {
		return today(DEFAULT_FORMAT);
	}
	
	public static Date add(Date date, int amount) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, amount);
		return cal.getTime();
	}
	/**
	 * remove time under miliseconds
	 * @param date
	 * @return
	 */
	public static Date noMiliSecondsDate(Date date) {
		Calendar testDateCal = Calendar.getInstance(TimeZone.getDefault());
	    testDateCal.setTime(date);
	    testDateCal.set(14, 0);
	    return testDateCal.getTime();
	}
}
