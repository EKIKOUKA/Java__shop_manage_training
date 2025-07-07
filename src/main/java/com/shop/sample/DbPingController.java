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
                return "✅ 資料庫連線成功";
            } else {
                return "❌ 資料庫連線失敗";
            }
        } catch (SQLException e) {
            return "❌ 資料庫錯誤: " + e.getMessage();
        }
    }
}