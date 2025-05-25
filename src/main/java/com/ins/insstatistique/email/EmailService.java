package com.ins.insstatistique.email;

import com.ins.insstatistique.entity.EmailToken;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Async
public class EmailService {

    private final JavaMailSender emailSender;
    private final EmailTemplateService emailTemplateService;

    public void sendVerificationCode(String to, String code) throws MessagingException {
        String htmlContent = emailTemplateService.getVerificationCodeTemplate(code);
        sendHtmlEmail(to, "Code de vérification - INS", htmlContent);
    }

    public void sendWelcomeEmail(String to, String userName) throws MessagingException {
        String htmlContent = emailTemplateService.getWelcomeTemplate(userName);
        sendHtmlEmail(to, "Bienvenue à l'INS", htmlContent);
    }

    public void sendSetPasswordEmail(String to, String userName, EmailToken emailToken) throws MessagingException {
        String htmlContent = emailTemplateService.getSetPasswordTemplate(userName, emailToken);
        sendHtmlEmail(to, "Set password - INS", htmlContent);
    }

    public void sendPasswordResetEmail(String to, String resetLink) throws MessagingException {
        String htmlContent = emailTemplateService.getPasswordResetTemplate(resetLink);
        sendHtmlEmail(to, "Réinitialisation de mot de passe - INS", htmlContent);
    }
    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("no-reply@insstatistique.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);

        emailSender.send(message);
    }
}