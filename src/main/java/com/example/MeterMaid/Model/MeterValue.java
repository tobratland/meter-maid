package com.example.MeterMaid.Model;

import java.time.Instant;
import java.util.UUID;


public class MeterValue {
    private UUID id;
    private UUID meterDataId;
    private String meterId;
    private String userId;
    private Instant hour;
    private double value;


    public MeterValue(){
    }

    public MeterValue(UUID id, UUID meterDataId, String meterId, String userId, Instant hour, double value) {
        this.id = id;
        this.meterDataId = meterDataId;
        this.meterId = meterId;
        this.userId = userId;
        this.hour = hour;
        this.value = value;
    }
    public MeterValue(UUID meterDataId, String meterId, String userId, Instant hour, double value) {
        this.id = UUID.randomUUID();
        this.meterDataId = meterDataId;
        this.meterId = meterId;
        this.userId = userId;
        this.hour = hour;
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getMeterDataId() {
        return meterDataId;
    }

    public void setMeterDataId(UUID meterDataId) {
        this.meterDataId = meterDataId;
    }

    public String getMeterId() {
        return meterId;
    }

    public void setMeterId(String meterId) {
        this.meterId = meterId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Instant getHour() {
        return hour;
    }

    public void setHour(Instant hour) {
        this.hour = hour;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
