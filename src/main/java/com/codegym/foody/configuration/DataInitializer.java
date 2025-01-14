package com.codegym.foody.configuration;

import com.codegym.foody.model.enumable.Role;
import com.codegym.foody.model.User;
import com.codegym.foody.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminUsername = "admin";
        String adminPassword = "admin";
        String adminEmail = "admin@example.com";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode(adminPassword));
            admin.setEmail(adminEmail);
            admin.setFullname("Administrator");
            admin.setPhone("+84999999999");
            admin.setAddress("Foody Store");
            admin.setRole(Role.ADMIN);
            admin.setStatus(true);

            userRepository.save(admin);
            System.out.println("Admin account created successfully!");
        }
    }
}
