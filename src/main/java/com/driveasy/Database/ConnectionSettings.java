package com.driveasy.Database;

public class ConnectionSettings {
    private String connectionUrl = null;
    private String username = null;
    private String password = null;
    private int timeout = 5;

    public ConnectionSettings(String connectionUrl, String username, String password, int timeout) {
        this.connectionUrl = connectionUrl;
        this.username = username;
        this.password = password;
        this.timeout = timeout;
    }

    public String getConnectionUrl() {
        return this.connectionUrl;
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public int getTimeout() {
        return this.timeout;
    }
}
