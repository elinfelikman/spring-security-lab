package com.example.demo.service;

import com.example.demo.model.UserEntity;
import com.example.demo.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. מחפשים את המשתמש בבסיס הנתונים שלנו לפי שם המשתמש
        UserEntity userEntity = userRepository.findByUsername(username);

        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // 2. מחזירים אובייקט ש-Spring Security יודע לקרוא, כולל הסיסמה המוצפנת מה-DB
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword()) // זו הסיסמה המוצפנת של ה-BCrypt
                .roles(userEntity.getRole().replace("ROLE_", "")) // מוריד את הקידומת לצרכי קונפיגורציה
                .build();
    }
}