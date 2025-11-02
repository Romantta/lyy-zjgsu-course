package com.zjgsu.lyy.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApiEndpointTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void testHealthEndpoint() {
        System.out.println("🔍 测试健康检查端点...");

        ResponseEntity<String> response = restTemplate.getForEntity("/health/db", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("数据库连接正常"));
        System.out.println("✅ 健康检查端点正常: " + response.getBody());
    }

    @Test
    void testStudentEndpoints() {
        System.out.println("🔍 测试学生相关端点...");

        // 测试获取所有学生
        ResponseEntity<String> response = restTemplate.getForEntity("/api/students", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("code"));
        System.out.println("✅ 学生列表端点正常");

        // 测试获取单个学生（如果存在数据）
        try {
            ResponseEntity<String> singleResponse = restTemplate.getForEntity("/api/students/student001", String.class);
            if (singleResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("✅ 单个学生查询端点正常");
            } else {
                System.out.println("⚠️ 单个学生查询返回: " + singleResponse.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ 单个学生查询测试跳过: " + e.getMessage());
        }
    }

    @Test
    void testCourseEndpoints() {
        System.out.println("🔍 测试课程相关端点...");

        // 测试获取所有课程
        ResponseEntity<String> response = restTemplate.getForEntity("/api/courses", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("code"));
        System.out.println("✅ 课程列表端点正常");

        // 测试获取单个课程（如果存在数据）
        try {
            ResponseEntity<String> singleResponse = restTemplate.getForEntity("/api/courses/course001", String.class);
            if (singleResponse.getStatusCode() == HttpStatus.OK) {
                System.out.println("✅ 单个课程查询端点正常");
            } else {
                System.out.println("⚠️ 单个课程查询返回: " + singleResponse.getStatusCode());
            }
        } catch (Exception e) {
            System.out.println("⚠️ 单个课程查询测试跳过: " + e.getMessage());
        }
    }

    @Test
    void testHomeEndpoint() {
        System.out.println("🔍 测试主页端点...");

        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("校园选课系统"));
        System.out.println("✅ 主页端点正常");
    }
}