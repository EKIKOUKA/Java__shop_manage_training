package com.shop.sample.controller;

import com.shop.sample.model.User;
import com.shop.sample.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/getUserList")
    public Map<String, Object> getUserList(HttpServletResponse response) {
        try {
            List<User> userList = userRepository.findAll();
            return Map.of("success", 1, "data", userList);
        }  catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "ユーザー一覧取得に失敗しました");
        }
    }
}
