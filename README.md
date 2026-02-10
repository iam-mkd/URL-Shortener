# URL Shortener - Spring Boot

This repository contains a basic Spring Boot project scaffold for a future URL Shortener service.

## Tech stack

- Java 17
- Spring Boot (web starter)
- Maven

## Running the application

From the project root:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`.

Test the health endpoint:

```bash
curl http://localhost:8080/api/health
```

You should see:

```text
URL Shortener Spring Boot app is running.
```

## Running tests

```bash
mvn test
```

