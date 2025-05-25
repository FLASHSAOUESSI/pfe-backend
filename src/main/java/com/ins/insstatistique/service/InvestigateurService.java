// InvestigateurService.java
package com.ins.insstatistique.service;

import com.ins.insstatistique.entity.Investigateur;
import com.ins.insstatistique.repository.InvestigateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InvestigateurService {

    private final InvestigateurRepository investigateurRepository;
    private final PasswordEncoder passwordEncoder;

    public List<Investigateur> getAllInvestigateurs() {
        return investigateurRepository.findAll();
    }

    public Investigateur getInvestigateurById(Long id) {
        return investigateurRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Investigateur non trouvé"));
    }

    public Investigateur updateInvestigateur(Long id, Investigateur investigateur) {
        if (!investigateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Investigateur non trouvé");
        }

        if (investigateur.getPassword() != null && !investigateur.getPassword().isEmpty()) {
            investigateur.setPassword(passwordEncoder.encode(investigateur.getPassword()));
        }

        investigateur.setId(id);
        return investigateurRepository.save(investigateur);
    }

    public void deleteInvestigateur(Long id) {
        if (!investigateurRepository.existsById(id)) {
            throw new EntityNotFoundException("Investigateur non trouvé");
        }
        investigateurRepository.deleteById(id);
    }

    public Investigateur getInvestigateurByEmail(String email) {
        return investigateurRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Investigateur non trouvé"));
    }
}