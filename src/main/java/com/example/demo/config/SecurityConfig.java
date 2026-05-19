package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    // 1. הגדרת אלגוריתם ההצפנה - BCrypt
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. הגדרת חוקי הגישה לאפליקציה (מה חסום ומה פתוח)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // מכבים לצרכי הבדיקה במעבדה
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/register", "/h2-console/**").permitAll() // דף הרשמה והדאטאבייס פתוחים לכולם
                        .anyRequest().authenticated() // כל שאר הדפים דורשים התחברות
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // דרוש בשביל ה-H2 Console
                .formLogin(form -> form
                        .defaultSuccessUrl("/signup.html", true) // ניתוח אוטומטי לדף ההרשמה לאחר התחברות מוצלחת!
                        .permitAll()
                ); // שימוש בטופס התחברות הדיפולטיבי של Spring

        return http.build();
    }
}