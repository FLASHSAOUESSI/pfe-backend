package com.ins.insstatistique.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "type_enquete")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeEnquete {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer annee;

    @Column(nullable = false)
    private String periodicite;

    @Column(nullable = false)
    private String session;

    @Column(nullable = false)
    private String statut; // "able" ou "enable"

    @Version
    private Integer version = 0;
}
