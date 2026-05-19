package com.example.demo.config;


import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;
import com.example.demo.model.UserEntity;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // בדיקה: האם בסיס הנתונים ריק לחלוטין?
        if (userRepository.count() == 0) {

            // יצירת סיסמה אקראית מאובטחת לחלוטין (בלי הארד-קוד!)
            String temporaryAdminPassword = UUID.randomUUID().toString();

            System.out.println("\n====================================================");
            System.out.println("  FIRST RUN: NO USERS FOUND IN DATABASE.");
            System.out.println("  GENERATED ADMIN PASSWORD: " + temporaryAdminPassword);
            System.out.println("  Please use this password to log in for the first time.");
            System.out.println("====================================================\n");

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            // הצפנת הסיסמה האקראית ושמירתה
            admin.setPassword(passwordEncoder.encode(temporaryAdminPassword));
            admin.setRole("ROLE_ADMIN");

            userRepository.save(admin);
        }
    }
}