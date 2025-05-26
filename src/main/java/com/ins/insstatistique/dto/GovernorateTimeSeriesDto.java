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
public class GovernorateTimeSeriesDto {
    private Long id;
    private String name;
    private Map<String, Integer> monthlyData;  // Month -> Enterprise count
    private double growthRate;                 // Overall growth rate for the period
}
