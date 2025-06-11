package com.ins.insstatistique.service;


import com.ins.insstatistique.dto.CreateEnqueteRecord;
import com.ins.insstatistique.dto.EnqueteResponseRecord;
import com.ins.insstatistique.dto.EnterpriseSimpleRecord;
import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.TypeEnquete;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.repository.EnqueteRepository;
import com.ins.insstatistique.repository.TypeEnqueteRepository;
import com.ins.insstatistique.repository.UserRepository;
import com.ins.insstatistique.shared.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class EnqueteService {

    private final EnqueteRepository enqueteRepository;
    private final UserRepository userRepository;
    private final TypeEnqueteRepository typeEnqueteRepository;

    @Transactional
    public EnqueteResponseRecord saveEnquete(CreateEnqueteRecord createEnqueteRecord) {
        if (createEnqueteRecord.identifiantStat() == null || createEnqueteRecord.identifiantStat().isEmpty()) {
            throw new IllegalArgumentException("Identifiant Stat cannot be empty");
        }

        // Get the current user's email
        String userEmail = SecurityUtils.getUserId();

        // Find the user by email
        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + userEmail));

        // Get the enterprise associated with the user
        Entreprise enterprise = currentUser.getEntreprise();
        if (enterprise == null) {
            throw new IllegalStateException("Current user does not have an associated enterprise");
        }

        // Convert record to entity and set the enterprise
        Enquete enquete = mapToEntity(createEnqueteRecord);
        enquete.setEntreprise(enterprise);

        TypeEnquete typeEnquete = typeEnqueteRepository.findById(createEnqueteRecord.typeEnquete().id()).orElse(null);
        if( typeEnquete != null ) {
            enquete.setTypeEnquete(typeEnquete);
        }

        // Save the entity
        Enquete savedEnquete = enqueteRepository.save(enquete);

        // Convert back to response record with enterprise details
        return mapToResponseRecord(savedEnquete);
    }

    @Transactional(readOnly = true)
    public List<EnqueteResponseRecord> getAllEnquetes() {
        return enqueteRepository.findAll().stream()
                .map(this::mapToResponseRecord)
                .collect(Collectors.toList());
    }

    // Helper method to convert Enquete entity to EnqueteResponseRecord
    private EnqueteResponseRecord mapToResponseRecord(Enquete enquete) {
        EnterpriseSimpleRecord enterpriseRecord = null;
        if (enquete.getEntreprise() != null) {
            enterpriseRecord = new EnterpriseSimpleRecord(
                    enquete.getEntreprise().getId(),
                    enquete.getEntreprise().getName()
            );
        }

        return new EnqueteResponseRecord(
                enquete.getId(),
                enquete.getIdentifiantStat(),
                enquete.getNomSociale(),
                enquete.getAdresse(),
                enquete.getTelephone(),
                enquete.getFax(),
                enquete.getNomRepondant(),
                enquete.getMailRepondant(),
                enquete.getBrancheActivite(),
                enquete.getExportatrice(),
                enquete.getVariaisonSaisoniere(),
                enquete.getSituation1erTrimestre(),
                enquete.getSituation2emeTrimestre(),
                enquete.getSituationFuture(),
                enquete.getEffectifs1erTrimestre(),
                enquete.getEffectifs2emeTrimestre(),
                enquete.getEffectifsFutur(),
                enquete.getPrixMatieres1erTrimestre(),
                enquete.getPrixMatieres2emeTrimestre(),
                enquete.getPrixMatieresFutur(),
                enquete.getAvoirDifficultes(),
                enquete.getDiffApprovisionnement(),
                enquete.getDiffAutre(),
                enquete.getPleineCapacite(),
                enquete.getTauxUtilisationCapacite(),
                enterpriseRecord
        );
    }

    // Helper method to convert CreateEnqueteRecord to Enquete entity
    private Enquete mapToEntity(CreateEnqueteRecord record) {
        Enquete enquete = new Enquete();
        enquete.setIdentifiantStat(record.identifiantStat());
        enquete.setNomSociale(record.nomSociale());
        enquete.setAdresse(record.adresse());
        enquete.setTelephone(record.telephone());
        enquete.setFax(record.fax());
        enquete.setNomRepondant(record.nomRepondant());
        enquete.setMailRepondant(record.mailRepondant());
        enquete.setBrancheActivite(record.brancheActivite());
        enquete.setExportatrice(record.exportatrice());
        enquete.setVariaisonSaisoniere(record.variaisonSaisoniere());
        enquete.setSituation1erTrimestre(record.situation1erTrimestre());
        enquete.setSituation2emeTrimestre(record.situation2emeTrimestre());
        enquete.setSituationFuture(record.situationFuture());
        enquete.setEffectifs1erTrimestre(record.effectifs1erTrimestre());
        enquete.setEffectifs2emeTrimestre(record.effectifs2emeTrimestre());
        enquete.setEffectifsFutur(record.effectifsFutur());
        enquete.setPrixMatieres1erTrimestre(record.prixMatieres1erTrimestre());
        enquete.setPrixMatieres2emeTrimestre(record.prixMatieres2emeTrimestre());
        enquete.setPrixMatieresFutur(record.prixMatieresFutur());
        enquete.setAvoirDifficultes(record.avoirDifficultes());
        enquete.setDiffApprovisionnement(record.diffApprovisionnement());
        enquete.setDiffAutre(record.diffAutre());
        enquete.setPleineCapacite(record.pleineCapacite());
        enquete.setTauxUtilisationCapacite(record.tauxUtilisationCapacite());
        return enquete;
    }
}