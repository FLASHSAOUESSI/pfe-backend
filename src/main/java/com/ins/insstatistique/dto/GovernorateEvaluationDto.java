package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovernorateEvaluationDto {
    private Long id;
    private String name;
    
    // Performance indicators
    private int enterpriseCount;
    private double validationRate;
    
    // Status evaluation based on predefined thresholds
    private String overallStatus;  // "EXCELLENT", "GOOD", "AVERAGE", "NEEDS_IMPROVEMENT"
    
    // Growth metrics
    private double growthRate;
    private String growthTrend;  // "GROWING", "STABLE", "DECLINING"
    
    // Performance score (0-100)
    private int performanceScore;
}
