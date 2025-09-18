# ===== Stage 1: Build Maven Project =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent pom and module pom
COPY . .

# Load dependencies first (cache)
RUN mvn dependency:go-offline

# Copy all source
COPY . .

# Build toàn bộ project
RUN mvn clean package -DskipTests

# ===== Stage 2: Run Spring Boot App =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy JAR của module bpm-gateway (main app)
COPY --from=builder /app/bpm-gateway/target/*.jar app.jar

# Open port
EXPOSE 8080

# Run app với profile docker
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
