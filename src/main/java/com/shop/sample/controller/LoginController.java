package com.shop.sample.controller;

import com.shop.sample.repository.UserRepository;
import com.shop.sample.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class LoginController {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginController(JwtUtil jwtUtil, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest, HttpServletResponse response) {
        String username = loginRequest.get("user_email");
        String password = loginRequest.get("password");

        return userRepository.findByUserEmail(username)
            .filter(user -> passwordEncoder.matches(password, user.getPassword()))
            .map(user -> {
                Map<String, Object> result = new java.util.HashMap<>();
                result.put("token", jwtUtil.generateToken(user.getUserEmail(), user.getUserId()));
                return result;
            })
            .orElseGet(() -> {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                Map<String, Object> errorMap = new java.util.HashMap<>();
                errorMap.put("error", "ユーザーネームやパスワードが間違いました");
                return errorMap;
            });
    }
}
