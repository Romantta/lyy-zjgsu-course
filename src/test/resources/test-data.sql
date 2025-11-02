-- 清空表数据
DELETE FROM enrollments;
DELETE FROM courses;
DELETE FROM students;

-- 插入测试学生数据
INSERT INTO students (id, student_id, name, major, grade, email, created_at) VALUES
('test_student_1', 'TEST001', '测试学生1', '计算机科学', 2023, 'test1@example.com', NOW()),
('test_student_2', 'TEST002', '测试学生2', '软件工程', 2023, 'test2@example.com', NOW()),
('test_student_3', 'TEST003', '测试学生3', '数据科学', 2023, 'test3@example.com', NOW());

-- 插入测试课程数据
INSERT INTO courses (id, code, title, instructor_id, instructor_name, instructor_email, day_of_week, start_time, end_time, expected_attendance, capacity, enrolled, created_at) VALUES
('test_course_1', 'TEST101', '测试课程1', 'T001', '测试教师1', 'teacher1@example.com', 'MONDAY', '09:00', '11:00', 2, 3, 0, NOW()),
('test_course_2', 'TEST102', '测试课程2', 'T002', '测试教师2', 'teacher2@example.com', 'TUESDAY', '14:00', '16:00', 2, 2, 0, NOW());