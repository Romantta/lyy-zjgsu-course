# Stage 1: Build the application
FROM maven:3-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final lightweight runtime image
FROM eclipse-temurin:17-jre-alpine
RUN apk add --no-cache curl gcompat
WORKDIR /app
COPY --from=builder /app/target/*.jar ./app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]