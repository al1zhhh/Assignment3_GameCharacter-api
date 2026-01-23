package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseConnection utility class for PostgreSQL
 * Manages JDBC connection to PostgreSQL database
 */
public class DatabaseConnection {
    // PostgreSQL connection parameters
    private static final String HOST = "localhost";
    private static final String PORT = "5432";  // Default PostgreSQL port
    private static final String DATABASE = "game_character_db";
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
    private static final String USER = "game_user";  // Default PostgreSQL user
    private static final String PASSWORD = "Ti4NtEhI";  // CHANGE THIS!

    private static Connection connection = null;

    /**
     * Get database connection (Singleton pattern)
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Load PostgreSQL JDBC Driver
                Class.forName("org.postgresql.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✓ PostgreSQL database connection established successfully!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("PostgreSQL JDBC Driver not found. " +
                        "Please add postgresql-XX.X.X.jar to classpath", e);
            } catch (SQLException e) {
                System.err.println("✗ Failed to connect to PostgreSQL database:");
                System.err.println("  URL: " + URL);
                System.err.println("  User: " + USER);
                System.err.println("  Error: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    /**
     * Close database connection
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("✓ PostgreSQL database connection closed.");
            } catch (SQLException e) {
                System.err.println("✗ Error closing connection: " + e.getMessage());
            }
        }
    }

    /**
     * Test database connection
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();

            if (isValid) {
                // Additional check: try to execute a simple query
                var statement = conn.createStatement();
                var rs = statement.executeQuery("SELECT version()");
                if (rs.next()) {
                    String version = rs.getString(1);
                    System.out.println("✓ Connected to: " + version);
                }
                rs.close();
                statement.close();
            }

            return isValid;
        } catch (SQLException e) {
            System.err.println("✗ Database connection test failed:");
            System.err.println("  " + e.getMessage());
            System.err.println("\nTroubleshooting:");
            System.err.println("  1. Check if PostgreSQL server is running");
            System.err.println("  2. Verify database 'game_character_db' exists");
            System.err.println("  3. Check username and password");
            System.err.println("  4. Ensure postgresql-XX.X.X.jar is in classpath");
            return false;
        }
    }

    /**
     * Get connection URL for debugging
     * @return connection URL string
     */
    public static String getConnectionURL() {
        return URL;
    }
}