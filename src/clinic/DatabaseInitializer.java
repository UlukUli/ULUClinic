package clinic;

import java.io.File;
import java.sql.Connection;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement()) {

            System.out.println("Using database: " + new File("clinic.db").getAbsolutePath());

// Users table
            String createUsers = """
CREATE TABLE IF NOT EXISTS users (
login TEXT PRIMARY KEY,
password TEXT NOT NULL,
role TEXT NOT NULL,
name TEXT NOT NULL
);
""";
            // Patients table
            String createPatientsTable = """
CREATE TABLE IF NOT EXISTS patients (
login TEXT PRIMARY KEY,
fio TEXT,
birth_date TEXT,
height INTEGER,
weight INTEGER,
blood_type TEXT,
treatment_days INTEGER
);
""";

// Medical history
            String createHistory = """
CREATE TABLE IF NOT EXISTS medical_history (
id INTEGER PRIMARY KEY AUTOINCREMENT,
patient_login TEXT,
date TEXT,
diagnosis TEXT,
FOREIGN KEY (patient_login) REFERENCES users(login)
);
""";

// Tasks assigned to nurses
            String createTasksTable = """
CREATE TABLE IF NOT EXISTS tasks (
id INTEGER PRIMARY KEY AUTOINCREMENT,
description TEXT,
doctor_login TEXT,
nurse_login TEXT,
status TEXT,
date_assigned TEXT,
date_done TEXT,
UNIQUE(description, doctor_login, nurse_login, date_assigned)
);
""";
            // Procedures table
            String createProceduresTable = """
CREATE TABLE IF NOT EXISTS procedures (
id INTEGER PRIMARY KEY AUTOINCREMENT,
description TEXT,
patient_login TEXT,
UNIQUE(description, patient_login)
);
""";

// Staff table (for chief doctor)
            String createStaffTable = """
CREATE TABLE IF NOT EXISTS staff (
id INTEGER PRIMARY KEY AUTOINCREMENT,
name TEXT UNIQUE,
role TEXT,
salary INTEGER,
hire_date TEXT
);
""";

            stmt.execute(createUsers);

        } catch (Exception e) {
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }