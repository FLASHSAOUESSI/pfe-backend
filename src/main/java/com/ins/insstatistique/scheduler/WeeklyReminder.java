package com.ins.insstatistique.scheduler;

import com.ins.insstatistique.email.EmailService;
import com.ins.insstatistique.entity.Enquete;
import com.ins.insstatistique.entity.Entreprise;
import com.ins.insstatistique.repository.EnqueteRepository;
import com.ins.insstatistique.repository.EntrepriseRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeeklyReminder {

    // Inject both repositories
    private final EnqueteRepository enqueteRepository;
    private final EntrepriseRepository entrepriseRepository;
    private final EmailService emailService;

//    @Scheduled(cron = "0 0 9 * * MON") // Every Monday at 9 AM
    @Scheduled(cron = "0 * * * * *")
    public void sendWeeklyReminders() {
        log.info("Starting weekly reminder task.");

        processPartiallyCompleted();

        processNotStarted();

        log.info("Finished weekly reminder task.");
    }

    /**
     * Handles reminders for users who have started but not completed the survey.
     * This logic remains unchanged.
     */
    private void processPartiallyCompleted() {
        List<Enquete> partialEnquetes = enqueteRepository.findPartiallyCompletedEnquetes();
        log.info("[Partial] Found {} partially completed enquêtes. Sending 'finish survey' reminders.", partialEnquetes.size());

        for (Enquete enquete : partialEnquetes) {
            try {
                if (enquete.getMailRepondant() != null && !enquete.getMailRepondant().isEmpty()) {
                    emailService.sendPartialEnqueteReminderEmail(enquete.getMailRepondant(), enquete.getNomSociale());
                    log.info("[Partial] Reminder sent to {} for company '{}'.", enquete.getMailRepondant(), enquete.getNomSociale());
                } else {
                    log.warn("[Partial] Could not send reminder for enquete ID {}: missing email.", enquete.getId());
                }
            } catch (MessagingException e) {
                log.error("[Partial] Failed to send reminder for enquete ID {} to {}. Error: {}",
                        enquete.getId(), enquete.getMailRepondant(), e.getMessage());
            }
        }
    }

    /**
     * Handles reminders for companies that have not started the survey at all.
     * This logic is new and queries the Entreprise table.
     */
    private void processNotStarted() {
        List<Entreprise> enterprisesWithoutEnquete = entrepriseRepository.findEntreprisesWithNoEnquete();
        log.info("[Not Started] Found {} entreprises with no enquete. Sending 'start survey' reminders.", enterprisesWithoutEnquete.size());

        for (Entreprise entreprise : enterprisesWithoutEnquete) {
            try {
                // We use the primary email and name from the Entreprise entity
                if (entreprise.getEmail() != null && !entreprise.getEmail().isEmpty()) {
                    emailService.sendInitialEnqueteReminderEmail(entreprise.getEmail(), entreprise.getName());
                    log.info("[Not Started] Initial reminder sent to {} for company '{}'.", entreprise.getEmail(), entreprise.getName());
                } else {
                    log.warn("[Not Started] Could not send reminder for entreprise ID {}: missing primary email.", entreprise.getId());
                }
            } catch (MessagingException e) {
                log.error("[Not Started] Failed to send initial reminder for entreprise ID {} to {}. Error: {}",
                        entreprise.getId(), entreprise.getEmail(), e.getMessage());
            }
        }
    }
}