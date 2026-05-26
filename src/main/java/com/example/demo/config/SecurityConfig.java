package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod; // הוספת ייבוא עבור סוגי בקשות ה-HTTP
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // הכנה לשלב מניעת ה-IDOR
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // הפעלת תשתית האבטחה של Spring Security
@EnableMethodSecurity // מאפשר הגנה מתקדמת ברמת ה-Service (קריטי למניעת IDOR בשלב הבא)
public class SecurityConfig {

    // 1. הגדרת אלגוריתם ההצפנה - BCrypt (נשאר ללא שינוי)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. הגדרת חוקי הגישה לאפליקציה (מעודכן עבור דרישות ההרשאה של TutorConnect)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // מכבים לצרכי הבדיקה במעבדה
                .authorizeHttpRequests(auth -> auth
                        // נתיבים ציבוריים קיימים
                        .requestMatchers("/api/register", "/h2-console/**").permitAll()

                        // --- דרישה 1: ממשק אדמין (Role-Based Access Control) ---
                        // נתיבי הניהול חסומים לכולם ונגישים אך ורק למשתמש עם תפקיד ADMIN
                        .requestMatchers("/api/admin/**").hasRole("ADMIN")

                        // --- דרישה 4: הפרדת הרשאות במחלקה מסוימת (פרופיל מורים) ---
                        // גישת צפייה: בקשות GET לקריאת פרופילים פתוחות לכולם (כולל אורחים / Guest)
                        .requestMatchers(HttpMethod.GET, "/api/tutors/**").permitAll()

                        // גישת עריכה: בקשות POST/PUT ליצירה ועדכון פרופיל מותרות אך ורק ל-TUTOR
                        .requestMatchers(HttpMethod.POST, "/api/tutors/**").hasRole("TUTOR")
                        .requestMatchers(HttpMethod.PUT, "/api/tutors/**").hasRole("TUTOR")

                        // כל שאר הדפים והפעולות (כמו שליחת בקשת שיעור) דורשים התחברות בסיסית
                        .anyRequest().authenticated()
                )
                .headers(headers -> headers.frameOptions(frame -> frame.disable())) // דרוש בשביל ה-H2 Console
                .formLogin(form -> form
                        .defaultSuccessUrl("/signup.html", true) // ניתוח אוטומטי לדף ההרשמה לאחר התחברות מוצלחת!
                        .permitAll()
                ); // שימוש בטופס התחברות הדיפולטיבי של Spring

        return http.build();
    }
}