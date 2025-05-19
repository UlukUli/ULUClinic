package clinic;

import java.sql.*;
import java.util.Scanner;

public class NurseService {

    // Show all procedures
    public static void showProcedures() {
        System.out.println("\n📋 List of procedures:");
        String sql = "SELECT * FROM procedures";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;
            while (rs.next()) {
                System.out.println("- " + rs.getString("description") + " | Patient: " + rs.getString("patient_login"));
                found = true;
            }
            if (!found) {
                System.out.println("No procedures found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while retrieving procedures: " + e.getMessage());
        }
    }

    // Find patient by name
    public static void findPatient() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter patient's name or surname: ");
        String name = scanner.nextLine();

        String sql = "SELECT * FROM users WHERE role = 'patient' AND name LIKE ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + name + "%");
            ResultSet rs = stmt.executeQuery();

            boolean found = false;
            while (rs.next()) {
                System.out.println("Patient: " + rs.getString("name") + " | Login: " + rs.getString("login"));
                found = true;
            }
            if (!found) {
                System.out.println("No patients found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while searching for patient: " + e.getMessage());
        }
    }

    // Show all active tasks
    public static void showTasks() {
        System.out.println("\n📋 List of nurse tasks:");
        String sql = "SELECT * FROM tasks WHERE status = 'pending'";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | " + rs.getString("description"));
                found = true;
            }
            if (!found) {
                System.out.println("No pending tasks.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while retrieving tasks: " + e.getMessage());
        }
    }

    // Mark a task as completed
    public static void completeTask() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter task ID: ");

        int id;
        try {
            id = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format.");
            return;
        }

        String sql = "UPDATE tasks SET status = 'done', date_done = CURRENT_DATE WHERE id = ?";
        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Task marked as completed.");
            } else {
                System.out.println("⚠ Task not found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while completing task: " + e.getMessage());
        }
    }

    // Show completed tasks
    public static void showCompletedTasks() {
        System.out.println("\n✅ Completed tasks:");
        String sql = "SELECT * FROM tasks WHERE status = 'done'";

        try (Connection conn = DatabaseManager.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            boolean found = false;
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") + " | Done: " + rs.getString("date_done"));
                found = true;
            }
            if (!found) {
                System.out.println("No completed tasks.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error while retrieving completed tasks: " + e.getMessage());
        }
    }
}