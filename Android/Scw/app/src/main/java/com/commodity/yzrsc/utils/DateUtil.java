package com.commodity.yzrsc.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liyushen on 2017/5/9 21:40
 * 日期工具类
 */
public class DateUtil {
    public static  SimpleDateFormat sdf= new SimpleDateFormat("MM-dd HH:mm");
    public static  SimpleDateFormat sdf2= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String longToDate(long datelong){
        return  sdf.format(new Date(datelong));
    }

    public static String getBetweenTime(String startDate,String endDate){
        long between = 0;
        Date begin;
        Date end;
        String resultStr="";
        try {
            begin= sdf2.parse(startDate);
            if (endDate==null||endDate.isEmpty()){
                end = new Date();
                endDate=sdf2.format(end);
                end=sdf2.parse(endDate);
            }else {
                end = sdf2.parse(endDate);
            }
            between = (end.getTime() - begin.getTime());// 得到两者的毫秒数
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        long day = between / (24 * 60 * 60 * 1000);
        long hour = (between / (60 * 60 * 1000) - day * 24);
        long min = ((between / (60 * 1000)) - day * 24 * 60 - hour * 60);
 //       long s = (between / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
//        long ms = (between - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
//                - min * 60 * 1000 - s * 1000);
//        System.out.println(day + "天" + hour + "小时" + min + "分" + s + "秒" + ms
//                + "毫秒");
        //Log.i( "getBetweenTime: ",startDate+"=="+endDate );
        if (startDate.substring(0,10).equals(endDate.substring(0,10))){//如果是在同一天
            if (hour==0){
                resultStr=Math.abs(min) + "分前";
            }else {
                resultStr=Math.abs(hour) + "小时前";
            }
            return resultStr;
        }else {//如果不在同一天
            return startDate.length()>10?startDate.substring(5,10):startDate;
        }
    }
}
