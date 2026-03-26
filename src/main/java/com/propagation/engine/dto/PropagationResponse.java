package com.propagation.engine.dto;

import com.propagation.engine.model.PropagationResult;

import java.util.List;

public class PropagationResponse {

    private String strategy;
    private int totalRecalculated;
    private int totalChanged;
    private long durationMs;
    private List<PropagationResult> results;

    public PropagationResponse(String strategy, int totalRecalculated, int totalChanged,
                                long durationMs, List<PropagationResult> results) {
        this.strategy = strategy;
        this.totalRecalculated = totalRecalculated;
        this.totalChanged = totalChanged;
        this.durationMs = durationMs;
        this.results = results;
    }

    public String getStrategy() { return strategy; }
    public int getTotalRecalculated() { return totalRecalculated; }
    public int getTotalChanged() { return totalChanged; }
    public long getDurationMs() { return durationMs; }
    public List<PropagationResult> getResults() { return results; }
}
