//package com.ins.insstatistique.controller;
//
//import com.ins.insstatistique.dto.AdminLoginRequest;
//import com.ins.insstatistique.dto.AdminRegisterRequest;
//import com.ins.insstatistique.entity.Admin;
//import com.ins.insstatistique.entity.Entreprise;
//import com.ins.insstatistique.entity.Enquete;
//import com.ins.insstatistique.entity.Responsable;
//import com.ins.insstatistique.service.AdminService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/admin")
//@RequiredArgsConstructor
//public class AdminController {
//    private final AdminService adminService;
//
//    @PostMapping("/login")
//    public ResponseEntity<Admin> login(@RequestBody AdminLoginRequest request) {
//        return ResponseEntity.ok(adminService.login(request));
//    }
//
//    // Endpoints pour la gestion des entreprises
//    @GetMapping("/entreprises")
//    public ResponseEntity<List<Entreprise>> getAllEntreprises() {
//        return ResponseEntity.ok(adminService.getAllEntreprises());
//    }
//
//    @PutMapping("/entreprises/{id}")
//    public ResponseEntity<Entreprise> updateEntreprise(@PathVariable Long id, @RequestBody Entreprise entreprise) {
//        return ResponseEntity.ok(adminService.updateEntreprise(id, entreprise));
//    }
//
//    @DeleteMapping("/entreprises/{id}")
//    public ResponseEntity<Void> deleteEntreprise(@PathVariable Long id) {
//        adminService.deleteEntreprise(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Endpoints pour la gestion des investigateurs
//    @GetMapping("/investigateurs")
//    public ResponseEntity<List<Responsable>> getAllInvestigateurs() {
//        return ResponseEntity.ok(adminService.getAllInvestigateurs());
//    }
//
//    @PutMapping("/investigateurs/{id}")
//    public ResponseEntity<Responsable> updateInvestigateur(@PathVariable Long id, @RequestBody Responsable responsable) {
//        return ResponseEntity.ok(adminService.updateInvestigateur(id, responsable));
//    }
//
//    @DeleteMapping("/investigateurs/{id}")
//    public ResponseEntity<Void> deleteInvestigateur(@PathVariable Long id) {
//        adminService.deleteInvestigateur(id);
//        return ResponseEntity.ok().build();
//    }
//
//    // Endpoints pour la gestion des enquêtes
//    @GetMapping("/enquetes")
//    public ResponseEntity<List<Enquete>> getAllEnquetes() {
//        return ResponseEntity.ok(adminService.getAllEnquetes());
//    }
//
//    @PutMapping("/enquetes/{id}")
//    public ResponseEntity<Enquete> updateEnquete(@PathVariable Long id, @RequestBody Enquete enquete) {
//        return ResponseEntity.ok(adminService.updateEnquete(id, enquete));
//    }
//
//    @DeleteMapping("/enquetes/{id}")
//    public ResponseEntity<Void> deleteEnquete(@PathVariable Long id) {
//        adminService.deleteEnquete(id);
//        return ResponseEntity.ok().build();
//    }
//}