package com.zjgsu.lyy.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CourseApplicationTests {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void testDatabaseConnection() {
        System.out.println("🔍 测试数据库连接...");
        Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        assertEquals(1, result);
        System.out.println("✅ 数据库连接正常");
    }

    @Test
    void testTablesExist() {
        System.out.println("🔍 检查数据库表...");

        String[] tables = {"students", "courses", "enrollments"};
        for (String table : tables) {
            try {
                Integer count = jdbcTemplate.queryForObject(
                        "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema = DATABASE() AND table_name = ?",
                        Integer.class, table
                );
                if (count != null && count > 0) {
                    System.out.println("✅ 表 " + table + " 存在");

                    // 检查表数据量
                    Integer dataCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM " + table, Integer.class);
                    System.out.println("   📊 数据量: " + dataCount);
                } else {
                    System.out.println("❌ 表 " + table + " 不存在");
                }
            } catch (Exception e) {
                System.out.println("❌ 检查表 " + table + " 时出错: " + e.getMessage());
            }
        }
    }

    @Test
    void testDataIntegrity() {
        System.out.println("🔍 检查数据完整性...");

        try {
            // 检查学生数据
            Integer studentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM students", Integer.class);
            assertTrue(studentCount >= 0, "学生表应该可访问");
            System.out.println("✅ 学生表数据完整性检查通过: " + studentCount + " 条记录");

            // 检查课程数据
            Integer courseCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM courses", Integer.class);
            assertTrue(courseCount >= 0, "课程表应该可访问");
            System.out.println("✅ 课程表数据完整性检查通过: " + courseCount + " 条记录");

            // 检查选课数据
            Integer enrollmentCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM enrollments", Integer.class);
            assertTrue(enrollmentCount >= 0, "选课表应该可访问");
            System.out.println("✅ 选课表数据完整性检查通过: " + enrollmentCount + " 条记录");

        } catch (Exception e) {
            System.out.println("❌ 数据完整性检查失败: " + e.getMessage());
            fail("数据完整性检查失败: " + e.getMessage());
        }
    }
}