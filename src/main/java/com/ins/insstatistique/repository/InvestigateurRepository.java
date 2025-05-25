// InvestigateurRepository.java
package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.Investigateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvestigateurRepository extends JpaRepository<Investigateur, Long> {
    Optional<Investigateur> findByEmail(String email);
    boolean existsByEmail(String email);
}