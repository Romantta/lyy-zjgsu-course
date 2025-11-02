package com.zjgsu.lyy.course.service;

import com.zjgsu.lyy.course.model.Course;
import com.zjgsu.lyy.course.repository.CourseRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(String id) {
        return courseRepository.findById(id);
    }

    public Course createCourse(Course course) {
        // 验证字段
        validateCourseFields(course);

        // 检查课程代码是否已存在
        if (courseRepository.existsByCode(course.getCode())) {
            throw new RuntimeException("课程代码已存在: " + course.getCode());
        }

        return courseRepository.save(course);
    }

    public Course updateCourse(String id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在: " + id));

        // 验证字段
        validateCourseFields(course);

        // 检查课程代码是否与其他课程冲突
        if (!existingCourse.getCode().equals(course.getCode()) &&
                courseRepository.existsByCodeAndIdNot(course.getCode(), id)) {
            throw new RuntimeException("课程代码已存在: " + course.getCode());
        }

        course.setId(id);
        course.setEnrolled(existingCourse.getEnrolled()); // 保持已选人数不变
        course.setCreatedAt(existingCourse.getCreatedAt()); // 保持创建时间不变
        return courseRepository.save(course);
    }

    public void deleteCourse(String id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("课程不存在: " + id));
        courseRepository.delete(course);
    }

    public void incrementEnrolledCount(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在: " + courseId));
        course.setEnrolled(course.getEnrolled() + 1);
        courseRepository.save(course);
    }

    public void decrementEnrolledCount(String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("课程不存在: " + courseId));
        course.setEnrolled(Math.max(0, course.getEnrolled() - 1));
        courseRepository.save(course);
    }

    public List<Course> getCoursesWithAvailableSeats() {
        return courseRepository.findCoursesWithAvailableSeats();
    }

    /**
     * 验证课程必填字段
     */
    private void validateCourseFields(Course course) {
        // 验证课程代码
        if (course.getCode() == null || course.getCode().trim().isEmpty()) {
            throw new RuntimeException("课程代码不能为空");
        }
        // 验证课程名称
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new RuntimeException("课程名称不能为空");
        }
        // 验证教师信息
        if (course.getInstructor() == null) {
            throw new RuntimeException("教师信息不能为空");
        }
        if (course.getInstructor().getId() == null || course.getInstructor().getId().trim().isEmpty()) {
            throw new RuntimeException("教师ID不能为空");
        }
        if (course.getInstructor().getName() == null || course.getInstructor().getName().trim().isEmpty()) {
            throw new RuntimeException("教师姓名不能为空");
        }
        if (course.getInstructor().getEmail() == null || course.getInstructor().getEmail().trim().isEmpty()) {
            throw new RuntimeException("教师邮箱不能为空");
        }
        // 验证邮箱格式
        if (!isValidEmail(course.getInstructor().getEmail())) {
            throw new RuntimeException("教师邮箱格式不正确: " + course.getInstructor().getEmail());
        }
        // 验证课程时间安排
        if (course.getSchedule() == null) {
            throw new RuntimeException("课程时间安排不能为空");
        }
        if (course.getSchedule().getDayOfWeek() == null || course.getSchedule().getDayOfWeek().trim().isEmpty()) {
            throw new RuntimeException("上课日期不能为空");
        }
        if (course.getSchedule().getStartTime() == null || course.getSchedule().getStartTime().trim().isEmpty()) {
            throw new RuntimeException("开始时间不能为空");
        }
        if (course.getSchedule().getEndTime() == null || course.getSchedule().getEndTime().trim().isEmpty()) {
            throw new RuntimeException("结束时间不能为空");
        }

        // 验证时间格式
        if (!isValidTimeFormat(course.getSchedule().getStartTime())) {
            throw new RuntimeException("开始时间格式不正确，应为 HH:mm 格式: " + course.getSchedule().getStartTime());
        }
        if (!isValidTimeFormat(course.getSchedule().getEndTime())) {
            throw new RuntimeException("结束时间格式不正确，应为 HH:mm 格式: " + course.getSchedule().getEndTime());
        }

        // 验证课程容量
        if (course.getCapacity() == null) {
            throw new RuntimeException("课程容量不能为空");
        }
        if (course.getCapacity() <= 0) {
            throw new RuntimeException("课程容量必须大于0");
        }

        // 验证预期出勤人数
        if (course.getSchedule().getExpectedAttendance() == null) {
            throw new RuntimeException("预期出勤人数不能为空");
        }
        if (course.getSchedule().getExpectedAttendance() < 0) {
            throw new RuntimeException("预期出勤人数不能为负数");
        }
        if (course.getSchedule().getExpectedAttendance() > course.getCapacity()) {
            throw new RuntimeException("预期出勤人数不能超过课程容量");
        }
    }

    // 验证邮箱格式
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email != null && email.matches(emailRegex);
    }

    // 验证时间格式
    private boolean isValidTimeFormat(String time) {
        String timeRegex = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$";
        return time != null && time.matches(timeRegex);
    }
}