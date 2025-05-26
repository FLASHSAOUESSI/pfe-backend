package com.ins.insstatistique.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidatedStatisticsDto {
    // Overall enterprise statistics
    private int totalEnterprises;
    private int validatedEnterprises;
    private int pendingEnterprises;
    private int rejectedEnterprises;
    
    // Governorate statistics
    private int totalGovernorates;
    private String governorateWithMostEnterprises;
    private String governorateWithHighestValidationRate;
    
    // Distribution statistics
    private Map<String, Integer> statusDistribution;
    private Map<String, Integer> governorateDistribution;
    
    // Top governorates
    private List<GovernorateRankingDto> topGovernorates;
    
    // Growth statistics
    private double averageGrowthRate;
    private Map<String, Double> monthlyGrowthRates;
}
