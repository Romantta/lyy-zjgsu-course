package com.zjgsu.lyy.course.controller;

import com.zjgsu.lyy.course.model.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.availability.ApplicationAvailability;
import org.springframework.boot.availability.AvailabilityState;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class DatabaseHealthController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/health/db")
    public ApiResponse<String> checkDatabaseHealth() {
        try {
            // 测试数据库连接
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5); // 5秒超时
                if (!isValid) {
                    return ApiResponse.error(503, "数据库连接无效");
                }
            }

            // 测试简单查询
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);

            return ApiResponse.success("数据库连接正常", "Database is healthy");

        } catch (Exception e) {
            return ApiResponse.error(503, "数据库连接失败: " + e.getMessage());
        }
    }
}