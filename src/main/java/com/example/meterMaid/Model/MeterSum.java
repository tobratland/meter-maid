package com.example.meterMaid.Model;

import java.time.Instant;

public class MeterSum {
    private String typeOfSum;
    private Instant from;
    private Instant to;
    private String id;
    private double sum;

    public MeterSum(){}

    public MeterSum(String typeOfSum, Instant from, Instant to, String id, double sum) {
        this.typeOfSum = typeOfSum;
        this.from = from;
        this.to = to;
        this.id = id;
        this.sum = sum;
    }

    public String getTypeOfSum() {
        return typeOfSum;
    }

    public void setTypeOfSum(String typeOfSum) {
        this.typeOfSum = typeOfSum;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public void addSum(double sum) {
        this.sum += sum;
    }
}
