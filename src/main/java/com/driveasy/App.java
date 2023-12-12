package com.driveasy;

import java.sql.Connection;
import java.sql.DriverManager;

public class App {
    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/driveasy","postgres","lol123");
            if (connection != null)
                System.out.println("Connection successful");
            else
                System.out.println("Connection failed");
            connection.close();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
}
