package com.ins.insstatistique.service;

import com.ins.insstatistique.dto.ContactMessageDTO;
import com.ins.insstatistique.entity.ContactMessage;
import com.ins.insstatistique.repository.ContactMessageRepository;
import com.ins.insstatistique.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactMessageService {
    private final ContactMessageRepository contactMessageRepository;
    private final EmailService emailService;
    
    @Value("${admin.email:admin@insstatistique.com}")
    private String adminEmail;

    public ContactMessageDTO createContactMessage(ContactMessageDTO dto) {
        ContactMessage message = new ContactMessage();
        BeanUtils.copyProperties(dto, message);
        message.setTimestamp(LocalDateTime.now());
        message = contactMessageRepository.save(message);
        
        // Notification email to admin
        try {
            String adminNotification = String.format("""
                <div style="font-family: Arial, sans-serif; padding: 20px;">
                    <h2 style="color: #003366;">Nouveau message de contact</h2>
                    <p><strong>De :</strong> %s (%s)</p>
                    <p><strong>Sujet :</strong> %s</p>
                    <div style="background-color: #f0f0f0; padding: 15px; margin: 20px 0;">
                        <p><strong>Message :</strong></p>
                        <p>%s</p>
                    </div>
                    <p>Vous pouvez répondre à ce message depuis le panneau d'administration.</p>
                </div>
                """,
                message.getName(),
                message.getEmail(),
                message.getSubject(),
                message.getMessage().replace("\n", "<br>")
            );
            
            emailService.sendHtmlEmail(
                adminEmail,
                "Nouveau message de contact - " + message.getSubject(),
                adminNotification
            );
        } catch (Exception e) {
            // Log the error but don't stop the process
            e.printStackTrace();
        }
        
        BeanUtils.copyProperties(message, dto);
        return dto;
    }

    public List<ContactMessageDTO> getAllMessages() {
        return contactMessageRepository.findAllByOrderByTimestampDesc()
            .stream()
            .map(message -> {
                ContactMessageDTO dto = new ContactMessageDTO();
                BeanUtils.copyProperties(message, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    public List<ContactMessageDTO> getUnrespondedMessages() {
        return contactMessageRepository.findByRespondedFalseOrderByTimestampAsc()
            .stream()
            .map(message -> {
                ContactMessageDTO dto = new ContactMessageDTO();
                BeanUtils.copyProperties(message, dto);
                return dto;
            })
            .collect(Collectors.toList());
    }

    @Transactional
    public ContactMessageDTO respondToMessage(Long messageId, String response) throws MessagingException {
        ContactMessage message = contactMessageRepository.findById(messageId)
            .orElseThrow(() -> new RuntimeException("Message not found"));

        message.setAdminResponse(response);
        message.setResponded(true);
        message.setResponseTimestamp(LocalDateTime.now());

        // Send email response with HTML formatting
        String emailBody = String.format("""
            <div style="font-family: Arial, sans-serif; padding: 20px;">
                <h2 style="color: #003366;">Institut National de la Statistique</h2>
                <p>Bonjour %s,</p>
                <p>Merci d'avoir contacté l'INS. Voici notre réponse à votre demande :</p>
                
                <div style="background-color: #f0f0f0; padding: 15px; margin: 20px 0;">
                    <p>%s</p>
                </div>
                
                <div style="margin-top: 20px; padding-top: 20px; border-top: 1px solid #ddd;">
                    <p><strong>Votre message original :</strong></p>
                    <p><strong>Sujet :</strong> %s</p>
                    <p><strong>Message :</strong></p>
                    <p>%s</p>
                </div>
                
                <p>Cordialement,<br>L'équipe INS</p>
            </div>
            """,
            message.getName(),
            response.replace("\n", "<br>"),
            message.getSubject(),
            message.getMessage().replace("\n", "<br>")
        );

        emailService.sendHtmlEmail(
            message.getEmail(),
            "Re: " + message.getSubject(),
            emailBody
        );

        message = contactMessageRepository.save(message);
        
        ContactMessageDTO dto = new ContactMessageDTO();
        BeanUtils.copyProperties(message, dto);
        return dto;
    }
} 