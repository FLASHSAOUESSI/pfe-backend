package com.ins.insstatistique.dto;

import lombok.Data;

@Data
public class AdminRegisterRequest {
    private String email;
    private String password;
    private String nom;
    private String prenom;
} 