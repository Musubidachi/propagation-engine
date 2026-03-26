package com.propagation.engine.service;

import com.propagation.engine.model.Person;
import com.propagation.engine.model.PropagationResult;
import com.propagation.engine.model.Relationship;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class DependencyPropagationService {

    private final InMemoryStore store;
    private final CalculationHelper calculationHelper;

    public DependencyPropagationService(InMemoryStore store, CalculationHelper calculationHelper) {
        this.store = store;
        this.calculationHelper = calculationHelper;
    }

    /**
     * Propagates changes starting from the given person using BFS.
     * Only continues propagation through people whose values actually changed.
     */
    public List<PropagationResult> propagateFrom(String personId) {
        Person startPerson = store.getPersonById(personId)
                .orElseThrow(() -> new NoSuchElementException("Person not found: " + personId));

        List<PropagationResult> results = new ArrayList<>();
        Set<String> visited = new LinkedHashSet<>();
        Queue<String> queue = new LinkedList<>();

        // Recalculate the starting person first
        PropagationResult startResult = calculationHelper.recalculate(startPerson);
        results.add(startResult);
        visited.add(personId);

        // Always enqueue dependents for the starting person (the caller signals a change)
        // For subsequent nodes, only propagate if the value actually changed
        enqueueDependents(personId, queue, visited);

        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Person current = store.getPersonById(currentId).orElse(null);
            if (current == null) continue;

            PropagationResult result = calculationHelper.recalculate(current);
            results.add(result);

            // Only continue propagation if this person's value actually changed
            if (result.isChanged()) {
                enqueueDependents(currentId, queue, visited);
            }
        }

        return results;
    }

    private void enqueueDependents(String personId, Queue<String> queue, Set<String> visited) {
        for (Relationship rel : store.getOutgoingRelationships(personId)) {
            String targetId = rel.getTargetPersonId();
            if (visited.add(targetId)) {
                queue.add(targetId);
            }
        }
    }
}
