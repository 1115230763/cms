package com.cms.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.StringUtils;

/**
 * 日期工具類
 */
public class DateUtil { 
	public static String format(Date date) {
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}

	public static String format(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).format(date);
	}

	public static Date parse(String dateString) throws Exception {
		return parse(dateString, "yyyy-MM-dd HH:mm:ss");
	}

	public static Date parse(String dateString, String pattern) throws Exception {
		if (StringUtils.isEmpty(dateString)) {
			return null;
		}
		if (StringUtils.isEmpty(pattern)) {
			pattern = "yyyy-MM-dd HH:mm:ss";
		}
		return new SimpleDateFormat(pattern).parse(dateString);
	}
    
    public static Date dateModified(Date modifyDate ,int number){
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(modifyDate);
		calendar.add(Calendar.DATE, number);
		return calendar.getTime();
	}

	public static void main(String[] args) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date d1 = df.parse("2019-06-24 00:00:00");
			Date d2 = df.parse("2016-06-22 00:00:00");
			long diff = d1.getTime() - d2.getTime();
			long days = diff / (1000 * 60 * 60 * 24);
			System.out.println(days);
			System.out.println(new Date().getTime()>d1.getTime());
		} catch (Exception e){
			
		}
	}

}
