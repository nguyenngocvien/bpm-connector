# ===== Stage 1: Build Maven Project =====
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy parent pom and module pom
COPY pom.xml .
COPY bpm-gateway/pom.xml bpm-gateway/
COPY core-service/pom.xml core-service/

# Load dependencies first (cache)
RUN mvn dependency:go-offline

# Copy all source
COPY . .

# Build all module
RUN mvn clean package -DskipTests

# ===== Stage 2: Run Spring Boot App =====
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy JAR of module bpm-gateway (main app)
COPY --from=builder /app/bpm-gateway/target/*.jar app.jar

# Open port
EXPOSE 8080

# Run app with profile docker
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
