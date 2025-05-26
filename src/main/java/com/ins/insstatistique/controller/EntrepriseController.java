
package com.ins.insstatistique.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.service.EntrepriseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/entreprises")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EntrepriseController {

    private final EntrepriseService entrepriseService;    @GetMapping
    public ResponseEntity<List<Entreprise>> getAllEntreprises() {
        return ResponseEntity.ok(entrepriseService.getAllEntreprises());
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Entreprise>> getEntreprisesByStatus(@PathVariable EntrepriseStatus status) {
        return ResponseEntity.ok(entrepriseService.getEntreprisesByStatus(status));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entreprise> getEntrepriseById(@PathVariable Long id) {
        return ResponseEntity.ok(entrepriseService.getEntrepriseById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entreprise> updateEntreprise(@PathVariable Long id,
                                                       @RequestBody Entreprise entreprise) {
        return ResponseEntity.ok(entrepriseService.updateEntreprise(id, entreprise));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEntreprise(@PathVariable Long id) {
        entrepriseService.deleteEntreprise(id);
        return ResponseEntity.ok().build();
    }
}