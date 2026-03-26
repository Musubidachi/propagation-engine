package com.propagation.engine.controller;

import com.propagation.engine.dto.RelationshipDto;
import com.propagation.engine.model.Relationship;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    private final InMemoryStore store;

    public RelationshipController(InMemoryStore store) {
        this.store = store;
    }

    @PostMapping
    public ResponseEntity<?> createRelationship(@RequestBody RelationshipDto request) {
        // Validate source and target exist
        if (store.getPersonById(request.getSourcePersonId()).isEmpty()) {
            return ResponseEntity.badRequest().body("Source person not found: " + request.getSourcePersonId());
        }
        if (store.getPersonById(request.getTargetPersonId()).isEmpty()) {
            return ResponseEntity.badRequest().body("Target person not found: " + request.getTargetPersonId());
        }

        // Validate percentage
        if (request.getPercentage() <= 0 || request.getPercentage() > 1) {
            return ResponseEntity.badRequest().body("Percentage must be between 0 (exclusive) and 1 (inclusive)");
        }

        // Self-referencing check
        if (request.getSourcePersonId().equals(request.getTargetPersonId())) {
            return ResponseEntity.badRequest().body("A person cannot have a relationship with themselves");
        }

        // Cycle detection
        if (store.wouldCreateCycle(request.getSourcePersonId(), request.getTargetPersonId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Adding this relationship would create a cycle in the dependency graph");
        }

        Relationship relationship = new Relationship(
                request.getSourcePersonId(),
                request.getTargetPersonId(),
                request.getPercentage()
        );
        store.addRelationship(relationship);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(relationship));
    }

    @GetMapping
    public List<RelationshipDto> getAllRelationships() {
        return store.getAllRelationships().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RelationshipDto> getRelationship(@PathVariable String id) {
        return store.getRelationshipById(id)
                .map(r -> ResponseEntity.ok(toDto(r)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRelationship(@PathVariable String id) {
        if (store.getRelationshipById(id).isPresent()) {
            store.removeRelationship(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private RelationshipDto toDto(Relationship rel) {
        return new RelationshipDto(
                rel.getId(),
                rel.getSourcePersonId(),
                rel.getTargetPersonId(),
                rel.getPercentage()
        );
    }
}
