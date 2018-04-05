package com.in_the_moment;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GsrMeasurement /*implements Comparable<GsrMeasurement>*/{
    private int measurement;
    private Date dateTime;
    private int increaseDiff;

    private DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");

    public GsrMeasurement(){}

    public GsrMeasurement(double measurement, String dateTime){

        int i =  (int)Math.round(measurement * 100);
        this.measurement = i;

        try {
            this.dateTime = dateTimeFormat.parse(dateTime);
        }catch (ParseException p) {
        }
    }

    /*@Override
    public int compareTo(GsrMeasurement o) {
        return this.getIncreaseDiff() - o.getIncreaseDiff();
    }*/

    public int getMeasurement() {
        return measurement;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getIncreaseDiff() {
        return increaseDiff;
    }

    public void setIncreaseDiff(int increaseDiff) {
        this.increaseDiff = increaseDiff;
    }
}
