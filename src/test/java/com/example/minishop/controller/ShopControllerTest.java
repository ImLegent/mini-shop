package com.example.minishop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShopControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void shouldReturnHealthStatus() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test
    public void shouldReturnAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").exists());
    }

    @Test
    public void shouldReturnProductById() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }

    @Test
    public void shouldReturnNotFoundForInvalidProductId() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldCreateOrder() throws Exception {
        String jsonBody = "[\"1\", \"2\"]"; // Product IDs

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.productIds[0]").value("1"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }
}
