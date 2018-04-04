package com.in_the_moment;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GsrMeasurement {
    private double measurement;
    private Date dateTime;
    private int i;
    private DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy");

    public GsrMeasurement(){}

    public GsrMeasurement(double measurement, String dateTime, int i){
        this.measurement = measurement;

        try {
            this.dateTime = dateTimeFormat.parse(dateTime);
        }catch (ParseException p) {
        }

        this.i = i;

    }

    public double getMeasurement() {
        return measurement;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public void setDateTime(String dateTime) {

        try {
            this.dateTime = dateTimeFormat.parse(dateTime);
        }catch (ParseException p) {
        }
    }

    public int getI() {
        return i;
    }
}
