# 💰 Expense Tracker API

A RESTful API built with **Spring Boot** for tracking personal expenses with **JWT Authentication** and **Refresh Token** support.

---

## 🛠️ Tech Stack

| Technology | Version |
|---|---|
| Java | 17+ |
| Spring Boot | 3.x |
| Spring Security | 6.x |
| JWT (jjwt) | 0.11.5 |
| MySQL | 8.x |
| Maven | 3.x |
| Lombok | Latest |

---

## ✅ Prerequisites

Make sure you have the following installed on your system before proceeding:

- [ ] **Java 17+** — [Download here](https://www.oracle.com/java/technologies/downloads/)
- [ ] **Maven 3+** — [Download here](https://maven.apache.org/download.cgi)
- [ ] **MySQL 8+** — [Download here](https://dev.mysql.com/downloads/)
- [ ] **Git** — [Download here](https://git-scm.com/downloads)
- [ ] **Postman** (optional, for testing) — [Download here](https://www.postman.com/downloads/)

---

## 🚀 Setup & Installation

### Step 1 — Clone the Repository

```bash
git clone https://github.com/your-username/expense-tracker.git
cd expense-tracker
```

---

### Step 2 — Create MySQL Database

Open your MySQL client or terminal and run:

```sql
CREATE DATABASE expense_tracker;
```

> Make sure your MySQL server is running before this step.

---

### Step 3 — Configure `application.properties`

Open `src/main/resources/application.properties` and update the following:

```properties
# ===========================
# Database Configuration
# ===========================
spring.datasource.url=jdbc:mysql://localhost:3306/expense_tracker
spring.datasource.username=YOUR_MYSQL_USERNAME
spring.datasource.password=YOUR_MYSQL_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# ===========================
# JPA / Hibernate
# ===========================
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

# ===========================
# JWT Configuration
# ===========================
jwt.secret=expense_tracker_super_secret_key_must_be_32chars_minimum_256bits
jwt.access-token-expiration=900000
jwt.refresh-token-expiration=604800000

# ===========================
# Jackson
# ===========================
spring.jackson.serialization.write-dates-as-timestamps=false
```

> Replace `YOUR_MYSQL_USERNAME` and `YOUR_MYSQL_PASSWORD` with your actual MySQL credentials.

---

### Step 4 — Install Dependencies

```bash
mvn clean install
```

> This will download all required dependencies from Maven. Make sure you have an internet connection.

---

### Step 5 — Run the Application

```bash
mvn spring-boot:run
```

Or if you prefer running the JAR directly:

```bash
mvn clean package
java -jar target/expense-tracker-0.0.1-SNAPSHOT.jar
```

> The application will start on **http://localhost:8080**

---

### Step 6 — Verify Application is Running

Open your browser or Postman and hit:

```
GET http://localhost:8080/api/auth/login
```

You should see a response — the app is up! ✅

---

## 📁 Project Structure

```
src/main/java/com/start/expense_tracker/
├── config/                          # App-level configuration
├── constants/                       # API paths and messages
├── controller/                      # REST controllers
│   └── advice/                      # Global exception handler
├── dto/                             # Request/Response DTOs
├── entity/                          # JPA entities
├── mapper/                          # Entity ↔ DTO mappers
├── repository/                      # JPA repositories
├── security/
│   ├── config/                      # SecurityConfig + AuthEntryPoint
│   ├── entity/                      # RefreshToken entity
│   ├── JWT/                         # JwtUtil + JwtAuthFilter
│   ├── repository/                  # RefreshTokenRepository
│   └── user/                        # UserDetailsServiceImpl
├── service/
│   ├── config/                      # Service interfaces
│   └── impl/                        # Service implementations
```

---

## 🔐 Authentication

This project uses **JWT (JSON Web Token)** based authentication.

### How it works:
1. Register a new user via `/api/auth/register`
2. Login via `/api/auth/login` → receive `accessToken` + `refreshToken`
3. Use `accessToken` in the `Authorization` header for all protected endpoints
4. When `accessToken` expires (15 min), use `refreshToken` to get a new one
5. Logout via `/api/auth/logout` to invalidate the `refreshToken`

---

## 📬 API Endpoints

### 🔓 Auth Endpoints (Public)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register a new user |
| POST | `/api/auth/login` | Login and get tokens |
| POST | `/api/auth/refresh` | Get new access token |
| POST | `/api/auth/logout` | Logout and invalidate refresh token |

### 🔒 Expense Endpoints (Protected — requires Bearer token)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/v1/expenses` | Create a new expense |
| GET | `/api/v1/expenses` | Get all expenses |
| PUT | `/api/v1/expenses/{id}` | Update an expense |
| DELETE | `/api/v1/expenses/{id}` | Delete an expense |
| GET | `/api/v1/expenses/summary` | Get total expense summary |
| GET | `/api/v1/expenses/summary/{yearMonth}` | Get monthly summary (e.g. `2026-04`) |
| GET | `/api/v1/expenses/category/{category}` | Get expenses by category |
| GET | `/api/v1/expenses/ids` | Get expenses by IDs |

---

## 📋 Request & Response Examples

### Register
```http
POST /api/auth/register
Content-Type: application/json

{
    "username": "john",
    "email": "john@example.com",
    "password": "john123"
}
```

**Response:**
```json
{
    "success": true,
    "httpStatus": 201,
    "message": "User registered successfully!",
    "data": null,
    "timestamp": "2026-05-01T10:30:00"
}
```

---

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
    "username": "john",
    "password": "john123"
}
```

**Response:**
```json
{
    "success": true,
    "httpStatus": 200,
    "message": "Login successful!",
    "data": {
        "accessToken": "eyJhbGci...",
        "refreshToken": "a1b2c3d4-e5f6-...",
        "username": "john",
        "email": "john@example.com"
    },
    "timestamp": "2026-05-01T10:30:00"
}
```

---

### Using Protected Endpoints

Add the `accessToken` to the `Authorization` header:

```http
GET /api/v1/expenses
Authorization: Bearer eyJhbGci...
```

---

### Refresh Token
```http
POST /api/auth/refresh
Content-Type: application/json

{
    "refreshToken": "a1b2c3d4-e5f6-..."
}
```

---

### Logout
```http
POST /api/auth/logout
Content-Type: application/json

{
    "refreshToken": "a1b2c3d4-e5f6-..."
}
```

---

## ⚠️ Common Errors

| Error | Reason | Fix |
|---|---|---|
| `401 Unauthorized` | No token or expired token | Login again or refresh token |
| `403 Forbidden` | Invalid/tampered token | Login again to get a new token |
| `409 Conflict` | Username or email already exists | Use a different username/email |
| `404 Not Found` | Resource does not exist | Check the ID or endpoint |
| `400 Bad Request` | Invalid request body | Check your JSON structure |

---

## 🔧 JWT Configuration Reference

| Property | Default | Description |
|---|---|---|
| `jwt.secret` | — | Secret key for signing tokens (min 32 chars) |
| `jwt.access-token-expiration` | `900000` | Access token expiry in ms (15 minutes) |
| `jwt.refresh-token-expiration` | `604800000` | Refresh token expiry in ms (7 days) |

---

## 📦 Expense Categories

The following categories are supported:

```
FOOD, TRANSPORT, ENTERTAINMENT, UTILITIES, HEALTH,
SHOPPING, EDUCATION, TRAVEL, OTHER
```

---

## 👨‍💻 Author

**Your Name**
- GitHub: [@your-username](https://github.com/your-username)
- Email: your-email@example.com

---

## 📄 License

This project is licensed under the MIT License.