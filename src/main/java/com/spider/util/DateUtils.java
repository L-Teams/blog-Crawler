package com.spider.util;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;

/**
 * 日期处理工具累
 * 
 * @author hongliang.sun
 */
public class DateUtils {
	public static final String DATE_SHORT_FORMAT = "yyyy-MM-dd";
	public static final String DATE_SHORT_FORMAT_NOSEPARATE = "yyyyMMdd";
	public static final String DATE_LONG_FORMAT_NOSEPARATE = "yyyyMMddHHmmss";
	public static final String DATE_SHORT_FORMAT_NOZERO = "yyyy/M/d";
	public static final String HOUR_MINUTE_FORMAT = "HH:mm";
	public static final String DATE_LONG_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_LONG_FORMAT_NOSECOND = "yyyy-MM-dd HH:mm";
	public static final String DATE_CHINESE_FORMAT = "yyyy年M月d日";
	public static final String DATE_CHINESE_SHORT_FORMAT = "MM月dd日";
	public static final String DATE_CHINESE_SHORT_MONTH_FORMAT = "M月";
	public static final String DATE_CHINESE_SHORT_DAY_FORMAT = "d日";
	public static final String DATE_CHINESE_SHORT_NOZERO_FORMAT = "M月d日";
	private DateUtils() {
	}

	/**
	 * 根据规则解析字符串为Date
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Optional<Date> parseStr2Date(String dateStr, String pattern) {
		if (StringUtils.isEmpty(dateStr))
			return Optional.empty();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return Optional.ofNullable(sdf.parse(dateStr));
		} catch (ParseException e) {
			return Optional.empty();
		}
	}

	/**
	 * 根据规则解析Date为字符串
	 * 
	 * @param dateStr
	 * @param pattern
	 * @return
	 */
	public static Optional<String> parseDate2Str(Date date, String pattern) {
		if (date == null)
			return Optional.empty();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return Optional.ofNullable(sdf.format(date));
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	/**
	 * 获取当前日期是星期几
	 *
	 * @param date
	 * @return 当前日期是星期几
	 */
	public static int getIntWeekOfDate(Date date) {
		int[] weekInts = { 7, 1, 2, 3, 4, 5, 6 };
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0)
			w = 0;
		return weekInts[w];
	}
	
	/**
	 * 添加日期
	 * @param date
	 * @param calendarField：Calendar.YEAR Calendar.HOUR等
	 * @param amount添加个数
	 * @return
	 */
	public static Optional<Date> add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(calendarField, amount);
		return Optional.of(c.getTime());
	}

	/**
	 * 取得两个日期的相隔天数
	 *
	 * @param day1
	 * @param day2
	 * @return
	 */
	public static long getDaysBetween(Date day1, Date day2) {
		long nd = 1000 * 24 * 60 * 60;// 一天的毫秒数
		long diff = Math.abs(day2.getTime() - day1.getTime());
		long day = diff / nd;//计算差多少天
		return day;
	}
	
}
