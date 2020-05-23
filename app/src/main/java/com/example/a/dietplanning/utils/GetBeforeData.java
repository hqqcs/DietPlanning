package com.example.a.dietplanning.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class GetBeforeData {
    public GetBeforeData(){

    }

    /**
     * 获取当前时间往前往后n的日期
     * eg: date ==null 默认是系统当前时间  否则以date为时间起点
     * n > 0 往后
     * n = 0 当前时间
     * n < 0 往前
     * @author zhangheng5@lenovo.com
     * @param date
     * @param n
     * @return
     */
    public static List<String> getBeforeData(Date date, int n){
        List<String> list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        Date today = new Date();
        if(null != date){
            today = date;
        }
        c.setTime(today);
        if(n > 0){
            for(int i=0;i<=n;i++){
                c.add(Calendar.DATE, i);
                list.add(new SimpleDateFormat("yyyy-MM-dd-EEEE").format(c.getTime()));
                c.setTime(today);
            }
        }else if (n < 0){
            for(int i=n;i<=0;i++){
                c.add(Calendar.DATE, i);
                list.add(new SimpleDateFormat("yyyy-MM-dd-EEEE").format(c.getTime()));
                c.setTime(today);
            }
        }else{
            c.add(Calendar.DATE, 0);
            list.add(new SimpleDateFormat("yyyy-MM-dd-EEEE").format(c.getTime()));
            c.setTime(today);
        }

        return list;
    }

    /**
     * 定义一个方法用来格式化获取到的时间
     */
    public static String formatTime(int time) {
        if (time  % 60 < 10) {
            return time / 60 + ":0" + time % 60;
        } else {
            return time / 60 + ":" + time  % 60;
        }
    }

    /**
     * 将字符串时间转化成数字
     */
    public static int transforTime(String time){
        String[] timeS=time.split(":");
        int t=Integer.parseInt(timeS[0])+1;
        return t;
    }
}

