# Docker 部署指南

## 环境要求

- Docker Engine 20.10+
- Docker Compose 2.0+
- 至少 2GB 可用内存
- 至少 1GB 可用磁盘空间

## 项目结构

```
course1.1.0/
├── src/                          # 源代码目录
├── Dockerfile                    # Docker 镜像构建文件
├── .dockerignore                 # Docker 忽略文件
├── docker-compose.yml            # Docker Compose 编排文件
├── pom.xml                       # Maven 配置文件
└── README.md                     # 项目说明文档
```

## 快速开始

### 1. 构建镜像

```bash
# 进入项目目录
cd course1.1.0

# 构建 Docker 镜像
docker compose build

# 或者强制重新构建（不使用缓存）
docker compose build --no-cache
```

### 2. 启动服务

```bash
# 启动所有服务（后台运行）
docker compose up -d

# 查看服务状态
docker compose ps
```

### 3. 验证部署

```bash
# 等待应用完全启动（约30秒）
sleep 30

# 测试健康检查
curl http://localhost:8080/actuator/health

# 测试 API 端点
curl http://localhost:8080/api/courses
```

## 完整部署步骤

### 第一步：环境准备

确保 Docker 和 Docker Compose 已安装并运行：

```bash
# 检查 Docker 版本
docker --version
docker compose version

# 检查 Docker 服务状态
sudo systemctl status docker
```

### 第二步：构建和部署

```bash
# 1. 克隆或下载项目代码
git clone <repository-url>
cd course1.1.0

# 2. 构建镜像
docker compose build

# 3. 启动服务
docker compose up -d

# 4. 查看服务状态
docker compose ps
```

### 第三步：功能测试

```bash
# 1. 创建测试课程
curl -X POST http://localhost:8080/api/courses \
  -H "Content-Type: application/json" \
  -d '{
    "code": "CS101",
    "title": "计算机导论",
    "instructor": {
        "id": "T001",
        "name": "李教授",
        "email": "li@example.zjgsu.cn"
    },
    "schedule": {
        "dayOfWeek": "MONDAY",
        "startTime": "10:00",
        "endTime": "12:00",
        "expectedAttendance": 50
    },
    "capacity": 60
}'

# 2. 创建测试学生
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{
    "studentId": "S001",
    "name": "学生一",
    "major": "计算机科学",
    "grade": 2024,
    "email": "s001@student.zjgsu.cn"
}'

# 3. 验证数据
curl http://localhost:8080/api/courses
curl http://localhost:8080/api/students
```

### 第四步：数据持久化验证

```bash
# 1. 记录当前数据
curl http://localhost:8080/api/courses
curl http://localhost:8080/api/students

# 2. 重启服务
docker compose down
docker compose up -d

# 3. 等待重启完成
sleep 30

# 4. 验证数据仍然存在
curl http://localhost:8080/api/courses
curl http://localhost:8080/api/students
```

## 服务管理

### 查看服务状态

```bash
# 查看运行状态
docker compose ps

# 查看详细状态
docker compose ps -a

# 查看资源使用情况
docker compose top
```

### 查看日志

```bash
# 查看所有服务日志
docker compose logs

# 查看应用日志
docker compose logs app

# 查看数据库日志
docker compose logs mysql

# 实时查看日志
docker compose logs -f app

# 查看最后 N 行日志
docker compose logs --tail=50 app
```

### 服务控制

```bash
# 停止服务
docker compose down

# 停止服务并删除数据卷（谨慎使用）
docker compose down -v

# 重启服务
docker compose restart

# 重启特定服务
docker compose restart app

# 暂停服务
docker compose pause

# 恢复服务
docker compose unpause
```

### 进入容器

```bash
# 进入应用容器
docker exec -it course-app bash

# 进入数据库容器
docker exec -it course-mysql bash

# 在数据库容器中连接 MySQL
docker exec -it course-mysql mysql -u course_user -pcourse_password course
```

## 镜像管理

```bash
# 查看镜像列表
docker images

# 查看项目相关镜像
docker images | grep course

# 查看镜像大小
docker images --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}"

# 删除镜像
docker rmi course-app:latest

# 清理未使用的镜像
docker image prune
```

## 网络和存储

```bash
# 查看网络
docker network ls

# 查看项目网络详情
docker network inspect course-course-network

# 查看数据卷
docker volume ls

# 查看数据卷详情
docker volume inspect course_mysql-data
```

## 故障排查

### 常见问题及解决方案

#### 1. 应用启动失败

```bash
# 查看应用日志
docker compose logs app

# 检查数据库连接
docker exec -it course-app ping mysql
docker exec -it course-app nc -zv mysql 3306
```

#### 2. 数据库连接问题

```bash
# 检查 MySQL 容器状态
docker compose ps mysql

# 检查 MySQL 日志
docker compose logs mysql

# 测试数据库连接
docker exec -it course-mysql mysql -u course_user -pcourse_password -e "SHOW DATABASES;"
```

#### 3. 端口被占用

```bash
# 检查端口占用
netstat -tulpn | grep 8080
netstat -tulpn | grep 3306

# 如果端口被占用，可以修改 docker-compose.yml 中的端口映射
```

#### 4. 健康检查失败

如果健康检查显示 unhealthy，可能是容器内缺少 curl 命令：

```bash
# 修改 docker-compose.yml 中的健康检查配置
# 将 curl 改为 wget 或暂时禁用健康检查
```

#### 5. 镜像构建缓慢

```bash
# 使用国内镜像源
# 修改 Dockerfile 中的 Maven 镜像源

# 清理构建缓存
docker system prune -f

# 分阶段构建
docker build --target builder -t course-builder .
docker build -t course-app .
```

### 诊断工具

创建诊断脚本 `diagnose.sh`：

```bash
#!/bin/bash
echo "=== 系统诊断 ==="

echo "1. 服务状态:"
docker compose ps

echo -e "\n2. 资源使用:"
docker system df

echo -e "\n3. 网络检查:"
docker network ls | grep course

echo -e "\n4. 应用健康:"
curl -s http://localhost:8080/actuator/health || echo "应用不可达"

echo -e "\n5. 数据库连接:"
docker exec course-mysql mysql -u course_user -pcourse_password -e "SELECT 1;" course 2>/dev/null && echo "数据库正常" || echo "数据库连接失败"

echo -e "\n=== 诊断完成 ==="
```

## 开发说明

### 配置文件

- `application.yml` - 主配置文件（开发环境）
- `application-docker.yml` - Docker 环境专用配置
- `docker-compose.yml` - 服务编排配置

### 环境变量

通过环境变量可以覆盖默认配置：

```bash
# 示例：使用不同的数据库密码
docker compose run -e SPRING_DATASOURCE_PASSWORD=new_password app
```

### 开发模式

对于开发环境，可以使用 volumes 挂载源代码实现热部署：

```yaml
# 在 docker-compose.yml 中添加
services:
  app:
    volumes:
      - ./src:/app/src
      - ./pom.xml:/app/pom.xml
```

## 生产部署建议

1. **使用特定版本标签**：避免使用 latest 标签
2. **配置资源限制**：设置 CPU 和内存限制
3. **使用外部数据库**：考虑使用云数据库服务
4. **配置日志收集**：使用 ELK 或类似方案
5. **设置监控告警**：监控容器状态和资源使用

## 清理资源

```bash
# 停止并删除所有容器
docker compose down

# 删除所有未使用的资源
docker system prune -a

# 删除所有数据卷（谨慎使用）
docker volume prune
```

## 获取帮助

如果遇到问题：

1. 查看本文档的故障排查部分
2. 检查服务日志：`docker compose logs`
3. 验证配置语法：`docker compose config`
4. 搜索相关错误信息

---

*最后更新: $(date)*
