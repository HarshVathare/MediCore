# 🏥 Medicore - Hospital Management System - Backend API

This project is a **Spring Boot-based Hospital Management System** that provides REST APIs for managing patients, doctors, appointments, medical records, and authentication.

---

# 🚀 Tech Stack

* Java 17+
* Spring Boot
* Spring Security (JWT)
* Hibernate / JPA
* MySQL
* Swagger (OpenAPI)

---

# 🔐 Authentication APIs

## Auth Controller

| Method | Endpoint             | Description                                |
| ------ | -------------------- | ------------------------------------------ |
| POST   | `/api/auth/register` | Register a new user (Patient/Doctor/Admin) |
| POST   | `/api/auth/login`    | Login and get JWT token                    |

---

# 🧑‍⚕️ Patient APIs

## Patient Controller

| Method | Endpoint                          | Description                   |
| ------ | --------------------------------- | ----------------------------- |
| GET    | `/api/patients/profile`           | Get logged-in patient profile |
| PUT    | `/api/patients/profile`           | Update patient profile        |
| GET    | `/api/patients/appointments`      | Get all patient appointments  |
| POST   | `/api/patients/appointments`      | Book a new appointment        |
| DELETE | `/api/patients/appointments/{id}` | Cancel appointment            |
| GET    | `/api/patients/docters`           | Get all doctors               |
| GET    | `/api/patients/docters/{id}`      | Get doctor details            |

---

# 👨‍⚕️ Doctor APIs

## Doctor Controller

| Method | Endpoint                                      | Description                       |
| ------ | --------------------------------------------- | --------------------------------- |
| GET    | `/api/docters/profile`                        | Get logged-in doctor profile      |
| PUT    | `/api/docters/profile`                        | Update doctor profile             |
| GET    | `/api/docters/appointments`                   | Get doctor appointments           |
| PUT    | `/api/docters/appointments/{id}/status`       | Update appointment status         |
| POST   | `/api/docters/{appointmentId}/medical-record` | Create medical record for patient |

---

# 🛠️ Admin APIs

## Admin Controller

| Method | Endpoint                 | Description     |
| ------ | ------------------------ | --------------- |
| POST   | `/api/admin/docter`      | Add new doctor  |
| GET    | `/api/admin/users`       | Get all users   |
| GET    | `/api/admin/docters`     | Get all doctors |
| DELETE | `/api/admin/user/{id}`   | Delete user     |
| DELETE | `/api/admin/docter/{id}` | Delete doctor   |

---

# 📄 Features

* 🔐 JWT-based Authentication & Authorization
* 👨‍⚕️ Doctor-Patient Appointment System
* 📝 Medical Records & Prescription Management
* 👤 Role-based Access (Admin, Doctor, Patient)
* 📊 Clean REST API Design

---

# 📌 API Base URL

```bash
http://localhost:8080/api
```

---

# 📘 Swagger UI

Access API documentation:

```bash
http://localhost:8080/swagger-ui/index.html
```

---

# ⚙️ How to Run

```bash
# Clone the repository
git clone https://github.com/your-username/hospital-management.git

# Navigate to project
cd hospital-management

# Run the application
mvn spring-boot:run
```

---

# 🧠 Future Improvements

* Payment Integration 💳
* Email Notifications 📧
* Doctor Availability Scheduling ⏰
* Frontend Integration (React / JS)

---

# 👨‍💻 Author

**Harshvardhan Vathare**

---

# ⭐ If you like this project, give it a star!
