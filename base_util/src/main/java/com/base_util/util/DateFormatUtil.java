package com.base_util.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;


/**
 * 时间格式化工具类
 */

public class DateFormatUtil {
	public static final String FORMAT_ISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	/**
	 * yyyyMMddHHmmss
	 */
	public static final String TYPE4 = "yyyyMMddHHmmss";
	public static final String TYPE_ALL = "yyyy-MM-dd HH:mm:ss";
	/**
	 * yyyy-MM-dd
	 */
	public static final String TYPE1 = "yyyy-MM-dd";
	/**
	 * yyyy/M/d
	 */
	public static final String YYYYMd = "yyyy/M/d";
	/**
	 * yyyy-MM
	 */
	public static final String TYPE_MONTH = "yyyy-MM";
	/**
	 * yyyyMM
	 */
	public static final String TYPE_MONTH2 = "yyyyMM";
	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static final String TYPE2 = "yyyy-MM-dd HH:mm";
	/**
	 * 日期格式"yyyy"
	 */
	public static final String YYYY = "yyyy";
	/**
	 * 日期格式"MM"
	 */
	public static final String MM = "MM";
	/**
	 * 日期格式"dd"
	 */
	public static final String dd = "dd";
	/**
	 * 日期格式"HH"
	 */
	public static final String HH = "HH";
	/**
	 * 日期格式"mm"
	 */
	public static final String mm = "mm";
	/**
	 * yyyyMMdd
	 */
	public static final String YYYYMMDD = "yyyyMMdd";
	/**
	 * 时间格式"HHmmss"
	 */
	public static final String HHMMSS2 = "HHmmss";
	/**
	 * yyyy年MM月dd日
	 */
	public static final String YMD = "yyyy年MM月dd日";

	/**
	 * yyyy年MM月dd日
	 */
	public static final String YMD2 = "yyyy年M月d日";

	/**
	 * yyyy年MM月dd日
	 */
	public static final String YM = "yyyy年M月";
	/**
	 * yyyy年MM月dd日
	 */
	public static final String YMM = "yyyy年MM月";

	/**
	 * yyyy年M月d日 H时m分
	 */
	public static final String YMDHM = "yyyy年M月d日 H时m分";
	/**
	 * yyyy年M月d日 H时m分
	 */
	public static final String YYYYMMDDHHMMM = "yyyy年MM月dd日HH时mm分";
	/**
	 * dd日 HH时mm分
	 */
	public static final String DHM = "dd日 HH时mm分";

	/**
	 * Date 类型的日期转换为字符串
	 */
	public static String dateToStr(Date date, String dateType) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		return sdf.format(date);
	}

	/**
	 * 将字符串转化为日期
	 * 
	 * @param dateStr  字符串的值
	 * @param dateType 要转化的类型
	 * @return
	 */
	public static Date strToDate(String dateStr, String dateType) {
		if (dateStr == null)
			return null;
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Date date = null;
		try {
			date = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 得到两个时间的时间差 格式：XX天XX小时XX分XX秒
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String getTimeDifference(Date d1, Date d2) {
		String timeDifference = "";
		long millisecond = d1.getTime() - d2.getTime();
		long day = millisecond / (24 * 60 * 60 * 1000);
		long hour = (millisecond / (60 * 60 * 1000) - day * 24);
		long minute = ((millisecond / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long senond = (millisecond / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		timeDifference = day + "天" + hour + "小时" + minute + "分" + senond + "秒";
		return timeDifference;
	}
	/**
	 * 得到两个时间的时间差 格式：XX天XX小时XX分XX秒
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static String getTimeDifference2(long time1, long time2) {
		String timeDifference = "";
		long millisecond = time2 - time1;
		long day = millisecond / (24 * 60 * 60 * 1000);
		long hour = (millisecond / (60 * 60 * 1000) - day * 24);
		long minute = ((millisecond / (60 * 1000)) - day * 24 * 60 - hour * 60);
		long senond = (millisecond / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - minute * 60);
		timeDifference = day + "天" + hour + "小时" + minute + "分" + senond + "秒";
		return timeDifference;
	}

	/**
	 * 得到昨天当时的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYesterday(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	/**
	 * 得到昨天当时的时间
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, -1);
		return dateToStr(cal.getTime(), TYPE_MONTH);
	}

	/**
	 * 获取上个月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastFirst(Date date) {
		// 获取前一个月第一天
		Calendar calendar1 = Calendar.getInstance();
		calendar1.add(Calendar.MONTH, -1);
		calendar1.set(Calendar.DAY_OF_MONTH, 1);
		return dateToStr(calendar1.getTime(), TYPE1);
	}

	/**
	 * 获取上个月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static String getLastLast(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 0);
		return dateToStr(calendar.getTime(), TYPE1);
	}

	/**
	 * 得到明天当时的时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getTomorrow(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_YEAR, 1);
		return cal.getTime();
	}

	/**
	 * 获取 date 对应 月份最后一天
	 * 
	 * @param date
	 * @return
	 * @author hxw
	 * @createDate 2013-07-10
	 */
	public static Date getLast(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));
		return cal.getTime();
	}

	/**
	 * 获取时间
	 * 
	 * @param date
	 * @param type 类型 1秒 2分 3时 4天 5月 6年
	 * @param num  增量
	 * @return
	 */
	public static Date getDate(Date date, int type, int num) {
		Calendar calendar = new GregorianCalendar();
		if (date == null) {
			calendar.setTime(new Date());
		} else {
			calendar.setTime(date);
		}
		switch (type) {
		case 1:
			calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + num);
			break;
		case 2:
			calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + num);
			break;
		case 3:
			calendar.set(Calendar.HOUR, calendar.get(Calendar.HOUR) + num);
			break;
		case 4:
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + num);
			break;
		case 5:
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + num);
			break;
		case 6:
			calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) + num);
			break;
		default:
			break;
		}
		return calendar.getTime();
	}

	/**
	 * 获取 date 对应月份的 第一天
	 *
	 * @return
	 * @update hxw
	 * @updateDate 2013-07-10
	 */
	public static Date getFirst(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return cal.getTime();
	}

	/**
	 * 得到本周一
	 *
	 * @param args
	 */
	public static Date getWeekFirst(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		// 得到当天是本周的第几天（星期一是第一天，星期二是第二天.....）
		int days = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (days == 0) {
			days = 7;
		}
		// 得到本周一的时间
		cal.add(Calendar.DAY_OF_YEAR, -(days - 1));
		return cal.getTime();
	}

	/**
	 * 根据页码获取当前页连续20天时间
	 * 
	 * @param pageNum
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static List<String> getPageDate(int pageNum) {
		Calendar ca = Calendar.getInstance();
		ca.setTime(new Date());// 设置时间为当前时间
		String dateTemp = "";
		List<String> dateList = new ArrayList<>();
		int page = (pageNum - 1) * 20;
		for (int i = 0; i < 20; i++) {
			if (i == 0) {
				ca.add(ca.DATE, -page); // 日期减 如果不够减会将月变动
			} else {
				ca.add(ca.DATE, -1);
			}
			dateTemp = dateToStr(ca.getTime(), TYPE1);
			dateList.add(dateTemp);
		}
		return dateList;
	}

	/**
	 * 获取最近一个月 例 20190421 到 20190521
	 * 
	 * @return
	 */
	public static HashMap<String, String> getLastMonth() {
		HashMap<String, String> map = new HashMap<>();
		map.put("start", dateToStr(getDate(new Date(), 4, -30), YYYYMMDD));
		map.put("end", dateToStr(new Date(), YYYYMMDD));
		return map;
	}

	/**
	 * 将任意格式时间字符串转为任意格式字符串。类型为空的话默认为yyyy-MM-dd转yyyyMMdd
	 * 
	 * @param date
	 * @return
	 */
	public static String strDateToStrDate(String date, String stateType, String overType) {
		if (stateType == null) {
			stateType = TYPE1;
		}
		if (overType == null) {
			overType = YYYYMMDD;
		}
		String str = dateToStr(strToDate(date, stateType), overType);
		return str;
	}

	/**
	 * 将long 类型的时间转化为String 字符串
	 * 
	 * @return
	 */
	public static String longToStr(String dateType, Long longTime) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(longTime);
		return sdf.format(cal.getTime());
	}

	public static String getSysYear(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		return year;
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year 年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 延迟second 秒之后的时间戳
	 * 
	 * @param second
	 * @return
	 */
	public static String getSecondTime(int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, second);
		return dateToStr(calendar.getTime(), FORMAT_ISO8601);
	}

	/**
	 * 延迟hour小史之后的时间戳
	 * 
	 * @param second
	 * @return
	 */
	public static String getHourTime(int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR, hour);
		return dateToStr(calendar.getTime(), FORMAT_ISO8601);
	}

	public static Long getDay(Date pastTime) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long days = null;
		try {
			Date currentTime = dateFormat.parse(dateFormat.format(new Date()));// 现在系统当前时间
//            Date pastTime = dateFormat.parse(date);//过去时间
			long diff = currentTime.getTime() - pastTime.getTime();
			days = diff / (1000 * 60 * 60 * 24);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return days;
	}

	public static String getYearFirst(Date date) {
		return dateToStr(date, YYYY) + "-01-01";
	}

	public static Date timestampToDate(String timestamp) {
		Date date = new Date(Long.parseLong(timestamp));
		return date;
	}

	public static List<Date> getBetweenDates(String startStr, String endStr) {
		Date start = strToDate(startStr, TYPE1);
		Date end = strToDate(endStr, TYPE1);
		List<Date> result = new ArrayList<Date>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		while (tempStart.before(tempEnd)) {
			result.add(tempStart.getTime());
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		return result;
	}

	public static List<String> getBetweenDays(String startStr, String endStr) {
		Date start = strToDate(startStr, TYPE1);
		Date end = strToDate(endStr, TYPE1);
		List<String> result = new ArrayList<String>();
		Calendar tempStart = Calendar.getInstance();
		tempStart.setTime(start);
		tempStart.add(Calendar.DAY_OF_YEAR, 1);

		Calendar tempEnd = Calendar.getInstance();
		tempEnd.setTime(end);
		result.add(startStr);
		while (tempStart.before(tempEnd)) {
			result.add(dateToStr(tempStart.getTime(), TYPE1));
			tempStart.add(Calendar.DAY_OF_YEAR, 1);
		}
		result.add(endStr);
		return result;
	}

	public static String getTime(long seconds) {
		if (seconds == 0)
			return "";
		int minutes = ((int) seconds) / 60;
		int remainingSeconds = ((int) seconds) % 60;
		if (minutes != 0) {
			if (remainingSeconds != 0) {
				return minutes + "分" + remainingSeconds + "秒";
			} else {
				return minutes + "分";
			}
		}
		return remainingSeconds + "秒";
	}

	/**
	 *
	 * @param minDate 最小时间
	 * @param maxDate 最大时间
	 * @return 日期集合 格式为 年-月
	 * @throws Exception
	 */
	public static List<String> getMonthBetween(String minDate, String maxDate) throws Exception {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");// 格式化为年月
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMM");// 格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(sdf.parse(minDate));
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(sdf.parse(maxDate));
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf2.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}
		return result;
	}

	/**
	 *
	 * @param minDate 最小时间
	 * @param maxDate 最大时间
	 * @return 日期集合 格式为 年-月
	 * @throws Exception
	 */
	public static List<String> getMonthBetween(Date minDate, Date maxDate) {
		ArrayList<String> result = new ArrayList<String>();
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");// 格式化为年月

		Calendar min = Calendar.getInstance();
		Calendar max = Calendar.getInstance();

		min.setTime(minDate);
		min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

		max.setTime(maxDate);
		max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

		Calendar curr = min;
		while (curr.before(max)) {
			result.add(sdf2.format(curr.getTime()));
			curr.add(Calendar.MONTH, 1);
		}
		return result;
	}

	/**
	 * 获取某时间前霍后的n个月
	 * 
	 * @param date
	 * @param month
	 * @return
	 * @throws Exception
	 */
	public static Date getOfMonth(Date date, int month) throws Exception {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		return c.getTime();
	}


	/**
	 * 得到两个时间的时间差 格式：XX天XX小时XX分XX秒
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static long getDiffDay(Date d1, Date d2) {
		long millisecond = d1.getTime() - d2.getTime();
		long day = millisecond / (24 * 60 * 60 * 1000);
		return day;
	}
	/**
	 * 时间转cron格式
	 * 
	 * @return
	 */
	public static String formatTime(Date date) {
		String dateFormat = "ss mm HH dd MM ?";
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String formatTimeStr = sdf.format(date);
		return formatTimeStr;
	}


	public static String changeDateStr(String dateStr) {
		if (!TextUtils.isEmpty(dateStr)) {
			dateStr = dateStr.replace("/", "-");
			String[] array = dateStr.split("-");
			if (array != null && array.length == 3) {
				if (array[2].length() == 4) {
					return array[2]+"-"+array[1]+"-"+array[0];
				}
			}
			String newStr = array[0];
			for (int i = 1;i<array.length;i++) {
				if (array[i].length() == 1) {
					newStr = newStr+"-0"+array[i];
				} else {
					newStr = newStr+"-"+array[i];
				}
			}
			return newStr;
		}
		return null;
	}
	
	
	public static void main(String[] args) {
		String str = "2021-12-2210：40：00";
		str = str.replace("：", ":");
		Date date1 = DateFormatUtil.strToDate(str, "yyyy-MM-ddHH:mm:ss");
		String date2 = DateFormatUtil.dateToStr(date1, DateFormatUtil.TYPE_ALL);
		System.out.println(date2);
	}
	
}
