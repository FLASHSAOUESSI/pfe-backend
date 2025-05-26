package com.ins.insstatistique.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ins.insstatistique.dto.EnterpriseDashboardDto;
import com.ins.insstatistique.dto.EnterpriseDto;
import com.ins.insstatistique.dto.EnterpriseStatusUpdateDto;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.service.EntrepriseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin/gestion")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EnterpriseAdminController {

    private final EntrepriseService entrepriseService;

    /**
     * Get all enterprises with a specific status
     * @param status The status to filter by (PENDING, VALIDATED, REJECTED)
     * @return List of enterprises with the specified status
     */
    @GetMapping("/entreprises/status/{status}")
    public ResponseEntity<List<Entreprise>> getEnterprisesByStatus(@PathVariable EntrepriseStatus status) {
        return ResponseEntity.ok(entrepriseService.getEntreprisesByStatus(status));
    }

    /**
     * Get all pending enterprises awaiting validation
     * @return List of pending enterprises
     */
    @GetMapping("/entreprises/pending")
    public ResponseEntity<List<Entreprise>> getPendingEnterprises() {
        return ResponseEntity.ok(entrepriseService.getEntreprisesByStatus(EntrepriseStatus.PENDING));
    }    /**
     * Update an enterprise's status
     * @param id The ID of the enterprise to update
     * @param status The new status (VALIDATED, REJECTED, or PENDING)
     * @return Detailed information about the status update, including notification status
     */
    @PutMapping("/entreprises/{id}/status")
    public ResponseEntity<EnterpriseStatusUpdateDto> updateEnterpriseStatus(
            @PathVariable Long id, 
            @RequestParam EntrepriseStatus status) {
        return ResponseEntity.ok(entrepriseService.validateEntreprise(id, status));
    }
    
    /**
     * Get enterprise dashboard statistics
     * @return Dashboard statistics including counts by status
     */
    @GetMapping("/dashboard")
    public ResponseEntity<EnterpriseDashboardDto> getEnterpriseDashboard() {
        return ResponseEntity.ok(entrepriseService.getEnterpriseDashboard());
    }

    /**
     * Get all enterprises with detailed information
     * @return List of enterprises with detailed information
     */
    @GetMapping("/entreprises/detailed")
    public ResponseEntity<List<EnterpriseDto>> getAllEnterprisesDetailed() {
        return ResponseEntity.ok(entrepriseService.getAllEnterpriseDtos());
    }
    
    /**
     * Get enterprises with detailed information filtered by status
     * @param status The status to filter by
     * @return List of enterprises with detailed information
     */
    @GetMapping("/entreprises/detailed/status/{status}")
    public ResponseEntity<List<EnterpriseDto>> getEnterprisesDetailedByStatus(@PathVariable EntrepriseStatus status) {
        return ResponseEntity.ok(entrepriseService.getEnterpriseDtosByStatus(status));
    }
    
    /**
     * Get detailed information for a specific enterprise
     * @param id The ID of the enterprise
     * @return Detailed information about the enterprise
     */
    @GetMapping("/entreprises/{id}/detailed")
    public ResponseEntity<EnterpriseDto> getEnterpriseDetailed(@PathVariable Long id) {
        return ResponseEntity.ok(entrepriseService.getEnterpriseDtoById(id));
    }
}
