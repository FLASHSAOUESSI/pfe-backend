package com.ins.insstatistique.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ins.insstatistique.dto.LoginDTO;
import com.ins.insstatistique.dto.RegisterDTO;
import com.ins.insstatistique.dto.VerifyDto;
import com.ins.insstatistique.dto.JwtResponse;
import com.ins.insstatistique.exception.EmailAlreadyExistsException;
import com.ins.insstatistique.exception.InvalidVerificationCodeException;
import com.ins.insstatistique.service.AuthService;
import com.ins.insstatistique.security.JwtTokenProvider;
import com.ins.insstatistique.security.UserDetailsServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = AuthController.class,
        excludeAutoConfiguration = {
                SecurityAutoConfiguration.class,
                UserDetailsServiceAutoConfiguration.class
        })
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithAnonymousUser
    void testLogin() throws Exception {
        LoginDTO loginDTO = new LoginDTO("test@example.com", "password");
        JwtResponse jwtResponse = JwtResponse.builder()
                .token("test-token")
                .email("test@example.com")
                .roles(Collections.singletonList("USER"))
                .build();

        when(authService.login(any(LoginDTO.class))).thenReturn(jwtResponse);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    @WithAnonymousUser
    void testRegister() throws Exception, InvalidVerificationCodeException, MessagingException {
        RegisterDTO registerDTO = RegisterDTO.builder()
                .investigatorName("Test User")
                .investigatorEmail("test@example.com")
                .password("password")
                .verificationCode("123456")
                .companyName("Test Company")
                .companyAddress("123 Test St")
                .companyEmail("company@example.com")
                .companyFax("123-456-7891")
                .governorateId(1L)
                .investigatorPhone("123-456-7890")
                .build();

        when(authService.register(any(RegisterDTO.class))).thenReturn(true);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDTO))
                .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    @WithAnonymousUser
    void testSendVerificationCode() throws Exception, EmailAlreadyExistsException, MessagingException {
        VerifyDto verifyDto = new VerifyDto("test@example.com", "Test Company", "company@example.com");

        doNothing().when(authService).sendVerificationCode(any(VerifyDto.class));

        mockMvc.perform(post("/api/auth/verify")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(verifyDto))
                .with(csrf()))
                .andExpect(status().isOk());
    }
}

