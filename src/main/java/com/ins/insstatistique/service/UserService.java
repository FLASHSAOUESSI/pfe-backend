package com.ins.insstatistique.service;

import java.util.List;

import com.ins.insstatistique.dto.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.Role;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.repository.EnqueteRepository;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntrepriseRepository entrepriseRepository;
    private final EnqueteRepository enqueteRepository;

    public User register(UserRegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNom(request.getNom());
        user.setEnabled(true);
        user.setRole(Role.RESPONSABLE);

        return userRepository.save(user);
    }

    public User login(UserLoginRequest request) {
        return userRepository.findByEmail(request.getEmail())
                .filter(user -> passwordEncoder.matches(request.getPassword(), user.getPassword()))
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
    }

    // Gestion des entreprises
    public List<Entreprise> getAllEntreprises() {
        return entrepriseRepository.findAll();
    }

    public Entreprise updateEntreprise(Long id, Entreprise entreprise) {
        if (!entrepriseRepository.existsById(id)) {
            throw new RuntimeException("Entreprise not found");
        }
        entreprise.setId(id);
        return entrepriseRepository.save(entreprise);
    }

    public void deleteEntreprise(Long id) {
        entrepriseRepository.deleteById(id);
    }

    // Gestion des investigateurs/responsables
    public List<User> getAllResponsables() {
        return userRepository.findByRole(Role.RESPONSABLE);
    }

    public User updateResponsable(Long id, User responsable) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Responsable not found");
        }
        User existingUser = userRepository.findById(id).orElseThrow();
        responsable.setId(id);
        responsable.setRole(Role.RESPONSABLE);
        responsable.setPassword(existingUser.getPassword()); // Preserve password if not changed
        return userRepository.save(responsable);
    }

    public void deleteResponsable(Long id) {
        userRepository.deleteById(id);
    }

    // Gestion des enquêtes
    public List<EnqueteResponseRecord> getAllEnquetes() {
        List<Enquete> enquetes= enqueteRepository.findAll();
        return enquetes.stream().map(this::mapToResponseRecord).toList();
    }

    public Enquete updateEnquete(Long id, Enquete enquete) {
        if (!enqueteRepository.existsById(id)) {
            throw new RuntimeException("Enquete not found");
        }
        enquete.setId(id);
        return enqueteRepository.save(enquete);
    }

    public void deleteEnquete(Long id) {
        enqueteRepository.deleteById(id);
    }

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
