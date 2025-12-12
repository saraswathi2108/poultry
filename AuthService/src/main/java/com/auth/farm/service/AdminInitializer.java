package com.auth.farm.service;

import com.auth.farm.entity.User;
import com.auth.farm.enums.Role;
import com.auth.farm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {

        boolean adminExists = userRepo.findAll().stream()
                .anyMatch(user -> user.getRoles().contains(Role.ADMIN));

        if (!adminExists) {
            User admin = new User();

            admin.setUsername("admin");
            admin.setEmail("admin@gmail.com");
            admin.setPassword(encoder.encode("admin123"));
            admin.setPhone("0000000000");
            admin.setIsActive(true);
            admin.getRoles().add(Role.ADMIN);

            userRepo.save(admin);

            System.out.println("🚀 DEFAULT ADMIN CREATED");
            System.out.println("   Username: admin");
            System.out.println("   Password: admin123");
        }
    }
}
