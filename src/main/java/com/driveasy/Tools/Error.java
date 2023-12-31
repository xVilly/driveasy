package com.driveasy.Tools;

public abstract class Error {
    public String Name = "UndefinedError";
    public String source;
    public String message;
    public String exception;

    public Error(String source, String message, String exception) {
        this.source = source;
        this.message = message;
        this.exception = exception;
    }

    @Override
    public String toString() {
        return "["+ Name + "] "+source+": "+message+" (details: "+exception+")";
    }

    public abstract void Handle();
}
