package com.propagation.engine.model;

public class PropagationResult {

    private String personId;
    private String personName;
    private double previousFinalValue;
    private double newFinalValue;
    private boolean changed;
    private String source;

    public PropagationResult(String personId, String personName, double previousFinalValue,
                             double newFinalValue, boolean changed, String source) {
        this.personId = personId;
        this.personName = personName;
        this.previousFinalValue = previousFinalValue;
        this.newFinalValue = newFinalValue;
        this.changed = changed;
        this.source = source;
    }

    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }

    public double getPreviousFinalValue() {
        return previousFinalValue;
    }

    public double getNewFinalValue() {
        return newFinalValue;
    }

    public boolean isChanged() {
        return changed;
    }

    public String getSource() {
        return source;
    }
}
