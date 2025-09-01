package com.blog.management.banking.service;
import com.blog.management.banking.entity.User;
import com.blog.management.banking.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepo;
    @Autowired private
    PasswordEncoder encoder;

    public User register(String username, String password) {
        if (userRepo.existsByUsername(username)) {
            throw new RuntimeException("User already exists");
        }
        // Proceed with registration
        User user = new User(username, password);
        userRepo.save(user);
        return user;
    }


    public boolean login(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username and password must not be null");
        }
        Optional<User> userOpt = userRepo.findByUsername(username);
        return userOpt.map(user -> user.getPassword().equals(password)).orElse(false);
    }


}
