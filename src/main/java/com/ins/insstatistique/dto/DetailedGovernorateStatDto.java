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
public class DetailedGovernorateStatDto {
    private Long id;
    private String name;
    
    // Basic statistics
    private int totalEnterprises;
    private int validatedEnterprises;
    private int pendingEnterprises;
    private int rejectedEnterprises;
    
    // Percentage statistics
    private double validationRate;  // Percentage of validated enterprises
    private double rejectionRate;   // Percentage of rejected enterprises
    private double pendingRate;     // Percentage of pending enterprises
    
    // Comparative statistics
    private double nationalAverageComparison;  // How this governorate compares to national average (ratio)
    private int regionalRanking;               // Ranking among all governorates (1 = highest enterprise count)
    
    // Time-based statistics (if available)
    private Map<String, Integer> monthlyGrowth; // Growth of enterprises over months
    
    // Enterprise density (number of enterprises per capita or per area)
    private double enterpriseDensity;
    
    // Distribution data for charts
    private List<EnterpriseDistributionDto> statusDistribution;
}
