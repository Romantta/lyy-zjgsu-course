# 校园选课系统 API 测试文档

## 测试环境信息
- **应用名称**: 校园选课系统
- **基础URL**: `http://localhost:8080`
- **测试工具**: Postman

## 测试场景1：完整的课程管理流程

### 1.1 创建3门不同的课程
**请求示例:**
```http
### 创建课程1 - 计算机科学导论
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "code": "CS101",
    "title": "计算机科学导论",
    "instructor": {
        "id": "T001",
        "name": "张教授",
        "email": "zhang@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "MONDAY",
        "startTime": "08:00",
        "endTime": "10:00",
        "expectedAttendance": 50
    },
    "capacity": 60
}
```

**预期响应:**
```json
{
    "code": 201,
    "message": "课程创建成功",
    "data": {
        "id": "生成的UUID",
        "code": "CS101",
        "title": "计算机科学导论",
        "instructor": {
            "id": "T001",
            "name": "张教授",
            "email": "zhang@example.zjgsu.cn"
        },
        "schedule": {
            "dayOfWeek": "MONDAY",
            "startTime": "08:00",
            "endTime": "10:00",
            "expectedAttendance": 50
        },
        "capacity": 60,
        "enrolled": 0
    }
}
```

**实际测试结果:**
- ✅ 状态码: 201 Created
- ✅ 响应包含生成的UUID
- ✅ 课程信息正确保存

### 1.2 创建课程2 - 高等数学
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "code": "MA101",
    "title": "高等数学",
    "instructor": {
        "id": "T002", 
        "name": "李教授",
        "email": "li@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "TUESDAY",
        "startTime": "10:00",
        "endTime": "12:00", 
        "expectedAttendance": 60
    },
    "capacity": 70
}
```

### 1.3 创建课程3 - 大学物理
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "code": "PH101",
    "title": "大学物理",
    "instructor": {
        "id": "T003",
        "name": "王教授",
        "email": "wang@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "WEDNESDAY",
        "startTime": "14:00",
        "endTime": "16:00",
        "expectedAttendance": 40
    },
    "capacity": 50
}
```

### 1.4 查询所有课程
**请求:**
```http
GET http://localhost:8080/api/courses
```

**预期响应:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {课程1数据},
        {课程2数据}, 
        {课程3数据}
    ]
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 返回3条课程记录
- ✅ 数据格式正确

### 1.5 根据ID查询特定课程
**请求:**
```http
GET http://localhost:8080/api/courses/{{courseId}}
```

**预期响应:**
```json
{
    "code": 200,
    "message": "Success", 
    "data": {
        "id": "{{courseId}}",
        "code": "CS101",
        "title": "计算机科学导论",
        // ... 完整课程信息
    }
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 返回正确的课程信息
- ✅ ID匹配查询参数

### 1.6 更新课程信息
**请求:**
```http
PUT http://localhost:8080/api/courses/{{courseId}}
Content-Type: application/json

{
    "code": "CS101-UPDATED",
    "title": "计算机科学导论（更新版）",
    "instructor": {
        "id": "T001",
        "name": "张教授",
        "email": "zhang@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "MONDAY",
        "startTime": "09:00", 
        "endTime": "11:00",
        "expectedAttendance": 55
    },
    "capacity": 65
}
```

**预期响应:**
```json
{
    "code": 200,
    "message": "课程更新成功",
    "data": {
        "id": "{{courseId}}",
        "code": "CS101-UPDATED",
        "title": "计算机科学导论（更新版）",
        // ... 更新后的课程信息
    }
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 课程信息成功更新
- ✅ enrolled字段保持不变

### 1.7 删除课程
**请求:**
```http
DELETE http://localhost:8080/api/courses/{{courseId}}
```

**预期响应:**
- 状态码: 204 No Content
- 无响应体

**实际测试结果:**
- ✅ 状态码: 204 No Content
- ✅ 课程成功删除

### 1.8 验证删除
**请求:**
```http
GET http://localhost:8080/api/courses/{{courseId}}
```

**预期响应:**
```json
{
    "code": 404,
    "message": "课程不存在",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 404 Not Found
- ✅ 正确返回课程不存在信息

---

## 测试场景2：选课业务流程

### 2.1 创建容量为2的课程
**请求:**
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "code": "TEST101",
    "title": "容量为2的课程",
    "instructor": {
        "id": "T004",
        "name": "方教授",
        "email": "test@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "THURSDAY",
        "startTime": "13:00",
        "endTime": "15:00",
        "expectedAttendance": 2
    },
    "capacity": 2
}
```

### 2.2 创建测试学生
**请求:**
```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
    "studentId": "S001",
    "name": "学生一",
    "major": "计算机科学",
    "grade": 2024,
    "email": "s001@student.zjgsu.cn"
}
```
重复创建 S002、S003 学生

### 2.3 学生S001选课
**请求:**
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
    "courseId": "{{testCourseId}}",
    "studentId": "S001"
}
```

**预期响应:**
```json
{
    "code": 201,
    "message": "选课成功",
    "data": {
        "id": "生成的选课记录ID",
        "courseId": "{{testCourseId}}",
        "studentId": "S001",
        "enrolledAt": "2024-01-01T10:00:00"
    }
}
```

**实际测试结果:**
- ✅ 状态码: 201 Created
- ✅ 选课记录创建成功
- ✅ 课程enrolled字段+1

### 2.4 学生S002选课
**请求:**
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
    "courseId": "{{testCourseId}}", 
    "studentId": "S002"
}
```

**实际测试结果:**
- ✅ 状态码: 201 Created
- ✅ 选课成功
- ✅ 课程enrolled字段变为2

### 2.5 学生S003选课（容量已满）
**请求:**
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
    "courseId": "{{testCourseId}}",
    "studentId": "S003"
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "课程容量已满",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回容量已满错误

### 2.6 学生S001重复选课
**请求:**
```http
POST http://localhost:8080/api/enrollments  
Content-Type: application/json

{
    "courseId": "{{testCourseId}}",
    "studentId": "S001"
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "学生已选过该课程",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回重复选课错误

### 2.7 查询课程验证enrolled字段
**请求:**
```http
GET http://localhost:8080/api/courses/{{testCourseId}}
```

**预期响应:**
```json
{
    "code": 200,
    "message": "Success",
    "data": {
        "id": "{{testCourseId}}",
        "enrolled": 2,
        "capacity": 2
        // ... 其他字段
    }
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ enrolled字段正确显示为2

---

## 测试场景3：学生管理流程
### 3.1 创建3个不同学号的学生
**请求:**
```http
POST http://localhost:8080/api/students
Content-Type: application/json
{
    "studentId": "S2024001",
    "name": "张三",
    "major": "计算机科学与技术", 
    "grade": 2024,
    "email": "zhangsan@student.zjgsu.cn"
}
```
重复创建 S2024002、S2024003 学生

**实际测试结果:**
- ✅ 状态码: 201 Created
- ✅ 系统自动生成ID和createdAt时间戳
- ✅ 学生信息正确保存

### 3.2 查询所有学生
**请求:**
```http
GET http://localhost:8080/api/students
```

**预期响应:**
```json
{
    "code": 200,
    "message": "Success",
    "data": [
        {学生1数据},
        {学生2数据},
        {学生3数据}
    ]
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 返回3条学生记录
- ✅ 包含完整学生信息

### 3.3 根据ID查询学生
**请求:**
```http
GET http://localhost:8080/api/students/{{studentId}}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 返回正确的学生信息
- ✅ ID匹配查询参数

### 3.4 更新学生信息
**请求:**
```http
PUT http://localhost:8080/api/students/{{studentId}}
Content-Type: application/json

{
    "studentId": "S2024001",
    "name": "张三",
    "major": "软件工程",
    "grade": 2024,
    "email": "zhangsan_new@student.zjgsu.cn" 
}
```

**预期响应:**
```json
{
    "code": 200,
    "message": "学生信息更新成功",
    "data": {
        "id": "{{studentId}}",
        "studentId": "S2024001", 
        "name": "张三",
        "major": "软件工程",
        "grade": 2024,
        "email": "zhangsan_new@student.zjgsu.cn",
        "createdAt": "原始创建时间"  // 保持不变
    }
}
```

**实际测试结果:**
- ✅ 状态码: 200 OK
- ✅ 专业和邮箱成功更新
- ✅ createdAt字段保持不变

### 3.5 不存在的学生选课
**请求:**
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
    "courseId": "{{testCourseId}}",
    "studentId": "NON_EXISTENT_STUDENT"
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "学生不存在: NON_EXISTENT_STUDENT",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回学生不存在错误

### 3.6 删除有选课记录的学生
**请求:**
```http
DELETE http://localhost:8080/api/students/{{enrolledStudentId}}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "无法删除：该学生存在选课记录",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回存在选课记录错误

### 3.7 删除没有选课记录的学生

**请求:**
```http
DELETE http://localhost:8080/api/students/{{studentWithoutEnrollmentId}}
```

**预期响应:**
- 状态码: 204 No Content
- 无响应体

**实际测试结果:**
- ✅ 状态码: 204 No Content
- ✅ 学生成功删除

---

## 测试场景4：错误处理

### 4.1 查询不存在的课程ID
**请求:**
```http
GET http://localhost:8080/api/courses/non_existent_id
```

**预期响应:**
```json
{
    "code": 404,
    "message": "课程不存在",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 404 Not Found
- ✅ 正确返回课程不存在信息

### 4.2 创建课程缺少必填字段
**请求:**
```http
POST http://localhost:8080/api/courses
Content-Type: application/json

{
    "code": "CS102"
    // 缺少title、instructor等必填字段
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "课程名称不能为空", 
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回必填字段验证错误

### 4.3 选课时提供不存在的课程ID
**请求:**
```http
POST http://localhost:8080/api/enrollments
Content-Type: application/json

{
    "courseId": "non_existent_course",
    "studentId": "S001"
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "课程不存在: non_existent_course",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回课程不存在错误

### 4.4 创建学生时使用重复的studentId
**请求:**
```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
    "studentId": "S2024001",  // 已存在的学号
    "name": "李四",
    "major": "计算机科学",
    "grade": 2024,
    "email": "lisi@student.zjgsu.cn"
}
```

**预期响应:**
```json
{
    "code": 400,
    "message": "学号已存在: S2024001",
    "data": null
}
```

**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回学号重复错误

### 4.5 创建学生时使用无效的邮箱格式
**请求:**
```http
POST http://localhost:8080/api/students
Content-Type: application/json

{
    "studentId": "S2024004",
    "name": "王五",
    "major": "计算机科学",
    "grade": 2024,
    "email": "invalid-email无效邮箱格式"
}
```
**预期响应:**
```json
{
    "code": 400,
    "message": "邮箱格式不正确: invalid-email",
    "data": null
}
```
**实际测试结果:**
- ✅ 状态码: 400 Bad Request
- ✅ 正确返回邮箱格式错误

