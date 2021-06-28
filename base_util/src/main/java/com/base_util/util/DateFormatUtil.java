package com.base_util.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class DateFormatUtil {

    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYNMMDD = "yyyy年MM月dd日";
    /**
     * 日期格式"yyyyMMdd"
     */
    public static final String yyyymmdd = "yyyyMMdd";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    /**
     * 月份
     */
    public static final String YYYY_MM= "yyyy-MM";
    /**
     * 月份
     */
    public static final String YYYY= "yyyy";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static final String YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";

    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm:ss";
    public static final String TYPE_ALL= "yyyy-MM-dd HH:mm";
    public static String dateToStr(String format, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static Date strToDate(String format, String dateStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 获得当前的时间转化为Long类型
     *
     * @return
     */
    public static Long getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        String currentDate = sdf.format(new Date());
        long currentDateLong = Long.parseLong(currentDate);
        return Long.valueOf(currentDateLong);
    }

    /**
     * 得到昨天当时的时间
     *
     * @param date
     * @return
     */
    public static Date getYesterday(Date date) {
        return getDiff(date, 'd', -1);
    }

    /**
     * 得到明天当时的时间
     *
     * @param date
     * @return
     */
    public static Date getTomorrow(Date date) {
        return getDiff(date, 'd', 1);
    }

    /**
     * 得到昨天当时的时间
     *
     * @param dateStr
     * @return
     */
    public static Date getYesterday(String dateStr) {
        Date date = getYesterday(strToDate(YYYY_MM_DD, dateStr));
        return date;
    }

    /**
     * 得到明天当时的时间
     *
     * @param dateStr
     * @return
     */
    public static Date getTomorrow(String dateStr) {
        Date date = getTomorrow(strToDate(YYYY_MM_DD, dateStr));
        return date;
    }

    /**
     * 时间加减
     *
     * @param date
     * @param part y:年;m:月;d:天
     * @param diff
     * @return
     */
    public static Date getDiff(Date date, char part, int diff) {
        Calendar c = Calendar.getInstance();
        c.clear();
        c.setTime(date);
        switch (part) {
            case 'y':
                c.add(Calendar.YEAR, diff);
                break;
            case 'M':
                c.add(Calendar.MONTH, diff);
                break;
            case 'd':
                c.add(Calendar.DAY_OF_MONTH, diff);
                break;
            case 'w':
                c.add(Calendar.WEEK_OF_YEAR, diff);
                break;
            case 'h':
                c.add(Calendar.HOUR_OF_DAY, diff);
                break;
            case 'm':
                c.add(Calendar.MINUTE, diff);
                break;
            case 's':
                c.add(Calendar.SECOND, diff);
                break;
        }
        return c.getTime();
    }

    /**
     * 根据日期获取星期(例：2014-06-27 为 周五)
     *
     * @param date
     * @return
     */
    public static String getWeekDayStr(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int week = c.get(Calendar.DAY_OF_WEEK); // 获取当前日期星期，英国那边的算法(周日算一周第一天)
        String weekStr = "周六";
        switch (week) {
            case 1:
                weekStr = "周日";
                break;
            case 2:
                weekStr = "周一";
                break;
            case 3:
                weekStr = "周二";
                break;
            case 4:
                weekStr = "周三";
                break;
            case 5:
                weekStr = "周四";
                break;
            case 6:
                weekStr = "周五";
                break;
            default:
                break;
        }
        return weekStr;
    }
    /**
     *  获取当前日期的所在年份的第一天
     */
    public static String getYearFirst(Date date) {
        return dateToStr(YYYY,date)+"-01-01";
    }
    /**
     *  获取当前日期的所在月份的第一天
     */
    public static String getMonthFirst(Date date) {
        return dateToStr(YYYY_MM,date)+"-01";
    }
    /**
     * 得到上个月的日期
     *
     * @param date 这个月的时间
     * @return 返回上个月的时间
     */
    public static Date getLastMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, -1);
        Date month = cal.getTime();
        return month;
    }

    /**
     * 获取两个日期 中 全部的日期集合
     *
     * @param sDate
     * @param eDate
     * @return
     */
    public static List<Date> getList(Date sDate, Date eDate) {
        Calendar sc = Calendar.getInstance();
        Calendar ec = Calendar.getInstance();
        Date s1 = DTD(sDate, "yyyy-MM-dd");
        Date e1 = DTD(eDate, "yyyy-MM-dd");
        List<Date> list = new ArrayList<Date>();
        ec.setTime(e1);
        while (true) {
            sc.setTime(s1);
            if (sc.getTimeInMillis() < ec.getTimeInMillis()) {
                list.add(s1);
                Date date = getDate(s1, 4, 1);
                s1 = date;
            } else {
                list.add(s1);
                break;
            }
        }
        return list;
    }

    /**
     * 日期类型 转换 DateToDate
     *
     * @param date
     * @param type
     * @return
     */
    public static Date DTD(Date date, String type) {
        return DateFormatUtil.strToDate(type, DateFormatUtil.dateToStr(type, date)
        );
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

    public static int getDatetoInt(Date date) {
        String dateStr = dateToStr(yyyymmdd, date);
        return Integer.parseInt(dateStr);
    }

    //根据给定日期，获取一周中的日期
    public static String getTimeInterval(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        String imptimeBegin = sdf.format(cal.getTime());
        cal.add(Calendar.DATE, 6);
        String imptimeEnd = sdf.format(cal.getTime());
        return imptimeBegin + "," + imptimeEnd;
    }

    //获取当前月的第一天和最后一天
    public static String[] getCurrentMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYY_MM_DD);
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);//设置为1号,当前日期既为本月第一天
        String first = sdf.format(c.getTime());

        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = sdf.format(c.getTime());
        String[] monthTime = {first, last};
        return monthTime;
    }

    //获取本年的起止时间
    public static Date[] getCurrentYear() {
        Date[] dates = new Date[2];
        dates[0] = getBeginDayOfYear();
        dates[1] = getEndDayYear();
        return dates;
    }

    //获取本年的开始时间
    public static Date getBeginDayOfYear() {
        int year = getNowYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 0, 1);
        return calendar.getTime();
    }

    //获取本年的结束时间
    public static Date getEndDayYear() {
        int year = getNowYear();
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, 11, 1);
        int day = calendar.getActualMaximum(Calendar.DATE);
        calendar.set(year, 11, day);
        return calendar.getTime();

    }

    //获取今年是哪一年
    public static Integer getNowYear() {
        Date date = new Date();
        GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
        gc.setTime(date);

        return Integer.valueOf(gc.get(Calendar.YEAR));
    }







}
