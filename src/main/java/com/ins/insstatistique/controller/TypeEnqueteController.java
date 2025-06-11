package com.ins.insstatistique.controller;

import com.ins.insstatistique.dto.TypeEnqueteDTO;
import com.ins.insstatistique.repository.EnqueteRepository;
import com.ins.insstatistique.service.TypeEnqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-enquete")
public class TypeEnqueteController {

    @Autowired
    private TypeEnqueteService typeEnqueteService;

    @Autowired
    private EnqueteRepository enqueteRepository;

    @GetMapping
    public List<TypeEnqueteDTO> getAll() {
        return typeEnqueteService.getAllTypeEnquetes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeEnqueteDTO> getById(@PathVariable Long id) {
        return typeEnqueteService.getTypeEnqueteById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TypeEnqueteDTO> create(@RequestBody TypeEnqueteDTO typeEnqueteDTO) {
        TypeEnqueteDTO created = typeEnqueteService.createTypeEnquete(typeEnqueteDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeEnqueteDTO> update(@PathVariable Long id, @RequestBody TypeEnqueteDTO typeEnqueteDTO) {
        return typeEnqueteService.updateTypeEnquete(id, typeEnqueteDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        long count = enqueteRepository.countByTypeEnquete_Id(id);
        if (count > 0) {
            return ResponseEntity.badRequest().build(); // Or add an explicit message
        }
        if (typeEnqueteService.deleteTypeEnquete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
