Authentication & Authorization System

A backend authentication and authorization system built using Spring Boot and Spring Security, implementing email-based OTP verification, JWT authentication, refresh tokens, and role-based access control.

This project focuses on correct backend security design and authentication flows commonly used in real-world applications.

Key Features

User registration with email OTP verification

Account activation only after successful OTP validation

Secure login using Spring Security authentication flow

Stateless authentication using JWT access tokens

Refresh token mechanism with expiry and rotation

Role-based authorization (ADMIN / USER)

Secure password hashing using BCrypt

Tech Stack

Java

Spring Boot

Spring Security

JWT

MySQL

JPA / Hibernate

High-Level Architecture
Client (Postman / Frontend)
        |
        v
+--------------------------+
|   Auth Controller        |
|  (Register / Login)      |
+--------------------------+
        |
        v
+--------------------------+
|   Auth Service           |
|  - OTP Verification      |
|  - Login                 |
|  - Token Handling        |
+--------------------------+
        |
        v
+--------------------------+
|   Spring Security        |
|  - JWT Filter            |
|  - AuthenticationManager |
|  - Role Authorization    |
+--------------------------+
        |
        v
+--------------------------+
|   Database (MySQL)       |
|  - Users                 |
|  - Roles                 |
|  - OTP Records           |
|  - Refresh Tokens        |
+--------------------------+


Why this matters:
This shows a clear separation between controllers, security, services, and persistence, which interviewers look for.

Authentication Flow (Step-by-Step)
1. User Registration

User registers with username, email, and password

Password is hashed using BCrypt

User account is created with enabled = false

OTP is generated and stored with an expiry time

2. OTP Verification

User submits OTP

OTP validity and expiry are checked

User account is activated (enabled = true)

OTP is marked as verified

3. Login

Authentication handled via AuthenticationManager

Spring Security validates:

Username

Password

Account enabled status

On successful login:

JWT access token is issued

Refresh token is generated and stored

4. Access Token Usage

Access token is sent in the Authorization header

JWT filter validates token on each request

Security context is set for authorized requests

5. Refresh Token Flow

Refresh token is used only to generate a new access token

Refresh tokens are validated from the database

Refresh token rotation is implemented to prevent reuse

Old refresh tokens are revoked after use

Token Strategy
Token	Purpose	Storage	Lifetime
Access Token	API authorization	Client	Short-lived
Refresh Token	Renew access token	Database	Long-lived
Security Design Decisions

Passwords are securely hashed using BCrypt

Access tokens are short-lived to limit exposure

Refresh tokens are persisted and validated server-side

Refresh token rotation prevents replay attacks

Refresh tokens are never used for API authorization

Role-based access is enforced using Spring Security annotations

API Endpoints Overview
Endpoint	Description
/api/auth/register	Register new user
/api/auth/verify-otp	Verify email OTP
/api/auth/login	User login
/api/auth/refresh-token	Generate new access token
Protected APIs	Require valid access token
How to Run the Project

Clone the repository

git clone https://github.com/your-username/authentication-system.git


Configure database details in application.properties

Run the application

mvn spring-boot:run


Test APIs using Postman or similar tools

Project Intent

This project was built to:

Understand Spring Security internals

Implement authentication without bypassing security layers

Practice token lifecycle management

Simulate real-world backend authentication scenarios

Scope & Notes

Backend-only project

No frontend included

Email delivery is simulated/logged for development

Focus is on correctness and security rather than UI
