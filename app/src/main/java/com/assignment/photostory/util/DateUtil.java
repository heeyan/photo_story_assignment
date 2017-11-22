package com.assignment.photostory.util;

import java.util.Date;

public class DateUtil {
    public static String parceToDateTimeString(Date d){
        Date date = new Date(d.getTime());
        return (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월 " + date.getDate()+ "일 "
                + date.getHours() + "시 " + date.getMinutes() + "분";
    }

    public static String parceToMonthString(Date d){
        Date date = new Date(d.getTime());
        return (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월";
    }
}
