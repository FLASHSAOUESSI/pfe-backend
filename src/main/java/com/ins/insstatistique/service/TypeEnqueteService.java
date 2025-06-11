package com.ins.insstatistique.service;

import com.ins.insstatistique.dto.TypeEnqueteDTO;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.TypeEnquete;
import com.ins.insstatistique.email.EmailService;
import com.ins.insstatistique.email.EmailTemplateService;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.repository.TypeEnqueteRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TypeEnqueteService {

    @Autowired
    private TypeEnqueteRepository typeEnqueteRepository;

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EmailTemplateService emailTemplateService;

    @Value("${app.url:http://localhost:4200/login}")
    private String appUrl;

    public List<TypeEnqueteDTO> getAllTypeEnquetes() {
        return typeEnqueteRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<TypeEnqueteDTO> getTypeEnqueteById(Long id) {
        return typeEnqueteRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public TypeEnqueteDTO createTypeEnquete(TypeEnqueteDTO typeEnqueteDTO) {
        TypeEnquete typeEnquete = convertToEntity(typeEnqueteDTO);
        TypeEnquete savedTypeEnquete = typeEnqueteRepository.save(typeEnquete);

        // Send email notification to all enterprises
        notifyEnterprises();

        return convertToDTO(savedTypeEnquete);
    }

    @Transactional
    public Optional<TypeEnqueteDTO> updateTypeEnquete(Long id, TypeEnqueteDTO typeEnqueteDTO) {
        return typeEnqueteRepository.findById(id)
                .map(existing -> {
                    existing.setAnnee(typeEnqueteDTO.getAnnee());
                    existing.setPeriodicite(typeEnqueteDTO.getPeriodicite());
                    existing.setSession(typeEnqueteDTO.getSession());
                    existing.setStatut(typeEnqueteDTO.getStatut());
                    TypeEnquete updated = typeEnqueteRepository.save(existing);
                    return convertToDTO(updated);
                });
    }

    @Transactional
    public boolean deleteTypeEnquete(Long id) {
        if (typeEnqueteRepository.existsById(id)) {
            typeEnqueteRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private TypeEnqueteDTO convertToDTO(TypeEnquete typeEnquete) {
        TypeEnqueteDTO dto = new TypeEnqueteDTO();
        dto.setId(typeEnquete.getId());
        dto.setAnnee(typeEnquete.getAnnee());
        dto.setPeriodicite(typeEnquete.getPeriodicite());
        dto.setSession(typeEnquete.getSession());
        dto.setStatut(typeEnquete.getStatut());
        return dto;
    }

    private TypeEnquete convertToEntity(TypeEnqueteDTO typeEnqueteDTO) {
        TypeEnquete entity = new TypeEnquete();

        entity.setAnnee(typeEnqueteDTO.getAnnee());
        entity.setPeriodicite(typeEnqueteDTO.getPeriodicite());
        entity.setSession(typeEnqueteDTO.getSession());
        entity.setStatut(typeEnqueteDTO.getStatut());
        return entity;
    }

    private void notifyEnterprises() {
        List<Entreprise> entreprises = entrepriseRepository.findAll();
        String subject = "Nouvelle enquête disponible sur INS Statistique";
        String htmlContent = emailTemplateService.getNewEnqueteTemplate(appUrl);

        for (Entreprise entreprise : entreprises) {
            try {
                emailService.sendHtmlEmail(entreprise.getEmail(), subject, htmlContent);
            } catch (MessagingException ex) {
                // Log error or handle exception appropriately
                System.err.println("Failed to send email to " + entreprise.getEmail() + ": " + ex.getMessage());
            }
        }
    }
}
