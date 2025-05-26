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
public class EnterpriseDto {
    private Long id;
    private String name;
    private String address;
    private String email;
    private String fax;
    private EntrepriseStatus status;
    private Long governorateId;
    private String governorateName;
    private int responsablesCount;
}
