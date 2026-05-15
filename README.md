 # MediCore

A full-stack Hospital Management System designed to handle real-world healthcare workflows such as appointment booking, doctor management, patient records, authentication, and role-based access control.

## Live Demo

Frontend: [https://medicore-hms-six.vercel.app](https://medicore-hms-six.vercel.app)
Backend: [https://medicore-p9x7.onrender.com](https://medicore-p9x7.onrender.com)
Swagger Docs: [https://medicore-p9x7.onrender.com/swagger-ui/index.html](https://medicore-p9x7.onrender.com/swagger-ui/index.html)

---

## About The Project

MediCore started as a backend-focused project to solve common hospital management problems using scalable REST APIs and secure authentication.

The system supports three major roles:

* Patient
* Doctor
* Admin

Each role has dedicated APIs and access control using JWT authentication and Spring Security.

The goal of this project was not only to build APIs, but also to simulate how a real hospital platform handles:

* Appointment scheduling
* Medical records
* Doctor workflows
* Authentication and authorization
* Email-based password recovery
* Dashboard and user management

The project is fully deployed and integrated with a frontend application.

---

## Features

### Authentication & Security

* JWT-based authentication
* Role-based authorization
* Access & refresh token flow
* Forgot password functionality
* Reset password via email
* Secure password encryption using Spring Security

### Patient Module

* Register and login
* Book appointments
* Cancel appointments
* View appointment history
* Access medical records
* Update profile and password

### Doctor Module

* Manage doctor profile
* View assigned appointments
* Update appointment status
* Create medical records for patients
* Search doctors

### Admin Module

* Add and manage doctors
* Manage users
* View system dashboard
* Remove doctors and users

### Additional Features

* RabbitMQ-based email workflow
* Swagger/OpenAPI integration
* Layered backend architecture
* DTO-based API design
* Global exception handling

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* Spring Data JPA
* Hibernate
* JWT Authentication
* RabbitMQ
* MySQL

### Deployment & Tools

* Render (Backend Hosting)
* Vercel (Frontend Hosting)
* Swagger / OpenAPI
* Maven
* Git & GitHub

---

## Architecture

The backend follows a layered architecture:

Controller → Service → Repository → Database

Additional integrations:

* JWT Security Layer
* RabbitMQ Queue Processing
* SMTP Email Service

---

## API Modules

### Auth APIs

* Login
* Register
* Refresh Token
* Forgot Password
* Reset Password
* Logout
* Email Verification

### Patient APIs

* Manage profile
* Book appointments
* Cancel appointments
* View reports
* Access medical records

### Doctor APIs

* Manage appointments
* Update appointment status
* Create medical records
* Search patients/doctors

### Admin APIs

* Add doctors
* Remove doctors
* Manage users
* Dashboard analytics

---

## What I Learned From This Project

While building MediCore, I improved my understanding of:

* Secure backend development using Spring Security
* JWT authentication workflows
* REST API design principles
* Queue-based asynchronous processing with RabbitMQ
* Database relationship handling with JPA/Hibernate
* Production deployment challenges
* SMTP integration and cloud deployment debugging

This project also helped me understand how production systems handle scalability, modularity, and secure API communication.

---

## Future Improvements

* Docker containerization
* Kubernetes deployment
* Real-time notifications using WebSockets
* Payment integration
* Video consultation support
* AI-based health recommendations

---

## Developer

Harsh Vathare
Java Backend Developer

Focused on building scalable backend systems and real-world applications using Java and Spring Boot.

---

## Feedback

If you have suggestions, feedback, or improvements, feel free to open an issue or connect with me.
