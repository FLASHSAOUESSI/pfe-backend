package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GovernorateRankingDto {
    private Long id;
    private String name;
    private int ranking;
    private int value;  // Value for the metric being ranked (enterprises, validation rate, etc.)
}
