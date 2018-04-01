package com.in_the_moment;


import java.util.Date;

public class GsrMeasurement {
    private double measurement;
    private String dateTime;
    private int i;

    public GsrMeasurement(double measurement, String dateTime, int i){
        this.measurement = measurement;
        this.dateTime = dateTime;
        this.i = i;
        //dateTime = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(dateTime);
    }

    public double getMeasurement() {
        return measurement;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setMeasurement(double measurement) {
        this.measurement = measurement;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getI() {
        return i;
    }
}
