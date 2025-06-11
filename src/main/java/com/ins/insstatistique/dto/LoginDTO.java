// LoginDTO.java
package com.ins.insstatistique.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginDTO {
    @Email
    private String email;
    @NotBlank
    private String password;
}
