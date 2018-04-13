package com.in_the_moment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Photo {
    private String ID;
    private Date dateTime;
    private String photoDescription;
    private DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");

    public Photo(String ID, String dateTime) {
        this.ID = ID;

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");

        try {

            String reformattedStringDate = myFormat.format(fromUser.parse(dateTime.replace(".jpg", "")));
            Date javaUtilDateFormat = dateTimeFormat.parse(reformattedStringDate);
            javaUtilDateFormat.setTime(javaUtilDateFormat.getTime() + 2 * 60 * 60 * 1000 );
            this.dateTime = javaUtilDateFormat;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public String getID() {
        return ID;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");

        try {

            String reformattedStringDate = myFormat.format(fromUser.parse(dateTime.replace(".jpg", "")));
            Date javaUtilDateFormat = dateTimeFormat.parse(reformattedStringDate);
            javaUtilDateFormat.setTime(javaUtilDateFormat.getTime() + 2 * 60 * 60 * 1000 );
            this.dateTime = javaUtilDateFormat;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public String getPhotoDescription() {
        return photoDescription;
    }

    public void setPhotoDescription(String photoDescription) {
        this.photoDescription = photoDescription;
    }
}
