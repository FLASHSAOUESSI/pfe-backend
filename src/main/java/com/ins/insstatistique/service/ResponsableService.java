//// InvestigateurService.java
//package com.ins.insstatistique.service;
//
//import com.ins.insstatistique.entity.Responsable;
//import com.ins.insstatistique.repository.ResponsableRepository;
//import jakarta.persistence.EntityNotFoundException;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@Transactional
//public class ResponsableService {
//
//    private final ResponsableRepository responsableRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    public List<Responsable> getAllInvestigateurs() {
//        return responsableRepository.findAll();
//    }
//
//    public Responsable getInvestigateurById(Long id) {
//        return responsableRepository.findById(id)
//                .orElseThrow(() -> new EntityNotFoundException("Investigateur non trouvé"));
//    }
//
//    public Responsable updateInvestigateur(Long id, Responsable responsable) {
//        if (!responsableRepository.existsById(id)) {
//            throw new EntityNotFoundException("Investigateur non trouvé");
//        }
//
//        if (responsable.getPassword() != null && !responsable.getPassword().isEmpty()) {
//            responsable.setPassword(passwordEncoder.encode(responsable.getPassword()));
//        }
//
//        responsable.setId(id);
//        return responsableRepository.save(responsable);
//    }
//
//    public void deleteInvestigateur(Long id) {
//        if (!responsableRepository.existsById(id)) {
//            throw new EntityNotFoundException("Investigateur non trouvé");
//        }
//        responsableRepository.deleteById(id);
//    }
//
//    public Responsable getInvestigateurByEmail(String email) {
//        return responsableRepository.findByEmail(email)
//                .orElseThrow(() -> new EntityNotFoundException("Investigateur non trouvé"));
//    }
//}