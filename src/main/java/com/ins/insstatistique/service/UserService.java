package com.ins.insstatistique.service;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ins.insstatistique.dto.UserLoginRequest;
import com.ins.insstatistique.dto.UserRegisterRequest;
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
    public List<Enquete> getAllEnquetes() {
        return enqueteRepository.findAll();
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
}
