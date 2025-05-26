package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseDistributionDto {
    private String status;
    private int count;
    private double percentage;
}
