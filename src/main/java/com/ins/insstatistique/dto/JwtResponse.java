// JwtResponse.java
package com.ins.insstatistique.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String email;
    private List<String> roles;
    
    public JwtResponse(String token) {
        this.token = token;
    }
}