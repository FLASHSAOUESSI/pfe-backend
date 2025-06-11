package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.entity.Entreprise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EnqueteRepository extends JpaRepository<Enquete, Long> {

    long countByTypeEnquete_Id(Long typeEnqueteId);

    /**
     * Finds Enquetes that have been started but not fully completed.
     * This is determined by checking that at least one of the initial fields is filled,
     * but at least one of the final fields is still null.
     *
     * @return A list of partially completed Enquete entities.
     */
    @Query("SELECT e FROM Enquete e WHERE " +
            "(e.situationFuture IS NULL OR e.effectifsFutur IS NULL OR e.prixMatieresFutur IS NULL) AND " +
            "(e.situation1erTrimestre IS NOT NULL OR e.effectifs1erTrimestre IS NOT NULL OR e.prixMatieres1erTrimestre IS NOT NULL)")
    List<Enquete> findPartiallyCompletedEnquetes();



}