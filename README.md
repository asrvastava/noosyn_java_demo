# Onboarding Demo Application

This is a Spring Boot based demo application for user onboarding, authentication, and role management. It uses JWT for security and an H2 in-memory database for data storage.

## Technologies Used

- **Java:** 17
- **Framework:** Spring Boot 3.2.1
- **Build Tool:** Maven
- **Database:** H2 (In-memory)
- **Security:** Spring Security, JWT (JSON Web Tokens)
- **Other Libraries:** Lombok, Spring Data JPA

## Prerequisites

- Java Development Kit (JDK) 17 or higher
- Maven 3.6 or higher

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone <repository-url>
    cd noosyn_java_demo
    ```

2.  **Build the project:**
    ```bash
    mvn clean install
    ```

3.  **Run the application:**
    ```bash
    mvn spring-boot:run
    ```
    Or run the generated JAR file:
    ```bash
    java -jar target/demo-0.0.1-SNAPSHOT.jar
    ```

The application will start on port **3000**.

## Configuration

The application configuration is located in `src/main/resources/application.yml`.

- **Server Port:** 3000
- **Database URL:** `jdbc:h2:mem:testdb` (Default H2 URL if not specified, though `application.yml` has empty url, Spring Boot defaults usually apply or it might need fixing if it fails. *Note: The provided `application.yml` has an empty `url`, assuming standard H2 behavior or environment variable injection.*)
- **H2 Console:** Enabled (usually at `/h2-console` if configured, check `application.yml` or add `spring.h2.console.enabled=true` if needed).
- **JWT Secret:** Configured in `application.yml`.
- **JWT Expiration:** 10 hours.

## API Documentation

### Authentication

The application uses JWT for authentication. Protected endpoints require the `Authorization` header with a Bearer token.

**Header Format:**
```
Authorization: Bearer <your_jwt_token>
```

### Endpoints

#### 1. User Signup
Registers a new user.

- **URL:** `/signup`
- **Method:** `POST`
- **Body:**
  ```json
  {
    "username": "user1",
    "password": "password123",
    "email": "user1@example.com"
  }
  ```
- **Response:** String message ("User signed up successfully")

#### 2. User Signin (Login)
Authenticates a user and returns a JWT token.

- **URL:** `/signin`
- **Method:** `POST`
- **Body:**
  ```json
  {
    "username": "user1",
    "password": "password123"
  }
  ```
- **Response:** JWT Token String

#### 3. Create Role
Creates a new role and assigns it to a user. **(Requires Authentication)**

- **URL:** `/role_create`
- **Method:** `POST`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "roleName": "ADMIN",
    "username": "user1"
  }
  ```
- **Response:** String message ("Role created successfully")

#### 4. Get User Role
Retrieves the role of a specific user. **(Requires Authentication)**

- **URL:** `/role`
- **Method:** `GET`
- **Headers:** `Authorization: Bearer <token>`
- **Body:**
  ```json
  {
    "username": "user1"
  }
  ```
- **Response:** Role name (e.g., "ADMIN")

## Database Access

Since the application uses H2 in-memory database, the data is lost when the application stops.
You can access the H2 console (if enabled) at `http://localhost:3000/h2-console`.
- **JDBC URL:** `jdbc:h2:mem:testdb` (or as configured)
- **User:** `sa`
- **Password:** `password`

## Project Structure

```
src/main/java/com/example/demo/
├── config/          # Security configuration
├── controller/      # REST Controllers
├── dto/             # Data Transfer Objects
├── exception/       # Global Exception Handling
├── model/           # JPA Entities
├── repository/      # JPA Repositories
├── service/         # Business Logic
└── util/            # Utility classes (JWT, Constants)
```
