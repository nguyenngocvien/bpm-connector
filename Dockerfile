# ===== Stage 1: Build Maven Project =====
FROM maven:3.9.6-eclipse-temurin-8 AS builder
WORKDIR /app

# Copy toàn bộ project cha (gồm cả module con)
COPY . .

# Build tất cả module, tạo JAR cho bpm-gateway
RUN mvn clean package -DskipTests

# ===== Stage 2: Run Spring Boot App =====
FROM eclipse-temurin:8-jre-jammy
WORKDIR /app

# Copy JAR của bpm-gateway vào container
COPY --from=builder /app/bpm-gateway/target/bpm-connector-1.0.0.jar app.jar

# Mở cổng ứng dụng
EXPOSE 8080

# Chạy app với profile docker
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
