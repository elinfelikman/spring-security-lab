package com.example.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/requests")
public class LessonRequestController {

    // כאן בעתיד תזריקי את ה-LessonRequestService שלך שיפנה למסד הנתונים
    // public LessonRequestController(LessonRequestService service) { ... }

    @GetMapping("/my-requests")
    public ResponseEntity<List<String>> getMyRequests(Authentication authentication) {

        // 1. חילוץ השם של המשתמש שכרגע מחובר לשרת מתוך ה-Token/Session
        // אי אפשר לזייף את זה דרך ה-URL!
        String currentUsername = authentication.getName();

        // 2. כאן נשלוף את הנתונים מהדאטאבייס (כרגע נשים רשימה דמה רק בשביל הבדיקה במעבדה)
        List<String> myRequests = new ArrayList<>();
        myRequests.add("Lesson request 1 for " + currentUsername);
        myRequests.add("Lesson request 2 for " + currentUsername);

        // מחזירים למשתמש רק את הבקשות ששייכות לו
        return ResponseEntity.ok(myRequests);
    }
}