package com.zjgsu.lyy.course.controller;

import com.zjgsu.lyy.course.model.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ApiResponse<String> home() {
        return ApiResponse.success("欢迎使用校园选课系统 API",
                "可用接口：\n" +
                        "- GET /api/courses - 查询所有课程\n" +
                        "- GET /api/students - 查询所有学生\n" +
                        "- GET /api/enrollments - 查询所有选课记录\n" +
                        "请使用 Postman 或 api-test.http 文件测试具体接口");
    }
}