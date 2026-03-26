package com.propagation.engine.controller;

import com.propagation.engine.model.Person;
import com.propagation.engine.model.Relationship;
import com.propagation.engine.store.InMemoryStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PropagationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryStore store;

    private Person alice;
    private Person bob;
    private Person charlie;

    @BeforeEach
    void setUp() {
        store.clear();

        alice = new Person("Alice", 2000);
        bob = new Person("Bob", 900);
        charlie = new Person("Charlie", 300);

        store.addPerson(alice);
        store.addPerson(bob);
        store.addPerson(charlie);

        store.addRelationship(new Relationship(alice.getId(), bob.getId(), 0.5));
        store.addRelationship(new Relationship(bob.getId(), charlie.getId(), 0.5));
    }

    @Test
    void fullRecalculation() throws Exception {
        mockMvc.perform(post("/api/propagation/full"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("FULL"))
                .andExpect(jsonPath("$.totalRecalculated").value(3))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    void dependencyPropagation() throws Exception {
        mockMvc.perform(post("/api/propagation/propagate/" + alice.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("DEPENDENCY"))
                .andExpect(jsonPath("$.results").isArray());
    }

    @Test
    void updateAndPropagate() throws Exception {
        // First establish initial state
        mockMvc.perform(post("/api/propagation/full"))
                .andExpect(status().isOk());

        // Update Alice and propagate
        mockMvc.perform(post("/api/propagation/update-and-propagate/" + alice.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newBaseValue\": 1400}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.strategy").value("DEPENDENCY"))
                .andExpect(jsonPath("$.totalChanged").isNumber());
    }

    @Test
    void propagateNonexistentPerson() throws Exception {
        mockMvc.perform(post("/api/propagation/propagate/nonexistent"))
                .andExpect(status().isNotFound());
    }
}
