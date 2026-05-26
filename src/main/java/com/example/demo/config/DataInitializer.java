package com.example.demo.config;

import com.example.demo.model.Role;
import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.UUID;

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

        // 1. יצירת האדמין (רק אם הוא עדיין לא קיים במסד הנתונים)
        if (userRepository.findByUsername("admin") == null) {
            String temporaryAdminPassword = UUID.randomUUID().toString();
            System.out.println("\n====================================================");
            System.out.println("  GENERATED ADMIN PASSWORD: " + temporaryAdminPassword);
            System.out.println("====================================================\n");

            UserEntity admin = new UserEntity();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode(temporaryAdminPassword));
            admin.setRole(Role.ROLE_ADMIN);
            userRepository.save(admin);
        }

        // 2. יצירת הסטודנט (רק אם הוא לא קיים)
        if (userRepository.findByUsername("student1") == null) {
            UserEntity student = new UserEntity();
            student.setUsername("student1");
            student.setPassword(passwordEncoder.encode("123"));
            student.setRole(Role.ROLE_STUDENT);
            userRepository.save(student);
        }

        // 3. יצירת המורה (רק אם הוא לא קיים)
        if (userRepository.findByUsername("tutor1") == null) {
            UserEntity tutor = new UserEntity();
            tutor.setUsername("tutor1");
            tutor.setPassword(passwordEncoder.encode("123"));
            tutor.setRole(Role.ROLE_TUTOR);
            userRepository.save(tutor);
        }
    }
}