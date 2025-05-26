package com.ins.insstatistique.service;

import org.springframework.stereotype.Service;

import com.ins.insstatistique.email.EmailService;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.entity.EntrepriseStatus;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.repository.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final EmailService emailService;
    private final UserRepository userRepository;

    public void notifyEnterpriseStatusChange(Entreprise entreprise, EntrepriseStatus previousStatus) {
        // Find all users associated with this enterprise
        for (User responsable : entreprise.getResponsables()) {
            try {
                switch (entreprise.getStatus()) {
                    case VALIDATED:
                        sendEnterpriseApprovedNotification(responsable, entreprise);
                        break;
                    case REJECTED:
                        sendEnterpriseRejectedNotification(responsable, entreprise);
                        break;
                    case PENDING:
                        // Typically won't notify when setting back to pending, but included for completeness
                        break;
                }
            } catch (Exception e) {
                log.error("Failed to send notification to user {}: {}", responsable.getEmail(), e.getMessage());
            }
        }
    }

    private void sendEnterpriseApprovedNotification(User user, Entreprise entreprise) throws MessagingException {
        String subject = "Votre entreprise a été validée";
        String content = String.format(
            "Bonjour %s,<br><br>" +
            "Nous sommes heureux de vous informer que votre entreprise <strong>%s</strong> a été validée.<br>" +
            "Vous pouvez maintenant accéder à tous les services de la plateforme.<br><br>" +
            "Cordialement,<br>L'équipe INS Statistique", 
            user.getNom(), 
            entreprise.getName()
        );
        
        emailService.sendHtmlEmail(user.getEmail(), subject, content);
    }

    private void sendEnterpriseRejectedNotification(User user, Entreprise entreprise) throws MessagingException {
        String subject = "Statut de votre entreprise";
        String content = String.format(
            "Bonjour %s,<br><br>" +
            "Nous regrettons de vous informer que votre entreprise <strong>%s</strong> n'a pas été validée.<br>" +
            "Veuillez nous contacter pour plus d'informations.<br><br>" +
            "Cordialement,<br>L'équipe INS Statistique", 
            user.getNom(), 
            entreprise.getName()
        );
        
        emailService.sendHtmlEmail(user.getEmail(), subject, content);
    }
}
