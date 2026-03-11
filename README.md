# WorkBuddy — Business Pitch & Product Documentation

> *One platform. Every HR operation. Zero infrastructure overhead.*

🌐 **WorkBuddy website url link:** [https://shivamsinghss.github.io/WorkBuddy/src/main/resources/static/index.html](https://shivamsinghss.github.io/WorkBuddy/src/main/resources/static/index.html)

---

## Executive Summary

WorkBuddy is a **free, professional HR management portal built specifically for small business owners** who need enterprise-grade HR capabilities without enterprise-grade costs. Whether you run a team of 5 or 50, WorkBuddy gives you the same tools used by large companies — employee management, attendance, leave, payroll, performance reviews, training, and recruitment — all in one place, completely free.

No subscription. No credit card. No IT team required. Just sign in and start managing your team like a pro.

At its core, WorkBuddy centralizes six critical HR operations into a single, role-aware portal. With a planned scheduler engine, WorkBuddy will automate routine HR tasks like monthly payroll generation, training assignments, and recruitment pipeline updates, reducing manual effort to near zero.

---

## The Problem

### HR is broken for small teams.

| Pain Point | Reality |
|---|---|
| **Scattered data** | Leave requests on WhatsApp, salaries in Excel, reviews in email |
| **No visibility** | Managers don't know who's on leave, who's been paid, who needs training |
| **Manual everything** | Payslips generated manually every month, one by one |
| **Enterprise tools are overkill** | SAP, Workday, BambooHR — expensive, complex, requires IT setup |
| **SaaS dependency** | Your data lives on someone else's server, behind a monthly bill |

Small teams spend **4–6 hours per week** on HR admin that could be automated. That's time taken away from building product, serving customers, and growing the business.

---

## Who Is This For?

WorkBuddy was built for **small business owners** who:
- Are tired of managing HR across WhatsApp, Excel, and email
- Can't afford BambooHR, Zoho People, or SAP
- Want a professional HR system that just works — for free
- Need both a management view (admin) and a self-service view (employees)

> WorkBuddy gives your small business the same HR discipline as a 500-person company — at zero cost.

---

## How to Use WorkBuddy

### Getting Started

1. Visit the live portal: [WorkBuddy](https://shivamsinghss.github.io/WorkBuddy/src/main/resources/static/index.html)
2. Click **Get Started**
3. Sign in with your credentials
4. You will automatically land in the right view based on your role

> **Note:** The backend runs on a free server instance. The first request may take up to **50 seconds** to wake up. Please be patient on first sign-in.

---

### Admin Point of View

> Sign in with: **admin credentials** to have admin power/role

As an admin (business owner or HR manager), you have **full control** over every module.

#### Employees
- Add new employees with their name, department, position, salary, and login credentials
- Edit or remove existing employees
- Every employee you add gets their own login to access the portal

#### Leave & Attendance
- View all leave, WFH, and Comp Off requests from every employee
- **Approve** or **Reject** requests with one click
- Rejecting a leave request automatically restores the employee's leave credit
- Add leave credits to any employee's balance

#### Payroll
- Create monthly salary records for each employee
- Set basic salary (auto-filled from employee profile), bonus, and deductions
- Net salary is calculated automatically
- Mark records as **Paid** once processed
- Download any employee's pay slip for a specific month or full year

#### Performance
- Create and manage performance review records for employees
- Track ratings and feedback per review cycle

#### Training
- Add training programs and assign them to employees
- Track completion status across your team

#### Recruitment
- Post open job positions with department, role, and description
- Track hiring pipeline from open to closed

---

### Employee Point of View

> Sign in with credentials set by the admin when your profile was created

As an employee, you have a **self-service portal** — you only see and manage your own data.

#### Leave & Attendance
- View your **today's status card** — mark yourself as Office, WFH, or On Leave for the day
- **Punch In** when you start work, **Punch Out** when you finish — timestamps are recorded
- Once you mark your status for the day, it is locked and cannot be changed
- Apply for **Leave**, **WFH**, or **Comp Off** from the Apply tab
- Track all your past requests and their approval status
- View your leave credit balance

#### Payroll
- See your **Yearly Salary** and **Fixed Monthly Gross** at the top of the page
- View all your payroll records — basic, bonus, deductions, net salary, and payment status
- **Download your pay slip** for any specific month or for a full year (opens as printable PDF)

#### Performance
- View your own performance review history — ratings and feedback from your manager

#### Training
- View training programs assigned to you and track your completion status

#### Recruitment
- Browse open job postings within the company

---

## The Solution

**WorkBuddy** brings all HR operations into one clean, role-aware portal — running entirely on your own machine or server. No database setup. No cloud dependency. No monthly fee.

It is designed around one principle: **HR operations should run themselves.**

With a scheduler layer (in roadmap), WorkBuddy will automatically generate payslips every month, assign training programs on schedule, notify managers of pending approvals, and update recruitment pipelines — with zero manual intervention.

---

## Product Overview

### Current Capabilities

#### 1. Employee Management
- Add, edit, view, and deactivate employees
- Store personal details, department, position, salary, and login credentials
- Admin has full CRUD; employees view their own profile only

#### 2. Leave & Attendance
- Employees apply for **Leave**, **WFH**, or **Comp Off** in one click
- Leave credit system: credits auto-deducted on submission, restored on rejection
- Punch In / Punch Out tracking with timestamps
- Today's status card: Office / WFH / On Leave — select once, locked for the day
- Admins view and action all requests; employees manage only their own

#### 3. Payroll
- Admin creates monthly salary records per employee
- Yearly salary stored; Fixed Monthly Gross auto-calculated
- Bonus and deductions tracked per record
- Mark Paid workflow with status tracking
- **Pay Slip Download** — employee downloads by specific month or full year
- Printable, professional pay slip generated in browser (PDF via print)

#### 4. Performance Reviews
- Admins create and manage performance review records
- Employees view their own review history
- Tracks review period, rating, and feedback

#### 5. Training Management
- Log training programs with assigned employees and completion status
- Admins manage all records; employees view their own assignments
- Foundation for scheduler-driven auto-assignment (roadmap)

#### 6. Recruitment
- Post open job positions with department, role, and status
- Track pipeline from open to closed
- Foundation for automated pipeline status updates (roadmap)

---

## The Scheduler Vision

> *What if HR ran on autopilot?*

The next major evolution of WorkBuddy is a **built-in scheduler engine** that automates recurring HR tasks across all modules. Here is what becomes possible:

### Payroll Automation
| Task | Trigger | Action |
|---|---|---|
| Monthly payroll generation | 1st of every month | Auto-create salary records for all active employees |
| Payslip distribution | After payroll generation | Mark records ready for employee download |
| Payment reminders | 5 days before pay date | Flag unpaid records for admin review |

### Training Automation
| Task | Trigger | Action |
|---|---|---|
| Onboarding training | New employee added | Auto-assign onboarding training program |
| Recurring compliance training | Annually / quarterly | Auto-assign to all employees in scope |
| Completion reminders | 7 days before due date | Notify employee and manager |
| Escalation | Past due date | Flag to admin dashboard |

### Recruitment Automation
| Task | Trigger | Action |
|---|---|---|
| Pipeline aging alerts | Position open > 30 days | Notify hiring manager |
| Auto-close | Position filled | Update status, notify team |
| Monthly hiring report | End of month | Summary of open, closed, and in-progress roles |

### Leave & Attendance Automation
| Task | Trigger | Action |
|---|---|---|
| Annual leave credit reset | January 1st | Reset leave balances for all employees |
| Leave balance alerts | Balance < 2 days | Notify employee proactively |
| Attendance summary | End of month | Generate monthly attendance report per employee |

### Performance Automation
| Task | Trigger | Action |
|---|---|---|
| Review cycle initiation | Quarterly / annually | Create review records for all active employees |
| Review reminders | 7 days before due | Notify manager to complete review |
| Review summary | Post-cycle | Aggregate scores and flag outliers |

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                    WorkBuddy Platform                │
├──────────────┬──────────────────────┬───────────────┤
│   Frontend   │      REST API        │   Scheduler   │
│  (HTML/JS)   │   (Spring Boot 4)    │  (Planned)    │
├──────────────┴──────────────────────┴───────────────┤
│              Excel Storage (Apache POI)              │
│         employees.xlsx  |  payroll.xlsx  | ...       │
└─────────────────────────────────────────────────────┘
```

### Why Excel as Storage?
- **Zero setup** — no database installation, no connection strings, no migrations
- **Human-readable** — any team member can open and inspect the data in Excel
- **Portable** — the entire company's HR data is a folder of files, easy to back up
- **Upgradeable** — the storage layer is abstracted; swap to PostgreSQL or MongoDB when scale demands it, with no changes to business logic

---

## Technology Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 4.0.3 · Java 17 |
| Storage | Apache POI 5.3.0 (Excel as Database) |
| Frontend | HTML5 · CSS3 · Vanilla JavaScript |
| API | RESTful JSON API |
| Auth | Role-based (Admin / Employee) · LocalStorage session |
| Build | Gradle |
| Scheduler *(roadmap)* | Spring `@Scheduled` · Quartz Scheduler |

---

## Business Model Potential

While WorkBuddy is currently an open-source proof of concept, the architecture supports a clear commercial path:

| Tier | Offering | Target |
|---|---|---|
| **Open Source** | Self-hosted, Excel storage, full features | Developers, startups, internal tools |
| **Team Edition** | Hosted, database-backed, scheduler included | Small businesses (5–50 employees) |
| **Business Edition** | Multi-department, SSO, audit logs, reports | Mid-size companies (50–500 employees) |
| **Enterprise** | Custom integrations, dedicated support, SLA | Large organizations |

---

## Competitive Positioning

| Feature | WorkBuddy | BambooHR | Zoho People | SAP SuccessFactors |
|---|---|---|---|---|
| Self-hosted | ✅ | ❌ | ❌ | ❌ |
| Zero DB setup | ✅ | ❌ | ❌ | ❌ |
| Open source | ✅ | ❌ | ❌ | ❌ |
| Pay slip download | ✅ | ✅ | ✅ | ✅ |
| Scheduler automation | 🔜 Roadmap | ✅ | ✅ | ✅ |
| Free to run | ✅ | ❌ | ❌ | ❌ |
| Setup time | < 5 min | Hours–Days | Hours–Days | Weeks–Months |

---

## Roadmap

### Phase 1 — Foundation ✅ *(Current)*
- [x] Employee CRUD with role-based access
- [x] Leave, WFH, Comp Off request flow with credit system
- [x] Attendance punch in/out with daily status tracking
- [x] Payroll records with yearly/monthly salary breakdown
- [x] Pay slip download — by month or by year
- [x] Performance, Training, and Recruitment modules
- [x] Auth system with admin and employee roles

### Phase 2 — Scheduler Engine 🔜
- [ ] Monthly payroll auto-generation for all employees
- [ ] Annual leave credit reset
- [ ] Onboarding training auto-assignment on new hire
- [ ] Recurring compliance training scheduler
- [ ] Review cycle auto-initiation (quarterly/annually)
- [ ] Recruitment pipeline aging alerts

### Phase 3 — Scale & Integrations 🔜
- [ ] PostgreSQL / MongoDB storage option
- [ ] Email notifications (leave approvals, payslip ready, review due)
- [ ] Export reports to PDF and CSV
- [ ] Docker containerization for one-command deployment
- [ ] Audit log for all admin actions
- [ ] REST webhook support for Slack / Teams notifications

### Phase 4 — Enterprise Readiness 🔜
- [ ] Multi-company / multi-department support
- [ ] SSO integration (Google, Microsoft)
- [ ] Advanced analytics dashboard
- [ ] Mobile-responsive UI
- [ ] Granular permission system

---

## Target Users

| User | Pain Today | WorkBuddy Solves |
|---|---|---|
| **Startup Founder** | Managing HR in spreadsheets alongside product work | Central portal, zero setup, runs locally |
| **HR Manager** | Manual payslip generation every month | Scheduler auto-generates, employee downloads own slip |
| **Employee** | Asking HR for leave status, payslip copies | Self-service portal — apply, track, download independently |
| **Engineering Lead** | Building internal HR tools from scratch | Open source, extend and customize freely |

---

## Summary

WorkBuddy is not just an HR tool — it is a foundation for how small teams can operate with the same HR discipline as large enterprises, without the cost or complexity.

With the scheduler engine, WorkBuddy transforms from a management portal into an **autonomous HR system** — one that generates payslips, assigns training, tracks attendance, and manages recruitment pipelines with minimal human input.

The goal is simple: **let HR run itself, so your team can focus on what matters.**

---

> Built with Spring Boot 4 · Apache POI · Java 17
>
> *Open source. Self-hosted. Zero dependency.*
