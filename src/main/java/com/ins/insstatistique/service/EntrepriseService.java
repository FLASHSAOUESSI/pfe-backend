package com.ins.insstatistique.service;

import java.util.List;
import java.util.stream.Collectors;

import com.ins.insstatistique.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ins.insstatistique.dto.EnterpriseDashboardDto;
import com.ins.insstatistique.dto.EnterpriseDto;
import com.ins.insstatistique.dto.EnterpriseStatusUpdateDto;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.repository.EntrepriseRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Entreprise getEntrepriseById(Long id) {
        return entrepriseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Entreprise non trouvée"));
    }

    public Entreprise updateEntreprise(Long id, Entreprise entreprise) {
        if (!entrepriseRepository.existsById(id)) {
            throw new EntityNotFoundException("Entreprise non trouvée");
        }
        entreprise.setId(id);
        return entrepriseRepository.save(entreprise);
    }

    public void deleteEntreprise(Long id) {
        if (!entrepriseRepository.existsById(id)) {
            throw new EntityNotFoundException("Entreprise non trouvée");
        }
        entrepriseRepository.deleteById(id);
    }
    
    public List<Entreprise> getEntreprisesByStatus(EntrepriseStatus status) {
        return entrepriseRepository.findByStatus(status);
    }
    
    public EnterpriseStatusUpdateDto validateEntreprise(Long id, EntrepriseStatus status) {
        Entreprise entreprise = getEntrepriseById(id);
        EntrepriseStatus previousStatus = entreprise.getStatus();
        entreprise.setStatus(status);
        Entreprise savedEntreprise = entrepriseRepository.save(entreprise);
        entreprise.getResponsables().stream().forEach(responsable ->{
            responsable.setEnabled(true);
            userRepository.save(responsable);
        } );
        
        boolean notificationSent = false;
        // Call notification service if it's injected
        if (notificationService != null) {
            try {
                notificationService.notifyEnterpriseStatusChange(savedEntreprise, previousStatus);
                notificationSent = true;
            } catch (Exception e) {
                // Log the error but continue
                notificationSent = false;
            }
        }
        
        return EnterpriseStatusUpdateDto.builder()
                .enterpriseId(savedEntreprise.getId())
                .enterpriseName(savedEntreprise.getName())
                .oldStatus(previousStatus)
                .newStatus(savedEntreprise.getStatus())
                .notificationSent(notificationSent)
                .build();
    }
    
    public EnterpriseDashboardDto getEnterpriseDashboard() {
        List<Entreprise> allEnterprises = entrepriseRepository.findAll();
        long totalCount = allEnterprises.size();
        long pendingCount = entrepriseRepository.findByStatus(EntrepriseStatus.PENDING).size();
        long validatedCount = entrepriseRepository.findByStatus(EntrepriseStatus.VALIDATED).size();
        long rejectedCount = entrepriseRepository.findByStatus(EntrepriseStatus.REJECTED).size();
        
        return EnterpriseDashboardDto.builder()
                .totalEnterprises(totalCount)
                .pendingEnterprises(pendingCount)
                .validatedEnterprises(validatedCount)
                .rejectedEnterprises(rejectedCount)
                .build();
    }
    
    /**
     * Convert Entreprise entity to EnterpriseDto
     */
    private EnterpriseDto convertToDto(Entreprise entreprise) {
        return EnterpriseDto.builder()
                .id(entreprise.getId())
                .name(entreprise.getName())
                .address(entreprise.getAddress())
                .email(entreprise.getEmail())
                .fax(entreprise.getFax())
                .status(entreprise.getStatus())
                .governorateId(entreprise.getGovernorate() != null ? entreprise.getGovernorate().getId() : null)
                .governorateName(entreprise.getGovernorate() != null ? entreprise.getGovernorate().getName() : null)
                .responsablesCount(entreprise.getResponsables() != null ? entreprise.getResponsables().size() : 0)
                .build();
    }
    
    /**
     * Convert Entreprise entities to EnterpriseDtos
     */
    private List<EnterpriseDto> convertToDtoList(List<Entreprise> enterprises) {
        return enterprises.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * Get all enterprises as DTOs
     */
    public List<EnterpriseDto> getAllEnterpriseDtos() {
        return convertToDtoList(getAllEntreprises());
    }
    
    /**
     * Get enterprises by status as DTOs
     */
    public List<EnterpriseDto> getEnterpriseDtosByStatus(EntrepriseStatus status) {
        return convertToDtoList(getEntreprisesByStatus(status));
    }
    
    /**
     * Get enterprise by ID as DTO
     */
    public EnterpriseDto getEnterpriseDtoById(Long id) {
        return convertToDto(getEntrepriseById(id));
    }
}
