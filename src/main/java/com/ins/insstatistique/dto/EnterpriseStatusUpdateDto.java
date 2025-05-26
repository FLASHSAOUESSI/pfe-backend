package com.ins.insstatistique.dto;

import com.ins.insstatistique.entity.EntrepriseStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterpriseStatusUpdateDto {
    private Long enterpriseId;
    private String enterpriseName;
    private EntrepriseStatus oldStatus;
    private EntrepriseStatus newStatus;
    private boolean notificationSent;
}
