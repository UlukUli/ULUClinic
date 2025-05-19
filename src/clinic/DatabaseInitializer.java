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
            stmt.execute(createUsers);

        } catch (Exception e) {
            System.out.println("❌ Error initializing database: " + e.getMessage());
        }
    }