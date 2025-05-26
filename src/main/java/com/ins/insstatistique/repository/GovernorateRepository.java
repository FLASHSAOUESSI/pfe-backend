package com.ins.insstatistique.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ins.insstatistique.entity.Governorate;

@Repository
public interface GovernorateRepository extends JpaRepository<Governorate, Long> {
    Optional<Governorate> findByName(String name);
    boolean existsByName(String name);
}
