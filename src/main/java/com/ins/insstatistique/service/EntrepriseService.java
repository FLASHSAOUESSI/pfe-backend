// EntrepriseService.java
package com.ins.insstatistique.service;

import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.repository.EntrepriseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class EntrepriseService {

    private final EntrepriseRepository entrepriseRepository;

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
}