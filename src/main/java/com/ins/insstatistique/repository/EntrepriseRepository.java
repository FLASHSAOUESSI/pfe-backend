// EntrepriseRepository.java
package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByEmail(String email);
    boolean existsByEmail(String email);
}