package com.propagation.engine.service;

import com.propagation.engine.model.Person;
import com.propagation.engine.model.PropagationResult;
import com.propagation.engine.model.Relationship;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class FullRecalculationService {

    private final InMemoryStore store;
    private final CalculationHelper calculationHelper;

    public FullRecalculationService(InMemoryStore store, CalculationHelper calculationHelper) {
        this.store = store;
        this.calculationHelper = calculationHelper;
    }

    /**
     * Recalculates all people in topological order (sources before targets).
     */
    public List<PropagationResult> recalculateAll() {
        List<Person> sorted = topologicalSort();
        List<PropagationResult> results = new ArrayList<>();

        for (Person person : sorted) {
            results.add(calculationHelper.recalculate(person));
        }

        return results;
    }

    private List<Person> topologicalSort() {
        Collection<Person> allPeople = store.getAllPeople();
        Map<String, Integer> inDegree = new HashMap<>();
        Map<String, Person> personMap = new HashMap<>();

        for (Person p : allPeople) {
            inDegree.put(p.getId(), 0);
            personMap.put(p.getId(), p);
        }

        for (Relationship rel : store.getAllRelationships()) {
            inDegree.merge(rel.getTargetPersonId(), 1, Integer::sum);
        }

        Queue<String> queue = new LinkedList<>();
        for (Map.Entry<String, Integer> entry : inDegree.entrySet()) {
            if (entry.getValue() == 0) {
                queue.add(entry.getKey());
            }
        }

        List<Person> sorted = new ArrayList<>();
        while (!queue.isEmpty()) {
            String currentId = queue.poll();
            Person current = personMap.get(currentId);
            if (current != null) {
                sorted.add(current);
            }

            for (Relationship rel : store.getOutgoingRelationships(currentId)) {
                String targetId = rel.getTargetPersonId();
                int newDegree = inDegree.merge(targetId, -1, Integer::sum);
                if (newDegree == 0) {
                    queue.add(targetId);
                }
            }
        }

        if (sorted.size() != allPeople.size()) {
            throw new IllegalStateException("Cycle detected in relationship graph. Cannot perform full recalculation.");
        }

        return sorted;
    }
}
