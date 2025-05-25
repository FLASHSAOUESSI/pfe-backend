// RegisterDTO.java
package com.ins.insstatistique.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterDTO {
    private String companyName;
    private String companyAddress;
    private String companyEmail;
    private String companyFax;
    private String investigatorName;
    private String investigatorPhone;
    private String investigatorEmail;
    private String password;
    private String verificationCode;
}