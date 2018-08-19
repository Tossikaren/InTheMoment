package com.in_the_moment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public interface DateUtility {
    DateFormat dateTimeFormat = new SimpleDateFormat("yyyyMMdd");
    DateFormat newDateTimeFormat = new SimpleDateFormat("EEE dd. MMMM yyyy");

    default String parseDate(String string){
        Date date = null;
        try {
            date = dateTimeFormat.parse(string);
        }catch (ParseException p) {
        }
        return newDateTimeFormat.format(date);
    }

    default String parseDateComboBox(Date date){
        return newDateTimeFormat.format(date);
    }

    default String parseTopTenDate(Date date){
        return dateTimeFormat.format(date);
    }

    default Date parsePhotoDate(String dateTime){
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");
        Date javaUtilDateFormat = null;
        try {
            String reformattedStringDate = myFormat.format(fromUser.parse(dateTime.replace(".jpg", "")));
            javaUtilDateFormat = myFormat.parse(reformattedStringDate);
            javaUtilDateFormat.setTime(javaUtilDateFormat.getTime() + 2 * 60 * 60 * 1000 );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return javaUtilDateFormat;
    }
}
