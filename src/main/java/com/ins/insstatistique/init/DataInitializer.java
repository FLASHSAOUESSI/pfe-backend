package com.ins.insstatistique.init;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.ins.insstatistique.service.GovernorateService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final GovernorateService governorateService;

    /**
     * Initialize reference data when the application starts
     * Only runs in development and production profiles, not in test
     */
    @Bean
    @Profile({"dev", "prod", "default"})
    public CommandLineRunner initReferenceData() {
        return args -> {
            log.info("Initializing reference data...");
            
            // Initialize Tunisian governorates
            try {
                governorateService.initializeTunisianGovernorates();
                log.info("Tunisian governorates initialized successfully");
            } catch (Exception e) {
                log.error("Error initializing Tunisian governorates", e);
            }
        };
    }
}
