// InvestigateurController.java
package com.ins.insstatistique.controller;

import com.ins.insstatistique.entity.Investigateur;
import com.ins.insstatistique.service.InvestigateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/investigateurs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class InvestigateurController {

    private final InvestigateurService investigateurService;

    @GetMapping
    public ResponseEntity<List<Investigateur>> getAllInvestigateurs() {
        return ResponseEntity.ok(investigateurService.getAllInvestigateurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Investigateur> getInvestigateurById(@PathVariable Long id) {
        return ResponseEntity.ok(investigateurService.getInvestigateurById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<Investigateur> getCurrentInvestigateur() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(investigateurService.getInvestigateurByEmail(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Investigateur> updateInvestigateur(@PathVariable Long id,
                                                             @RequestBody Investigateur investigateur) {
        return ResponseEntity.ok(investigateurService.updateInvestigateur(id, investigateur));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteInvestigateur(@PathVariable Long id) {
        investigateurService.deleteInvestigateur(id);
        return ResponseEntity.ok().build();
    }
}