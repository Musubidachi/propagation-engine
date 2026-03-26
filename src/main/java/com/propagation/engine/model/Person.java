package com.propagation.engine.model;

import java.util.UUID;

public class Person {

    private final String id;
    private String name;
    private double baseValue;
    private double finalValue;
    private boolean usingDerivedValue;
    private String derivedFromPersonId;

    public Person(String name, double baseValue) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.baseValue = baseValue;
        this.finalValue = baseValue;
        this.usingDerivedValue = false;
        this.derivedFromPersonId = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(double baseValue) {
        this.baseValue = baseValue;
    }

    public double getFinalValue() {
        return finalValue;
    }

    public void setFinalValue(double finalValue) {
        this.finalValue = finalValue;
    }

    public boolean isUsingDerivedValue() {
        return usingDerivedValue;
    }

    public void setUsingDerivedValue(boolean usingDerivedValue) {
        this.usingDerivedValue = usingDerivedValue;
    }

    public String getDerivedFromPersonId() {
        return derivedFromPersonId;
    }

    public void setDerivedFromPersonId(String derivedFromPersonId) {
        this.derivedFromPersonId = derivedFromPersonId;
    }
}
