package com.propagation.engine.store;

import com.propagation.engine.model.Person;
import com.propagation.engine.model.Relationship;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class InMemoryStore {

    private final Map<String, Person> people = new ConcurrentHashMap<>();
    private final Map<String, Relationship> relationships = new ConcurrentHashMap<>();

    // sourcePersonId -> list of relationships where this person is the source
    private final Map<String, List<Relationship>> outgoing = new ConcurrentHashMap<>();
    // targetPersonId -> list of relationships where this person is the target
    private final Map<String, List<Relationship>> incoming = new ConcurrentHashMap<>();

    // Person operations

    public Person addPerson(Person person) {
        people.put(person.getId(), person);
        return person;
    }

    public Optional<Person> getPersonById(String id) {
        return Optional.ofNullable(people.get(id));
    }

    public Collection<Person> getAllPeople() {
        return people.values();
    }

    public void removePerson(String id) {
        people.remove(id);
        // Remove all relationships involving this person
        List<String> toRemove = relationships.values().stream()
                .filter(r -> r.getSourcePersonId().equals(id) || r.getTargetPersonId().equals(id))
                .map(Relationship::getId)
                .collect(Collectors.toList());
        toRemove.forEach(this::removeRelationship);
    }

    // Relationship operations

    public Relationship addRelationship(Relationship relationship) {
        relationships.put(relationship.getId(), relationship);
        outgoing.computeIfAbsent(relationship.getSourcePersonId(), k -> new ArrayList<>()).add(relationship);
        incoming.computeIfAbsent(relationship.getTargetPersonId(), k -> new ArrayList<>()).add(relationship);
        return relationship;
    }

    public Optional<Relationship> getRelationshipById(String id) {
        return Optional.ofNullable(relationships.get(id));
    }

    public Collection<Relationship> getAllRelationships() {
        return relationships.values();
    }

    public void removeRelationship(String id) {
        Relationship rel = relationships.remove(id);
        if (rel != null) {
            List<Relationship> out = outgoing.get(rel.getSourcePersonId());
            if (out != null) out.removeIf(r -> r.getId().equals(id));
            List<Relationship> in = incoming.get(rel.getTargetPersonId());
            if (in != null) in.removeIf(r -> r.getId().equals(id));
        }
    }

    // Graph queries

    public List<Relationship> getOutgoingRelationships(String personId) {
        return outgoing.getOrDefault(personId, Collections.emptyList());
    }

    public List<Relationship> getIncomingRelationships(String personId) {
        return incoming.getOrDefault(personId, Collections.emptyList());
    }

    /**
     * Checks if adding an edge from sourceId to targetId would create a cycle.
     * A cycle exists if there is already a path from targetId to sourceId.
     */
    public boolean wouldCreateCycle(String sourceId, String targetId) {
        Set<String> visited = new HashSet<>();
        Queue<String> queue = new LinkedList<>();
        queue.add(targetId);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            if (current.equals(sourceId)) {
                return true;
            }
            if (visited.add(current)) {
                for (Relationship r : getOutgoingRelationships(current)) {
                    queue.add(r.getTargetPersonId());
                }
            }
        }
        return false;
    }

    public void clear() {
        people.clear();
        relationships.clear();
        outgoing.clear();
        incoming.clear();
    }
}
