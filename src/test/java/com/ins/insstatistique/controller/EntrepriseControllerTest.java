package com.ins.insstatistique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.security.JwtTokenProvider;
import com.ins.insstatistique.security.UserDetailsServiceImpl;
import com.ins.insstatistique.service.EntrepriseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EntrepriseController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
public class EntrepriseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntrepriseService entrepriseService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void testGetAllEntreprises() throws Exception {
        Entreprise entreprise1 = Entreprise.builder().id(1L).name("Entreprise A").status(EntrepriseStatus.VALIDATED).build();
        Entreprise entreprise2 = Entreprise.builder().id(2L).name("Entreprise B").status(EntrepriseStatus.PENDING).build();
        List<Entreprise> allEntreprises = Arrays.asList(entreprise1, entreprise2);

        when(entrepriseService.getAllEntreprises()).thenReturn(allEntreprises);

        mockMvc.perform(get("/api/entreprises"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("Entreprise A"));
    }

    @Test
    @WithMockUser
    void testGetEntreprisesByStatus() throws Exception {
        Entreprise entreprise1 = Entreprise.builder().id(1L).name("Entreprise A").status(EntrepriseStatus.VALIDATED).build();
        List<Entreprise> validatedEntreprises = Collections.singletonList(entreprise1);

        when(entrepriseService.getEntreprisesByStatus(EntrepriseStatus.VALIDATED)).thenReturn(validatedEntreprises);

        mockMvc.perform(get("/api/entreprises/status/VALIDATED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("VALIDATED"));
    }

    @Test
    @WithMockUser
    void testGetEntrepriseById() throws Exception {
        Entreprise entreprise = Entreprise.builder().id(1L).name("Entreprise A").status(EntrepriseStatus.VALIDATED).build();

        when(entrepriseService.getEntrepriseById(1L)).thenReturn(entreprise);

        mockMvc.perform(get("/api/entreprises/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Entreprise A"));
    }

    @Test
    @WithMockUser
    void testUpdateEntreprise() throws Exception {
        Entreprise entrepriseToUpdate = Entreprise.builder().id(1L).name("Updated Entreprise").status(EntrepriseStatus.VALIDATED).build();

        when(entrepriseService.updateEntreprise(eq(1L), any(Entreprise.class))).thenReturn(entrepriseToUpdate);

        mockMvc.perform(put("/api/entreprises/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(entrepriseToUpdate))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Entreprise"));
    }

    @Test
    @WithMockUser
    void testDeleteEntreprise() throws Exception {
        mockMvc.perform(delete("/api/entreprises/1")
                .with(csrf()))
                .andExpect(status().isOk());
    }
}

