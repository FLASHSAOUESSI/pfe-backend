package com.ins.insstatistique.service;


import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.repository.EnqueteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class EnqueteService {

    private final EnqueteRepository enqueteRepository;

    @Transactional
    public Enquete saveEnquete(Enquete enquete) {

        if (enquete.getIdentifiantStat() == null || enquete.getIdentifiantStat().isEmpty()) {
            throw new IllegalArgumentException("Identifiant Stat cannot be empty");
        }

        return enqueteRepository.save(enquete);
    }
    @Transactional(readOnly = true)
    public List<Enquete> getAllEnquetes() {
        return enqueteRepository.findAll();
    }

}