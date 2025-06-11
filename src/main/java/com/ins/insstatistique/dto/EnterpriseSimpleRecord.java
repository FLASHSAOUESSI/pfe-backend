package com.ins.insstatistique.dto;

import com.ins.insstatistique.entity.EntrepriseStatus;

/**
 * A simplified record representation of an Enterprise with only id and name
 */
public record EnterpriseSimpleRecord(
    Long id,
    String name,
             String address,
             String email,
             String fax,
             EntrepriseStatus status,
             Long governorateId,
             String governorateName,
             int responsablesCount
) {
}
