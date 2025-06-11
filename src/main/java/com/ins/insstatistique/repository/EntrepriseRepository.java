// EntrepriseRepository.java
package com.ins.insstatistique.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;

@Repository
public interface EntrepriseRepository extends JpaRepository<Entreprise, Long> {
    Optional<Entreprise> findByEmail(String email);
    boolean existsByEmail(String email);
    List<Entreprise> findByStatus(EntrepriseStatus status);

    /**
     * Finds Enquetes that have not been started at all.
     * This is determined by checking that all key answer fields are null.
     * We also ensure there's a respondent email to send the reminder to.
     *
     * @return A list of Enquete entities that have not been started.
     */
    @Query("SELECT ent FROM Entreprise ent WHERE NOT EXISTS " +
            "(SELECT 1 FROM Enquete enq WHERE enq.mailRepondant = ent.email)")
    List<Entreprise> findEntreprisesWithNoEnquete();
}