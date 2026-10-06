# 832402219 Calculator System — Backend (calculator_backend)

Backend part of the separated front-end/back-end calculator assignment. It provides a RESTful API for expression evaluation and history management, with results persisted to a SQLite database.

Frontend repository: [832402219_calculator_frontend](https://github.com/Tong-10905/832402219_calculator_frontend)

## Basic Information

- **Student ID**: 832402219
- **GitHub Username**: Tong-10905
- **Backend Repository**: https://github.com/Tong-10905/832402219_calculator_backend
- **Backend Code Style**: https://github.com/Tong-10905/832402219_calculator_backend/blob/main/codestyle.md

## Tech Stack

| Component | Choice | Notes |
|---|---|---|
| Language | Java 17 | |
| Framework | Spring Boot 3.3.4 | Embedded Tomcat, one-click startup |
| Web Layer | spring-boot-starter-web | Provides the REST API |
| Persistence | Spring Data JPA + Hibernate | Interface-based data access, no handwritten SQL |
| Database | SQLite (sqlite-jdbc 3.46.1.3) | Single-file `calculator.db`, zero installation, data survives restarts |
| Hibernate Dialect | hibernate-community-dialects 6.5.3 | Hibernate has no official SQLite dialect, so the community dialect is used |
| Expression Parsing | exp4j 0.4.8 | Builds an expression tree for evaluation; user input is never executed as code |

## Features

- `POST /api/calculate`: Evaluates arithmetic expressions on the server, supporting `+ - * /`, parentheses, decimals and unary plus/minus, with correct operator precedence. Results are kept to 10 decimal places to avoid floating-point noise such as `0.1+0.2=0.30000000000000004`
- `GET /api/history`: Returns all history records in descending order by id
- `DELETE /api/history/{id}`: Deletes a specific history record
- Security design: the input is first validated against a character whitelist (digits, arithmetic operators, parentheses, decimal point and spaces only), then handed to exp4j to build an expression tree — no eval/exec-style code injection is possible; division by zero and invalid expressions uniformly return 400 with an error message

## Run Locally

Prerequisites: JDK 17+, Maven 3.6+ (or simply open the project in IntelliJ IDEA)

```bash
# Option 1: run with Maven
mvn spring-boot:run

# Option 2: package and run
mvn clean package
java -jar target/calculator-backend-1.0.0.jar
```

Alternatively, open the project in IDEA and run the `CalculatorApplication` main class. The service starts at `http://localhost:8080`. On first launch it automatically creates `calculator.db` in the project root and builds the tables.

## API Reference

Base URL: `http://localhost:8080/api`

### 1. Calculate — `POST /calculate`

Request body:

```json
{ "expression": "(1+2)*3" }
```

Success (200):

```json
{ "success": true, "expression": "(1+2)*3", "result": 9, "message": null }
```

Failure (400 — invalid expression / division by zero / expression too long or empty):

```json
{ "success": false, "expression": "1++", "result": null, "message": "Invalid expression" }
```

curl example:

```bash
curl -X POST http://localhost:8080/api/calculate \
  -H "Content-Type: application/json" \
  -d "{\"expression\": \"(1+2)*3\"}"
```

### 2. History — `GET /history`

Response (200, descending by id):

```json
[
  { "id": 2, "expression": "10/4", "result": "2.5", "time": "2026-10-06 02:20:11" },
  { "id": 1, "expression": "(1+2)*3", "result": "9", "time": "2026-10-06 02:19:58" }
]
```

### 3. Delete History — `DELETE /history/{id}`

Returns 204 on success, 404 if the record does not exist.

```bash
curl -X DELETE http://localhost:8080/api/history/1
```

> CORS note: the controller opens cross-origin access via `@CrossOrigin(origins = "*")`, so both the local page and the publicly deployed frontend can call the API directly.

## Project Structure

```
calculator_backend
├── pom.xml
└── src/main
    ├── java/com/fzu/calculator
    │   ├── CalculatorApplication.java            # Entry point
    │   ├── controller/CalculatorController.java  # REST controller layer
    │   ├── service/CalculatorService.java        # Calculation logic (whitelist + exp4j)
    │   ├── repository/HistoryRepository.java     # JPA data access interface
    │   └── model
    │       ├── CalculateRequest.java             # Calculation request body
    │       ├── CalculateResponse.java            # Unified calculation response
    │       ├── HistoryEntry.java                 # History DTO
    │       └── CalculationHistory.java           # History entity (calculation_history table)
    └── resources/application.properties          # Port 8080, SQLite datasource config
```

## Frontend Contract

The frontend always calls `/api/calculate`, `/api/history` and `/api/history/{id}`; request/response fields follow the examples above. Any interface change must be synchronized with the frontend.
