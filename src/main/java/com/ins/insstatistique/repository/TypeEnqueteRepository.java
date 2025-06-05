package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.TypeEnquete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeEnqueteRepository extends JpaRepository<TypeEnquete, Long> {
} 