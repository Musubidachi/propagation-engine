package com.propagation.engine.dto;

public class PersonDto {

    private String id;
    private String name;
    private double baseValue;
    private double finalValue;
    private boolean usingDerivedValue;
    private String derivedFromPersonId;

    public PersonDto() {}

    public PersonDto(String id, String name, double baseValue, double finalValue,
                     boolean usingDerivedValue, String derivedFromPersonId) {
        this.id = id;
        this.name = name;
        this.baseValue = baseValue;
        this.finalValue = finalValue;
        this.usingDerivedValue = usingDerivedValue;
        this.derivedFromPersonId = derivedFromPersonId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseValue() { return baseValue; }
    public void setBaseValue(double baseValue) { this.baseValue = baseValue; }

    public double getFinalValue() { return finalValue; }
    public void setFinalValue(double finalValue) { this.finalValue = finalValue; }

    public boolean isUsingDerivedValue() { return usingDerivedValue; }
    public void setUsingDerivedValue(boolean usingDerivedValue) { this.usingDerivedValue = usingDerivedValue; }

    public String getDerivedFromPersonId() { return derivedFromPersonId; }
    public void setDerivedFromPersonId(String derivedFromPersonId) { this.derivedFromPersonId = derivedFromPersonId; }
}
