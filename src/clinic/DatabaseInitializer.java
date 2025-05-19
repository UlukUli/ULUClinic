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
            // Execute table creation
            stmt.execute(createUsers);
            stmt.execute(createPatientsTable);
            stmt.execute(createHistory);
            stmt.execute(createTasksTable);
            stmt.execute(createProceduresTable);
            stmt.execute(createStaffTable);

// ====== Sample data ======

            stmt.execute("""
                    INSERT OR IGNORE INTO users (login, password, role, name) VALUES
                    ('patient1', '1234', 'patient', 'Bekzat Patient'),
                    ('akzhol', '1234', 'patient', 'Akzhol Aliev'),
                    ('doc1', '1234', 'doctor', 'Aigul K'),
                    ('nurse1', '1234', 'nurse', 'Nurse Natalia'),
                    ('chief1', '1234', 'chief', 'Chief Ivanova');
                    """);

            stmt.execute("""
                    INSERT OR IGNORE INTO patients (login, fio, birth_date, height, weight, blood_type, treatment_days)
                    VALUES
                    ('patient1', 'Bekzat Patient', '2000-01-01', 180, 75, 'A+', 5),
                    ('akzhol', 'Akzhol Aliev', '2005-01-01', 175, 68, 'O-', 0);
                    """);

            stmt.execute("""
                    INSERT OR IGNORE INTO staff (name, role, salary, hire_date) VALUES
                    ('Aigul K', 'doctor', 350000, '2022-09-01'),
                    ('Nurse Natalia', 'nurse', 220000, '2023-01-15'),
                    ('Chief Ivanova', 'chief', 500000, '2021-06-10');
                    """);

            stmt.execute("""
                    INSERT OR IGNORE INTO procedures (description, patient_login)
                    VALUES ('Measure blood pressure', 'patient1');
                    """);

            stmt.execute("""
                    INSERT OR IGNORE INTO tasks (description, doctor_login, nurse_login, status, date_assigned)
                    VALUES ('Set up IV', 'doc1', 'nurse1', 'pending', '2025-05-14');
                    """);

            System.out.println("✅ Database initialized.");

            stmt.execute(createUsers);

        } catch (Exception e) {
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }
}