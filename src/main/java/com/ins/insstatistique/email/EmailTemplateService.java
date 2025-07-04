package com.ins.insstatistique.email;

import com.ins.insstatistique.entity.EmailToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public String getVerificationCodeTemplate(String code) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process("emails/verification-code", context);
    }

    public String getInitialEnqueteReminderTemplate(String companyName) {
        Context context = new Context();
        context.setVariable("companyName", companyName);
        return templateEngine.process("emails/enquete-initial-reminder", context);
    }

    public String getPartialEnqueteReminderTemplate(String companyName) {
        Context context = new Context();
        context.setVariable("companyName", companyName);
        return templateEngine.process("emails/enquete-partial-reminder", context);
    }

    // Ajoutez d'autres méthodes pour différents templates d'emails
    public String getWelcomeTemplate(String userName) {
        Context context = new Context();
        context.setVariable("userName", userName);
        return templateEngine.process("emails/welcome", context);
    }

    public String getPasswordResetTemplate(String resetLink) {
        Context context = new Context();
        context.setVariable("resetLink", resetLink);
        return templateEngine.process("emails/password-reset", context);
    }

    public String getSetPasswordTemplate(String userName, EmailToken emailToken) {
        Context context = new Context();
        context.setVariable("userName", userName);
        context.setVariable("token", emailToken.getToken());
        return templateEngine.process("emails/set-password", context);
    }

    public String getNewEnqueteTemplate(String appUrl) {
        Context context = new Context();
        context.setVariable("appUrl", appUrl);
        return templateEngine.process("emails/new-enquete", context);
    }
}