package com.in_the_moment.Model;

import java.util.Date;
import java.util.UUID;

public class Moment {
    private final UUID uuid;
    private final GsrMeasurement gsrMeasurement;
    private final Photo photo;
    private final Date momentDate;
    private String momentDescription;

    public Moment(GsrMeasurement gsrMeasurement, Photo photo, Date momentDate) {
        this.uuid = UUID.randomUUID();
        this.gsrMeasurement = gsrMeasurement;
        this.photo = photo;
        this.momentDate = momentDate;
    }

    public Moment(UUID uuid, GsrMeasurement gsrMeasurement, Photo photo, String momentDescription, Date momentDate) {
        this.uuid = uuid;
        this.gsrMeasurement = gsrMeasurement;
        this.photo = photo;
        this.momentDescription = momentDescription;
        this.momentDate = momentDate;
    }

    public void setMomentDescription(String momentDescription) {
        this.momentDescription = momentDescription;
    }

    public GsrMeasurement getGsrMeasurement() {
        return gsrMeasurement;
    }

    public Photo getPhoto() {
        return photo;
    }

    public Date getMomentDate() {
        return momentDate;
    }

    public String getMomentDescription() {
        return momentDescription;
    }

    public UUID getMomentUUID() {
        return uuid;
    }

    @Override
    public String toString() {
        return "Moment{ " +
                "gsrMeasurement= " + gsrMeasurement.getIncreaseDiff() + " " + gsrMeasurement.getDateTime() +
                ", photo= " + photo.getFileName() + " " + photo.getDateTime() +
                ", momentDate= " + momentDate + '\'' +
                '}';
    }
}
