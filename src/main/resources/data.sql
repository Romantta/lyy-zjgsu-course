-- 插入学生数据（使用 INSERT IGNORE 避免重复）
INSERT IGNORE INTO students (id, student_id, name, major, grade, email, created_at) VALUES
('student001', 'S2023001', '张三', '计算机科学', 2023, 'zhangsan@example.com', NOW()),
('student002', 'S2023002', '李四', '软件工程', 2023, 'lisi@example.com', NOW()),
('student003', 'S2023003', '王五', '数据科学', 2023, 'wangwu@example.com', NOW()),
('student004', 'S2023004', '赵六', '人工智能', 2023, 'zhaoliu@example.com', NOW()),
('student005', 'S2023005', '钱七', '网络安全', 2023, 'qianqi@example.com', NOW());

-- 插入课程数据（使用 INSERT IGNORE 避免重复）
INSERT IGNORE INTO courses (id, code, title, instructor_id, instructor_name, instructor_email, day_of_week, start_time, end_time, expected_attendance, capacity, enrolled, created_at) VALUES
('course001', 'CS101', 'Java程序设计', 'T001', '张教授', 'prof.zhang@example.com', 'MONDAY', '09:00', '11:00', 30, 50, 0, NOW()),
('course002', 'CS102', '数据库原理', 'T002', '李教授', 'prof.li@example.com', 'TUESDAY', '14:00', '16:00', 25, 40, 0, NOW()),
('course003', 'CS103', 'Web开发技术', 'T003', '王教授', 'prof.wang@example.com', 'WEDNESDAY', '10:00', '12:00', 20, 30, 0, NOW()),
('course004', 'CS104', '数据结构与算法', 'T004', '赵教授', 'prof.zhao@example.com', 'THURSDAY', '08:00', '10:00', 35, 45, 0, NOW()),
('course005', 'CS105', '操作系统', 'T005', '钱教授', 'prof.qian@example.com', 'FRIDAY', '15:00', '17:00', 28, 35, 0, NOW());

-- 插入选课记录（使用 INSERT IGNORE 避免重复）
INSERT IGNORE INTO enrollments (id, course_id, student_id, enrolled_at, status) VALUES
('enroll001', 'course001', 'student001', NOW(), 'ACTIVE'),
('enroll002', 'course001', 'student002', NOW(), 'ACTIVE'),
('enroll003', 'course002', 'student001', NOW(), 'ACTIVE'),
('enroll004', 'course002', 'student003', NOW(), 'ACTIVE'),
('enroll005', 'course003', 'student004', NOW(), 'ACTIVE');