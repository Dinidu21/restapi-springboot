# Order Management REST API

[![API Docs](https://img.shields.io/badge/Postman-Docs-blue?logo=postman)](https://documenter.getpostman.com/view/28172939/2sB34bM4AW)
![Java](https://img.shields.io/badge/Java-17-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)
[![Build Status](https://img.shields.io/github/actions/workflow/status/yourname/restapi/build.yml)]()

## 📖 Overview

The **Order Management REST API** is a Spring Boot-based application designed to manage users, products, orders, and order items for an e-commerce or similar system. It provides a robust, scalable backend with endpoints for CRUD operations, built with Java 17, Spring Boot, Spring Data JPA, and PostgreSQL. The API enforces validation, handles auditing (created/updated timestamps), and ensures data integrity with unique constraints and enum-based statuses.

This project is ideal for developers building e-commerce platforms, order management systems, or learning about RESTful API development with Spring Boot.

## ✨ Features

- **User Management**: Create, read, update, and delete users with unique usernames and emails.
- **Product Management**: Manage product inventory with details like name, description, price, and stock quantity.
- **Order Processing**: Create and manage orders with associated order items, including automatic order number generation and subtotal calculations.
- **Auditing**: Automatically tracks creation and update timestamps for users, products, and orders.
- **Validation**: Ensures data integrity with constraints (e.g., unique email/username, valid email format, positive prices).
- **API Documentation**: Interactive API documentation via Postman, available at [Postman Collection](https://documenter.getpostman.com/view/28172939/2sB34bM4AW).

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot 3.2.5, Spring Data JPA
- **Database**: PostgreSQL 16
- **Validation**: Jakarta Bean Validation
- **API Testing**: Postman
- **Logging**: SLF4J with Logback
- **Build Tool**: Maven

## 🚀 Getting Started

### Prerequisites

- **Java 17**: Ensure JDK 17 is installed. [Download](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
- **Maven**: Install Maven for dependency management. [Download](https://maven.apache.org/download.cgi)
- **PostgreSQL**: Install PostgreSQL 16. [Download](https://www.postgresql.org/download/)
- **Postman**: Install Postman for API testing. [Download](https://www.postman.com/downloads/)

### Installation

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/your-username/restapi.git
   cd restapi
   ```

2. **Set Up PostgreSQL**:
   - Create a database named `restapi_ordermgt`:
     ```sql
     CREATE DATABASE restapi_ordermgt;
     ```
   - Update the `application.properties` file in `src/main/resources` with your PostgreSQL credentials:
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/restapi_ordermgt
     spring.datasource.username=postgres
     spring.datasource.password=your-password
     spring.jpa.hibernate.ddl-auto=update
     spring.jpa.show-sql=true
     spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
     ```

3. **Run the Application**:
   - Build and run the project using Maven:
     ```bash
     mvn clean install
     mvn spring-boot:run
     ```
   - The API will be available at `http://localhost:8080`.

4. **Set Up Database Schema**:
   - Execute the provided SQL script to create tables and insert sample data:
     ```sql
     -- Connect to the database
     \c restapi_ordermgt

     -- Set the search path
     SET search_path TO public;

     -- Create tables
     CREATE TABLE public.users (
         id BIGSERIAL PRIMARY KEY,
         username VARCHAR(50) NOT NULL UNIQUE,
         email VARCHAR(255) NOT NULL UNIQUE,
         full_name VARCHAR(100) NOT NULL,
         status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
         created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
         updated_at TIMESTAMP,
         CONSTRAINT valid_status CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
     );

     -- [Include other table definitions from previous response]

     -- Insert sample data
     INSERT INTO public.users (username, email, full_name, status, created_at, updated_at)
     VALUES ('johndoe', 'john.doe@example.com', 'John Doe', 'ACTIVE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
     -- [Include other insert statements]
     ```

### API Usage

The API provides endpoints for managing users, products, orders, and order items. Below is an example of creating a user using the Postman collection.

#### Create a User
- **Endpoint**: `POST /api/v1/users`
- **Content-Type**: `application/json`
- **Request Body**:
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
      "id": 1,
      "username": "dinidu",
      "email": "dinidu@example.com",
      "fullName": "Dinidu Sachintha",
      "status": "ACTIVE",
      "createdAt": "2025-07-04T13:07:00.123456",
      "updatedAt": "2025-07-04T13:07:00.123456"
  }
  ```

#### Postman Collection
- Import the Postman collection for comprehensive API testing:
  [Order Management API Collection](https://documenter.getpostman.com/view/28172939/2sB34bM4AW)[](https://documenter.getpostman.com/view/9625258/SzS8tQrQ)
- Follow the instructions in the collection to test endpoints with sample requests and responses.

### Running Tests

- **Unit Tests**: Run unit tests using Maven:
  ```bash
  mvn test
  ```
- **API Tests**: Use the Postman collection to test endpoints manually or automate tests using Postman’s scripting features.

### Project Structure

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
│   │       └── application.properties  # Configuration
├── pom.xml                      # Maven dependencies
└── README.md                    # Project documentation
```

## 🤝 Contributing

We welcome contributions to enhance the Order Management REST API! Please follow these steps:

1. **Fork the Repository**: Create a fork of this repository.
2. **Create a Branch**: Use a descriptive branch name (e.g., `feature/add-authentication`).
3. **Make Changes**: Implement your feature or bug fix.
4. **Run Tests**: Ensure all tests pass (`mvn test`).
5. **Submit a Pull Request**: Provide a clear description of your changes and reference any related issues.

Please adhere to the [Code of Conduct](CODE_OF_CONDUCT.md) and follow the [Contribution Guidelines](CONTRIBUTING.md).

## 📜 License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

## 📬 Contact

For questions or feedback, reach out to:
- **Email**: dinidu@example.com
- **GitHub Issues**: Open an issue in this repository.

## 🙌 Acknowledgments

- [Spring Boot](https://spring.io/projects/spring-boot) for the robust framework.
- [PostgreSQL](https://www.postgresql.org/) for reliable database management.
- [Postman](https://www.postman.com/) for API testing and documentation.[](https://www.postman.com/api-documentation-tool/)
- Contributors and open-source community for inspiration and support.

---

⭐ **Star this repository** if you find it useful! Contributions and feedback are greatly appreciated.
