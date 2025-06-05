package com.ins.insstatistique.repository;

import com.ins.insstatistique.entity.ContactMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContactMessageRepository extends JpaRepository<ContactMessage, Long> {
    List<ContactMessage> findAllByOrderByTimestampDesc();
    List<ContactMessage> findByRespondedFalseOrderByTimestampAsc();
} 