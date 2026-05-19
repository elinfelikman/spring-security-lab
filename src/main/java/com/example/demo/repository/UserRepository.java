package com.example.demo.repository;

import com.example.demo.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // פונקציה שתעזור לנו למצוא משתמש לפי השם שלו בזמן ההתחברות
    UserEntity findByUsername(String username);
}