package com.ins.insstatistique.controller;

import com.ins.insstatistique.entity.TypeEnquete;
import com.ins.insstatistique.repository.TypeEnqueteRepository;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.email.EmailService;
import com.ins.insstatistique.email.EmailTemplateService;
import com.ins.insstatistique.repository.EnqueteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-enquete")
public class TypeEnqueteController {

    @Autowired
    private TypeEnqueteRepository typeEnqueteRepository;
    @Autowired
    private EntrepriseRepository entrepriseRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailTemplateService emailTemplateService;
    @Autowired
    private EnqueteRepository enqueteRepository;

    @Value("${app.url:http://localhost:4200/login}")
    private String appUrl;

    @GetMapping
    public List<TypeEnquete> getAll() {
        return typeEnqueteRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeEnquete> getById(@PathVariable Long id) {
        Optional<TypeEnquete> typeEnquete = typeEnqueteRepository.findById(id);
        return typeEnquete.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public TypeEnquete create(@RequestBody TypeEnquete typeEnquete) {
        TypeEnquete saved = typeEnqueteRepository.save(typeEnquete);
        // Envoi d'un email à toutes les entreprises
        List<Entreprise> entreprises = entrepriseRepository.findAll();
        String subject = "Nouvelle enquête disponible sur INS Statistique";
        String htmlContent = emailTemplateService.getNewEnqueteTemplate(appUrl);
        for (Entreprise e : entreprises) {
            try {
                emailService.sendHtmlEmail(e.getEmail(), subject, htmlContent);
            } catch (MessagingException ex) {
                // Log ou gestion d'erreur (optionnel)
            }
        }
        return saved;
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeEnquete> update(@PathVariable Long id, @RequestBody TypeEnquete typeEnquete) {
        return typeEnqueteRepository.findById(id)
                .map(existing -> {
                    existing.setAnnee(typeEnquete.getAnnee());
                    existing.setPeriodicite(typeEnquete.getPeriodicite());
                    existing.setSession(typeEnquete.getSession());
                    existing.setStatut(typeEnquete.getStatut());
                    return ResponseEntity.ok(typeEnqueteRepository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        long count = enqueteRepository.countByTypeEnquete_Id(id);
        if (count > 0) {
            return ResponseEntity.badRequest().build(); // Ou ajouter un message explicite
        }
        if (typeEnqueteRepository.existsById(id)) {
            typeEnqueteRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 