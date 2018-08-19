package com.in_the_moment.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class GsrMeasurement extends CollectedData{
    private int measurement;
    private int increaseDiff;

    public GsrMeasurement(double measurement, Date dateTime){
        super(dateTime);
        this.measurement = calculateMeasurement(measurement);
    }

    public GsrMeasurement(UUID uuid, int measurement, Date dateTime, int increaseDiff) {
        super(uuid, dateTime);
        this.measurement = measurement;
        this.increaseDiff = increaseDiff;
    }

    public int getMeasurement() {
        return measurement;
    }

    public int getIncreaseDiff() {
        return increaseDiff;
    }

    public void setIncreaseDiff(int increaseDiff) {
        this.increaseDiff = increaseDiff;
    }

    @Override
    public String toString() {
        return "GsrMeasurement{" +
                "measurement=" + measurement +
                ", increaseDiff=" + increaseDiff +
                '}';
    }

    private int calculateMeasurement(double measurement) {
        return  (int)Math.round(measurement * 100);
    }

    /*private Date dateUtility(String dateTime){
        //String dateTimeFormat = new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").format(dateTime);
        Date javaSqlTimestampFormat = null;
        try{
            javaSqlTimestampFormat = new Date(new SimpleDateFormat("HH:mm:ss.SSS-dd/MM/yyyy").parse(dateTime).getTime());
        }catch (ParseException p) {
            System.out.println(p);
        }
        return javaSqlTimestampFormat;
    }*/
}
