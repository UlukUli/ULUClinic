package clinic;

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
        System.out.println("Doctor menu for " + name);
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