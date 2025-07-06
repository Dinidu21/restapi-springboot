# Order Management REST API

[![API Docs](https://img.shields.io/badge/Postman-Docs-blue?logo=postman)](https://documenter.getpostman.com/view/28172939/2sB34bM4AW)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-28.3-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
[![Build Status](https://img.shields.io/github/actions/workflow/status/yourname/restapi/build.yml)](https://github.com/Dinidu21/restapi/actions)

## 📖 Overview

The **Order Management REST API** is a production-grade, Spring Boot-based backend designed for e-commerce and order management systems. Built with **Java 17**, **Spring Boot 3.2.5**, and **PostgreSQL 16**, it provides robust, scalable endpoints for managing users, products, orders, and order items. The API emphasizes clean code, data integrity, and performance, making it an ideal foundation for enterprise-grade applications or a learning resource for RESTful API development.

This project showcases my expertise in designing and implementing secure, maintainable, and scalable REST APIs, with a focus on best practices in software engineering and modern deployment practices using Docker.

## ✨ Key Features

- **User Management**: CRUD operations for users with unique usernames and emails, including audit tracking.
- **Product Management**: Inventory management with validation for product details (name, description, price, stock).
- **Order Processing**: Automated order creation with atomic stock deduction and subtotal calculations.
- **Standardized API Responses**: Consistent `ApiResponse<T>` structure for all endpoints.
- **DTOs & Validation**: Use of Data Transfer Objects with `@Valid` and field-level constraints.
- **Transactional Integrity**: `@Transactional` for atomic operations.
- **Enums & Constraints**: Enums for statuses, validated in Java and SQL.
- **Read-Only Queries**: Optimized with `@Transactional(readOnly = true)`.
- **Error Handling**: Custom exceptions with appropriate HTTP status codes.
- **Logging**: Comprehensive SLF4J logging.
- **SQL-Level Integrity**: Foreign keys, unique constraints, and `CHECK` constraints.
- **Scalable Pagination**: Spring Data `Pageable` for efficient list endpoints.
- **Containerization**: Dockerized application with Docker Compose for easy deployment.

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Data JPA
- **Database**: PostgreSQL 16
- **Validation**: Jakarta Bean Validation
- **Logging**: SLF4J with Logback
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose
- **Testing**: Postman

## 🚀 Getting Started

### Prerequisites

- **Java 17**: [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven**: [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL 16**: [Download](https://www.postgresql.org/download/) (optional if using Docker)
- **Docker**: [Download](https://www.docker.com/get-started)
- **Postman**: [Download](https://www.postman.com/downloads/)

### Installation (Without Docker)

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Dinidu21/restapi.git
   cd restapi
   ```

2. **Configure PostgreSQL**:
    - Create a database:
      ```sql
      CREATE DATABASE restapi_ordermgt;
      ```
    - Update `src/main/resources/application.properties`:
      ```properties
      spring.datasource.url=jdbc:postgresql://localhost:5432/restapi_ordermgt
      spring.datasource.username=postgres
      spring.datasource.password=your-password
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
      ```

3. **Run the Application**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The API will be accessible at `http://localhost:8080/api/v1/`.

4. **Initialize Database**:
    - Execute the provided SQL script (`init.sql`) to set up tables and seed data.

### Installation (With Docker)

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Dinidu21/restapi.git
   cd restapi
   ```

2. **Build and Run with Docker Compose**:
    - Ensure Docker and Docker Compose are installed.
    - Build the application JAR:
      ```bash
      mvn clean install
      ```
    - Run the application and database:
      ```bash
      docker-compose up --build
      ```
    - The API will be available at `http://localhost:8080/api/v1/`, and the database will be accessible on `localhost:5432`.

3. **Stop the Containers**:
   ```bash
   docker-compose down -v
   ```
+  The `-v` flag removes the database volume to ensure a clean state on restart.

### API Usage Example

#### Create a User
- **Endpoint**: `POST users`
- **Request**:
  ```json
  {
      "username": "dinidu",
      "email": "dinidu@example.com",
      "fullName": "Dinidu Sachintha",
      "status": "ACTIVE"
  }
  ```
- **Response** (201 Created):
  ```json
  {
      "success": true,
      "message": "User created successfully",
      "data": {
          "id": 1,
          "username": "dinidu",
          "email": "dinidu@example.com",
          "fullName": "Dinidu Sachintha",
          "status": "ACTIVE",
          "createdAt": "2025-07-06T17:04:00.123456",
          "updatedAt": "2025-07-06T17:04:00.123456"
      },
      "metadata": {}
  }
  ```

- **Postman Collection**: Test all endpoints using the [Postman Collection](https://documenter.getpostman.com/view/28172939/2sB34bM4AW).

### Running Tests
- **API Tests**: Use the Postman collection for manual or automated testing.

## 🏗️ Project Structure

```
restapi/
├── src/
│   ├── main/
│   │   ├── java/com/dinidu/restapi/
│   │   │   ├── controllers/      # REST controllers
│   │   │   ├── dtos/            # Data Transfer Objects
│   │   │   ├── models/          # JPA entities
│   │   │   ├── services/        # Business logic
│   │   │   └── repositories/    # JPA repositories
│   │   └── resources/
│   │       └── application.properties
├── Dockerfile                  # Docker configuration
├── docker-compose.yml         # Docker Compose configuration
├── init.sql                   # Database initialization script
├── pom.xml
└── README.md
```

## 🌟 Why This Project?

This project demonstrates my ability to build a production-grade REST API with:
- **Scalability**: Pagination, optimized queries, and Docker support for easy deployment.
- **Maintainability**: Clean code, DTOs, logging, and comprehensive documentation.
- **Reliability**: Transactional integrity, SQL constraints, and robust error handling.
- **Security**: Input validation and custom exceptions to mitigate vulnerabilities.
- **DevOps Readiness**: Containerized with Docker and orchestrated with Docker Compose for production environments.

## 🤝 Contributing

Contributions are welcome! To contribute:
1. Fork the repository.
2. Create a feature branch (`git checkout -b feature/your-feature`).
3. Commit changes (`git commit -m "Add your feature"`).
4. Push to the branch (`git push origin feature/your-feature`).
5. Open a pull request with a detailed description.

See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for our code of conduct.

## 📜 License

This project is licensed under the MIT License. See [LICENSE](LICENSE) for details.

## 📬 Contact

- **Email**: dinidusachintha3@gmail.com
- **GitHub**: [Dinidu21](https://github.com/Dinidu21)
- **Issues**: Open an issue on this repository.

## 🙌 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot)
- [PostgreSQL](https://www.postgresql.org/)
- [Docker](https://www.docker.com/)
- [Postman](https://www.postman.com/)
- Open-source community for inspiration and resources.

---

⭐ **Star this repository** if you find it valuable! Feedback and contributions are greatly appreciated.