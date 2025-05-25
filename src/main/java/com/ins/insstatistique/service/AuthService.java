
package com.ins.insstatistique.service;

import com.ins.insstatistique.dto.*;
import com.ins.insstatistique.email.EmailService;
import com.ins.insstatistique.entity.EmailToken;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.Investigateur;
import com.ins.insstatistique.exception.EmailAlreadyExistsException;
import com.ins.insstatistique.exception.InvalidVerificationCodeException;
import com.ins.insstatistique.repository.EntrepriseRepository;
import com.ins.insstatistique.repository.InvestigateurRepository;
import com.ins.insstatistique.repository.TokenRepository;
import com.ins.insstatistique.security.JwtTokenProvider;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final EntrepriseRepository entrepriseRepository;
    private final InvestigateurRepository investigateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    private final TokenRepository tokenRepository;

    // todo: save generated codes in database
    private final Map<String, String> verificationCodes = new HashMap<>();

    public void sendVerificationCode(VerifyDto request) throws EmailAlreadyExistsException, MessagingException {
        if (entrepriseRepository.existsByEmail(request.companyEmail())) {
            throw new EmailAlreadyExistsException("Cette adresse email est déjà utilisée");
        }

        String code = generateVerificationCode();
        verificationCodes.put(request.companyEmail(), code);
        this.emailService.sendVerificationCode(request.investigatorEmail(),code);
    }

    public boolean register(RegisterDTO registerDTO) throws InvalidVerificationCodeException, MessagingException {
        validateVerificationCode(registerDTO.getCompanyEmail(), registerDTO.getVerificationCode());

        Entreprise entreprise = Entreprise.builder()
                .name(registerDTO.getCompanyName())
                .address(registerDTO.getCompanyAddress())
                .email(registerDTO.getCompanyEmail())
                .fax(registerDTO.getCompanyFax())
                .build();

        Investigateur investigateur = Investigateur.builder()
                .name(registerDTO.getInvestigatorName())
                .email(registerDTO.getInvestigatorEmail())
                .phone(registerDTO.getInvestigatorPhone())
                //.password(passwordEncoder.encode(registerDTO.getPassword()))
                .entreprise(entreprise)
                .build();

        entrepriseRepository.save(entreprise);
       Investigateur createdUser =  investigateurRepository.save(investigateur);
        EmailToken emailToken = EmailToken.builder()
                .token(String.valueOf(UUID.randomUUID()))
                .userId(createdUser.getId())
                .build();
       EmailToken createdToken = this.tokenRepository.save(emailToken);

        this.emailService.sendSetPasswordEmail(createdUser.getEmail(), createdUser.getUsername(), createdToken);

        //String token = tokenProvider.generateToken(investigateur.getEmail());
        return true;
    }

    public JwtResponse login(LoginDTO loginDTO) {
        System.out.println("login");
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getPassword()
                )
        );

        String token = tokenProvider.generateToken(loginDTO.getEmail());
        System.out.println(token);
        return new JwtResponse(token);
    }

    private String generateVerificationCode() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }

    private void validateVerificationCode(String email, String code) throws InvalidVerificationCodeException {
        String storedCode = verificationCodes.get(email);
        if (storedCode == null || !storedCode.equals(code)) {
            throw new InvalidVerificationCodeException("Code de vérification invalide");
        }
        verificationCodes.remove(email);
    }

    public Boolean setPassword(SetPasswordDto request) {

        EmailToken token= this.tokenRepository.findByToken(request.token());
        if(token != null) {
            Investigateur user = investigateurRepository.findById(token.getUserId()).orElse(null);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(request.password()));
                 investigateurRepository.save(user);
            }
            return false;
        }
        return true;
    }

    public Boolean forgetPassword(String email) throws MessagingException, BadRequestException {
        Investigateur investigateur = investigateurRepository.findByEmail(email).orElseThrow(
                ()->   new BadRequestException("email not found")
        );
        String code = generateVerificationCode();
        EmailToken emailToken = EmailToken.builder()
                .token(code)
                .userId(investigateur.getId())
                .build();
        this.tokenRepository.save(emailToken);

        this.emailService.sendVerificationCode(email,code);
        return true;
    }

    public Boolean changePassword(ChangePasswordDto request) {
        if(!request.password().equals(request.confirmPassword())){
            throw new RuntimeException("Passwords doesnt match");
        }
        EmailToken token= this.tokenRepository.findByToken(request.code());
        if(token != null) {
            Investigateur user = investigateurRepository.findById(token.getUserId()).orElse(null);
            if (user != null) {
                user.setPassword(passwordEncoder.encode(request.password()));
                investigateurRepository.save(user);
                return true;
            }
            return false;
        }
        return true;
    }

    public Boolean verifyCode(VerifyCodeDto verifyDto) {
       EmailToken token = tokenRepository.findByToken(verifyDto.code());
        return token != null;
    }
}