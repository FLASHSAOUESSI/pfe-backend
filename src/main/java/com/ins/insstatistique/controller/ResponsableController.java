//// InvestigateurController.java
//package com.ins.insstatistique.controller;
//
//import com.ins.insstatistique.entity.Responsable;
//import com.ins.insstatistique.service.ResponsableService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/responsables")
//@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//public class ResponsableController {
//
//    private final ResponsableService responsableService;
//
//    @GetMapping
//    public ResponseEntity<List<Responsable>> getAllInvestigateurs() {
//        return ResponseEntity.ok(responsableService.getAllInvestigateurs());
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<Responsable> getInvestigateurById(@PathVariable Long id) {
//        return ResponseEntity.ok(responsableService.getInvestigateurById(id));
//    }
//
//    @GetMapping("/me")
//    public ResponseEntity<Responsable> getCurrentInvestigateur() {
//        String email = SecurityContextHolder.getContext().getAuthentication().getName();
//        return ResponseEntity.ok(responsableService.getInvestigateurByEmail(email));
//    }
//
//    @PutMapping("/{id}")
//    public ResponseEntity<Responsable> updateInvestigateur(@PathVariable Long id,
//                                                           @RequestBody Responsable responsable) {
//        return ResponseEntity.ok(responsableService.updateInvestigateur(id, responsable));
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<?> deleteInvestigateur(@PathVariable Long id) {
//        responsableService.deleteInvestigateur(id);
//        return ResponseEntity.ok().build();
//    }
//}