package com.chxip.alarmsystem.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtil {
    /**
     * 获取当前系统时间
     * @return
     */
    public static Date getNowDate(){
        Date date = new Date();
        return date;
    }

    /**
     * 获取系统当前时间
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowDateStr(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    /**
     * 获取系统当前时间
     * yyyy-MM-dd HH:mm:ss
     * @return
     */
    public static String getNowDateStrOne(){
        SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
        sdf.applyPattern("yyyyMMddHHmmssSSS");// a为am/pm的标记
        Date date = new Date();// 获取当前时间
        return sdf.format(date);
    }

    /**
     * String 转 date
     * @return
     */
    public static Date StringToDate(String dateStr){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    /*
     * 将时间戳转换为时间
     */
    public static Date stampToDate(long s){
        return new Date(s);
    }

    /**
     * 判断date大小,startDate大返回true
     * @return
     */
    public static boolean dateBefore(Date startDate, Date endDate){
       return !startDate.before(endDate);
    }

    public static Date addDay(Date date, int day) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_MONTH, day);
        return c.getTime();
    }


    }
