package com.propagation.engine.dto;

public class RelationshipDto {

    private String id;
    private String sourcePersonId;
    private String targetPersonId;
    private double percentage;

    public RelationshipDto() {}

    public RelationshipDto(String id, String sourcePersonId, String targetPersonId, double percentage) {
        this.id = id;
        this.sourcePersonId = sourcePersonId;
        this.targetPersonId = targetPersonId;
        this.percentage = percentage;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getSourcePersonId() { return sourcePersonId; }
    public void setSourcePersonId(String sourcePersonId) { this.sourcePersonId = sourcePersonId; }

    public String getTargetPersonId() { return targetPersonId; }
    public void setTargetPersonId(String targetPersonId) { this.targetPersonId = targetPersonId; }

    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
}
