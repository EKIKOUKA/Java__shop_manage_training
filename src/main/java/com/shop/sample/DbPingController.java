package com.shop.sample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@RestController
public class DbPingController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/ping-db")
    public String pingDatabase() {
        try (Connection conn = dataSource.getConnection()) {
            if (conn.isValid(2)) {
                return "✅ データーベース接続成功";
            } else {
                return "❌ データーベース接続失敗";
            }
        } catch (SQLException e) {
            return "❌ データーベース錯誤: " + e.getMessage();
        }
    }
}