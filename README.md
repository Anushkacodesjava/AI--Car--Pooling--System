# 🚗 PoolAI — Smart Carpooling Platform

> An AI-powered carpooling system with JWT security, real-time Eco Impact tracking, SOS safety alerts, and automated scheduling.

---

## ✨ Key Features

| Feature | Details |
|---|---|
| 🔐 JWT Auth | BCrypt passwords, role-based access (DRIVER / RIDER / BOTH / ADMIN) |
| 🤖 AI Ride Matching | Levenshtein + word-overlap location scoring, time/date proximity, price & freshness bonuses |
| 🌿 Eco Impact Engine | CO₂ saved, fuel saved, money saved, tree equivalents, Eco Points (driver 60% / passengers 40%) |
| 🚨 SOS Alerts | Real-time distress alerts with GPS coordinates, admin resolution flow |
| 📅 Auto Scheduler | Cron job at 6AM auto-generates rides from recurring route schedules |
| ⏰ Auto Cancel | Hourly job cancels PENDING rides that passed 2+ hours without confirmation |
| 💳 Payments | UPI / Cash / Card with duplicate-payment guard |
| ⭐ Ratings | 1–5 star system with duplicate-rating guard, self-rating prevention, auto avg update |
| 🗺️ Live Maps | Leaflet + OpenStreetMap + OSRM road routing (no API key needed) |
| 🔔 Notifications | Per-user + broadcast with bulk markAllRead (single UPDATE query) |

---

## 🛠️ Tech Stack

**Backend:** Spring Boot 3.2 · Spring Security · Spring Data JPA · MySQL · JJWT · HikariCP

**Frontend:** Vanilla HTML/CSS/JS · Chart.js · Leaflet.js · Font Awesome · Plus Jakarta Sans + Syne

---

## 🚀 Quick Start

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+

### 1. Create Database
```sql
CREATE DATABASE my_database;
```

### 2. Configure (src/main/resources/application.properties)
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/my_database
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD
```

### 3. Run
```bash
mvn spring-boot:run
```

### 4. Open
```
http://localhost:8081/login.html
```

**Admin login:** `admin@poolai.com` / `admin123`

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/example/demo/
│   │   ├── config/          # Security, CORS, GlobalExceptionHandler
│   │   ├── controller/      # REST controllers (Auth, Ride, User, Admin...)
│   │   ├── dto/             # Request/Response DTOs + ApiResponse wrapper
│   │   ├── model/           # JPA Entities
│   │   ├── repository/      # Spring Data JPA repositories
│   │   ├── security/        # JwtUtil, JwtFilter
│   │   └── service/         # Business logic
│   └── resources/
│       ├── application.properties
│       └── static/          # Frontend (HTML, CSS, JS)
```

---

## 🔗 API Endpoints (selected)

| Method | Endpoint | Description |
|---|---|---|
| POST | `/auth/register` | Register new user |
| POST | `/auth/login` | Login, get JWT |
| GET | `/ride/all` | All rides |
| GET | `/ridematch/search` | AI-matched ride search |
| POST | `/participation/join` | Book a ride (atomic) |
| GET | `/impact/global` | Community eco stats |
| GET | `/admin/dashboard` | Platform health |
| GET | `/admin/ride-status` | Ride status breakdown |
| POST | `/notification/broadcast` | Send to all users |
| GET | `/sos/active` | Active SOS alerts |

---

## 🔒 Security Notes

- All write endpoints require `Authorization: Bearer <token>` header
- Passwords hashed with BCrypt (strength 10)
- JWT expiry: 24 hours
- DB credentials should be set via env vars `DB_URL`, `DB_USER`, `DB_PASS` in production

---

## 👩‍💻 Developed by

Anushka Shrivastava — Mini Project, 2026
