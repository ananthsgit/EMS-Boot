# Employee Management System (EMS-Boot)

A full-stack Spring Boot application for managing employees, built with REST APIs, Thymeleaf UI, JWT Security, Swagger/OpenAPI documentation, and SOLID design principles.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.5.x |
| Database | MySQL |
| ORM | Spring Data JPA / Hibernate |
| Frontend | Thymeleaf + Custom CSS |
| Security | Spring Security + JWT (JJWT 0.12.6) |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Validation | Jakarta Bean Validation |
| Build Tool | Maven |

---

## Project Structure

```
src/main/java/com/springboot/ems/
│
├── controller/
│   ├── AuthController.java           # REST: /auth/register, /auth/login
│   ├── EmployeeController.java       # REST: /api/employees/**
│   ├── EmployeeWebController.java    # Thymeleaf: /employees/**
│   └── HomeController.java           # Thymeleaf: / (dashboard)
│
├── Service/
│   ├── EmployeeReadService.java      # Interface: read operations
│   ├── EmployeeWriteService.java     # Interface: write operations
│   ├── EmployeeStatsService.java     # Interface: stats/reporting
│   ├── EmployeeServiceImpl.java      # Implements Read + Write
│   ├── EmployeeStatsServiceImpl.java # Implements Stats
│   ├── AuthService.java              # Interface: register + login
│   └── AuthServiceImpl.java          # Implements Auth logic
│
├── model/
│   ├── Employee.java                 # JPA Entity
│   └── User.java                     # JPA Entity (auth)
│
├── repository/
│   ├── EmployeeRepository.java       # JPA Repository
│   └── UserRepository.java           # JPA Repository
│
├── security/
│   ├── TokenProvider.java            # Interface: token operations
│   ├── JwtUtil.java                  # Implements TokenProvider
│   ├── JwtFilter.java                # OncePerRequestFilter
│   ├── SecurityConfig.java           # Filter chain configuration
│   └── UserDetailsServiceImpl.java   # Loads user from DB
│
├── SwaggerConfig.java                # OpenAPI bean configuration
└── EmsBootApplication.java           # Main class
```

---

## Getting Started

### Prerequisites
- Java 17+
- MySQL running on port 3306
- Maven

### Database Setup
```sql
CREATE DATABASE ems_db;
```

### application.properties
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ems_db
spring.datasource.username=root
spring.datasource.password=<your_password>
spring.jpa.hibernate.ddl-auto=update

springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
springdoc.swagger-ui.operationsSorter=method
springdoc.swagger-ui.displayRequestDuration=true
```

### Run
```bash
mvn spring-boot:run
```

---

## Application URLs

| URL | Description |
|---|---|
| `http://localhost:8080/` | Dashboard (Thymeleaf UI) |
| `http://localhost:8080/employees` | Employee list (Thymeleaf UI) |
| `http://localhost:8080/swagger-ui.html` | Swagger UI |
| `http://localhost:8080/api-docs` | Raw OpenAPI JSON |

---

## REST API Endpoints

### Auth API — Public
| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register a new user |
| POST | `/auth/login` | Login and get JWT token |

### Employee API — Protected (JWT required)
| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Add new employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |
| GET | `/api/employees/search?keyword=` | Search by name or department |

---

## Swagger / SpringDoc OpenAPI

### Dependency Used
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.6.0</version>
</dependency>
```

> SpringDoc OpenAPI is used instead of Springfox because Springfox does not support
> Spring Boot 3.x (Jakarta EE). SpringDoc is the actively maintained standard for Spring Boot 3+.

### What's Configured

**SwaggerConfig.java** sets up:
- API title, version, description
- Bearer Auth security scheme — adds the `Authorize 🔒` button in Swagger UI

```java
new OpenAPI()
    .info(new Info().title("Employee Management System API").version("1.0.0"))
    .addSecurityItem(new SecurityRequirement().addList("Bearer Auth"))
    .components(new Components()
        .addSecuritySchemes("Bearer Auth", new SecurityScheme()
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")));
```

**Annotations used in controllers:**
- `@Tag` — groups endpoints under a named section in Swagger UI
- `@Operation` — describes what each endpoint does

**SecurityConfig.java** whitelists Swagger URLs so they are accessible without a JWT token:
```java
.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/api-docs/**", "/swagger-ui.html").permitAll()
```

### How to Test APIs in Swagger UI
1. Open `http://localhost:8080/swagger-ui.html`
2. Use `POST /auth/register` to create a user
3. Use `POST /auth/login` — copy the `token` from the response
4. Click **Authorize 🔒** (top right) → paste the token
5. All `/api/employees/**` endpoints are now accessible

---

## JWT Security

### How It Works

```
Client                          Server
  |                               |
  |-- POST /auth/login ---------> |
  |   { username, password }      | 1. Validate credentials
  |                               | 2. Generate JWT token
  |<-- { token: "eyJ..." } ------ |
  |                               |
  |-- GET /api/employees -------> |
  |   Authorization: Bearer eyJ..| 3. JwtFilter extracts token
  |                               | 4. TokenProvider validates it
  |                               | 5. Sets auth in SecurityContext
  |<-- 200 OK [ {...} ] --------- |
```

### Key Components

**TokenProvider (interface)**
Abstraction for token operations — generate, extract, validate.

**JwtUtil (implements TokenProvider)**
Concrete implementation using JJWT library with HS256 signing.

**JwtFilter (OncePerRequestFilter)**
Intercepts every request, reads `Authorization: Bearer <token>` header,
validates the token and sets the authenticated user in `SecurityContextHolder`.

**SecurityConfig**
- Disables CSRF (stateless REST API)
- Stateless session management
- Permits: `/auth/**`, `/swagger-ui/**`, `/api-docs/**`, `/css/**`, `/js/**`, `/`, `/employees/**`
- Protects: everything else (i.e. `/api/employees/**`) requires a valid JWT

---

## SOLID Principles Implementation

### S — Single Responsibility Principle
> A class should have only one reason to change.

**Problem:** `EmployeeServiceImpl` was handling both CRUD operations and statistics/reporting in one class. `AuthController` was directly encoding passwords, saving users, and generating tokens — three jobs in one controller.

**Solution:**

- Split `EmployeeServiceImpl` — CRUD stays in `EmployeeServiceImpl`, stats moved to `EmployeeStatsServiceImpl`
- Extracted `AuthServiceImpl` — all auth business logic (password encoding, saving user, token generation) moved out of `AuthController` into a dedicated service

```
Before                          After
──────────────────────          ──────────────────────────────
EmployeeServiceImpl             EmployeeServiceImpl   → CRUD only
  CRUD + Stats mixed            EmployeeStatsServiceImpl → Stats only

AuthController                  AuthController        → HTTP only
  HTTP + business logic         AuthServiceImpl       → business logic
```

**Files:** `EmployeeServiceImpl.java`, `EmployeeStatsServiceImpl.java`, `AuthController.java`, `AuthServiceImpl.java`

---

### O — Open/Closed Principle
> Open for extension, closed for modification.

**Problem:** `EmployeeServiceImpl.updateEmployee()` manually copied every field one by one using setters. Every time a new field was added to `Employee`, this method had to be modified.

```java
// Before — must modify this every time a new field is added
existing.setName(emp.getName());
existing.setEmail(emp.getEmail());
// ... 8 more lines
```

**Solution:** Added a `copyFrom(Employee other)` method directly on the `Employee` model. The model owns the responsibility of copying its own fields. `updateEmployee()` now just calls `existing.copyFrom(emp)` and is never touched again.

```java
// After — closed for modification
existing.copyFrom(emp);
```

Now adding a new field to `Employee` only requires updating `copyFrom()` inside `Employee` itself — `EmployeeServiceImpl` stays untouched.

**Files:** `Employee.java`, `EmployeeServiceImpl.java`

---

### L — Liskov Substitution Principle
> Subtypes must be substitutable for their base types.

**Status: Already followed ✅**

`EmployeeServiceImpl` correctly implements all methods of `EmployeeReadService` and `EmployeeWriteService` without breaking any contract. All controllers depend on interfaces — swapping the implementation would work transparently without any changes to the controllers.

---

### I — Interface Segregation Principle
> No client should be forced to depend on methods it does not use.

**Problem:** The original `EmployeeService` interface had 12 methods — CRUD, search, and stats all in one. `EmployeeController` (REST) was forced to depend on stats methods it never called. `getTotalEmployees()` was a stat method sitting inside a CRUD interface.

**Solution:** Split `EmployeeService` into three focused interfaces:

```
EmployeeReadService              EmployeeWriteService         EmployeeStatsService
  getAllEmployees()                 addEmployee()                getTotalEmployees()
  getEmployeeById()                updateEmployee()             getActiveCount()
  searchEmployees()                deleteEmployee()             getInactiveCount()
                                                                getOnLeaveCount()
                                                                getDepartmentStats()
```

Each controller now depends only on what it actually uses:

```
EmployeeController     → ReadService + WriteService
EmployeeWebController  → ReadService + WriteService + StatsService
HomeController         → ReadService + StatsService
```

**Files:** `EmployeeReadService.java`, `EmployeeWriteService.java`, `EmployeeStatsService.java`, `EmployeeController.java`, `EmployeeWebController.java`, `HomeController.java`

---

### D — Dependency Inversion Principle
> High-level modules should not depend on low-level modules. Both should depend on abstractions.

**Problem:** `AuthServiceImpl` and `JwtFilter` both directly injected `JwtUtil` — a concrete class. High-level modules (business logic, security filter) were tightly coupled to a low-level implementation detail (JJWT library).

```java
// Before — coupled to concrete implementation
@Autowired
private JwtUtil jwtUtil;
```

**Solution:** Introduced `TokenProvider` interface. `JwtUtil` implements it. Both `AuthServiceImpl` and `JwtFilter` now depend on the `TokenProvider` abstraction.

```java
// After — depends on abstraction
@Autowired
private TokenProvider tokenProvider;
```

```
Before                          After
──────────────────────          ──────────────────────────────
AuthServiceImpl ──► JwtUtil     AuthServiceImpl ──► TokenProvider
JwtFilter       ──► JwtUtil     JwtFilter       ──► TokenProvider
                                                        ↑
                                                   JwtUtil (implements it)
```

If the JWT library is ever swapped, only `JwtUtil` changes — `AuthServiceImpl` and `JwtFilter` remain completely untouched.

**Files:** `TokenProvider.java`, `JwtUtil.java`, `AuthServiceImpl.java`, `JwtFilter.java`

---

## SOLID Summary

| Principle | Where Applied | Key Files |
|---|---|---|
| S — Single Responsibility | Split employee service + extracted auth logic | `EmployeeStatsServiceImpl`, `AuthServiceImpl` |
| O — Open/Closed | `copyFrom()` on Employee model | `Employee.java`, `EmployeeServiceImpl.java` |
| L — Liskov Substitution | Already followed — impl substitutable for interface | `EmployeeServiceImpl.java` |
| I — Interface Segregation | Split into Read, Write, Stats interfaces | `EmployeeReadService`, `EmployeeWriteService`, `EmployeeStatsService` |
| D — Dependency Inversion | `TokenProvider` abstraction over `JwtUtil` | `TokenProvider.java`, `JwtUtil.java` |

---

## Employee Model Fields

| Field | Type | Validation |
|---|---|---|
| id | int | Auto-generated |
| name | String | Not blank |
| email | String | Not blank, valid email, unique |
| phone | String | Not blank |
| department | String | Not blank |
| designation | String | Not blank |
| salary | double | Not null |
| joinDate | LocalDate | Not null |
| status | Enum | ACTIVE / INACTIVE / ON_LEAVE |
| gender | Enum | MALE / FEMALE / OTHER |
| address | String | Optional |

---

## UI Features (Thymeleaf)

- Dashboard with stats cards — total, active, on leave, inactive counts
- Department-wise employee distribution with progress bars
- Employee list with search by name or department
- Add / Edit / View / Delete employee
- Quick **Mark On Leave** and **Mark Active** buttons on edit form — no need to go through the full status dropdown

---
