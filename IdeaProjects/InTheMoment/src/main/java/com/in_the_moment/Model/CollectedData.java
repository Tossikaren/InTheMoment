package com.in_the_moment.Model;

import java.util.Date;
import java.util.UUID;

public abstract class CollectedData {
    private final UUID ID;
    private Date dateTime;

    public CollectedData(Date dateTime) {
        this.ID = UUID.randomUUID();
        this.dateTime = dateTime;
    }

    public CollectedData(UUID ID, Date dateTime) {
        this.ID = ID;
        this.dateTime = dateTime;
    }

    public UUID getUUID() {
        return ID;
    }

    public Date getDateTime() {
        return dateTime;
    }
}
