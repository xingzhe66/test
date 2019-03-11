package com.dcits.comet.batch.util;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    private static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    private static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    private static final String YYYYMMDD = "yyyy-MM-dd";
    private static final String HHMMSS = "HH:mm:ss";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

    private DateUtil() {
    }
    //todo 事务不安全。

    public static String getCurrentStr() {
        return sdf.format(new Date());
    }

    public static String[] getCurrentDatetimeStrs() {
        String[] strs = new String[2];
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        strs[0] = sdf.format(date);
        sdf = new SimpleDateFormat("HH:mm:ss");
        strs[1] = sdf.format(date);
        return strs;
    }

    public static String getCurrentDateStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getCurrentTimeStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getCurrentMilliStr() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return sdf.format(new Date());
    }

    public static String getStr(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(date);
    }

    public static Date getDate(String yyyyMMddHHmmss) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            return sdf.parse(yyyyMMddHHmmss);
        } catch (ParseException var3) {
            throw new RuntimeException(var3);
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
        int day = calendar.get(5);
        calendar.set(5, day - i);
        return calendar.getTime();
    }

    public static void main(String[] args) throws Exception {
        String str = "20150831101033";
        String curr = getCurrentStr();
        System.out.println(curr);
        if (str.compareTo(curr) < 0) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        System.out.println(getDate(str));
        String[] datetime = getCurrentDatetimeStrs();
        System.out.println(datetime[0]);
        System.out.println(datetime[1]);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016, 2, 1);
        System.out.println(calendar.getTime());
        System.out.println(getBeforeDay(calendar));
    }

    public static Object parse(String date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);

        try {
            Date d = sdf.parse(date);
            return d;
        } catch (ParseException var4) {
            throw new RuntimeException("日期转换错误", var4);
        }
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
