package com.ins.insstatistique.init;

import com.ins.insstatistique.entity.Role;
import com.ins.insstatistique.entity.User;
import com.ins.insstatistique.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAdmin {

    private final UserRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public ApplicationRunner initialAdminCreator() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                String defaultEmail = "admin@ins.com";

                if (!adminRepository.existsByEmail(defaultEmail)) {
                    User admin = new User();
                    admin.setEmail(defaultEmail);
                    admin.setPassword(passwordEncoder.encode("admin123"));
                    admin.setNom("Admin");
                    admin.setEnabled(true);
                    admin.setRole(Role.ADMIN);

                    adminRepository.save(admin);
                    System.out.println("Default admin account created");
                }
            }
        };
    }
}