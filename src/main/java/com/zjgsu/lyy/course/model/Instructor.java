package com.zjgsu.lyy.course.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * 教师实体类
 * 包含教师的基本信息：ID、姓名、邮箱
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Instructor {

    @Column(name = "instructor_id", nullable = false, length = 50)
    private String id;

    @Column(name = "instructor_name", nullable = false, length = 100)
    private String name;

    @Column(name = "instructor_email", nullable = false, length = 100)
    private String email;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
}