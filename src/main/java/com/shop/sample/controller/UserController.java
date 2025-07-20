package com.shop.sample.controller;

import com.shop.sample.dto.UserSearchRequest;
import com.shop.sample.model.User;
import com.shop.sample.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/getUserList")
    public Map<String, Object> getUserList(@RequestBody UserSearchRequest request, HttpServletResponse response) {
        try {
            int offset = (request.getCurrent() - 1) * request.getPageSize();

            List<Map<String, Object>> userList = userRepository.findUsersWithFilters(
                request.getUsername(),
                request.getGender(),
                request.getUserEdu(),
                request.getUserEmail(),
                request.getIsActive(),
                request.getPageSize(),
                offset
            );

            int total = userRepository.countUsersWithFilters(
                request.getUsername(),
                request.getGender(),
                request.getUserEdu(),
                request.getUserEmail(),
                request.getIsActive()
            );

            return Map.of(
                "success", 1,
                "data", userList,
                "current", request.getCurrent(),
                "pageSize", request.getPageSize(),
                "total", total
            );
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "ユーザー一覧取得に失敗しました");
        }
    }

    @PostMapping("/addUser")
    public Map<String, Object> addUser(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            User user = new User();
            user.setUsername((String) request.get("username"));
            user.setAvatar((String) request.get("avatar"));
            user.setGender((String) request.get("gender"));
            user.setUserEdu((Integer) request.get("user_edu"));
            user.setUserEmail((String) request.get("user_email"));
            user.setIsActive((Integer) request.get("is_active"));
            userRepository.save(user);

            return Map.of("success", 1, "message", "ユーザーが追加しました");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "ユーザー追加に失敗しました");
        }
    }

    @PostMapping("/updateUserActive")
    public Map<String, Object> updateUserActive(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Long userId = Long.parseLong(request.get("user_id").toString());
            Optional<User> optionalUser = userRepository.findByUserId(userId);
            if (optionalUser.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Map.of("success", 0, "message", "ユーザーが見つかりません");
            }

            User user = optionalUser.get();
            user.setIsActive((Integer) request.get("is_active"));
            userRepository.save(user);

            return Map.of("success", 1, "message", "更新しました");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "ユーザーの状態更新に失敗しました");
        }
    }

    @PostMapping("/updateUser")
    public Map<String, Object> updateUser(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Long userId = Long.parseLong(request.get("user_id").toString());
            Optional<User> optionalUser = userRepository.findByUserId(userId);
            if (optionalUser.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Map.of("success", 0, "message", "ユーザーが見つかりません");
            }

            User user = optionalUser.get();
            user.setUsername((String) request.get("username"));
            user.setAvatar((String) request.get("avatar"));
            user.setGender((String) request.get("gender"));
            user.setUserEdu((Integer) request.get("user_edu"));
            user.setUserEmail((String) request.get("user_email"));
            user.setIsActive((Integer) request.get("is_active"));
            userRepository.save(user);

            return Map.of("success", 1, "message", "更新しました");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "ユーザー更新に失敗しました");
        }
    }

    @PostMapping("/deleteUser")
    public Map<String, Object> deleteUser(@RequestBody Map<String, Object> request, HttpServletResponse response) {
        try {
            Long userId = Long.parseLong(request.get("user_id").toString());
            if (!userRepository.existsById(userId)) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return Map.of("success", 0, "message", "ユーザーが見つかりません");
            }

            userRepository.deleteById(userId);
            return Map.of("success", 1, "message", "削除しました");
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return Map.of("success", 0, "message", "削除失敗しました");
        }
    }
}
