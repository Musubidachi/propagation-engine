package com.propagation.engine.service;

import com.propagation.engine.model.Person;
import com.propagation.engine.model.PropagationResult;
import com.propagation.engine.model.Relationship;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CalculationHelper {

    private final InMemoryStore store;

    public CalculationHelper(InMemoryStore store) {
        this.store = store;
    }

    /**
     * Recalculates the final value for a person based on their base value
     * and all incoming relationships. Returns a PropagationResult describing
     * what changed.
     */
    public PropagationResult recalculate(Person person) {
        double previousFinalValue = person.getFinalValue();
        List<Relationship> incomingRels = store.getIncomingRelationships(person.getId());

        double highestDerived = 0;
        String bestSourceId = null;

        for (Relationship rel : incomingRels) {
            Person source = store.getPersonById(rel.getSourcePersonId()).orElse(null);
            if (source != null) {
                double derived = source.getFinalValue() * rel.getPercentage();
                if (derived > highestDerived) {
                    highestDerived = derived;
                    bestSourceId = source.getId();
                }
            }
        }

        double newFinalValue;
        boolean usingDerived;
        String derivedFrom;

        if (highestDerived > person.getBaseValue()) {
            newFinalValue = highestDerived;
            usingDerived = true;
            derivedFrom = bestSourceId;
        } else {
            newFinalValue = person.getBaseValue();
            usingDerived = false;
            derivedFrom = null;
        }

        person.setFinalValue(newFinalValue);
        person.setUsingDerivedValue(usingDerived);
        person.setDerivedFromPersonId(derivedFrom);

        boolean changed = Math.abs(previousFinalValue - newFinalValue) > 0.0001;
        String source = usingDerived ? "DERIVED:" + derivedFrom : "OWN";

        return new PropagationResult(
                person.getId(),
                person.getName(),
                previousFinalValue,
                newFinalValue,
                changed,
                source
        );
    }
}
