//package com.ins.insstatistique.service;
//
//import com.ins.insstatistique.dto.AdminLoginRequest;
//import com.ins.insstatistique.dto.AdminRegisterRequest;
//import com.ins.insstatistique.entity.Admin;
//import com.ins.insstatistique.entity.Entreprise;
//import com.ins.insstatistique.entity.Enquete;
//import com.ins.insstatistique.repository.AdminRepository;
//import com.ins.insstatistique.repository.EntrepriseRepository;
//import com.ins.insstatistique.repository.EnqueteRepository;
//import com.ins.insstatistique.repository.ResponsableRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class AdminService {
//    private final AdminRepository adminRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final EntrepriseRepository entrepriseRepository;
//    private final ResponsableRepository responsableRepository;
//    private final EnqueteRepository enqueteRepository;
//
//    public Admin register(AdminRegisterRequest request) {
//        if (adminRepository.existsByEmail(request.getEmail())) {
//            throw new RuntimeException("Email already exists");
//        }
//
//        Admin admin = new Admin();
//        admin.setEmail(request.getEmail());
//        admin.setPassword(passwordEncoder.encode(request.getPassword()));
//        admin.setNom(request.getNom());
//        admin.setPrenom(request.getPrenom());
//        admin.setEnabled(true);
//
//        return adminRepository.save(admin);
//    }
//
//    public Admin login(AdminLoginRequest request) {
//        return adminRepository.findByEmail(request.getEmail())
//                .filter(admin -> passwordEncoder.matches(request.getPassword(), admin.getPassword()))
//                .orElseThrow(() -> new RuntimeException("Invalid credentials"));
//    }
//
//    // Gestion des entreprises
//    public List<Entreprise> getAllEntreprises() {
//        return entrepriseRepository.findAll();
//    }
//
//    public Entreprise updateEntreprise(Long id, Entreprise entreprise) {
//        if (!entrepriseRepository.existsById(id)) {
//            throw new RuntimeException("Entreprise not found");
//        }
//        entreprise.setId(id);
//        return entrepriseRepository.save(entreprise);
//    }
//
//    public void deleteEntreprise(Long id) {
//        entrepriseRepository.deleteById(id);
//    }
//
//    // Gestion des investigateurs
//    public List<Responsable> getAllInvestigateurs() {
//        return responsableRepository.findAll();
//    }
//
//    public Responsable updateInvestigateur(Long id, Responsable responsable) {
//        if (!responsableRepository.existsById(id)) {
//            throw new RuntimeException("Investigateur not found");
//        }
//        responsable.setId(id);
//        return responsableRepository.save(responsable);
//    }
//
//    public void deleteInvestigateur(Long id) {
//        responsableRepository.deleteById(id);
//    }
//
//    // Gestion des enquêtes
//    public List<Enquete> getAllEnquetes() {
//        return enqueteRepository.findAll();
//    }
//
//    public Enquete updateEnquete(Long id, Enquete enquete) {
//        if (!enqueteRepository.existsById(id)) {
//            throw new RuntimeException("Enquete not found");
//        }
//        enquete.setId(id);
//        return enqueteRepository.save(enquete);
//    }
//
//    public void deleteEnquete(Long id) {
//        enqueteRepository.deleteById(id);
//    }
//}