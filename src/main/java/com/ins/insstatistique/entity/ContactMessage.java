package com.ins.insstatistique.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    private String subject;
    
    @Column(length = 1000)
    private String message;
    
    private LocalDateTime timestamp = LocalDateTime.now();
    
    private boolean responded = false;
    
    @Column(length = 1000)
    private String adminResponse;
    
    private LocalDateTime responseTimestamp;
} 