package com.gym.controller;

import com.gym.config.JwtUtil;
import com.gym.dto.LoginRequest;
import com.gym.model.User;
import com.gym.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ REGISTER API
    @PostMapping("/register")
    public User register(@RequestBody User user) {

        // 🔐 Encrypt password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // ✅ LOGIN API (JWT Token return karega)
    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found ❌"));

        // 🔐 Password match
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {

            // 🔥 Token generate
            return jwtUtil.generateToken(user.getEmail());

        } else {
            throw new RuntimeException("Invalid Password ❌");
        }
    }

    // ✅ TEST API
    @GetMapping("/test")
    public String test() {
        return "API Working 🚀";
    }
}