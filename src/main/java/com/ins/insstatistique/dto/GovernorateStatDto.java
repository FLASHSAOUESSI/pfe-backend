package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovernorateStatDto {
    private Long id;
    private String name;
    private int totalEnterprises;
    private int validatedEnterprises;
    private int pendingEnterprises;
    private int rejectedEnterprises;
}
