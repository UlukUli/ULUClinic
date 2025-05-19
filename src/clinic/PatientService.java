package clinic;

import java.sql.*;

public class PatientService {

    // Medical history
    public static void showHistory(String login) {
        String sql = "SELECT date, diagnosis FROM medical_history WHERE patient_login = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            System.out.println("\nMedical History:");
            boolean found = false;

            while (rs.next()) {
                System.out.println(rs.getString("date") + ": " + rs.getString("diagnosis"));
                found = true;
            }

            if (!found) {
                System.out.println("Medical history is empty.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Last diagnosis date
    public static void showLastDate(String login) {
        String sql = "SELECT date FROM medical_history WHERE patient_login = ? ORDER BY date DESC LIMIT 1";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("Last diagnosis date: " + rs.getString("date"));
            } else {
                System.out.println("Medical history is empty.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Treatment days
    public static void showTreatmentDays(String login) {
        String sql = "SELECT treatment_days FROM patients WHERE login = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int days = rs.getInt("treatment_days");
                System.out.println("Treatment days: " + days);
            } else {
                System.out.println("Patient not found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }

    // Doctor schedule
    public static void showSchedule() {
        System.out.println("\n📅 Doctor schedule:");
        try (var reader = new java.io.BufferedReader(new java.io.FileReader("schedule.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            System.out.println("❌ Error reading schedule: " + e.getMessage());
        }
    }

    // Personal information of patient
    public static void showPersonalInfo(String login) {
        String sql = "SELECT * FROM patients WHERE login = ?";

        try (Connection conn = DatabaseManager.connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.println("\n🧾 Patient Information:");
                System.out.println("Full Name: " + rs.getString("fio"));
                System.out.println("Date of Birth: " + rs.getString("birth_date"));
                System.out.println("Height: " + rs.getInt("height") + " cm");
                System.out.println("Weight: " + rs.getInt("weight") + " kg");
                System.out.println("Blood Type: " + rs.getString("blood_type"));
            } else {
                System.out.println("Patient not found.");
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }
    }
}