package com.propagation.engine.controller;

import com.propagation.engine.dto.PersonDto;
import com.propagation.engine.dto.ValueUpdateRequest;
import com.propagation.engine.model.Person;
import com.propagation.engine.store.InMemoryStore;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
public class PersonController {

    private final InMemoryStore store;

    public PersonController(InMemoryStore store) {
        this.store = store;
    }

    @PostMapping
    public ResponseEntity<PersonDto> createPerson(@RequestBody PersonDto request) {
        Person person = new Person(request.getName(), request.getBaseValue());
        store.addPerson(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(person));
    }

    @GetMapping
    public List<PersonDto> getAllPersons() {
        return store.getAllPeople().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonDto> getPerson(@PathVariable String id) {
        return store.getPersonById(id)
                .map(p -> ResponseEntity.ok(toDto(p)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/value")
    public ResponseEntity<PersonDto> updateValue(@PathVariable String id,
                                                  @RequestBody ValueUpdateRequest request) {
        return store.getPersonById(id)
                .map(person -> {
                    person.setBaseValue(request.getNewBaseValue());
                    return ResponseEntity.ok(toDto(person));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable String id) {
        if (store.getPersonById(id).isPresent()) {
            store.removePerson(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private PersonDto toDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getName(),
                person.getBaseValue(),
                person.getFinalValue(),
                person.isUsingDerivedValue(),
                person.getDerivedFromPersonId()
        );
    }
}
