package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseDashboardDto {
    private long totalEnterprises;
    private long pendingEnterprises;
    private long validatedEnterprises;
    private long rejectedEnterprises;
}
