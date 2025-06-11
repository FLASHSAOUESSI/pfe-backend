package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for TypeEnquete
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeEnqueteDTO {
    private Long id;
    private Integer annee;
    private String periodicite;
    private String session;
    private String statut;
}
