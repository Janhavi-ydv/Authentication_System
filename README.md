# Authentication & Authorization System

A backend authentication and authorization system built using **Spring Boot** and **Spring Security**, implementing email-based OTP verification, JWT-based authentication, refresh tokens, and role-based access control.

This project focuses on **correct backend security design and real-world authentication flows**, without relying on frontend frameworks or shortcuts.

---

## Features

* User registration with email OTP verification
* Account activation only after OTP verification
* Secure login using Spring Security authentication flow
* Stateless authentication using JWT access tokens
* Refresh token mechanism with expiry and rotation
* Role-based authorization (ADMIN / USER)
* Secure password hashing using BCrypt

---

## Tech Stack

* **Java**
* **Spring Boot**
* **Spring Security**
* **JWT**
* **MySQL**
* **JPA / Hibernate**

---

## High-Level Architecture

```
Client (Postman / Frontend)
        |
        v
+---------------------------+
|  Auth Controller          |
|  (Register / Login)       |
+---------------------------+
        |
        v
+---------------------------+
|  Auth Service Layer       |
|  - OTP Verification       |
|  - Login                  |
|  - Token Handling         |
+---------------------------+
        |
        v
+---------------------------+
|  Spring Security          |
|  - JWT Filter             |
|  - AuthenticationManager  |
|  - Role Authorization     |
+---------------------------+
        |
        v
+---------------------------+
|  Database (MySQL)         |
|  - Users                  |
|  - Roles                  |
|  - OTP Records            |
|  - Refresh Tokens         |
+---------------------------+
```

---

## Authentication Flow

### 1. User Registration

* User registers with username, email, and password
* Password is hashed using BCrypt
* User account is created with `enabled = false`
* OTP is generated and stored with an expiry time

---

### 2. OTP Verification

* User submits the OTP
* OTP validity and expiry are verified
* User account is activated (`enabled = true`)
* OTP is marked as verified and cannot be reused

---

### 3. Login

* Authentication is handled by Spring Security
* Username, password, and account status are validated
* On successful login:

  * JWT access token is issued
  * Refresh token is generated and stored

---

### 4. Access Token Usage

* Access token is sent in the `Authorization` header
* JWT filter validates token on each request
* Security context is populated for authorized users

---

### 5. Refresh Token Flow

* Refresh token is used only to generate a new access token
* Refresh tokens are validated from the database
* Expired or revoked refresh tokens are rejected
* Refresh token rotation is implemented to prevent reuse

---

## Token Strategy

| Token Type    | Purpose            | Storage     | Lifetime    |
| ------------- | ------------------ | ----------- | ----------- |
| Access Token  | API authentication | Client-side | Short-lived |
| Refresh Token | Renew access token | Database    | Long-lived  |

---

## Security Design

* Passwords are never stored in plain text
* BCrypt is used for password hashing
* JWT access tokens are short-lived
* Refresh tokens are stored, validated, and revoked server-side
* Refresh tokens are never used for API authorization
* Role-based access is enforced using Spring Security annotations

---

## API Endpoints Overview

| Endpoint                  | Description                |
| ------------------------- | -------------------------- |
| `/api/auth/register`      | Register a new user        |
| `/api/auth/verify-otp`    | Verify email OTP           |
| `/api/auth/login`         | User login                 |
| `/api/auth/refresh-token` | Generate new access token  |
| Protected APIs            | Require valid access token |

---

## How to Run the Project

1. Clone the repository

```bash
git clone https://github.com/your-username/authentication-system.git
```

2. Configure database credentials in `application.properties`

3. Run the application

```bash
mvn spring-boot:run
```

4. Test the APIs using Postman or similar tools

---

## Project Scope

* Backend-only project
* No frontend included
* Email delivery is simulated/logged for development
* Focus is on authentication and security fundamentals

---

## Project Purpose

This project was built to:

* Understand Spring Security internals
* Implement authentication without bypassing security layers
* Learn JWT and refresh token lifecycle management
* Practice real-world backend authentication design

---

## Notes

This project is intended as a learning and demonstration project for backend authentication concepts and does not include frontend integration.
