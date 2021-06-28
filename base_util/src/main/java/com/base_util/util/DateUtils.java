package com.base_util.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    private boolean flag;
    private File file;

    public static Date getformatDate(String dateStr, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.parse(dateStr);

    }
    public static int compare_date3(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    static SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.US);

    public static Date parseDate(String str, String format) throws ParseException {
        if (str == null || "".equals(str)) {
            return null;
        }
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        return sdf.parse(str);
    }

    public static String formatDate(String date, String format) {
        if (date == null || "".equals(date)) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        String dateStr = sdf.format(new Date(date));
        return dateStr;
    }

    public static String dateStr2(String time) {
        SimpleDateFormat fullSimpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        String newDate = "";
        try {
            newDate = fullSimpleDateFormat.format(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    /**
     * 格式化时间
     *
     * @param date
     * @param format
     * @return
     */
    public static String formatDate(Date date, String format) {
        // Date date = new2 Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String format(long ms) {// 将毫秒数换算成x天x时x分x秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        return strDay + "天" + strHour + "小时" + strMinute + "分钟" + strSecond + "秒";
    }

    public static String fen(long ms) {// 将毫秒数换算成x天x时x分x秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        if(strHour.equals("00") ){
            return strMinute + "分钟" + strSecond + "秒";
        }else{
            return strHour+"小时"+strMinute + "分钟" + strSecond + "秒";
        }
    }

    public static String fen2(long ms) {// 将毫秒数换算成x天x时x分x秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "" + day : "" + day;
        String strHour = hour < 10 ? "" + hour : "" + hour;
        String strMinute = minute < 10 ? "" + minute : "" + minute;
        String strSecond = second < 10 ? "" + second : "" + second;
        return strMinute + "分";
    }
    public static String fenzhong(long ms) {// 将毫秒数换算成x天x时x分x秒
        int ss = 1000;
        int mi = ss * 60;
        int hh = mi * 60;
        int dd = hh * 24;

        long day = ms / dd;
        long hour = (ms - day * dd) / hh;
        long minute = (ms - day * dd - hour * hh) / mi;
        long second = (ms - day * dd - hour * hh - minute * mi) / ss;

        String strDay = day < 10 ? "0" + day : "" + day;
        String strHour = hour < 10 ? "0" + hour : "" + hour;
        String strMinute = minute < 10 ? "0" + minute : "" + minute;
        String strSecond = second < 10 ? "0" + second : "" + second;
        return strHour + "小时" + strMinute + "分";
    }
    public static String todayDate(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String todayDate2(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(date);
        return dateStr;
    }


    public static String dayDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    public static String zuoDate() {
        Calendar cal= Calendar.getInstance();
        cal.add(Calendar.DATE,-1);
        Date d=cal.getTime();
        SimpleDateFormat sp=new SimpleDateFormat("yyyy-MM-dd");
        String ZUOTIAN=sp.format(d);//获取昨天日期
        return sp.format(d);
    }
    public static String dayTime() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    public static String dayTimeOnline() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    public static String getStrDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String dayStringDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String caseNumber() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String yearDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static int monthDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String dateStr = sdf.format(date);

        return Integer.parseInt(dateStr);
    }

    public static int getDays(String date) throws ParseException {
        int days = 0;
        StringBuffer endTimeBu = new StringBuffer(date);
        String endTime = endTimeBu.insert(4, "-").insert(7, "-").toString();
        Date end = getDate(endTime);
        Date start = new Date();
        long interval = end.getTime() - start.getTime();
        days = (int) interval / 1000 / 60 / 60 / 24;
        return days;
    }

    public static Date getDate(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);

    }

    public static Date getDate2(String dateStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.parse(dateStr);

    }

    public static String string2Date(String time) {
        Date d = new Date(time);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time = formatter.format(d);
        return time;
    }

    public static String month() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    public static String month2() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    /**
     * @param date
     * @param days
     * @return
     * @description 获取后days天
     * @version 1.0
     * @author xieruibing
     * @update 2011-12-2 下午05:15:18
     */
    public static String getAfterDate(Date date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return df.format(calendar.getTime());
    }

    // 获取当月第一天
    public static String getFirstDayOfMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 计算当月最后一天,返回字符串
    public static String getDefaultDay() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 计算当月最后一天,返回字符串
    public static String getTwoMonth() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, -3);// 加一个月，变为下月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 获取当月第一天
    public static String getFirstDayOfMont() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        str = sdf.format(lastDate.getTime());
        return str;
    }

    // 计算当月最后一天,返回字符串
    public static String getDefaultDa() {
        String str = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar lastDate = Calendar.getInstance();
        lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
        lastDate.add(Calendar.MONTH, 1);// 加一个月，变为下月的1号
        lastDate.add(Calendar.DATE, -1);// 减去一天，变为当月最后一天
        str = sdf.format(lastDate.getTime());
        return str;
    }

    public static String getFileLastModifiedTime(String filePath) {
        File f = new File(filePath);
        // Date time=new2 Date(f.lastModified());//两种方法都可以
        if (f.exists()) {// 喜欢的话可以判断一下。。。
            System.out.println("File Exist.");
        }
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String result = formatter.format(time);
        return result;
    }

    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^[0-9]*$");
        Matcher m = p.matcher(mobiles);
        return m.matches();

    }

    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compare_date(String DATE1, String DATE2, String DATE3) {
        DateFormat df = new SimpleDateFormat("yyyy-MM");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            Date dt3 = df.parse(DATE3);
            if (dt1.getTime() > dt2.getTime()) {
                if (dt3.getTime() < dt2.getTime()) {
                    return 3;
                } else {
                    return 2;
                }
            } else if (dt1.getTime() < dt2.getTime()) {
                if (dt3.getTime() < dt1.getTime()) {
                    return 1;
                } else {
                    return 3;
                }
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static String getAfterDate(String date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        return df.format(calendar.getTime());
    }


    public static String getAfterDate3(String date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - days);
        return df.format(calendar.getTime());
    }
    public static String getDate(String date, int days) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(df.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + days);
        return df.format(calendar.getTime());
    }
    public static int getDays(int year, int month) {
        int days;
        int FebDay = 28;
        if (isLeap(year))
            FebDay = 29;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                days = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                days = 30;
                break;
            case 2:
                days = FebDay;
                break;
            default:
                days = 0;
                break;
        }
        return days;
    }

    // 判断闰年
    public static boolean isLeap(int year) {
        if (((year % 100 == 0) && year % 400 == 0) || ((year % 100 != 0) && year % 4 == 0))
            return true;
        else
            return false;
    }

    // 返回当前年份
    public static int getYear() {
        Date date = new Date();
        String year = new SimpleDateFormat("yyyy").format(date);
        return Integer.parseInt(year);
    }

    // 返回当前月份
    public static int getMonth() {
        Date date = new Date();
        String month = new SimpleDateFormat("MM").format(date);
        return Integer.parseInt(month);
    }

    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        String dayForWee = "";
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if (dayForWeek == 1) {
            dayForWee = "周一";
        }
        if (dayForWeek == 2) {
            dayForWee = "周二";
        }
        if (dayForWeek == 3) {
            dayForWee = "周三";
        }
        if (dayForWeek == 4) {
            dayForWee = "周四";
        }
        if (dayForWeek == 5) {
            dayForWee = "周五";
        }
        if (dayForWeek == 6) {
            dayForWee = "周六";
        }
        if (dayForWeek == 7) {
            dayForWee = "周日";
        }
        return dayForWee;
    }
    public static String dayForWeek2(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int dayForWeek = 0;
        String dayForWee = "";
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        if (dayForWeek == 1) {
            dayForWee = "周一";
        }
        if (dayForWeek == 2) {
            dayForWee = "周二";
        }
        if (dayForWeek == 3) {
            dayForWee = "周三";
        }
        if (dayForWeek == 4) {
            dayForWee = "周四";
        }
        if (dayForWeek == 5) {
            dayForWee = "周五";
        }
        if (dayForWeek == 6) {
            dayForWee = "周六";
        }
        if (dayForWeek == 7) {
            dayForWee = "周日";
        }
        return dayForWee;
    }




    /**
     * 日期转星期
     *
     * @param datetime
     * @return
     */
    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "周日", "周一", "周二", "周三", "周四", "周五", "周六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }
    /**
     * 根据路径删除指定的目录或文件，无论存在与否
     *
     * @param sPath
     *            要删除的目录或文件
     * @return 删除成功返回 true，否则返回 false。
     */
    public boolean DeleteFolder(String sPath) {
        flag = false;
        file = new File(sPath);
        // 判断目录或文件是否存在
        if (!file.exists()) { // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) { // 为文件时调用删除文件方法
                return deleteFile(sPath);
            } else { // 为目录时调用删除目录方法
                return deleteDirectory(sPath);
            }
        }
    }

    /**
     * 删除目录（文件夹）以及目录下的文件
     *
     * @param sPath
     *            被删除目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public boolean deleteDirectory(String sPath) {
        // 如果sPath不以文件分隔符结尾，自动添加文件分隔符
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        // 删除文件夹下的所有文件(包括子目录)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // 删除子目录
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // 删除当前目录
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 删除单个文件
     *
     * @param sPath
     *            被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String sPath) {
        flag = false;
        file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static void delete(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                delete(childFiles[i]);
            }
            file.delete();
        }
    }

    private static String mYear;
    private static String mMonth;
    private static String mDay;
    private static String mWay;

    public static String StringData() {
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if ("1".equals(mWay)) {
            mWay = "天";
        } else if ("2".equals(mWay)) {
            mWay = "一";
        } else if ("3".equals(mWay)) {
            mWay = "二";
        } else if ("4".equals(mWay)) {
            mWay = "三";
        } else if ("5".equals(mWay)) {
            mWay = "四";
        } else if ("6".equals(mWay)) {
            mWay = "五";
        } else if ("7".equals(mWay)) {
            mWay = "六";
        }
        return "星期" + mWay;
    }

    public static String StringAnwer(int key) {
        String zimu = "";
        switch (key) {
            case 0:
                zimu = "A";
                break;
            case 1:
                zimu = "B";
                break;
            case 2:
                zimu = "C";
                break;
            case 3:
                zimu = "D";
                break;
            case 4:
                zimu = "E";
                break;
            case 5:
                zimu = "F";
                break;
            case 6:
                zimu = "G";
                break;
            case 7:
                zimu = "H";
                break;
            case 8:
                zimu = "I";
                break;
            case 9:
                zimu = "J";
                break;
            default:
                break;
        }
        return zimu;
    }


    public static int compare_date2(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compare_date12(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取时间戳
     *
     * @return / 1000  的时间戳
     */
    public static String getCurrentTime() {
        String time = System.currentTimeMillis() / 1000 + "";
        return time;
    }

    public static String getDateYc(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sDate=sdf2.format(date);
        return sDate;
    }

    public static String getDateYc2(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZZ");
        Date date = new Date();
        try {
            date = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf2=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String sDate=sdf2.format(date);
        return sDate;
    }

    public static String getTimePick(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }

}

