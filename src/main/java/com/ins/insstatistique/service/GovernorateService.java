package com.ins.insstatistique.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ins.insstatistique.dto.ConsolidatedStatisticsDto;
import com.ins.insstatistique.dto.DetailedGovernorateStatDto;
import com.ins.insstatistique.dto.EnterpriseDistributionDto;
import com.ins.insstatistique.dto.GovernorateComparisonDto;
import com.ins.insstatistique.dto.GovernorateEvaluationDto;
import com.ins.insstatistique.dto.GovernorateRankingDto;
import com.ins.insstatistique.dto.GovernorateStatDto;
import com.ins.insstatistique.dto.GovernorateTimeSeriesDto;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.entity.Governorate;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.repository.GovernorateRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class GovernorateService {

    private final GovernorateRepository governorateRepository;
    private final EntrepriseRepository entrepriseRepository;

    public List<Governorate> getAllGovernorates() {
        return governorateRepository.findAll();
    }

    public Governorate getGovernorateById(Long id) {
        return governorateRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Governorate non trouvé"));
    }

    public Governorate createGovernorate(Governorate governorate) {
        return governorateRepository.save(governorate);
    }

    public Governorate updateGovernorate(Long id, Governorate governorate) {
        if (!governorateRepository.existsById(id)) {
            throw new EntityNotFoundException("Governorate non trouvé");
        }
        governorate.setId(id);
        return governorateRepository.save(governorate);
    }

    public void deleteGovernorate(Long id) {
        if (!governorateRepository.existsById(id)) {
            throw new EntityNotFoundException("Governorate non trouvé");
        }
        governorateRepository.deleteById(id);
    }

    /**
     * Initialize Tunisian governorates in the database
     */
    public void initializeTunisianGovernorates() {
        List<String> governorateNames = Arrays.asList(
                "Ariana", "Béja", "Ben Arous", "Bizerte", "Gabès", 
                "Gafsa", "Jendouba", "Kairouan", "Kasserine", "Kébili", 
                "Kef", "Mahdia", "Manouba", "Médenine", "Monastir", 
                "Nabeul", "Sfax", "Sidi Bouzid", "Siliana", "Sousse", 
                "Tataouine", "Tozeur", "Tunis", "Zaghouan"
        );

        for (String name : governorateNames) {
            if (!governorateRepository.existsByName(name)) {
                governorateRepository.save(Governorate.builder().name(name).build());
            }
        }
    }

    /**
     * Get statistics for all governorates including enterprise counts
     * @return List of governorate statistics
     */
    public List<GovernorateStatDto> getGovernorateStatistics() {
        List<Governorate> governorates = governorateRepository.findAll();
        List<GovernorateStatDto> stats = new ArrayList<>();
        
        for (Governorate governorate : governorates) {
            List<Entreprise> enterprises = governorate.getEnterprises();
            if (enterprises == null) {
                // If no enterprises, create empty stats
                stats.add(GovernorateStatDto.builder()
                        .id(governorate.getId())
                        .name(governorate.getName())
                        .totalEnterprises(0)
                        .validatedEnterprises(0)
                        .pendingEnterprises(0)
                        .rejectedEnterprises(0)
                        .build());
                continue;
            }
            
            int totalCount = enterprises.size();
            int validatedCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.VALIDATED)
                    .count();
            int pendingCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.PENDING)
                    .count();
            int rejectedCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.REJECTED)
                    .count();
            
            stats.add(GovernorateStatDto.builder()
                    .id(governorate.getId())
                    .name(governorate.getName())
                    .totalEnterprises(totalCount)
                    .validatedEnterprises(validatedCount)
                    .pendingEnterprises(pendingCount)
                    .rejectedEnterprises(rejectedCount)
                    .build());
        }
        
        return stats;
    }

    /**
     * Get detailed statistics for all governorates including enterprise distribution, rankings, and comparisons
     * @return List of detailed governorate statistics
     */
    public List<DetailedGovernorateStatDto> getDetailedGovernorateStatistics() {
        List<Governorate> governorates = governorateRepository.findAll();
        List<DetailedGovernorateStatDto> stats = new ArrayList<>();
        
        // Get total enterprises for national averages
        long totalNationalEnterprises = entrepriseRepository.count();
        double nationalAveragePerGovernorate = totalNationalEnterprises / (double) governorates.size();
        
        // Calculate basic statistics for each governorate
        Map<Long, DetailedGovernorateStatDto> governorateStatsMap = new HashMap<>();
        for (Governorate governorate : governorates) {
            List<Entreprise> enterprises = governorate.getEnterprises();
            if (enterprises == null) {
                enterprises = new ArrayList<>();
            }
            
            int totalCount = enterprises.size();
            int validatedCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.VALIDATED)
                    .count();
            int pendingCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.PENDING)
                    .count();
            int rejectedCount = (int) enterprises.stream()
                    .filter(e -> e.getStatus() == EntrepriseStatus.REJECTED)
                    .count();
            
            // Calculate percentages
            double validationRate = totalCount > 0 ? (validatedCount * 100.0 / totalCount) : 0;
            double rejectionRate = totalCount > 0 ? (rejectedCount * 100.0 / totalCount) : 0;
            double pendingRate = totalCount > 0 ? (pendingCount * 100.0 / totalCount) : 0;
            
            // Calculate comparison with national average
            double nationalAverageComparison = nationalAveragePerGovernorate > 0 ? 
                    (totalCount / nationalAveragePerGovernorate) : 0;
            
            // Create distribution data for charts
            List<EnterpriseDistributionDto> distribution = new ArrayList<>();
            distribution.add(EnterpriseDistributionDto.builder()
                    .status("VALIDATED")
                    .count(validatedCount)
                    .percentage(validationRate)
                    .build());
            distribution.add(EnterpriseDistributionDto.builder()
                    .status("PENDING")
                    .count(pendingCount)
                    .percentage(pendingRate)
                    .build());
            distribution.add(EnterpriseDistributionDto.builder()
                    .status("REJECTED")
                    .count(rejectedCount)
                    .percentage(rejectionRate)
                    .build());
            
            // Build initial stats (without ranking yet)
            DetailedGovernorateStatDto governorateStat = DetailedGovernorateStatDto.builder()
                    .id(governorate.getId())
                    .name(governorate.getName())
                    .totalEnterprises(totalCount)
                    .validatedEnterprises(validatedCount)
                    .pendingEnterprises(pendingCount)
                    .rejectedEnterprises(rejectedCount)
                    .validationRate(validationRate)
                    .rejectionRate(rejectionRate)
                    .pendingRate(pendingRate)
                    .nationalAverageComparison(nationalAverageComparison)
                    .statusDistribution(distribution)
                    // Enterprise density will be set if population data is available
                    .enterpriseDensity(0.0)
                    // Monthly growth will be null if time-based data isn't available
                    .monthlyGrowth(null)
                    .build();
            
            governorateStatsMap.put(governorate.getId(), governorateStat);
            stats.add(governorateStat);
        }
        
        // Sort governorates by total enterprise count for ranking
        List<DetailedGovernorateStatDto> sortedByCount = new ArrayList<>(stats);
        sortedByCount.sort(Comparator.comparingInt(DetailedGovernorateStatDto::getTotalEnterprises).reversed());
        
        // Update rankings
        for (int i = 0; i < sortedByCount.size(); i++) {
            DetailedGovernorateStatDto stat = sortedByCount.get(i);
            stat.setRegionalRanking(i + 1);
        }
        
        return stats;
    }

    /**
     * Get detailed statistics for a specific governorate
     * @param id The governorate ID
     * @return Detailed governorate statistics
     */
    public DetailedGovernorateStatDto getDetailedGovernorateStatisticsById(Long id) {
        // First get all statistics to ensure ranking is correctly calculated
        List<DetailedGovernorateStatDto> allStats = getDetailedGovernorateStatistics();
        
        // Find the specific governorate statistics
        return allStats.stream()
                .filter(stat -> stat.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Governorate non trouvé"));
    }

    /**
     * Compare two governorates based on their enterprise statistics
     * @param firstGovernorateId The ID of the first governorate
     * @param secondGovernorateId The ID of the second governorate
     * @return Comparison data between the two governorates
     */
    public GovernorateComparisonDto compareGovernorates(Long firstGovernorateId, Long secondGovernorateId) {
        DetailedGovernorateStatDto first = getDetailedGovernorateStatisticsById(firstGovernorateId);
        DetailedGovernorateStatDto second = getDetailedGovernorateStatisticsById(secondGovernorateId);
        
        // Calculate differences
        int totalDiff = first.getTotalEnterprises() - second.getTotalEnterprises();
        int validatedDiff = first.getValidatedEnterprises() - second.getValidatedEnterprises();
        double validationRateDiff = first.getValidationRate() - second.getValidationRate();
        
        // Calculate ratio
        double ratio = second.getTotalEnterprises() > 0 ? 
                (first.getTotalEnterprises() / (double) second.getTotalEnterprises()) : 0;
        
        // Calculate status differences
        Map<String, Integer> statusDiffs = new HashMap<>();
        statusDiffs.put("VALIDATED", first.getValidatedEnterprises() - second.getValidatedEnterprises());
        statusDiffs.put("PENDING", first.getPendingEnterprises() - second.getPendingEnterprises());
        statusDiffs.put("REJECTED", first.getRejectedEnterprises() - second.getRejectedEnterprises());
        
        // Build comparison DTO
        return GovernorateComparisonDto.builder()
                .firstGovernorate(first)
                .secondGovernorate(second)
                .totalEnterpriseDifference(totalDiff)
                .validatedEnterpriseDifference(validatedDiff)
                .validationRateDifference(validationRateDiff)
                .firstToSecondRatio(ratio)
                .statusDifferences(statusDiffs)
                .build();
    }

    /**
     * Get governorate rankings based on a specific metric
     * @param metric The metric to rank by ("total", "validated", "validation_rate", "pending", "rejected")
     * @return List of governorates ranked by the specified metric
     */
    public List<GovernorateRankingDto> getGovernorateRankings(String metric) {
        List<DetailedGovernorateStatDto> stats = getDetailedGovernorateStatistics();
        List<GovernorateRankingDto> rankings = new ArrayList<>();
        
        // Sort based on the specified metric
        Comparator<DetailedGovernorateStatDto> comparator;
        switch (metric.toLowerCase()) {
            case "total":
                comparator = Comparator.comparingInt(DetailedGovernorateStatDto::getTotalEnterprises).reversed();
                break;
            case "validated":
                comparator = Comparator.comparingInt(DetailedGovernorateStatDto::getValidatedEnterprises).reversed();
                break;
            case "validation_rate":
                comparator = Comparator.comparingDouble(DetailedGovernorateStatDto::getValidationRate).reversed();
                break;
            case "pending":
                comparator = Comparator.comparingInt(DetailedGovernorateStatDto::getPendingEnterprises).reversed();
                break;
            case "rejected":
                comparator = Comparator.comparingInt(DetailedGovernorateStatDto::getRejectedEnterprises).reversed();
                break;
            default:
                comparator = Comparator.comparingInt(DetailedGovernorateStatDto::getTotalEnterprises).reversed();
        }
        
        List<DetailedGovernorateStatDto> sortedStats = new ArrayList<>(stats);
        sortedStats.sort(comparator);
        
        // Create ranking DTOs
        for (int i = 0; i < sortedStats.size(); i++) {
            DetailedGovernorateStatDto stat = sortedStats.get(i);
            int value = 0;
            
            switch (metric.toLowerCase()) {
                case "total":
                    value = stat.getTotalEnterprises();
                    break;
                case "validated":
                    value = stat.getValidatedEnterprises();
                    break;
                case "validation_rate":
                    value = (int) stat.getValidationRate();
                    break;
                case "pending":
                    value = stat.getPendingEnterprises();
                    break;
                case "rejected":
                    value = stat.getRejectedEnterprises();
                    break;
                default:
                    value = stat.getTotalEnterprises();
            }
            
            rankings.add(GovernorateRankingDto.builder()
                    .id(stat.getId())
                    .name(stat.getName())
                    .ranking(i + 1)
                    .value(value)
                    .build());
        }
        
        return rankings;
    }
    
    /**
     * Generate time series data for governorates
     * Note: This generates simulated data since actual time-based data isn't available
     * @return List of governorate time series data
     */
    public List<GovernorateTimeSeriesDto> getGovernorateTimeSeries() {
        List<Governorate> governorates = governorateRepository.findAll();
        List<GovernorateTimeSeriesDto> timeSeriesData = new ArrayList<>();
        
        // Get current month and year
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        for (Governorate governorate : governorates) {
            int totalEnterprises = governorate.getEnterprises() != null ? governorate.getEnterprises().size() : 0;
            
            // Generate simulated monthly data
            Map<String, Integer> monthlyData = new HashMap<>();
            int startValue = Math.max(0, totalEnterprises - (int)(totalEnterprises * 0.3)); // Start with 70% of current value
            double growthRate = 0;
            
            if (totalEnterprises > 0) {
                double monthlyGrowth = 0.03 + (Math.random() * 0.05); // 3-8% monthly growth
                growthRate = monthlyGrowth * 12; // Annual growth
                
                int currentValue = startValue;
                for (String month : months) {
                    monthlyData.put(month, currentValue);
                    currentValue = (int) (currentValue * (1 + monthlyGrowth));
                }
            } else {
                // No enterprises, just fill with zeros
                for (String month : months) {
                    monthlyData.put(month, 0);
                }
            }
            
            timeSeriesData.add(GovernorateTimeSeriesDto.builder()
                    .id(governorate.getId())
                    .name(governorate.getName())
                    .monthlyData(monthlyData)
                    .growthRate(growthRate * 100) // Convert to percentage
                    .build());
        }
        
        return timeSeriesData;
    }
    
    /**
     * Get time series data for a specific governorate
     * @param id The governorate ID
     * @return Governorate time series data
     */
    public GovernorateTimeSeriesDto getGovernorateTimeSeriesById(Long id) {
        return getGovernorateTimeSeries().stream()
                .filter(ts -> ts.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Governorate non trouvé"));
    }

    /**
     * Get consolidated statistics for the entire system
     * @return Consolidated statistics DTO
     */
    public ConsolidatedStatisticsDto getConsolidatedStatistics() {
        // Get basic enterprise counts
        int totalEnterprises = (int) entrepriseRepository.count();
        int validatedEnterprises = entrepriseRepository.findByStatus(EntrepriseStatus.VALIDATED).size();
        int pendingEnterprises = entrepriseRepository.findByStatus(EntrepriseStatus.PENDING).size();
        int rejectedEnterprises = entrepriseRepository.findByStatus(EntrepriseStatus.REJECTED).size();
        
        // Status distribution
        Map<String, Integer> statusDistribution = new HashMap<>();
        statusDistribution.put("VALIDATED", validatedEnterprises);
        statusDistribution.put("PENDING", pendingEnterprises);
        statusDistribution.put("REJECTED", rejectedEnterprises);
        
        // Get governorate statistics
        List<DetailedGovernorateStatDto> governorateStats = getDetailedGovernorateStatistics();
        int totalGovernorates = governorateStats.size();
        
        // Find governorate with most enterprises
        DetailedGovernorateStatDto mostEnterprises = governorateStats.stream()
                .max(Comparator.comparingInt(DetailedGovernorateStatDto::getTotalEnterprises))
                .orElse(null);
        
        // Find governorate with highest validation rate
        DetailedGovernorateStatDto highestValidationRate = governorateStats.stream()
                .filter(g -> g.getTotalEnterprises() > 0)  // Only consider governorates with enterprises
                .max(Comparator.comparingDouble(DetailedGovernorateStatDto::getValidationRate))
                .orElse(null);
        
        // Governorate distribution
        Map<String, Integer> governorateDistribution = new HashMap<>();
        governorateStats.forEach(g -> governorateDistribution.put(g.getName(), g.getTotalEnterprises()));
          // Top 5 governorates
        List<GovernorateRankingDto> topGovernorates = getGovernorateRankings("total").stream()
                .limit(5)
                .collect(Collectors.toList());
        
        // Growth statistics
        List<GovernorateTimeSeriesDto> timeSeriesData = getGovernorateTimeSeries();
        double averageGrowthRate = timeSeriesData.stream()
                .mapToDouble(GovernorateTimeSeriesDto::getGrowthRate)
                .average()
                .orElse(0);
        
        // Monthly growth rates
        Map<String, Double> monthlyGrowthRates = new HashMap<>();
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        
        // Calculate total enterprises per month across all governorates
        for (int i = 0; i < months.length; i++) {
            final String month = months[i];
            int monthTotal = timeSeriesData.stream()
                    .mapToInt(ts -> ts.getMonthlyData().getOrDefault(month, 0))
                    .sum();
            
            // Calculate growth rate from previous month (if not first month)
            if (i > 0) {
                final String prevMonth = months[i-1];
                int prevMonthTotal = timeSeriesData.stream()
                        .mapToInt(ts -> ts.getMonthlyData().getOrDefault(prevMonth, 0))
                        .sum();
                
                double growthRate = prevMonthTotal > 0 ? 
                        ((monthTotal - prevMonthTotal) / (double) prevMonthTotal) * 100 : 0;
                monthlyGrowthRates.put(month, growthRate);
            } else {
                // For first month, just put 0 as growth rate
                monthlyGrowthRates.put(month, 0.0);
            }
        }
        
        // Build consolidated statistics
        return ConsolidatedStatisticsDto.builder()
                .totalEnterprises(totalEnterprises)
                .validatedEnterprises(validatedEnterprises)
                .pendingEnterprises(pendingEnterprises)
                .rejectedEnterprises(rejectedEnterprises)
                .totalGovernorates(totalGovernorates)
                .governorateWithMostEnterprises(mostEnterprises != null ? mostEnterprises.getName() : null)
                .governorateWithHighestValidationRate(highestValidationRate != null ? highestValidationRate.getName() : null)
                .statusDistribution(statusDistribution)
                .governorateDistribution(governorateDistribution)
                .topGovernorates(topGovernorates)
                .averageGrowthRate(averageGrowthRate)
                .monthlyGrowthRates(monthlyGrowthRates)
                .build();
    }
    
    /**
     * Evaluate governorate performance based on enterprise metrics
     * @return List of governorate evaluations
     */
    public List<GovernorateEvaluationDto> evaluateGovernorates() {
        // Get both detailed statistics and time series data
        List<DetailedGovernorateStatDto> detailedStats = getDetailedGovernorateStatistics();
        List<GovernorateTimeSeriesDto> timeSeriesData = getGovernorateTimeSeries();
        
        // Create a map for quick lookup of time series data by governorate id
        Map<Long, GovernorateTimeSeriesDto> timeSeriesMap = timeSeriesData.stream()
                .collect(Collectors.toMap(GovernorateTimeSeriesDto::getId, ts -> ts));
        
        List<GovernorateEvaluationDto> evaluations = new ArrayList<>();
        
        for (DetailedGovernorateStatDto stat : detailedStats) {
            String overallStatus;
            int performanceScore = 0;
            
            // Calculate performance score based on various metrics
            // 1. Enterprise count (max 40 points)
            int enterpriseScore = 0;
            if (stat.getTotalEnterprises() >= 100) enterpriseScore = 40;
            else if (stat.getTotalEnterprises() >= 50) enterpriseScore = 30;
            else if (stat.getTotalEnterprises() >= 25) enterpriseScore = 20;
            else if (stat.getTotalEnterprises() >= 10) enterpriseScore = 10;
            performanceScore += enterpriseScore;
            
            // 2. Validation rate (max 40 points)
            int validationScore = 0;
            if (stat.getValidationRate() >= 90) validationScore = 40;
            else if (stat.getValidationRate() >= 75) validationScore = 30;
            else if (stat.getValidationRate() >= 60) validationScore = 20;
            else if (stat.getValidationRate() >= 40) validationScore = 10;
            performanceScore += validationScore;
            
            // 3. Growth trend (max 20 points)
            int growthScore = 0;
            double growthRate = 0;
            String growthTrend = "STABLE";
            
            GovernorateTimeSeriesDto timeSeries = timeSeriesMap.get(stat.getId());
            if (timeSeries != null) {
                growthRate = timeSeries.getGrowthRate();
                
                if (growthRate >= 20) {
                    growthScore = 20;
                    growthTrend = "GROWING";
                } else if (growthRate >= 10) {
                    growthScore = 15;
                    growthTrend = "GROWING";
                } else if (growthRate >= 5) {
                    growthScore = 10;
                    growthTrend = "GROWING";
                } else if (growthRate >= 0) {
                    growthScore = 5;
                    growthTrend = "STABLE";
                } else {
                    growthTrend = "DECLINING";
                }
            }
            performanceScore += growthScore;
            
            // Determine overall status based on performance score
            if (performanceScore >= 85) {
                overallStatus = "EXCELLENT";
            } else if (performanceScore >= 70) {
                overallStatus = "GOOD";
            } else if (performanceScore >= 50) {
                overallStatus = "AVERAGE";
            } else {
                overallStatus = "NEEDS_IMPROVEMENT";
            }
            
            // Build evaluation DTO
            evaluations.add(GovernorateEvaluationDto.builder()
                    .id(stat.getId())
                    .name(stat.getName())
                    .enterpriseCount(stat.getTotalEnterprises())
                    .validationRate(stat.getValidationRate())
                    .overallStatus(overallStatus)
                    .growthRate(growthRate)
                    .growthTrend(growthTrend)
                    .performanceScore(performanceScore)
                    .build());
        }
        
        // Sort by performance score (highest first)
        evaluations.sort(Comparator.comparingInt(GovernorateEvaluationDto::getPerformanceScore).reversed());
        
        return evaluations;
    }
    
    /**
     * Get evaluation for a specific governorate
     * @param id The governorate ID
     * @return Governorate evaluation
     */
    public GovernorateEvaluationDto evaluateGovernorate(Long id) {
        return evaluateGovernorates().stream()
                .filter(eval -> eval.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Governorate non trouvé"));
    }
}
