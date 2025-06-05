package com.ins.insstatistique.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ContactMessageDTO {
    private Long id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private LocalDateTime timestamp;
    private boolean responded;
    private String adminResponse;
    private LocalDateTime responseTimestamp;
} 