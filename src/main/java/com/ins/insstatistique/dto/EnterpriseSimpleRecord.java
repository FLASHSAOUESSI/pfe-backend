package com.ins.insstatistique.dto;

/**
 * A simplified record representation of an Enterprise with only id and name
 */
public record EnterpriseSimpleRecord(
    Long id,
    String name
) {
}
