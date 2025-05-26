package com.ins.insstatistique.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ins.insstatistique.dto.ConsolidatedStatisticsDto;
import com.ins.insstatistique.dto.DetailedGovernorateStatDto;
import com.ins.insstatistique.dto.GovernorateComparisonDto;
import com.ins.insstatistique.dto.GovernorateEvaluationDto;
import com.ins.insstatistique.dto.GovernorateRankingDto;
import com.ins.insstatistique.dto.GovernorateStatDto;
import com.ins.insstatistique.dto.GovernorateTimeSeriesDto;
import com.ins.insstatistique.entity.Governorate;
import com.ins.insstatistique.service.GovernorateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/governorates")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GovernorateController {

    private final GovernorateService governorateService;

    @GetMapping
    public ResponseEntity<List<Governorate>> getAllGovernorates() {
        return ResponseEntity.ok(governorateService.getAllGovernorates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Governorate> getGovernorateById(@PathVariable Long id) {
        return ResponseEntity.ok(governorateService.getGovernorateById(id));
    }

    @PostMapping
    public ResponseEntity<Governorate> createGovernorate(@RequestBody Governorate governorate) {
        return ResponseEntity.ok(governorateService.createGovernorate(governorate));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Governorate> updateGovernorate(@PathVariable Long id, @RequestBody Governorate governorate) {
        return ResponseEntity.ok(governorateService.updateGovernorate(id, governorate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGovernorate(@PathVariable Long id) {
        governorateService.deleteGovernorate(id);
        return ResponseEntity.ok().build();
    }

    /**
     * Initialize all Tunisian governorates
     * @return A message indicating the initialization status
     */
    @PostMapping("/initialize")
    public ResponseEntity<String> initializeTunisianGovernorates() {
        governorateService.initializeTunisianGovernorates();
        return ResponseEntity.ok("Tunisian governorates initialized successfully");
    }

    /**
     * Get statistics for all governorates including enterprise counts
     * @return List of governorate statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<List<GovernorateStatDto>> getGovernorateStatistics() {
        return ResponseEntity.ok(governorateService.getGovernorateStatistics());
    }

    /**
     * Get detailed statistics for all governorates
     * @return List of detailed governorate statistics
     */
    @GetMapping("/statistics/detailed")
    public ResponseEntity<List<DetailedGovernorateStatDto>> getDetailedGovernorateStatistics() {
        return ResponseEntity.ok(governorateService.getDetailedGovernorateStatistics());
    }

    /**
     * Get detailed statistics for a specific governorate
     * @param id The governorate ID
     * @return Detailed governorate statistics
     */
    @GetMapping("/{id}/statistics/detailed")
    public ResponseEntity<DetailedGovernorateStatDto> getDetailedGovernorateStatisticsById(@PathVariable Long id) {
        return ResponseEntity.ok(governorateService.getDetailedGovernorateStatisticsById(id));
    }

    /**
     * Compare two governorates based on their enterprise statistics
     * @param firstId The ID of the first governorate
     * @param secondId The ID of the second governorate
     * @return Comparison data between the two governorates
     */
    @GetMapping("/compare")
    public ResponseEntity<GovernorateComparisonDto> compareGovernorates(
            @RequestParam Long firstId, 
            @RequestParam Long secondId) {
        return ResponseEntity.ok(governorateService.compareGovernorates(firstId, secondId));
    }

    /**
     * Get governorates ranked by a specific metric
     * @param metric The metric to rank by ("total", "validated", "validation_rate", "pending", "rejected")
     * @return List of governorates ranked by the specified metric
     */
    @GetMapping("/rankings")
    public ResponseEntity<List<GovernorateRankingDto>> getGovernorateRankings(
            @RequestParam(defaultValue = "total") String metric) {
        return ResponseEntity.ok(governorateService.getGovernorateRankings(metric));
    }

    /**
     * Get time series data for all governorates
     * @return List of governorate time series data
     */
    @GetMapping("/time-series")
    public ResponseEntity<List<GovernorateTimeSeriesDto>> getGovernorateTimeSeries() {
        return ResponseEntity.ok(governorateService.getGovernorateTimeSeries());
    }

    /**
     * Get time series data for a specific governorate
     * @param id The governorate ID
     * @return Governorate time series data
     */
    @GetMapping("/{id}/time-series")
    public ResponseEntity<GovernorateTimeSeriesDto> getGovernorateTimeSeriesById(@PathVariable Long id) {
        return ResponseEntity.ok(governorateService.getGovernorateTimeSeriesById(id));
    }

    /**
     * Get consolidated statistics for the entire system
     * @return Consolidated statistics DTO with overall system metrics
     */
    @GetMapping("/statistics/consolidated")
    public ResponseEntity<ConsolidatedStatisticsDto> getConsolidatedStatistics() {
        return ResponseEntity.ok(governorateService.getConsolidatedStatistics());
    }

    /**
     * Evaluate all governorates based on enterprise metrics
     * @return List of governorate evaluations
     */
    @GetMapping("/evaluations")
    public ResponseEntity<List<GovernorateEvaluationDto>> evaluateGovernorates() {
        return ResponseEntity.ok(governorateService.evaluateGovernorates());
    }

    /**
     * Evaluate a specific governorate
     * @param id The governorate ID
     * @return Governorate evaluation
     */
    @GetMapping("/{id}/evaluation")
    public ResponseEntity<GovernorateEvaluationDto> evaluateGovernorate(@PathVariable Long id) {
        return ResponseEntity.ok(governorateService.evaluateGovernorate(id));
    }
}
