# Mini Online Shop Demo

A minimal Spring Boot application for testing purposes.

## Requirements
- Java 21
- Maven

## Building and Running

1. **Build the project:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **Run Tests:**
   ```bash
   mvn test
   ```

## API Endpoints

- **GET /health**
  - Returns `{"status": "UP"}`
- **GET /products**
  - Returns list of products.
- **GET /products/{id}**
  - Returns product details.
- **POST /orders**
  - Creates a new order.
  - Body: List of product IDs (e.g., `["1", "2"]`)
