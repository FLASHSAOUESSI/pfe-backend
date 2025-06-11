package com.ins.insstatistique.controller;

import java.util.List;

import com.ins.insstatistique.dto.EnqueteResponseRecord;
import com.ins.insstatistique.dto.UserWithEnterpriseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ins.insstatistique.dto.UserLoginRequest;
import com.ins.insstatistique.dto.UserRegisterRequest;
import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserWithEnterpriseDTO> getCurrentInvestigateur() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserWithEnterpriseByEmail(email));
    }

    @GetMapping("/me/with-enterprise")
    public ResponseEntity<UserWithEnterpriseDTO> getCurrentUserWithEnterprise() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(userService.getUserWithEnterpriseByEmail(email));
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody UserLoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }
    
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    // Endpoints pour la gestion des entreprises
    @GetMapping("/entreprises")
    public ResponseEntity<List<Entreprise>> getAllEntreprises() {
        return ResponseEntity.ok(userService.getAllEntreprises());
    }

    @PutMapping("/entreprises/{id}")
    public ResponseEntity<Entreprise> updateEntreprise(@PathVariable Long id, @RequestBody Entreprise entreprise) {
        return ResponseEntity.ok(userService.updateEntreprise(id, entreprise));
    }

    @DeleteMapping("/entreprises/{id}")
    public ResponseEntity<Void> deleteEntreprise(@PathVariable Long id) {
        userService.deleteEntreprise(id);
        return ResponseEntity.ok().build();
    }

    // Endpoints pour la gestion des responsables
    @GetMapping("/responsables")
    public ResponseEntity<List<User>> getAllResponsables() {
        return ResponseEntity.ok(userService.getAllResponsables());
    }

    @PutMapping("/responsables/{id}")
    public ResponseEntity<User> updateResponsable(@PathVariable Long id, @RequestBody User responsable) {
        return ResponseEntity.ok(userService.updateResponsable(id, responsable));
    }

    @DeleteMapping("/responsables/{id}")
    public ResponseEntity<Void> deleteResponsable(@PathVariable Long id) {
        userService.deleteResponsable(id);
        return ResponseEntity.ok().build();
    }

    // Endpoints pour la gestion des enquêtes
    @GetMapping("/enquetes")
    public ResponseEntity<List<EnqueteResponseRecord>> getAllEnquetes() {
        return ResponseEntity.ok(userService.getAllEnquetes());
    }

    @PutMapping("/enquetes/{id}")
    public ResponseEntity<Enquete> updateEnquete(@PathVariable Long id, @RequestBody Enquete enquete) {
        return ResponseEntity.ok(userService.updateEnquete(id, enquete));
    }

    @DeleteMapping("/enquetes/{id}")
    public ResponseEntity<Void> deleteEnquete(@PathVariable Long id) {
        userService.deleteEnquete(id);
        return ResponseEntity.ok().build();
    }
}
