package com.ins.insstatistique.dto;

import com.ins.insstatistique.entity.Role;

/**
 * DTO representing a User with their Enterprise information
 */
public record UserWithEnterpriseDTO(
    Long id,
    String nom,
    String email,
    String phone,
    Role role,
    boolean enabled,
    EnterpriseSimpleRecord enterprise
) {
}
