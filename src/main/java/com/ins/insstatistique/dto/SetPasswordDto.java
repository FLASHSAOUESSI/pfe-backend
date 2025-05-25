package com.ins.insstatistique.dto;

public record SetPasswordDto(
        String token,
        String password,
        String confirmPassword

) {
}
