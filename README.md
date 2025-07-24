# 🚀 BPM Integration Connector

A modular backend system to integrate BPM systems with REST, SQL, Email, and authentication services. Designed for extensibility and reuse in BPM microservice architectures.

---

## 📆 Module Overview

- **bpm-gateway**: Main Spring Boot application. Handles client requests and routing.
- **core-service**: Reusable Spring module providing REST invocation, SQL execution, email, and authentication utilities.

---

## 🧹 Core Features

### 🟢 core-service

- **RESTInvoker**: Dynamically invokes external REST APIs based on runtime configuration.
- **SQLConnector**: Executes SQL queries, updates, and procedures from JSON payloads.
- **EmailService**: Sends HTML emails using configurable SMTP settings.
- **AuthManager**: Supports Basic Auth and JWT validation (merged from legacy auth-service).
- **AuthFilter**: Integrates with Spring Security to protect API endpoints.

---

### 🟢 bpm-gateway

- **REST Controllers**: Expose public APIs for external clients or internal BPM systems.
- **Uses core-service**: Delegates logic to SQL, REST, Email services for execution.
- **Authentication**: Applies AuthFilter for securing endpoints via Authorization header.

---

## ⚙️ Configuration (`application.yml`)

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/yourdb
    username: dbuser
    password: dbpass
    driver-class-name: com.mysql.cj.jdbc.Driver

mail:
  username: your@email.com
  password: mailpassword
  smtp-host: smtp.example.com
  smtp-port: 587

auth:
  jwt-secret: your-jwt-secret
  basic-users:
    user1: password1
    admin: admin123
```

> ℹ️ **Note:** Place this config in `bpm-gateway/src/main/resources/application.yml`. It will also auto-configure beans in `core-service`.

---

## 💠 Integration Guide

### Maven Dependency (inside bpm-gateway)

```xml
<dependency>
    <groupId>com.bpm</groupId>
    <artifactId>core-service</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Example: SQL Query API

```java
@RestController
@RequestMapping("/api/sql")
public class SqlController {

    private final SQLConnector sqlConnector;

    public SqlController(SQLConnector sqlConnector) {
        this.sqlConnector = sqlConnector;
    }

    @PostMapping("/query")
    public String executeQuery(@RequestBody String jsonPayload) {
        return sqlConnector.executeQuery(jsonPayload);
    }
}
```

---

## 🏗️ Build & Run

### Build all modules

```bash
mvn clean install
```

### Run bpm-gateway

```bash
cd bpm-gateway
mvn spring-boot:run
```

> ✅ Requires Java 17+, Maven 3.8+, and a running MySQL/Postgres database.

---

## 🌟 Design Goals

* 🔄 **Modular**: Reuse `core-service` across projects.
* ⚙️ **Extensible**: Easily add new services like file-upload, reporting, or gRPC integration.
* 🔐 **Secure**: Simple and pluggable authentication via Basic or JWT.
* 📊 **Efficient**: Optimized for BPM system automation and external system integration.

---

## 📄 License

**Internal Use Only** — This codebase is proprietary to VcoreStack projects. Redistribution requires approval.

---

## 📩 Contact / Support

For questions, integration support, or contribution guidelines, contact the core backend team.
