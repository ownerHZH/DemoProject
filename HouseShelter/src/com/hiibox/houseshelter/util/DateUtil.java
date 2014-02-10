package com.hiibox.houseshelter.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.util.Log;

    
  
  
  
  
  

@SuppressLint("SimpleDateFormat")
public class DateUtil {

	public final static String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	@SuppressLint("SimpleDateFormat")
	public final static SimpleDateFormat DEFAULT_SDF = new SimpleDateFormat(
			DEFAULT_PATTERN);

	    
  
  
  
  
  
	public static String convertDate(Date date) {
		return DEFAULT_SDF.format(date);
	}

	    
  
  
  
  
  
  
	@SuppressLint("SimpleDateFormat")
	public static String convertDate(Date date, String pattern) {
		return new SimpleDateFormat(pattern).format(date);
	}

	    
  
  
  
	public static Date convertStringToDate(String date) {
		try {
			return DEFAULT_SDF.parse(date);
		} catch (ParseException e) {
		}
		return new Date();
	}

	    
  
  
  
  
	@SuppressLint("SimpleDateFormat")
	public static Date convertStringToDate(String date, String pattern) {
		try {
			return new SimpleDateFormat(pattern).parse(date);
		} catch (ParseException e) {
		}
		return new Date();
	}

	    
  
  
  
  
  
	public static boolean isFirstDayInMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) == 1;
	}

	    
  
  
  
  
  
	public static boolean isFirstDayInYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_YEAR) == 1;
	}

	    
  
  
  
  
  
	public static Date rounding(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	    
  
  
  
  
  
  
	public static Date dateAdd(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + day);
		return calendar.getTime();
	}

	    
  
  
  
  
	public static Date getFirstDayOfPreviousMonth() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	    
  
  
  
  
	public static Date getFirstDayOfPreviousYear() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - 1);
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DATE, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		System.out.println(convertDate(getFirstDayOfPreviousMonth(),
				DEFAULT_PATTERN));
		System.out.println(convertDate(getFirstDayOfPreviousYear(),
				DEFAULT_PATTERN));
		System.out.println(convertStringToDate("2012-07-10 11:09:38"));
	}

	    
  
  
  
  
  
	public static String cutDate(String date) {
		String dateStr = null;
		Date dateTemp;
		Date dateNow;
		dateNow = new Date();
		dateTemp = convertStringToDate(date);
		long day = (dateNow.getTime() - dateTemp.getTime())
				/ (24 * 60 * 60 * 1000);
		long hour = ((dateNow.getTime() - dateTemp.getTime())
				/ (60 * 60 * 1000) - day * 24);
		                                               
		if (day > 0) {
			dateStr = day + "天前";
		} else if (day == 0 && hour == 0) {
			dateStr = "刚刚更新";
		} else {
			dateStr = hour + "小时前";
		}
		return dateStr;
	}

	    
  
  
  
  
  
	public static String cutDateYmd(String date) {
		String dateStr = null;
		Date dateTemp = convertStringToDate(date);
		Log.i("dateTemp", "" + dateTemp);
		dateStr = dateTemp.getYear() + 1900 + "-" + (dateTemp.getMonth() + 1)
				+ "-" + dateTemp.getDate();
		return dateStr;
	}
	
	    
  
  
  
  
  
	public static String changeDateToYmd(String date) {
	    String dateStr = null;
	    Date dateTemp = convertStringToDate(date);
	    Log.i("dateTemp", "" + dateTemp);
	    dateStr = dateTemp.getYear() + 1900 + "年" + (dateTemp.getMonth() + 1)
	            + "月" + dateTemp.getDate() + "日";
	    return dateStr;
	}

	             
	public static String cutDateMdHm(String date) {
		String dateStr = null;
		Date dateTemp = convertStringToDate(date);
		dateStr = dateTemp.getYear() + 1900 + "-" +(dateTemp.getMonth() + 1) + "-" + dateTemp.getDate() + "  "
				+ dateTemp.getHours() + ":" + dateTemp.getMinutes();
		return dateStr;
	}

	    
  
  
  
  
	public static String getcurrentDay() {
		Date curDate = new Date(System.currentTimeMillis());          

		String str = DEFAULT_SDF.format(curDate);
		return str;
	}
	
	    
  
  
  
	public static String getTime() {
		String[] s = getcurrentDay().split(" ");
		return s[1].substring(0, s[1].lastIndexOf(":"));
	}
	
	    
  
  
  
	public static String getLastHalfAnHour() {
	    Date currDate = new Date(System.currentTimeMillis()-30*60*1000);
	    String[] s = DEFAULT_SDF.format(currDate).split(" ");
	    return s[1].substring(0, s[1].lastIndexOf(":"));
	}
	
	    
  
  
  
	public static String getLastHalfAnHour(String time) {
	    String[] str = time.split(":");
	    Date currDate = new Date(Integer.valueOf(str[0])*60*60*1000 + Integer.valueOf(str[1])*60*1000 -30*60*1000);
	    String[] s = DEFAULT_SDF.format(currDate).split(" ");
	    return s[1].substring(0, s[1].lastIndexOf(":"));
	}

	    
  
  
	@SuppressLint("SimpleDateFormat")
	public static int getWorkTime(String endtime) {
		String starttime = getcurrentDay();
		starttime = starttime.substring(0, 10);
		          
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		        
		Date dateFrom = null;
		Date dateTo = null;
		try {
			dateFrom = dateFormat.parse(starttime);
			dateTo = dateFormat.parse(endtime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int workdays = 0;
		Calendar cal = null;
		while (dateFrom.before(dateTo) || dateFrom.equals(dateTo)) {
			cal = Calendar.getInstance();
			        
			cal.setTime(dateFrom);
			                                                            
			                                                            
			                              
			workdays++;
			     
			        
			cal.add(Calendar.DAY_OF_MONTH, 1);
			dateFrom = cal.getTime();
		}
		return workdays;
	}
	
	public static long[] dateDiff(String startTime, String endTime, String format) {
		long[] res = new long[3];
		SimpleDateFormat sd = new SimpleDateFormat(format);
		long nd = 1000*24*60*60;
		long nh = 1000*60*60;
		long nm = 1000*60;
		long diff;
		try {
			diff = sd.parse(endTime).getTime() - sd.parse(startTime).getTime();
			long day = diff/nd;         
			long hour = diff%nd/nh;          
			long min = diff%nd%nh/nm;          
			res[0] = day;
			res[1] = hour;
			res[2] = min;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static long[] dateDiff(String startTime) {
		long[] res = new long[2];
		String currTime = DEFAULT_SDF.format(new Date());
		long nd = 1000*24*60*60;
		long nh = 1000*60*60;
		long diff;
		try {
			diff = DEFAULT_SDF.parse(currTime).getTime() - DEFAULT_SDF.parse(startTime).getTime();
			long day = diff/nd;         
			long hour = diff%nd/nh;          
			res[0] = day;
			res[1] = hour;
		} catch (ParseException e) {
			e.printStackTrace();
		}
                                                                                                                  
		return res;
	}

}
