package com.shop.sample.controller;

import com.shop.sample.model.UserEducation;
import com.shop.sample.repository.UserEducationRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UserEducationController {

    private final UserEducationRepository userEducationRepository;

    public UserEducationController(UserEducationRepository userEducationRepository) {
        this.userEducationRepository = userEducationRepository;
    }

    @PostMapping("/getEducationList")
    public Map<String, Object> getEducationList(HttpServletResponse response) {
        try {
            List<UserEducation> userEducationResult = userEducationRepository.findAll();
            return Map.of("success", 1, "data", userEducationResult);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "学歴一覧取得に失敗しました");
        }
    }
}
