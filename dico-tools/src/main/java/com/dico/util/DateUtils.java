package com.dico.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateUtils {


    static final SimpleDateFormat DAY_SDF = new SimpleDateFormat("yyyy-MM-dd");
    static final SimpleDateFormat MONTH_SDF = new SimpleDateFormat("yyyy-MM");
    static final SimpleDateFormat YEAR_SDF = new SimpleDateFormat("yyyy");

    /**
     * 获取两个日期字符串之间的日期集合
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getDayBetweenDateList(String startTime, String endTime) {
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = DAY_SDF.parse(startTime);
            Date endDate = DAY_SDF.parse(endTime);
            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(DAY_SDF.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.DATE, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取两个日期之间所有的月份集合
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getMonthBetweenDateList(String startTime, String endTime) {
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        try {
            // 转化成日期类型
            Date startDate = MONTH_SDF.parse(startTime);
            Date endDate = MONTH_SDF.parse(endTime);

            //用Calendar 进行日期比较判断
            Calendar calendar = Calendar.getInstance();
            while (startDate.getTime() <= endDate.getTime()) {
                // 把日期添加到集合
                list.add(MONTH_SDF.format(startDate));
                // 设置日期
                calendar.setTime(startDate);
                //把日期增加一天
                calendar.add(Calendar.MONTH, 1);
                // 获取增加后的日期
                startDate = calendar.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取两个日期之间的周
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static List<String> getWeekBetweenDateList(String startTime, String endTime) {
        int dayNum = getDayNumBetweenDate(startTime, endTime);
        int weekNum = 0;
        weekNum = dayNum / 7;
        weekNum++;
        int startWeek = dateToWeek(startTime);
        int endWeek = dateToWeek(endTime);
        if (startWeek > endWeek) {
            weekNum++;
        }
        // 声明保存日期集合
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < weekNum; i++) {
            list.add("第" + (i + 1) + "周");
        }
        return list;
    }

    /**
     * 根据日期获取当天是周几
     *
     * @param datetime 日期
     * @return 周几
     */
    public static int dateToWeek(String datetime) {
        Calendar cal = Calendar.getInstance();
        Date date;
        try {
            date = DAY_SDF.parse(datetime);
            cal.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return cal.get(Calendar.DAY_OF_WEEK) - 1;
    }

    /**
     * 获取日期相差天数
     *
     * @param startTime
     * @param endTime
     * @return
     */
    public static int getDayNumBetweenDate(String startTime, String endTime) {
        try {
            // 转化成日期类型
            Date startDate = DAY_SDF.parse(startTime);
            Date endDate = DAY_SDF.parse(endTime);
            Long spaceTime = endDate.getTime() - startDate.getTime();
            return Integer.valueOf(String.valueOf(spaceTime / (1000 * 3600 * 24)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 根据时间范围获得季度集
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getQuarterList(String beginDate, String endDate) {
        List<String> rangeSet = null;
        Date begin_date = null;
        Date end_date = null;
        String[] numStr = null;
        String Q = null;
        rangeSet = new java.util.ArrayList<String>();
        try {
            begin_date = MONTH_SDF.parse(beginDate);//定义起始日期
            end_date = MONTH_SDF.parse(endDate);//定义结束日期
        } catch (ParseException e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(begin_date);//设置日期起始时间
        Map<String, Object> dataMap = new LinkedHashMap<>();
        while (!dd.getTime().after(end_date)) {//判断是否到结束日期
            numStr = MONTH_SDF.format(dd.getTime()).split("-", 0);
            Q = calcQuarter(Integer.valueOf(numStr[1])) + "";
            dataMap.put(numStr[0].toString() + "年第" + Q + "季度", Q);
            //进行当前日期月份加1
            dd.add(Calendar.MONTH, 1);
        }
        for (String key : dataMap.keySet()) {
            rangeSet.add(key);
        }
        return rangeSet;
    }

    /**
     * 根据时间范围获得年度集
     *
     * @param beginDate
     * @param endDate
     * @return
     */
    public static List<String> getYearList(String beginDate, String endDate) {
        List<String> rangeSet = null;
        Date begin_date = null;
        Date end_date = null;
        String[] numStr = null;
        String Q = null;
        rangeSet = new java.util.ArrayList<String>();
        try {
            begin_date = MONTH_SDF.parse(beginDate);//定义起始日期
            end_date = MONTH_SDF.parse(endDate);//定义结束日期
        } catch (ParseException e) {
            System.out.println("时间转化异常，请检查你的时间格式是否为yyyy-MM或yyyy-MM-dd");
        }
        Calendar dd = Calendar.getInstance();//定义日期实例
        dd.setTime(begin_date);//设置日期起始时间
        Map<String, Object> dataMap = new LinkedHashMap<>();
        while (!dd.getTime().after(end_date)) {//判断是否到结束日期
            numStr = YEAR_SDF.format(dd.getTime()).split("-", 0);
            Q = calcQuarter(Integer.valueOf(numStr[0])) + "";
            dataMap.put(numStr[0].toString() + "年", Q);
            //进行当前日期月份加1
            dd.add(Calendar.YEAR, 1);
        }
        for (String key : dataMap.keySet()) {
            rangeSet.add(key);
        }
        return rangeSet;
    }

    /**
     * 根据月获得季度
     *
     * @param month 月
     * @return 季度
     */
    private static int calcQuarter(int month) {
        if (month == 1 || month == 2 || month == 3) {
            return 1;
        } else if (month == 4 || month == 5 || month == 6) {
            return 2;
        } else if (month == 7 || month == 8 || month == 9) {
            return 3;
        } else {
            return 4;
        }
    }

    /**
     * 根据时间获得本季度开始时间和下季度开始时间
     *
     * @param date
     * @return
     */
    public static List<Date> getQuarterBeginAndNextBeginDate(Date date) {
        List<Date> dateList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int quarter = calcQuarter(calendar.get(Calendar.MONTH) + 1);
        switch (quarter) {
            case 1:
                dateList = calc(calendar, 0, 3);
                break;
            case 2:
                dateList = calc(calendar, 3, 6);
                break;
            case 3:
                dateList = calc(calendar, 6, 9);
                break;
            case 4:
                dateList = calc(calendar, 9, 12);
                break;
        }
        return dateList;
    }

    /**
     *
     * @author Gaodl
     * @date 2020-04-21 15:18
     * @description： 根据年和季度获取季度开始时间和季度结束时间
     *
     */
    public static List<Date> getQuarterBeginAndEndDate(int year, int quarter) {
        List<Date> dateList = new LinkedList<>();
        Calendar bc = Calendar.getInstance();
        bc.set(Calendar.YEAR, year);
        bc.set(Calendar.DAY_OF_MONTH, 1);
        bc.set(Calendar.HOUR_OF_DAY, 0);
        bc.set(Calendar.MINUTE, 0);
        bc.set(Calendar.SECOND, 0);
        Calendar ec = Calendar.getInstance();
        ec.set(Calendar.YEAR, year);
        ec.set(Calendar.DAY_OF_MONTH, 0);
        ec.set(Calendar.HOUR_OF_DAY, 23);
        ec.set(Calendar.MINUTE, 59);
        ec.set(Calendar.SECOND, 59);
        Date beginDate = null;
        Date endDate = null;
        switch (quarter) {
            case 0:
                bc.set(Calendar.MONTH, 0);
                ec.set(Calendar.MONTH, 3);
                beginDate = bc.getTime();
                endDate = ec.getTime();
                break;
            case 1:
                bc.set(Calendar.MONTH, 3);
                ec.set(Calendar.MONTH, 6);
                beginDate = bc.getTime();
                endDate = ec.getTime();
                break;
            case 2:
                bc.set(Calendar.MONTH, 6);
                ec.set(Calendar.MONTH, 9);
                beginDate = bc.getTime();
                endDate = ec.getTime();
                break;
            case 3:
                bc.set(Calendar.MONTH, 9);
                ec.set(Calendar.MONTH, 12);
                beginDate = bc.getTime();
                endDate = ec.getTime();
                break;
        }
        dateList.add(beginDate);
        dateList.add(endDate);
        return dateList;
    }

    /**
     * 根据时间获得下季度开始时间
     *
     * @param date
     * @return
     */
    public static Date getNextQuarterBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int quarter = calcQuarter(calendar.get(Calendar.MONTH) + 1);
        switch (quarter) {
            case 1:
                calendar.set(calendar.get(Calendar.YEAR), 3, 1);
                break;
            case 2:
                calendar.set(calendar.get(Calendar.YEAR), 6, 1);
                break;
            case 3:
                calendar.set(calendar.get(Calendar.YEAR), 9, 1);
                break;
            case 4:
                calendar.set(calendar.get(Calendar.YEAR), 12, 1);
                break;
        }
        return calendar.getTime();
    }

    /**
     * 根据时间获得下一年开始时间
     *
     * @param date
     * @return
     */
    public static Date getNextYearBeginDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        return calendar.getTime();
    }

    /**
     * 根据时间获得下一年结束时间
     *
     * @param date
     * @return
     */
    public static Date getNextYearEndDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 2);
        calendar.set(Calendar.MONTH, 0);
        calendar.set(Calendar.DAY_OF_YEAR, 0);
        return calendar.getTime();
    }

    /**
     * 计算当前日期本季度开始时间和下季度开始时间
     *
     * @param calendar
     * @param beginMonth
     * @param nextBeginMonth
     * @return
     */
    private static List<Date> calc(Calendar calendar, int beginMonth, int nextBeginMonth) {
        List<Date> dateList = new ArrayList<>();
        calendar.set(calendar.get(Calendar.YEAR), beginMonth, 1);
        dateList.add(calendar.getTime());
        calendar.set(calendar.get(Calendar.YEAR), nextBeginMonth, 1);
        dateList.add(calendar.getTime());
        return dateList;
    }

    /**
     * 获取当前月第一天
     * @return
     */
    public static Date getCurrentMonthFirstDay(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        return c.getTime();
    }

    /**
     * 获取当前月最后一天
     * @return
     */
    public static Date getCurrentMonthLastDay(){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /**
     * 获取月第一天第一秒
     * @return
     */
    public static Date getMonthFirstDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c.getTime();
    }

    /**
     * 获取月最后一天最后一秒
     * @return
     */
    public static Date getMonthLastDay(Date date){
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return ca.getTime();
    }

    /**
     * 获取当前年第一天
     * @return
     */
    public static Date getCurrentYearFirstDay(){
        Date now = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(now);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    /**
     * 获取当前年最后一天
     * @return
     */
    public static Date getCurrentYearLastDay(){
        Calendar c = Calendar.getInstance();
        c.setTime(getCurrentYearFirstDay());
        c.add(Calendar.YEAR, 1);
        c.set(Calendar.DAY_OF_YEAR, 0);
        return c.getTime();
    }

    /**
     * 获取指定年第一天
     * @return
     */
    public static Date getYearFirstDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    /**
     * 获取指定年最后一天
     * @return
     */
    public static Date getYearLastDay(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.YEAR, 1);
        c.set(Calendar.DAY_OF_YEAR, 0);
        return c.getTime();
    }

    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, 2020);

    }
}
