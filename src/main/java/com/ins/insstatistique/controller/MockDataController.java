package com.ins.insstatistique.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ins.insstatistique.service.MockDataService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/mock-data")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MockDataController {

    private final MockDataService mockDataService;

    /**
     * Generate mock enterprises
     * 
     * @param count The number of mock enterprises to generate
     * @return A message indicating the number of enterprises generated
     */
    @PostMapping("/enterprises")
    public ResponseEntity<String> generateMockEnterprises(@RequestParam(defaultValue = "10") int count) {
        try {
            mockDataService.generateMockEnterprises(count);
            return ResponseEntity.ok(count + " mock enterprises generated successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error generating mock data: " + e.getMessage());
        }
    }
}
