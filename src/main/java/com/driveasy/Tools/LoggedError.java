package com.driveasy.Tools;

import com.driveasy.Database.FileManager;

public abstract class LoggedError extends Error {
    public LoggedError(String source, String message, String exception) {
        super(source, message, exception);
    }

    private void Log() {
        FileManager manager = FileManager.getInstance();
        manager.WriteFile("logs/errors.txt", this.toString());
    }

    public void Handle() {
        Log();
    }
}
