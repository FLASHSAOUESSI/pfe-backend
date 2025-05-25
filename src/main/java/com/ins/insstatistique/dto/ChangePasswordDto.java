package com.ins.insstatistique.dto;

public record ChangePasswordDto(
        String code,
        String password,
        String confirmPassword
) {
}
