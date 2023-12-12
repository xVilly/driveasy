package com.driveasy.Database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Connection manager class handling connection operations of the database
 * Implements singleton pattern, should be used from ConnectionManager.getInstance()
 */
public class ConnectionManager {
    private static ConnectionManager instance = null;

    private ConnectionSettings settings = null;
    private Connection connection = null;

    private ConnectionManager() { }

    public static ConnectionManager getInstance() {
        if (instance == null)
            instance = new ConnectionManager();
        return instance;
    }

    public boolean isSetup() {
        return this.settings != null;
    }

    public ConnectionSettings getSettings() {
        return this.settings;
    }

    public void setup(String connectionUrl, String username, String password, int timeout) {
        this.settings = new ConnectionSettings(connectionUrl, username, password, timeout);
    }

    public void setup(ConnectionSettings settings) {
        this.settings = settings;
    }

    public void connect() {
        if (!isSetup()) {
            // TODO: log error using logger
            System.out.println("ConnectionManager not setup");
            return;
        }

        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(settings.getConnectionUrl(),settings.getUsername(),settings.getPassword());
            // TODO: log error using logger
            if (connection != null)
                System.out.println("Connection successful");
            else
                System.out.println("Connection failed");
        } catch (Exception e) {
            // TODO: log error using logger
            System.out.println("Error: "+e.getMessage());
        }
    }

    public boolean isConnected() {
        if (!isSetup())
            return false;
        try {
            return this.connection != null && this.connection.isValid(getSettings().getTimeout());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean disconnect() {
        if (isConnected())
        {
            try {
                this.connection.close();
                return true;
            } catch (Exception e) {
                System.out.println("Failed to close connection");
                // TODO: log error using logger
                return false;
            }
        } else
            return false;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
