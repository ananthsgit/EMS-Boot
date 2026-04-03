# 🚀 Employee Management System (EMS-Boot)

A production-ready **Spring Boot backend application** for managing employees, built with **JWT-based authentication, REST APIs, Thymeleaf UI**, and designed using **SOLID principles**.

---

## 🔗 Project Overview

This project demonstrates a **real-world backend architecture** with secure API design, modular service layers, and scalable structure.

It supports:

* Employee CRUD operations
* Authentication & authorization using JWT
* Admin dashboard with statistics
* API documentation using Swagger/OpenAPI

---

## 🧰 Tech Stack

| Layer      | Technology                     |
| ---------- | ------------------------------ |
| Backend    | Spring Boot 3.x                |
| Security   | Spring Security + JWT          |
| Database   | MySQL                          |
| ORM        | Spring Data JPA / Hibernate    |
| Frontend   | Thymeleaf + CSS                |
| API Docs   | SpringDoc OpenAPI (Swagger UI) |
| Build Tool | Maven                          |

---

## 🏗️ Architecture

The application follows a **layered architecture**:

Controller → Service → Repository → Database

Key design principles:

* Separation of concerns
* Interface-based programming
* SOLID principles implementation

---

## 📂 Project Structure

```
com.springboot.ems
├── controller/     # REST + Web Controllers
├── service/        # Business logic (interfaces + implementations)
├── repository/     # Data access layer
├── security/       # JWT + Spring Security config
├── model/          # JPA entities
└── config/         # Swagger configuration
```

---

## 🔐 Authentication Flow (JWT)

1. User logs in using `/auth/login`
2. Server validates credentials
3. JWT token is generated
4. Client sends token in headers
5. Requests are authorized using JWT filter

---

## 🌐 API Endpoints

### 🔓 Public APIs

| Method | Endpoint         | Description           |
| ------ | ---------------- | --------------------- |
| POST   | `/auth/register` | Register new user     |
| POST   | `/auth/login`    | Login and receive JWT |

---

### 🔒 Protected APIs (JWT Required)

| Method | Endpoint                         | Description        |
| ------ | -------------------------------- | ------------------ |
| GET    | `/api/employees`                 | Get all employees  |
| GET    | `/api/employees/{id}`            | Get employee by ID |
| POST   | `/api/employees`                 | Create employee    |
| PUT    | `/api/employees/{id}`            | Update employee    |
| DELETE | `/api/employees/{id}`            | Delete employee    |
| GET    | `/api/employees/search?keyword=` | Search employees   |

---

## 📘 API Documentation

Swagger UI available at:

http://localhost:8080/swagger-ui.html

👉 Features:

* Interactive API testing
* JWT authorization support
* Endpoint grouping & descriptions

---

## ⚙️ Setup & Run

### Prerequisites

* Java 17+
* MySQL
* Maven

---

### Database Setup

```sql
CREATE DATABASE ems_db;
```

---

### Configuration

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

---

### Run Application

```bash
mvn spring-boot:run
```

---

## 🖥️ Application URLs

| URL                                   | Description |
| ------------------------------------- | ----------- |
| http://localhost:8080/                | Dashboard   |
| http://localhost:8080/employees       | Employee UI |
| http://localhost:8080/swagger-ui.html | Swagger UI  |

---

## 🧠 Key Highlights

* ✅ JWT-based stateless authentication
* ✅ Clean layered architecture
* ✅ SOLID principles applied
* ✅ Swagger API documentation
* ✅ Modular service design (Read / Write / Stats separation)
* ✅ Secure REST APIs

---

## 📊 SOLID Principles Implementation

| Principle | Implementation                               |
| --------- | -------------------------------------------- |
| SRP       | Split services into focused responsibilities |
| OCP       | Model-driven updates via `copyFrom()`        |
| LSP       | Interface-based service implementations      |
| ISP       | Separate Read, Write, Stats interfaces       |
| DIP       | TokenProvider abstraction for JWT            |

---

## 🚀 Future Improvements

* Global Exception Handling
* DTO Layer (decouple entities from API)
* Pagination & Sorting APIs
* Role-based authorization (ADMIN / USER)
* Dockerization

---

## 👨‍💻 Author

**Ananth**
Backend Developer (Java | Spring Boot | Security)

---

## ⭐ If you like this project

Give it a ⭐ on GitHub — it motivates me to build more!
