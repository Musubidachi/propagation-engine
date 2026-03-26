package com.propagation.engine.controller;

import com.propagation.engine.store.InMemoryStore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InMemoryStore store;

    @BeforeEach
    void setUp() {
        store.clear();
    }

    @Test
    void createAndGetPerson() throws Exception {
        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Alice\", \"baseValue\": 2000}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.baseValue").value(2000))
                .andExpect(jsonPath("$.finalValue").value(2000))
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andReturn().getResponse().getContentAsString();

        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void getPersonNotFound() throws Exception {
        mockMvc.perform(get("/api/persons/nonexistent"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatePersonValue() throws Exception {
        // Create person
        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Alice\", \"baseValue\": 2000}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extract id (simple parse)
        String id = response.split("\"id\":\"")[1].split("\"")[0];

        // Update value
        mockMvc.perform(patch("/api/persons/" + id + "/value")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"newBaseValue\": 1400}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.baseValue").value(1400));
    }

    @Test
    void deletePerson() throws Exception {
        String response = mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Alice\", \"baseValue\": 2000}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        String id = response.split("\"id\":\"")[1].split("\"")[0];

        mockMvc.perform(delete("/api/persons/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/persons/" + id))
                .andExpect(status().isNotFound());
    }
}
