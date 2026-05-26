package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users") // שם הטבלה ב-DB
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // כאן תישמר אך ורק סיסמה מוצפנת!

    // השינוי שביצענו: שימוש ב-Role Enum במקום String
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    // גטרים וסטרים (Getters and Setters)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // עדכון הגטר והסטר של ה-Role
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}