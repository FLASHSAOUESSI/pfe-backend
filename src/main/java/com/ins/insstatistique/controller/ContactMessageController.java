package com.ins.insstatistique.controller;

import com.ins.insstatistique.dto.ContactMessageDTO;
import com.ins.insstatistique.service.ContactMessageService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ContactMessageController {
    private final ContactMessageService contactMessageService;

    @PostMapping
    public ResponseEntity<ContactMessageDTO> createMessage(@RequestBody ContactMessageDTO messageDTO) {
        return ResponseEntity.ok(contactMessageService.createContactMessage(messageDTO));
    }

    @GetMapping
    public ResponseEntity<List<ContactMessageDTO>> getAllMessages() {
        return ResponseEntity.ok(contactMessageService.getAllMessages());
    }

    @GetMapping("/unresponded")
    public ResponseEntity<List<ContactMessageDTO>> getUnrespondedMessages() {
        return ResponseEntity.ok(contactMessageService.getUnrespondedMessages());
    }

    @PostMapping("/{messageId}/respond")
    public ResponseEntity<ContactMessageDTO> respondToMessage(
            @PathVariable Long messageId,
            @RequestBody String response) {
        try {
            return ResponseEntity.ok(contactMessageService.respondToMessage(messageId, response));
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 