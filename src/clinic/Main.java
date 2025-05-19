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
    }
}