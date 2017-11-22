package com.assignment.photostory.util;

import java.util.Date;

/**
 * Created by heeyan on 16. 7. 29..
 */
public class DateUtil {
    public static Date parceToDate(long dateTime){
//        Date date;
//        try{
//            SimpleDateFormat sdf = new SimpleDateFormat(FORMAT, Locale.KOREA);
//            date = sdf.parse(format);
//        }catch (ParseException e){
//            long now = System.currentTimeMillis();
//            date =
//            Log.d("tttttt", "e :: " + e);
//        }
        return new Date(dateTime);
    }

    public static String parceToDateTimeString(Date d){
        Date date = parceToDate(d.getTime());
        return (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월 " + date.getDate()+ "일 "
                + date.getHours() + "시 " + date.getMinutes() + "분";
    }

    public static String parceToMonthString(Date d){
        Date date = parceToDate(d.getTime());
        return (date.getYear() + 1900) + "년 " + (date.getMonth() + 1) + "월";
    }
}
