package com.shop.sample.controller;

import com.shop.sample.repository.UserRepository;
import com.shop.sample.security.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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

        Map<String, Object>[] resultWrapper = new Map[1];
        userRepository.findByUserEmail(username).ifPresentOrElse(user -> {
            if (passwordEncoder.matches(password, user.getPassword())) {
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("userId", user.getUserId());
                userInfo.put("user_email", user.getUserEmail());
                userInfo.put("username", user.getUsername());
                userInfo.put("avatar",  user.getAvatar());
                userInfo.put("totp_secret", user.getTotpSecret() != null && !user.getTotpSecret().isEmpty());

                if (user.getTotpSecret() == null || user.getTotpSecret().isEmpty()) {
                    userInfo.put("token", jwtUtil.generateToken(user.getUserEmail(), user.getUserId()));
                }

                Map<String, Object> result = new HashMap<>();
                result.put("success", 1);
                result.put("userInfo", userInfo);
                result.put("message", "ログイン成功");

                resultWrapper[0] = result;
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resultWrapper[0] = Map.of(
                    "success", 0,
                    "message", "ユーザーネームやパスワードが間違っています"
                );
            }
        }, () -> {
            // ユーザーが存在しない
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resultWrapper[0] = Map.of(
                "success", 0,
                "message", "ユーザーネームやパスワードが間違っています"
            );
        });

        return resultWrapper[0];
    }
}
