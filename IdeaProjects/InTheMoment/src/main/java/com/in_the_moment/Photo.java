package com.in_the_moment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Photo {
    String ID;
    String dateTime;

    public Photo(String ID) {
        this.ID = ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd_HHmmss");
        SimpleDateFormat myFormat = new SimpleDateFormat("HH:mm:ss-dd/MM/yyyy");

        try {

            String reformattedStr = myFormat.format(fromUser.parse(dateTime.replace("_000.jpg", "")));

            this.dateTime = reformattedStr;
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
