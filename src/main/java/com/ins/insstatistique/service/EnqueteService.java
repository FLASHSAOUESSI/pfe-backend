package com.ins.insstatistique.service;


import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.repository.EnqueteRepository;
import lombok.RequiredArgsConstructor; // Lombok for constructor injection
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Good practice for service methods modifying data

import java.util.List;


@Service // Marks this as a Spring service component
@RequiredArgsConstructor // Lombok: generates constructor with required (final) fields
public class EnqueteService {

    private final EnqueteRepository enqueteRepository; // Inject repository (final makes it required)

    @Transactional // Ensures the save operation is part of a transaction
    public Enquete saveEnquete(Enquete enquete) {
        // Add any business logic/validation before saving if needed
        // Example validation (very basic):
        if (enquete.getIdentifiantStat() == null || enquete.getIdentifiantStat().isEmpty()) {
            throw new IllegalArgumentException("Identifiant Stat cannot be empty");
        }
        // ... more validation ...

        return enqueteRepository.save(enquete);
    }
    @Transactional(readOnly = true)
    public List<Enquete> getAllEnquetes() {
        return enqueteRepository.findAll();
    }
    // --- Implement optional methods if you added them to the interface ---
    /*
    @Override
    @Transactional(readOnly = true) // Good practice for read operations
    public Optional<Enquete> getEnqueteById(Long id) {
        return enqueteRepository.findById(id);
    }



    @Override
    @Transactional
    public void deleteEnquete(Long id) {
         if (!enqueteRepository.existsById(id)) {
             // Optional: Handle case where ID doesn't exist
             throw new RuntimeException("Enquete not found with id: " + id); // Or a custom exception
         }
        enqueteRepository.deleteById(id);
    }
    */
}