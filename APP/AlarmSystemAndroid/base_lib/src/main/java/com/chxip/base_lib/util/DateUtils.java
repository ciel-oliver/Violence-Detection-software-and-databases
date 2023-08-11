package com.chxip.base_lib.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2017/3/7.
 */
public class DateUtils {

    public static String YYYYMMDDHHMM = "yyyy-MM-dd HH:mm";
    public static String YYYY = "yyyy";
    public static String MMDDHHMM = "MM-dd HH:mm";
    public static String MM = "MM";
    public static String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static String HHMM = "HH:mm";
    public static String YYYYMMDD = "yyyy-MM-dd";
    public static String YYYYMMDDONE = "yyyyMMdd";
    public static String YYYYMM = "yyyy-MM";
    public static String YYYYMMOne = "yyyy年MM月";
    public static String MMDD = "MM-dd";
    public static String MMDDONE = "MM/dd";
    public static String HH = "HH";

    /**
     * 判断两个日期差多少天
     */
    public static int DifferenceDay(String smallDate, String bigDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(smallDate);
            Date d2 = sdf.parse(bigDate);
            long intervalMilli = d2.getTime() - d1.getTime();

            return (int) (intervalMilli / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断两个日期差多少天
     */
    public static int DifferenceDay(String smallDate, String bigDate,String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date d1 = sdf.parse(smallDate);
            Date d2 = sdf.parse(bigDate);
            long intervalMilli = d2.getTime() - d1.getTime();

            return (int) (intervalMilli / (24 * 60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断两个日期差多少小时
     */
    public static int differenceHour(String smallDate, String bigDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMMSS);
        try {
            Date d1 = sdf.parse(smallDate);
            Date d2 = sdf.parse(bigDate);
            long intervalMilli = d2.getTime() - d1.getTime();

            return (int) (intervalMilli / (60 * 60 * 1000));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 转换日期格式
     *
     * @param oldDate
     * @param oldDataFormat
     * @param newDateFormat
     * @return
     */
    public static String convertDate(String oldDate, String oldDataFormat, String newDateFormat) {
        try {
            if (null != oldDate) {
                SimpleDateFormat sdf = new SimpleDateFormat(oldDataFormat);
                Date date = sdf.parse(oldDate);
                SimpleDateFormat sf = new SimpleDateFormat(newDateFormat);
                return sf.format(date);
            } else {
                return "";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static String convertDateByLong(long dateLong, String dateFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            Date date = new Date(dateLong);
            return sdf.format(date);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String addDay(int num, String newDate) {
        String enddate = "";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

        try {
            Date date = sdf.parse(newDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, num);
            Date time = rightNow.getTime();
            enddate = sdf.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return enddate;
    }

    /**
     * 指定日期加上天数后的日期
     *
     * @param num     为增加的天数
     * @param newDate 创建时间
     * @return
     * @throws ParseException
     */
    public static String addDay(int num, String newDate,String format) {
        String enddate = "";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);

        try {
            Date date = sdf.parse(newDate);
            Calendar rightNow = Calendar.getInstance();
            rightNow.setTime(date);
            rightNow.add(Calendar.DAY_OF_YEAR, num);
            Date time = rightNow.getTime();
            enddate = sdf.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return enddate;
    }


    /**
     * 日期加多少月
     */
    public static String addMonth(String date, int month) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date d1 = sdf.parse(date);
            Calendar cl = Calendar.getInstance();
            cl.setTime(d1);

            cl.add(Calendar.MONTH, month);
            String temp = sdf.format(cl.getTime());
            return temp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 日期加多少分钟
     */
    public static String addMinutes(String date, int minutes) {
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDHHMM);
        try {
            Date d1 = sdf.parse(date);
            Calendar cl = Calendar.getInstance();
            cl.setTime(d1);

            cl.add(Calendar.MINUTE, minutes);
            String temp = sdf.format(cl.getTime());
            return temp;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 转换时间格式 yyyy-MM-dd  去掉中间的T
     *
     * @param dateString
     * @return
     */
    public static String getDateTYYYYMMDD(String dateString) {
        try {
            if (null != dateString) {
                if (dateString.length() > 10) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                    Date date = sdf.parse(dateString);
                    SimpleDateFormat sf = new SimpleDateFormat(YYYYMMDD);
                    return sf.format(date);
                } else {
                    return dateString;
                }
            } else {
                return "";
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 比较2个日期的大小 startTime大返回true else 返回false
     *
     * @param startTime
     * @param endTime
     * @param format
     * @return
     */
    public static boolean isDate(String startTime, String endTime, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            if (d1.getTime() >= d2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 比较2个日期的大小 startTime大返回true else 返回false
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static boolean isDateHHMM(String startTime, String endTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(HHMM);
            Date d1 = sdf.parse(startTime);
            Date d2 = sdf.parse(endTime);
            if (d1.getTime() >= d2.getTime()) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 根据日期获取月份
     *
     * @return
     */
    public static int getMonth(String dateStr, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);//注意月份是MM
        Date date = null;
        try {
            date = simpleDateFormat.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int month = cal.get(Calendar.MONTH);
            return month;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;


    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTimeYYYYMMDD() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        return key;
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getNowTimeYYYYMMDDHHmmssSSS() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        return key;
    }

    /**
     * 获取前日期，带毫秒
     *
     * @return
     */
    public static String getCurrentTime() {
        return getCurrentTime("yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 获取前日期，带毫秒
     *
     * @return
     */
    public static String getCurrentTime1() {
        return getCurrentTime("yyyyMMddHHmmss");
    }

    /**
     * 获取前日期，传入日期格式
     *
     * @return
     */
    public static String getCurrentTime(String format) {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }


}
