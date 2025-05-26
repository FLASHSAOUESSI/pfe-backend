package com.ins.insstatistique.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovernorateComparisonDto {
    private DetailedGovernorateStatDto firstGovernorate;
    private DetailedGovernorateStatDto secondGovernorate;
    
    // Difference metrics
    private int totalEnterpriseDifference;
    private int validatedEnterpriseDifference;
    private double validationRateDifference;
    
    // Percentages
    private double firstToSecondRatio;  // Ratio of total enterprises
    
    // Comparison for each status
    private Map<String, Integer> statusDifferences;
}
