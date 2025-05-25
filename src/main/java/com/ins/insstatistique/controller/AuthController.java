
package com.ins.insstatistique.controller;

import com.ins.insstatistique.dto.*;
import com.ins.insstatistique.exception.EmailAlreadyExistsException;
import com.ins.insstatistique.exception.InvalidVerificationCodeException;
import com.ins.insstatistique.service.AuthService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/verify")
    public ResponseEntity<?> sendVerificationCode(@RequestBody VerifyDto registerDTO) throws EmailAlreadyExistsException, MessagingException {
        System.out.println("verify ");
        authService.sendVerificationCode(registerDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Boolean> register(@RequestBody RegisterDTO registerDTO) throws InvalidVerificationCodeException, MessagingException {
        return ResponseEntity.ok(authService.register(registerDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginDTO loginDTO) {
        System.out.println(loginDTO);
        return ResponseEntity.ok(authService.login(loginDTO));
    }

    @PostMapping("/set-password")
    public ResponseEntity<Boolean> login(@RequestBody SetPasswordDto setPasswordDto) {
        return ResponseEntity.ok(authService.setPassword(setPasswordDto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Boolean> forgetPassword(@RequestBody ForgetPasswordDto forgetPasswordDto) throws MessagingException, BadRequestException {
        return ResponseEntity.ok(authService.forgetPassword(forgetPasswordDto.email()));
    }

    @PostMapping("/change-password")
    public ResponseEntity<Boolean> changePassword(@RequestBody ChangePasswordDto changePasswordDto){
            return  ResponseEntity.ok(authService.changePassword(changePasswordDto));
    }

    @PostMapping("/verify-code")
    public ResponseEntity<Boolean> verifyCode(@RequestBody VerifyCodeDto verifyDto){
        return ResponseEntity.ok(authService.verifyCode(verifyDto));
    }

}
