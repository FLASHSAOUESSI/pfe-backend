package com.ins.insstatistique.dto;

import lombok.Data;

@Data
public class UserRegisterRequest {
    private String email;
    private String password;
    private String nom;
    private String prenom;
}
