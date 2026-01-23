package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseConnection {
    // PostgreSQL connection parameters
    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE = "game_character_db";
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE;
    private static final String USER = "game_user";
    private static final String PASSWORD = "Ti4NtEhI";

    private static Connection connection = null;


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

    public static boolean testConnection() {
        try {
            Connection conn = getConnection();
            boolean isValid = conn != null && !conn.isClosed();

            if (isValid) {

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


    public static String getConnectionURL() {
        return URL;
    }
}