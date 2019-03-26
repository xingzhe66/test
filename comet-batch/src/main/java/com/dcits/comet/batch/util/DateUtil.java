/**
 * 
 */
package com.dcits.comet.batch.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 
 * @author Hing
 * 
 */
public class DateUtil {
    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    private static final String YYYYMMDD = "yyyy-MM-dd";
    private static final String HHMMSS = "HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);


    private DateUtil() {
    }


    public static String getCurrentStr() {

        return sdf.format(new Date());
    }


    public static String[] getCurrentDatetimeStrs() {
        String[] strs = new String[2];
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        strs[0] = sdf.format(date);
        sdf = new SimpleDateFormat(HHMMSS);
        strs[1] = sdf.format(date);
        return strs;
    }


    public static String getCurrentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDD);
        return sdf.format(new Date());
    }


    public static String getCurrentTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(HHMMSS);
        return sdf.format(new Date());
    }


    public static String getCurrentMilliStr() {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSSSSS);
        return sdf.format(new Date());
    }


    public static String getStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        return sdf.format(date);
    }


    public static Date getDate(String yyyyMMddHHmmss) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        try {
            return sdf.parse(yyyyMMddHHmmss);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getNow() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    public static String getNowDate10() {
        return getNow().substring(0, 10);
    }


    public static String getNowTime8() {
        String now = getNow();
        return now.substring(10, now.length());
    }


    public static Date getBeforeDay(Calendar calendar) {
        return getBeforeDay(calendar, 1);
    }


    public static Date getBeforeDay(Calendar calendar, int i) {
        int day = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, day - i);
        return calendar.getTime();
    }


    public static void main(String[] args) throws Exception {
        String str = "20150831101033";
        String curr = DateUtil.getCurrentStr();
        System.out.println(curr);
        if (str.compareTo(curr) < 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }
        System.out.println(DateUtil.getDate(str));
        // for (int i = 0; i < 1000000; i++) {
        // System.out.println(DateUtil.getCurrentMilliStr() +
        // RandomUtils.nextInt(9999));
        // }
        String[] datetime = DateUtil.getCurrentDatetimeStrs();
        System.out.println(datetime[0]);
        System.out.println(datetime[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 02, 01);
        System.out.println(calendar.getTime());
        System.out.println(getBeforeDay(calendar));

    }


    public static Object parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException e) {
            throw new RuntimeException("日期转换错误", e);
        }
    }


    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
