package com.example.meterMaid.Model;

import java.time.Instant;
import java.util.Map;

public class MeterDataWithValues extends MeterData{
    private Map<Instant, Double> values;

    public MeterDataWithValues(){}

    public MeterDataWithValues(String meter_id, String customer_id, String resolution, Instant from, Instant to, Map<Instant, Double> values) {
        super(meter_id,customer_id,resolution,from,to);
        this.values = values;
    }
    public MeterDataWithValues(MeterData meterData, Map<Instant, Double> values) {
        this.setId(meterData.getId());
        this.setMeter_id(meterData.getMeter_id());
        this.setCustomer_id(meterData.getCustomer_id());
        this.setResolution(meterData.getResolution());
        this.setFrom(meterData.getFrom());
        this.setTo(meterData.getTo());
        this.values = values;
    }


    public Map<Instant, Double> getValues() {
        return values;
    }

    public void setValues(Map<Instant, Double> values) {
        this.values = values;
    }

    public void addValues(MeterValue value){
        this.values.put(value.getHour(),value.getValue());
    }

}
