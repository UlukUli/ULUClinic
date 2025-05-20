# ULUClinic

ULUClinic is a **Java console application** that simulates a hospital management system with multiple roles:
**Doctor**, **Nurse**, **Chief Doctor**, and **Patient**.

## Features by Role

### Doctor
- View all patients
- Count total patients
- Search patients by name
- Add a diagnosis from `diagnos.txt`
- Assign tasks to nurses
- View assigned and completed tasks

### Nurse
- View list of procedures
- Find patient by name
- View all active tasks
- Mark task as completed
- View completed tasks

### Chief Doctor
- View all nurses
- View all doctors
- Count patients
- Show max salary among staff
- Show min salary among staff

### Patient
- View medical history
- View last diagnosis date
- View treatment days
- View personal info
- View doctors’ weekly schedule from `schedule.txt`

---

## Technologies Used

- **Java 17**
- **SQLite** with **JDBC**
- **Text files**:
- `diagnos.txt` — diagnosis name = description ; treatment days
- `schedule.txt` — weekly doctor schedule

---

## How to Run

1. Open the project in IntelliJ IDEA.
2. Run `Main.java`.
3. The database will be created automatically (if it doesn't exist).
4. Log in with one of the test accounts below.

### Test Users

- **Login:** doc1, **Password:** 1234 *(Doctor)*
- **Login:** nurse1, **Password:** 1234 *(Nurse)*
- **Login:** chief1, **Password:** 1234 *(Chief Doctor)*
- **Login:** patient1, **Password:** 1234 *(Patient)*

---

## Author

Created by Uluk for an educational Java project to demonstrate:
- **OOP**
- **JDBC**
- **CRUD**
- **Console application architecture**