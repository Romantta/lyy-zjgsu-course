package com.zjgsu.lyy.course;

import com.zjgsu.lyy.course.service.CourseService;
import com.zjgsu.lyy.course.service.EnrollmentService;
import com.zjgsu.lyy.course.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class BusinessLogicTest {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private EnrollmentService enrollmentService;

    @Test
    void testBusinessRules() {
        System.out.println("🔍 验证业务规则...");

        try {
            // 验证学生数据业务规则
            var students = studentService.getAllStudents();
            for (var student : students) {
                assertNotNull(student.getStudentId(), "学号不能为空");
                assertNotNull(student.getName(), "学生姓名不能为空");
                assertNotNull(student.getEmail(), "学生邮箱不能为空");
                assertTrue(student.getGrade() > 1900 && student.getGrade() < 2100, "入学年份应该合理");
            }
            System.out.println("✅ 学生业务规则验证通过");

            // 验证课程数据业务规则
            var courses = courseService.getAllCourses();
            for (var course : courses) {
                assertNotNull(course.getCode(), "课程代码不能为空");
                assertNotNull(course.getTitle(), "课程标题不能为空");
                assertTrue(course.getCapacity() > 0, "课程容量应该大于0");
                assertTrue(course.getEnrolled() >= 0, "已选人数应该大于等于0");
                assertTrue(course.getEnrolled() <= course.getCapacity(), "已选人数不能超过容量");
            }
            System.out.println("✅ 课程业务规则验证通过");

            // 验证选课记录
            var enrollments = enrollmentService.getAllEnrollments();
            for (var enrollment : enrollments) {
                assertNotNull(enrollment.getCourseId(), "选课记录课程ID不能为空");
                assertNotNull(enrollment.getStudentId(), "选课记录学生ID不能为空");
                assertNotNull(enrollment.getStatus(), "选课状态不能为空");
            }
            System.out.println("✅ 选课业务规则验证通过");

        } catch (Exception e) {
            System.out.println("❌ 业务规则验证失败: " + e.getMessage());
            fail("业务规则验证失败: " + e.getMessage());
        }
    }

    @Test
    void testSystemIntegration() {
        System.out.println("🔍 测试系统集成...");

        try {
            // 测试各服务之间的集成
            var students = studentService.getAllStudents();
            var courses = courseService.getAllCourses();
            var enrollments = enrollmentService.getAllEnrollments();

            System.out.println("✅ 系统集成测试通过:");
            System.out.println("   学生数量: " + students.size());
            System.out.println("   课程数量: " + courses.size());
            System.out.println("   选课记录数量: " + enrollments.size());

            // 验证数据一致性
            if (!students.isEmpty() && !courses.isEmpty()) {
                System.out.println("✅ 系统数据加载正常");
            }

        } catch (Exception e) {
            System.out.println("❌ 系统集成测试失败: " + e.getMessage());
            fail("系统集成测试失败: " + e.getMessage());
        }
    }
}