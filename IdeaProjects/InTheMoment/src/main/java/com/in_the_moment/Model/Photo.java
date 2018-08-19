package com.in_the_moment.Model;

import java.util.Date;
import java.util.UUID;

public class Photo extends CollectedData{
    private final String fileName;

    public Photo(String fileName, Date dateTime) {
        super(dateTime);
        this.fileName = fileName;
    }

    public Photo(UUID uuid, String fileName, Date dateTime) {
        super(uuid, dateTime);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}