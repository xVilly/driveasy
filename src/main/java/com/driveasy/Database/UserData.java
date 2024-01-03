package com.driveasy.Database;

import java.util.ArrayList;

import com.driveasy.Core.User;

/*
 * Interface that defines the methods for saving and retrieving user data
 * Should be implemented by any manager class that supports this functionality
 */
public interface UserData {
    public final String destinationName = "Users";

    public void SaveUsers(ArrayList<User> users);
    public ArrayList<User> GetUsers();
}
