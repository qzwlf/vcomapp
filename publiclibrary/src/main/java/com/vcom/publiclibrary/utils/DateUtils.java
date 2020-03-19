package com.vcom.publiclibrary.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期工具类 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 */
public final class DateUtils {
    public final static String TAG = "DateUtils";
    /**
     * 英文简写（默认）如：2010-12-01
     */
    public static String FORMAT_SHORT = "yyyy-MM-dd";

    public static String FORMATLONG = "yyyyMMddHHmmss";

    public static String FORMAT_WEEK = "yyyy-MM-dd EEEE";
    /**
     * 英文全称 如：2010-12-01 23:15:06
     */
    public static String FORMAT_LONG = "yyyy-MM-dd HH:mm:ss";
    /**
     * 精确到毫秒的完整时间 如：yyyy-MM-dd HH:mm:ss.S
     */
    public static String FORMAT_FULL = "yyyy-MM-dd HH:mm:ss.S";
    /**
     * 中文简写 如：2010年12月01日
     */
    public static String FORMAT_SHORT_CN = "yyyy年MM月dd日";
    /**
     * 中文全称 如：2010年12月01日 23时15分06秒
     */
    public static String FORMAT_LONG_CN = "yyyy年MM月dd日  HH时mm分ss秒";
    /**
     * 精确到毫秒的完整中文时间
     */
    public static String FORMAT_FULL_CN = "yyyy年MM月dd日  HH时mm分ss秒SSS毫秒";
    private static Date dateCursor = new Date();

    /**
     * 获得默认的 date pattern
     */
    public static String getDatePattern() {
        return FORMAT_LONG;
    }

    /**
     * 根据预设格式返回当前日期
     *
     * @return
     */
    public static String getNow() {
        return format(new Date());
    }

    /**
     * 根据用户格式返回当前日期
     *
     * @param format
     * @return
     */
    public static String getNow(String format) {
        return format(new Date(), format);
    }

    /**
     * 使用预设格式格式化日期
     *
     * @param date
     * @return
     */
    public static String format(Date date) {
        return format(date, getDatePattern());
    }


    /**
     * 使用用户格式格式化日期
     *
     * @param date    日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    /**
     * 使用预设格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @return
     */
    public static Date parse(String strDate) {
        return parse(strDate, getDatePattern());
    }

    /**
     * 使用用户格式提取字符串日期
     *
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parse(long timeStamp) {
        return new Date(timeStamp);
    }

    /**
     * 在日期上增加数个整月
     *
     * @param date 日期
     * @param n    要增加的月数
     * @return
     */
    public static Date addMonth(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, n);
        return cal.getTime();
    }

    /**
     * 在日期上增加天数
     *
     * @param date 日期
     * @param n    要增加的天数
     * @return
     */
    public static Date addDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day);
        return cal.getTime();
    }

    /**
     * 获取时间戳
     */
    public static String getTimeString() {
        SimpleDateFormat df = new SimpleDateFormat(FORMAT_FULL);
        Calendar calendar = Calendar.getInstance();
        return df.format(calendar.getTime());
    }

    /**
     * 获取日期年份
     *
     * @param date 日期
     * @return
     */
    public static String getYear(Date date) {
        return format(date).substring(0, 4);
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date 日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date   日期字符串
     * @param format 日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parse(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /***
     * 获取当天的起始时间
     *
     * @return
     */
    public static Date getStartDay() {
        Calendar currentDate = new GregorianCalendar();

        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        return currentDate.getTime();
    }

    /**
     * 获取当天的最后时间
     *
     * @return
     */
    public static Date getEndDay() {
        Calendar currentDate = new GregorianCalendar();
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime();
    }

    /**
     * 获取几天前的其实时间
     *
     * @param day
     * @return
     */
    public static Date getBeforStartDay(int day) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.set(Calendar.DATE, currentDate.get(Calendar.DATE) + day);
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);
        Log.i(TAG, "getBeforStartDay: " + day * 24 + "-->" + format(currentDate.getTime()));
        return currentDate.getTime();
    }

    /**
     * 获取几天前的最后时间
     *
     * @param day
     * @return
     */
    public static Date getBeforEndDay(int day) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(new Date());
        currentDate.set(Calendar.DATE, currentDate.get(Calendar.DATE) + day);
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        Log.i(TAG, "getBeforEndDay: " + day * 24 + "-->" + format(currentDate.getTime()));
        return currentDate.getTime();
    }

    public static Date getDayStart(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.set(Calendar.DATE, currentDate.get(Calendar.DATE));
        currentDate.set(Calendar.HOUR_OF_DAY, 00);
        currentDate.set(Calendar.MINUTE, 00);
        currentDate.set(Calendar.SECOND, 00);
        return currentDate.getTime();
    }

    public static Date getDayEnd(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        currentDate.set(Calendar.DATE, currentDate.get(Calendar.DATE));
        currentDate.set(Calendar.HOUR_OF_DAY, 23);
        currentDate.set(Calendar.MINUTE, 59);
        currentDate.set(Calendar.SECOND, 59);
        return currentDate.getTime();
    }

    public static int getYearNew(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        return currentDate.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        return currentDate.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar currentDate = new GregorianCalendar();
        currentDate.setTime(date);
        return currentDate.get(Calendar.DAY_OF_MONTH);
    }

    public static int getIntervalTimeByMinute(Date date1, Date date2) {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        int min = calendar1.get(Calendar.MINUTE) - calendar2.get(Calendar.MINUTE);
        if (min < 0)
            min = 20;
        return min;
    }


    public static long getIntervalTimeBySecond(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();//这样得到的差值是微秒级别
        long days = diff / (1000 * 60 * 60 * 24);

        long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
        return diff / 1000;
    }

    public static long getTimestampNow() {
        Date time = new Date();
        return time.getTime();
    }

    //判断选择的日期是否是本周
    public static boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        if (paramWeek == currentWeek) {
            return true;
        }
        return false;
    }

    //判断选择的日期是否是今天
    public static boolean isToday(long time) {
        return isThisTime(time, "yyyy-MM-dd");
    }

    //判断选择的日期是否是本月
    public static boolean isThisMonth(long time) {
        return isThisTime(time, "yyyy-MM");
    }

    private static boolean isThisTime(long time, String pattern) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        String param = sdf.format(date);//参数时间
        String now = sdf.format(new Date());//当前时间
        if (param.equals(now)) {
            return true;
        }
        return false;
    }
}