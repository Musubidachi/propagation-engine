package com.propagation.engine.controller;

import com.propagation.engine.dto.PropagationResponse;
import com.propagation.engine.dto.ValueUpdateRequest;
import com.propagation.engine.model.Person;
import com.propagation.engine.model.PropagationResult;
import com.propagation.engine.service.DependencyPropagationService;
import com.propagation.engine.service.FullRecalculationService;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/propagation")
public class PropagationController {

    private final FullRecalculationService fullRecalculationService;
    private final DependencyPropagationService dependencyPropagationService;
    private final InMemoryStore store;

    public PropagationController(FullRecalculationService fullRecalculationService,
                                  DependencyPropagationService dependencyPropagationService,
                                  InMemoryStore store) {
        this.fullRecalculationService = fullRecalculationService;
        this.dependencyPropagationService = dependencyPropagationService;
        this.store = store;
    }

    @PostMapping("/full")
    public PropagationResponse fullRecalculation() {
        long start = System.currentTimeMillis();
        List<PropagationResult> results = fullRecalculationService.recalculateAll();
        long duration = System.currentTimeMillis() - start;

        int totalChanged = (int) results.stream().filter(PropagationResult::isChanged).count();
        return new PropagationResponse("FULL", results.size(), totalChanged, duration, results);
    }

    @PostMapping("/propagate/{personId}")
    public ResponseEntity<?> propagate(@PathVariable String personId) {
        try {
            long start = System.currentTimeMillis();
            List<PropagationResult> results = dependencyPropagationService.propagateFrom(personId);
            long duration = System.currentTimeMillis() - start;

            int totalChanged = (int) results.stream().filter(PropagationResult::isChanged).count();
            return ResponseEntity.ok(
                    new PropagationResponse("DEPENDENCY", results.size(), totalChanged, duration, results));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/update-and-propagate/{personId}")
    public ResponseEntity<?> updateAndPropagate(@PathVariable String personId,
                                                 @RequestBody ValueUpdateRequest request) {
        return store.getPersonById(personId)
                .map(person -> {
                    person.setBaseValue(request.getNewBaseValue());

                    long start = System.currentTimeMillis();
                    List<PropagationResult> results = dependencyPropagationService.propagateFrom(personId);
                    long duration = System.currentTimeMillis() - start;

                    int totalChanged = (int) results.stream().filter(PropagationResult::isChanged).count();
                    return ResponseEntity.ok(
                            new PropagationResponse("DEPENDENCY", results.size(), totalChanged, duration, results));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
