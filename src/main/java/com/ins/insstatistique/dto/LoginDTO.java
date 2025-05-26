// LoginDTO.java
package com.ins.insstatistique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginDTO {
    private String email;
    private String password;
}
