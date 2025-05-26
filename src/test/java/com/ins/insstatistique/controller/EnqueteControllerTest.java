package com.ins.insstatistique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.service.EnqueteService;
import com.ins.insstatistique.security.JwtTokenProvider;
import com.ins.insstatistique.security.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EnqueteController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
public class EnqueteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EnqueteService enqueteService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testCreateEnquete() throws Exception {
        Enquete enqueteToSave = new Enquete(); // Populate with minimal required test data if necessary
        // Example: enqueteToSave.setSomeField("TestData");

        Enquete savedEnquete = new Enquete(); // Simulate the saved entity
        savedEnquete.setId(1L); // Example: if Enquete has an ID
        // Example: savedEnquete.setSomeField("TestData");

        when(enqueteService.saveEnquete(any(Enquete.class))).thenReturn(savedEnquete);

        mockMvc.perform(post("/api/enquetes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enqueteToSave))
                .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    // Add more tests for other EnqueteController methods if they exist, applying similar security annotations
}

