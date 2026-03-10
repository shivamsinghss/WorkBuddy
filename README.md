# WorkBuddy — HR Management, Simplified

> *Because managing people shouldn't require managing infrastructure.*

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://openjdk.org/)
[![Apache POI](https://img.shields.io/badge/Apache%20POI-5.3.0-blue)](https://poi.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

---

## The Problem

Every growing team hits the same wall — HR operations scattered across spreadsheets, WhatsApp messages, and email threads. Tracking who's on leave, who's been paid, and who's due for a performance review becomes a full-time job in itself.

Enterprise HR tools are bloated, expensive, and need dedicated IT teams to run them. Startups and small businesses deserve better.

---

## The Solution

**WorkBuddy** is a modern, self-hosted HR portal that brings all your people operations into one clean interface — without the overhead of a database, a cloud subscription, or a DevOps engineer.

Built for teams that move fast, WorkBuddy runs out of the box with zero infrastructure setup. Drop it on any machine with Java 17, and it works.

---

## Features

| Module | Capability |
|---|---|
| **Employees** | Full CRUD — add, edit, view, and remove employees |
| **Leave & Attendance** | Apply for Leave, WFH, or Comp Off; punch in/out; leave credit system with auto deduction and restoration on rejection |
| **Payroll** | Salary records with yearly/monthly breakdown, mark-paid workflow, per-month or full-year pay slip download (printable PDF) |
| **Performance** | Track performance reviews and growth metrics per employee |
| **Training** | Log and manage training programs across teams |
| **Recruitment** | Post job openings, manage pipeline, and close positions |
| **Auth** | Role-based access — admins control everything, employees see only their own data |

---

## Why WorkBuddy

- **No database required** — all data persists in `.xlsx` files your team already understands
- **Role-based access** — full admin control + scoped employee views out of the box
- **Pay slip generation** — professional, printable pay slips per month or full year in one click
- **Self-hosted** — your data never leaves your machine
- **Instant setup** — clone, build, run. No migrations, no config files, no surprises

---

## Tech Stack

```
Backend   →  Spring Boot 4.0.3  ·  Java 17  ·  Apache POI 5.3.0
Frontend  →  HTML5  ·  CSS3  ·  Vanilla JavaScript  ·  REST API
Storage   →  Excel (.xlsx) files via Apache POI — zero DB dependency
Auth      →  Role-based (Admin / Employee)  ·  LocalStorage session
Build     →  Gradle
```

---

## Project Structure

```
WorkBuddy/
├── src/main/
│   ├── java/com/workbuddy/workbuddy/
│   │   ├── controller/          # REST controllers (8 modules)
│   │   ├── model/               # Data models
│   │   ├── service/             # Business logic
│   │   └── storage/             # ExcelStorageHelper (Apache POI)
│   └── resources/
│       ├── static/              # Frontend (HTML + CSS + JS)
│       │   ├── css/
│       │   ├── js/
│       │   └── *.html           # One page per module
│       └── application.properties
├── data/                        # Auto-created — Excel data files live here
├── build.gradle
└── README.md
```

---

## Getting Started

### Prerequisites

- Java 17+
- Gradle (or use the included `gradlew` wrapper)

### Run Locally

```bash
# Clone the repo
git clone https://github.com/your-username/WorkBuddy.git
cd WorkBuddy

# Build and run
./gradlew bootRun
```

The server starts on **`http://localhost:8000`**

Open `src/main/resources/static/index.html` in your browser (or serve via IntelliJ / VS Code Live Server).

### Default Credentials

| Role | Username | Password |
|---|---|---|
| Admin | `admin` | `admin123` |
| Employee | *(add via admin panel)* | *(set on creation)* |

> The `data/` directory is auto-created on first run. All Excel files are generated automatically — no manual setup needed.

---

## API Reference

Base URL: `http://localhost:8000`

### Auth
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/auth/login` | Sign in (returns user object) |

### Employees
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/employees` | Get all employees |
| `GET` | `/api/employees/{id}` | Get employee by ID |
| `POST` | `/api/employees` | Create employee |
| `PUT` | `/api/employees/{id}` | Update employee |
| `DELETE` | `/api/employees/{id}` | Delete employee |

### Leave & WFH
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/leaves` | Get all requests |
| `POST` | `/api/leaves` | Submit leave / WFH / Comp Off request |
| `PUT` | `/api/leaves/{id}/approve` | Approve request |
| `PUT` | `/api/leaves/{id}/reject` | Reject + restore leave credit |
| `DELETE` | `/api/leaves/{id}` | Delete request |

### Attendance
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/attendance/today/{empId}` | Get today's record |
| `GET` | `/api/attendance/history/{empId}` | Get history |
| `GET` | `/api/attendance/balance/{empId}` | Get leave balance |
| `POST` | `/api/attendance/punch-in` | Punch in (marks Present) |
| `POST` | `/api/attendance/punch-out` | Punch out |
| `POST` | `/api/attendance/mark` | Mark WFH or Leave |
| `POST` | `/api/attendance/add-credits` | Add leave credits (admin) |

### Payroll
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/payroll` | Get all payroll records |
| `POST` | `/api/payroll` | Create payroll record |
| `PUT` | `/api/payroll/{id}` | Update record |
| `PUT` | `/api/payroll/{id}/pay` | Mark as paid |
| `DELETE` | `/api/payroll/{id}` | Delete record |

### Performance, Training, Recruitment
All follow the same pattern:
```
GET    /api/{module}
POST   /api/{module}
PUT    /api/{module}/{id}
DELETE /api/{module}/{id}
```
Where `{module}` is `performance`, `training`, or `recruitment`.

---

## Data Storage

All data is stored as `.xlsx` files under the `data/` directory:

```
data/
├── employees.xlsx
├── leaves.xlsx
├── attendance.xlsx
├── payroll.xlsx
├── performance.xlsx
├── training.xlsx
└── recruitment.xlsx
```

Each file is created automatically on first write. You can open, inspect, and even edit these files directly in Excel or Google Sheets.

---

## Screenshots

> *Add screenshots here*

| Dashboard | Leave & Attendance | Payroll |
|---|---|---|
| ![Dashboard](UI/dashboard.png) | ![Leaves](UI/leaves.png) | ![Payroll](UI/payroll.png) |

---

## Roadmap

- [ ] Export reports to PDF / CSV
- [ ] Email notifications for leave approvals
- [ ] Multi-company / department support
- [ ] Docker containerization
- [ ] Audit log for admin actions
- [ ] Mobile-responsive layout improvements

---

## Contributing

Pull requests are welcome. For major changes, please open an issue first to discuss what you'd like to change.

1. Fork the repo
2. Create your feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -m 'Add your feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a pull request

---

## License

This project is licensed under the [MIT License](LICENSE).

---

> *WorkBuddy is a proof-of-concept HR portal — built to demonstrate that powerful internal tools don't need complex infrastructure to deliver real value.*
