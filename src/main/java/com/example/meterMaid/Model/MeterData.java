package com.example.meterMaid.Model;

import java.time.Instant;
import java.util.UUID;

public class MeterData {
    private UUID id;
    private String meter_id;
    private String customer_id;
    private String resolution;
    private Instant from;
    private Instant to;

    public MeterData(MeterDataWithValues meterDataWithValues){
        this.id = meterDataWithValues.getId();
        this.meter_id = meterDataWithValues.getMeter_id();
        this.customer_id = meterDataWithValues.getCustomer_id();
        this.resolution = meterDataWithValues.getResolution();
        this.from = meterDataWithValues.getFrom();
        this.to = meterDataWithValues.getTo();
    }
    public MeterData(String meter_id, String customer_id, String resolution, Instant from, Instant to) {
        this.id = UUID.randomUUID();
        this.meter_id = meter_id;
        this.customer_id = customer_id;
        this.resolution = resolution;
        this.from = from;
        this.to = to;
    }
    public MeterData(UUID id, String meter_id, String customer_id, String resolution, Instant from, Instant to) {
        this.id = id;
        this.meter_id = meter_id;
        this.customer_id = customer_id;
        this.resolution = resolution;
        this.from = from;
        this.to = to;
    }
    public MeterData(){}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getMeter_id() {
        return meter_id;
    }

    public void setMeter_id(String meter_id) {
        this.meter_id = meter_id;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;


    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;

    }
}
