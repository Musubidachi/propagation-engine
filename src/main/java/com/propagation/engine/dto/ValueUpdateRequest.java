package com.propagation.engine.dto;

public class ValueUpdateRequest {

    private double newBaseValue;

    public ValueUpdateRequest() {}

    public ValueUpdateRequest(double newBaseValue) {
        this.newBaseValue = newBaseValue;
    }

    public double getNewBaseValue() { return newBaseValue; }
    public void setNewBaseValue(double newBaseValue) { this.newBaseValue = newBaseValue; }
}
