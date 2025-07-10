package com.shop.sample.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.shop.sample.repository.UserRepository;
import com.shop.sample.security.JwtUtil;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
public class SecretTOTPController {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public SecretTOTPController(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    private final Map<Long, String> tempSecrets = new ConcurrentHashMap<>();
    @PostMapping("/generate-secret")
    public Map<String, Object> generateSecret(@RequestBody Map<String, String> body) {
        Long userId = Long.valueOf(body.getOrDefault("userId", "0"));

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        GoogleAuthenticatorKey key = gAuth.createCredentials();

        String secret = key.getKey();
        String otpAuthURL = "otpauth://totp/shop-sample-TOTP-SECRET-" + userId + "?secret=" + secret;

        tempSecrets.put(userId, secret);

        try {
            String imageData = generateQRCodeDataUri(otpAuthURL);

            Map<String, Object> data = new HashMap<>();
            data.put("imageData", imageData);
            data.put("secret", secret);

            return Map.of("success", 1, "data", data);
        } catch (Exception e) {
            return Map.of("success", 0, "message", "QRコード生成に失敗しました");
        }
    }
    private String generateQRCodeDataUri(String text) throws WriterException, IOException {
        int width = 300;
        int height = 300;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray();

        String pngBase64 = Base64.getEncoder().encodeToString(pngData);
        return "data:image/png;base64," + pngBase64;
    }

    @PostMapping("/verify-login-totp")
    public Map<String, Object> verifyLoginTOTP(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String userIdStr = request.get("userId");
        String codeStr = request.get("token");

        if (userIdStr == null || codeStr == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("error", "必要なパラメーターが不足しています");
        }
        Long userId = Long.valueOf(userIdStr);
        Map<String, Object>[] resultWrapper = new Map[1];

        userRepository.findByUserId(userId).ifPresentOrElse(user -> {
            if (user.getTotpSecret() == null || user.getTotpSecret().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resultWrapper[0] = Map.of(
                    "success", 0,
                    "message", "TOTPが設定されていない"
                );
                return;
            }

            int code = Integer.parseInt(codeStr);
            GoogleAuthenticator gAuth = new GoogleAuthenticator();
            boolean isCodeValid = gAuth.authorize(user.getTotpSecret(), code);

            if (!isCodeValid) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resultWrapper[0] = Map.of(
                    "success", 0,
                    "message", "TOTPコードが正しくありません"
                );
                return;
            }

            resultWrapper[0] = Map.of(
                "success", 1,
                "token", jwtUtil.generateToken(user.getUserEmail(), user.getUserId()),
                "message", "TOTP認証成功"
            );
        }, () -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resultWrapper[0] = Map.of(
                "success", 0,
                "message", "ユーザーが存在しません"
            );
        });

        return resultWrapper[0];
    }

    @PostMapping("/verify-setup-totp")
    public Map<String, Object> verifySetupTOTP(@RequestBody Map<String, String> request, HttpServletResponse response) {
        String userIdStr = request.get("userId");
        String token = request.get("token");
        if (userIdStr == null || token == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("success", 0, "message", "必要なパラメーターが不足しています");
        }

        Long userId = Long.valueOf(userIdStr);
        String secret = tempSecrets.get(userId);
        if (secret == null) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("success", 0, "message", "コード未生成");
        }

        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        boolean verified = gAuth.authorize(secret, Integer.parseInt(token));
        if (!verified) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("success", 0, "message", "TOTP認証失敗");
        }

        userRepository.findByUserId(userId).ifPresent(user -> {
            user.setTotpSecret(secret);
            userRepository.save(user);
        });

        tempSecrets.remove(userId);
        return Map.of("success", 1, "message", "TOTP設定完了");
    }
}