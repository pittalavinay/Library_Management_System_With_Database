package com.library.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections.
 */
public class DatabaseConnection {
    
    private static final String CONFIG_FILE = "database.properties";
    private static Properties properties;
    
    static {
        loadProperties();
    }
    
    /**
     * Load database properties from configuration file.
     */
    private static void loadProperties() {
        properties = new Properties();
        
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                System.err.println("Unable to find " + CONFIG_FILE + ". Using default values.");
                // Set default properties
                properties.setProperty("db.url", "jdbc:mysql://localhost:3306/library_management");
                properties.setProperty("db.username", "root");
                properties.setProperty("db.password", "password");
                properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
                System.out.println("Default database properties set");
                return;
            }
            
            properties.load(input);
            System.out.println("Database properties loaded successfully");
            System.out.println("Driver: " + properties.getProperty("db.driver"));
            System.out.println("URL: " + properties.getProperty("db.url"));
            System.out.println("Username: " + properties.getProperty("db.username"));
            
        } catch (IOException e) {
            System.err.println("Error loading database properties: " + e.getMessage() + ". Using default values.");
            // Set default properties on error
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/library_management");
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "password");
            properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            System.out.println("Default database properties set due to error");
        } catch (Exception e) {
            System.err.println("Unexpected error loading database properties: " + e.getMessage() + ". Using default values.");
            // Set default properties on any unexpected error
            properties.setProperty("db.url", "jdbc:mysql://localhost:3306/library_management");
            properties.setProperty("db.username", "root");
            properties.setProperty("db.password", "password");
            properties.setProperty("db.driver", "com.mysql.cj.jdbc.Driver");
            System.out.println("Default database properties set due to unexpected error");
        }
    }
    
    /**
     * Get a database connection.
     * 
     * @return Database connection
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        // Ensure properties are loaded
        if (properties == null) {
            loadProperties();
        }
        
        // Double-check that properties are loaded
        if (properties == null) {
            throw new SQLException("Failed to load database properties");
        }
        
        try {
            // Load the MySQL driver
            Class.forName(properties.getProperty("db.driver"));
            
            // Create connection
            Connection connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
            );
            
            // Set auto-commit to false for transaction management
            connection.setAutoCommit(false);
            
            System.out.println("Database connection established");
            return connection;
            
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Connector/J not found in classpath. Please download mysql-connector-java-8.0.33.jar and add it to the classpath.");
            System.err.println("Download from: https://dev.mysql.com/downloads/connector/j/");
            throw new SQLException("Database driver not found", e);
        } catch (SQLException e) {
            System.err.println("Failed to establish database connection: " + e.getMessage());
            throw e;
        }
    }
    
    /**
     * Close a database connection.
     * 
     * @param connection Connection to close
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed");
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }
    
    /**
     * Rollback a transaction.
     * 
     * @param connection Connection to rollback
     */
    public static void rollback(Connection connection) {
        if (connection != null) {
            try {
                connection.rollback();
                System.out.println("Transaction rolled back");
            } catch (SQLException e) {
                System.err.println("Error rolling back transaction: " + e.getMessage());
            }
        }
    }
    
    /**
     * Commit a transaction.
     * 
     * @param connection Connection to commit
     */
    public static void commit(Connection connection) {
        if (connection != null) {
            try {
                connection.commit();
                System.out.println("Transaction committed");
            } catch (SQLException e) {
                System.err.println("Error committing transaction: " + e.getMessage());
                rollback(connection);
            }
        }
    }
    
    /**
     * Test database connection.
     * 
     * @return true if connection successful, false otherwise
     */
    public static boolean testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Database connection test successful");
            return true;
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
}
