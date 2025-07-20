package com.shop.sample.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@RestController
public class UploadController {

    private final Path uploadDir = Paths.get("/home/Project/React_shop_manage/uploads");

    @PostMapping("/upload")
    public Map<String, Object> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return Map.of("success", 0, "message", "アップロードしたのファイルはない");
        }

        try {
            String ext =  file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = System.currentTimeMillis() + "-" + UUID.randomUUID() + ext;
            Path targetPath = uploadDir.resolve(fileName);
            Files.createDirectories(uploadDir);
            file.transferTo(targetPath.toFile());

            String url = "https://www.makotodeveloper.website/shop_sample/uploads/" + fileName;

            return Map.of("success", 1, "url", url);
        } catch (IOException e) {
            return Map.of("success", 0, "message", "アップロード失敗");
        }
    }

    @PostMapping("/upload-file-delete")
    public Map<String, Object> deleteFile(@RequestBody Map<String, String> body) {
        String avatar = body.get("avatar");
        if (avatar == null || avatar.isEmpty()) {
            return Map.of("success", 0, "message", "刪除失敗，ファイルパスが提供されていません");
        }

        String fileName = Paths.get(avatar).getFileName().toString();
        Path filePath = uploadDir.resolve(fileName);

        try {
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                return Map.of("success", 1, "message", "刪除成功");
            } else {
                return Map.of("success", 0, "message", "ファイルは存在しない");
            }
        } catch (IOException e) {
            return Map.of("success", 0, "message", "刪除失敗");
        }
    }
}
