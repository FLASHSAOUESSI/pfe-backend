package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.EmailToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<EmailToken, Long> {
    EmailToken findByToken(String token);
}
