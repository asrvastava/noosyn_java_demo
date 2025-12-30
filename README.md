# Noosyn Java Demo Application

This is a **Spring Boot 3.2** based application designed to demonstrate robust authentication, role-based access control (RBAC), and modern API development practices. It uses JWT for security, H2 for in-memory data storage, and includes features for observability and documentation.

## 🚀 Key Features

*   **Authentication & Security**: Secure user signup and signin using **JWT (JSON Web Tokens)** and **Spring Security**.
*   **Role-Based Access Control (RBAC)**: Fine-grained permission management with `ADMIN` and `USER` roles.
*   **Product Management**: CRUD operations for products, securing endpoints based on roles (Admins can create/update/delete, Users can view).
*   **API Documentation**: Integrated **OpenAPI (Swagger UI)** for interactive API exploration and testing.
*   **Observability**: **Spring Boot Actuator** enabled for health checks (`/actuator/health`) and metrics.
*   **Centralized Configuration**:
    *   Magic strings and error codes centralized in `AppConstants`.
    *   Externalized JWT configuration via `JwtConfig`.
    *   Global Exception Handling for consistent generic error responses.
*   **Data Validation**: Comprehensive input validation using `@Valid` and custom error messages from `messages.properties`.

## 🛠️ Technologies Used

-   **Java:** 21
-   **Framework:** Spring Boot 3.2.1
-   **Build Tool:** Maven
-   **Database:** H2 (In-memory)
-   **Security:** Spring Security, JJWT (Basic support for JSON Web Token)
-   **Documentation:** SpringDoc OpenAPI (Swagger UI)
-   **Utilities:** Lombok, Spring Boot Actuator

## 📋 Prerequisites

-   Java Development Kit (JDK) 21
-   Maven 3.6 or higher

## ⚙️ Setup and Installation

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
    The application will start on port **3000**.

## 🖥️ API Documentation

Once the application is running, you can access the interactive Swagger UI documentation at:

👉 **[http://localhost:3000/swagger-ui/swagger-ui.html](http://localhost:3000/swagger-ui/swagger-ui.html)**

This interface allows you to seeing all available endpoints, their request/response schemas, and even execute requests directly from the browser (Authentication is supported).

## 🧪 Demo Showcase

A shell script `demo_showcase.sh` is provided to demonstrate the full workflow of the application, including:
1.  Admin Signup & Login
2.  Role Assignment
3.  Product Creation (Admin)
4.  Product Retrieval (User)
5.  Access Control Tests (User fetching specific product, User trying to create product)

**To run the demo:**
```bash
chmod +x demo_showcase.sh
./demo_showcase.sh
```

## 🏗️ Project Structure

```
src/main/java/com/example/demo/
├── config/          # Configuration (Security, OpenAPI, JWT, Actuator)
├── controller/      # REST Controllers (Auth, Product)
├── dto/             # Data Transfer Objects (Request/Response records)
├── exception/       # Global Exception Handling & Custom Exceptions
├── model/           # JPA Entities (User, Role, Product)
├── repository/      # JPA Repositories
├── service/         # Business Logic (User, Role, Product, Auth)
└── util/            # Utilities (JwtUtil, AppConstants)
```

## 🔒 Configuration Details

The application configuration is located in `src/main/resources/application.yml`.

-   **Server Port:** 3000
-   **Database URL:** `jdbc:h2:mem:testdb`
-   **H2 Console:** `/h2-console`
-   **JWT Config:** Secrets and expiration are configurable properties.
-   **Actuator:** Exposed at `/actuator`.

## 🧪 Testing

The project includes unit tests for controllers, services, and utilities.

Run tests with:
```bash
mvn test
```
