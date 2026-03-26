package com.propagation.engine.model;

import java.util.UUID;

public class Relationship {

    private final String id;
    private String sourcePersonId;
    private String targetPersonId;
    private double percentage;

    public Relationship(String sourcePersonId, String targetPersonId, double percentage) {
        this.id = UUID.randomUUID().toString();
        this.sourcePersonId = sourcePersonId;
        this.targetPersonId = targetPersonId;
        this.percentage = percentage;
    }

    public String getId() {
        return id;
    }

    public String getSourcePersonId() {
        return sourcePersonId;
    }

    public void setSourcePersonId(String sourcePersonId) {
        this.sourcePersonId = sourcePersonId;
    }

    public String getTargetPersonId() {
        return targetPersonId;
    }

    public void setTargetPersonId(String targetPersonId) {
        this.targetPersonId = targetPersonId;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
}
