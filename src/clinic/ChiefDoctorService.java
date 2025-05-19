package clinic;

import java.sql.*;

public class ChiefDoctorService {

    // Show all nurses
    public static void showNurses() {
        String sql = "SELECT name, hire_date, salary FROM staff WHERE role = 'nurse'";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n🩺 List of Nurses:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("name") +
                        " | Hired: " + rs.getString("hire_date") +
                        " | Salary: " + rs.getInt("salary"));
                found = true;
            }
            if (!found) {
                System.out.println("No nurses found in the database.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Show all doctors
    public static void showDoctors() {
        String sql = "SELECT name, hire_date, salary FROM staff WHERE role = 'doctor'";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n👨‍⚕️ List of Doctors:");
            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("name") +
                        " | Hired: " + rs.getString("hire_date") +
                        " | Salary: " + rs.getInt("salary"));
                found = true;
            }
            if (!found) {
                System.out.println("No doctors found in the database.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Count all patients
    public static void countPatients() {
        String sql = "SELECT COUNT(*) AS total FROM patients";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("Total number of patients: " + rs.getInt("total"));
            } else {
                System.out.println("Unable to retrieve patient count.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Show max salary
    public static void showMaxSalary() {
        String sql = "SELECT name, role, salary FROM staff ORDER BY salary DESC LIMIT 1";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("💰 Highest salary: " + rs.getString("name") +
                        " (" + rs.getString("role") + ") — " + rs.getInt("salary"));
            } else {
                System.out.println("No staff data found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Show min salary
    public static void showMinSalary() {
        String sql = "SELECT name, role, salary FROM staff ORDER BY salary ASC LIMIT 1";
        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                System.out.println("💵 Lowest salary: " + rs.getString("name") +
                        " (" + rs.getString("role") + ") — " + rs.getInt("salary"));
            } else {
                System.out.println("No staff data found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}