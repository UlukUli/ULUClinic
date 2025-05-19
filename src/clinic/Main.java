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
            System.out.println("4. Exit");
            System.out.print("Your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> showAllPatients();
                case 2 -> showPatientCount();
                case 3 -> findPatient();
                case 4 -> {
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

    public static void showMedAssistantMenu(String name) {
        System.out.println("Nurse menu for " + name);
    }

    public static void showMainDoctorMenu(String name) {
        System.out.println("Chief doctor menu for " + name);
    }

    public static void showPatientMenu(String login) {
        System.out.println("Patient menu for login: " + login);
    }
}