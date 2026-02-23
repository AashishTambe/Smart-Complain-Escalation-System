package com.complaintsystem.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // TODO: change DB name, user, and password to match your MySQL setup
    private static final String URL =
            "jdbc:mysql://localhost:3306/complaint_system";

    private static final String USER = "root";          
    private static final String PASS = "root";

    static {
        try {
            // Try to load the MySQL Connector/J driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("✓ MySQL JDBC Driver loaded successfully");
        } catch (ClassNotFoundException e) {
            System.err.println("✗ MySQL JDBC Driver not found in classpath");
            System.err.println("Make sure mysql-connector-j is added to pom.xml");
            System.err.println("Error: " + e.getMessage());
            // Don't throw, let connection fail with better error
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("✓ Database connection established");
            return conn;
        } catch (SQLException e) {
            System.err.println("✗ Failed to connect to database");
            System.err.println("URL: " + URL);
            System.err.println("User: " + USER);
            System.err.println("Error: " + e.getMessage());
            throw e;
        }
    }
}

