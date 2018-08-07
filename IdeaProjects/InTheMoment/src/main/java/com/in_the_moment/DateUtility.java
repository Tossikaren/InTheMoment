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
}
