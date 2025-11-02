package com.zjgsu.lyy.course;

import com.zjgsu.lyy.course.service.CourseService;
import com.zjgsu.lyy.course.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ServiceFunctionalityTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Test
    void testStudentService() {
        System.out.println("🔍 测试学生服务...");

        try {
            var students = studentService.getAllStudents();
            assertNotNull(students, "学生列表不应该为null");
            System.out.println("✅ 学生服务查询正常，返回 " + students.size() + " 名学生");

            if (!students.isEmpty()) {
                var firstStudent = students.get(0);
                assertNotNull(firstStudent.getId(), "学生ID不应该为null");
                assertNotNull(firstStudent.getName(), "学生姓名不应该为null");
                System.out.println("✅ 学生数据结构正确: " + firstStudent.getName());
            }

        } catch (Exception e) {
            System.out.println("❌ 学生服务测试失败: " + e.getMessage());
            fail("学生服务测试失败: " + e.getMessage());
        }
    }

    @Test
    void testCourseService() {
        System.out.println("🔍 测试课程服务...");

        try {
            var courses = courseService.getAllCourses();
            assertNotNull(courses, "课程列表不应该为null");
            System.out.println("✅ 课程服务查询正常，返回 " + courses.size() + " 门课程");

            if (!courses.isEmpty()) {
                var firstCourse = courses.get(0);
                assertNotNull(firstCourse.getId(), "课程ID不应该为null");
                assertNotNull(firstCourse.getTitle(), "课程标题不应该为null");
                System.out.println("✅ 课程数据结构正确: " + firstCourse.getTitle());

                // 测试课程容量逻辑
                assertTrue(firstCourse.getCapacity() >= 0, "课程容量应该大于等于0");
                assertTrue(firstCourse.getEnrolled() >= 0, "已选人数应该大于等于0");
                System.out.println("✅ 课程容量逻辑正确: 容量=" + firstCourse.getCapacity() + ", 已选=" + firstCourse.getEnrolled());
            }

        } catch (Exception e) {
            System.out.println("❌ 课程服务测试失败: " + e.getMessage());
            fail("课程服务测试失败: " + e.getMessage());
        }
    }
}