# 1. Build stage (build app bằng Gradle)
FROM gradle:8-jdk17 AS build

WORKDIR /app

# Copy mã nguồn (không copy .env)
COPY build.gradle ./
COPY src ./src

# Build project, tạo jar
RUN gradle clean bootJar -x test --no-daemon

# 2. Run stage (chỉ chạy app)
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy jar từ build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose port ứng dụng (cấu hình trong application.yml là 8080)
EXPOSE 8080

# Chạy app
ENTRYPOINT ["java", "-jar", "app.jar"]