package com.example.demo.controller;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // הזרקת ה-Repository וה-PasswordEncoder שיוצר את ה-Hash
    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ה-Endpoint של ה-Sign Up (הרשמה)
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserEntity user) {

        // 1. בדיקה למניעת כפילויות: האם שם המשתמש כבר תפוס?
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Error: Username already exists!");
        }

        // 2. מניעת ה-Pit-fall: הצפנת הסיסמה של המשתמש החדש בעזרת BCrypt
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword); // מעדכנים את הישות בסיסמה המוצפנת
        user.setRole("ROLE_USER");        // מגדירים לו תפקיד ברירת מחדל של משתמש רגיל

        // 3. שמירת המשתמש החדש בדאטאבייס
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully with hashed password!");
    }
}