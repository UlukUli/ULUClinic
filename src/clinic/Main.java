package clinic;
import java.sql.*;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        System.out.println("🔃 Starting application...");

// Initialize database
        DatabaseInitializer.initialize();

// Login
        String[] user = AuthService.login();
        if (user == null) {
            System.out.println("❌ Invalid login or password.");
            return;
        }

        String name = user[0];
        String role = user[1];
        String login = user[2];

        System.out.println("✅ Welcome, " + name + " (" + role + ")");

        switch (role) {
            case "doctor" -> showDoctorMenu(name);
            case "nurse" -> showMedAssistantMenu(name);
            case "chief" -> showMainDoctorMenu(name);
            case "patient" -> showPatientMenu(login);
            default -> System.out.println("⚠ Unknown role.");
        }
    }
    public static void showDoctorMenu(String name) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\nHello, Doctor " + name + "!");
            System.out.println("1. Show all patients");
            System.out.println("2. Show patient count");
            System.out.println("3. Find patient");
            System.out.println("4. Assign a diagnosis");
            System.out.println("5. Show active nurse tasks");
            System.out.println("6. Assign a task to nurse");
            System.out.println("7. Show completed nurse tasks");
            System.out.println("8. Exit");
            System.out.print("Your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAllPatients();
                case 2 -> showPatientCount();
                case 3 -> findPatient();
                case 4 -> addDiagnosis();
                case 5 -> showActiveNurseTasks();
                case 6 -> assignTaskForAssistant();
                case 7 -> showCompletedNurseTasks();
                case 8 -> {
                    System.out.println("Goodbye!");
                    return;
                }

                default -> System.out.println("Invalid choice.");
            }
        }
    }
    public static void showAllPatients() {
        String sql = "SELECT login, birth_date FROM patients";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\nPatient list:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("login") + " | Birth date: " + rs.getString("birth_date"));
                found = true;
            }
            if (!found) System.out.println("No patients found.");

        } catch (Exception e) {
            System.out.println("❌ Error fetching patients: " + e.getMessage());
        }
    }
    public static void showPatientCount() {
        String sql = "SELECT COUNT(*) AS count FROM patients";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Total patients: " + rs.getInt("count"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error counting patients: " + e.getMessage());
        }
    }
    public static void findPatient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter patient name (or part): ");
        String namePart = scanner.nextLine();

        String sql = "SELECT users.name, patients.birth_date FROM users " +
                "JOIN patients ON users.login = patients.login " +
                "WHERE users.name LIKE ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + namePart + "%");
            ResultSet rs = stmt.executeQuery();

            System.out.println("Search results:");
            while (rs.next()) {
                System.out.println("- " + rs.getString("name") + " | Birth date: " + rs.getString("birth_date"));
            }

        } catch (Exception e) {
            System.out.println("❌ Error searching: " + e.getMessage());
        }
    }
    public static void addDiagnosis() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter patient login: ");
        String login = scanner.nextLine();

        String checkSql = "SELECT * FROM patients WHERE login = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, login);
            ResultSet rs = checkStmt.executeQuery();
            if (!rs.next()) {
                System.out.println("❌ Patient not found.");
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Error checking patient: " + e.getMessage());
            return;
        }

        System.out.print("Enter diagnosis: ");
        String diagnosis = scanner.nextLine();
        String date = java.time.LocalDate.now().toString();

        String description = null;
        int treatmentDays = 0;

        try (Scanner fileScanner = new Scanner(new java.io.File("diagnos.txt"))) {
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split("=");
                if (parts.length == 2 && parts[0].equalsIgnoreCase(diagnosis)) {
                    String[] info = parts[1].split(";");
                    if (info.length == 2) {
                        description = info[0];
                        treatmentDays = Integer.parseInt(info[1]);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading diagnos.txt: " + e.getMessage());
            return;
        }

        if (description == null) {
            System.out.println("❌ Diagnosis not found in diagnos.txt.");
            return;
        }

        String insertSql = "INSERT INTO medical_history (patient_login, date, diagnosis) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            insertStmt.setString(1, login);
            insertStmt.setString(2, date);
            insertStmt.setString(3, diagnosis);
            insertStmt.executeUpdate();

            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE patients SET treatment_days = ? WHERE login = ?");
            updateStmt.setInt(1, treatmentDays);
            updateStmt.setString(2, login);
            updateStmt.executeUpdate();

            System.out.println("✅ Diagnosis assigned for " + treatmentDays + " days.");

        } catch (Exception e) {
            System.out.println("❌ Error assigning diagnosis: " + e.getMessage());
        }
    }
    public static void assignTaskForAssistant() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter task description for nurse: ");
        String description = scanner.nextLine();

        String nurseLogin = "nurse1"; // static for simplicity
        String doctorLogin = "doc1";
        String date = java.time.LocalDate.now().toString();

        String sql = "INSERT OR IGNORE INTO tasks (description, doctor_login, nurse_login, status, date_assigned) " +
                "VALUES (?, ?, ?, 'pending', ?)";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, description);
            stmt.setString(2, doctorLogin);
            stmt.setString(3, nurseLogin);
            stmt.setString(4, date);
            stmt.executeUpdate();

            System.out.println("✅ Task assigned to nurse.");

        } catch (Exception e) {
            System.out.println("❌ Error assigning task: " + e.getMessage());
        }
    }
    public static void showActiveNurseTasks() {
        String sql = "SELECT * FROM tasks WHERE status = 'pending'";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nActive nurse tasks:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("description") + " (to: " + rs.getString("nurse_login") + ")");
                found = true;
            }
            if (!found) System.out.println("No active tasks.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
    public static void showCompletedNurseTasks() {
        String sql = "SELECT * FROM tasks WHERE status = 'done'";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\nCompleted nurse tasks:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("description") + " (by: " + rs.getString("nurse_login") + ")");
                found = true;
            }
            if (!found) System.out.println("No completed tasks.");

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    public static void showMedAssistantMenu(String name) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\nЗдравствуйте, медсестра " + name + "!");
            System.out.println("1. Показать список процедур");
            System.out.println("2. Найти пациента");
            System.out.println("3. Показать список поручений");
            System.out.println("4. Выполнить поручение");
            System.out.println("5. Показать завершенные поручения");
            System.out.println("6. Выход");
            System.out.print("Ваш выбор: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Попробуйте ещё раз.");
                continue;
            }

            switch (choice) {
                case 1 -> NurseService.showProcedures();
                case 2 -> NurseService.findPatient();
                case 3 -> NurseService.showTasks();
                case 4 -> NurseService.completeTask();
                case 5 -> NurseService.showCompletedTasks();
                case 6 -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    public static void showMainDoctorMenu(String name) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("\nЗдравствуйте, главврач " + name + "!");
            System.out.println("1. Показать список медсестёр");
            System.out.println("2. Показать список лечащих врачей");
            System.out.println("3. Показать количество пациентов");
            System.out.println("4. Сотрудник с максимальной зарплатой");
            System.out.println("5. Сотрудник с минимальной зарплатой");
            System.out.println("6. Выход");
            System.out.print("Ваш выбор: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Попробуйте ещё раз.");
                continue;
            }

            switch (choice) {
                case 1 -> ChiefDoctorService.showNurses();
                case 2 -> ChiefDoctorService.showDoctors();
                case 3 -> ChiefDoctorService.countPatients();
                case 4 -> ChiefDoctorService.showMaxSalary();
                case 5 -> ChiefDoctorService.showMinSalary();
                case 6 -> {
                    System.out.println("До свидания!");
                    return;
                }
                default -> System.out.println("Неверный выбор.");
            }
        }
    }

    public static void showPatientMenu(String login) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        System.out.println("Logged in as: " + login);

        while (true) {
            System.out.println("\nWelcome, patient!");
            System.out.println("1. View medical history");
            System.out.println("2. View last diagnosis date");
            System.out.println("3. View treatment days");
            System.out.println("4. View doctor schedule");
            System.out.println("5. View personal information");
            System.out.println("6. Exit");
            System.out.print("Your choice: ");

            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1 -> PatientService.showHistory(login);
                case 2 -> PatientService.showLastDate(login);
                case 3 -> PatientService.showTreatmentDays(login);
                case 4 -> PatientService.showSchedule();
                case 5 -> PatientService.showPersonalInfo(login);
                case 6 -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }
}