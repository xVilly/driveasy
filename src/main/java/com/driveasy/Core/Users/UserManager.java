package com.driveasy.Core.Users;

import java.util.ArrayList;

import com.driveasy.Core.Orders.Order;
import com.driveasy.Database.FileManager;
import com.driveasy.Database.UserData;
import com.driveasy.Tools.LoggedError;

public class UserManager {
    private static final boolean forceSave = true;
    private boolean initialized = false;
    private boolean loaded = false;

    private UserData dataManager;
    private ArrayList<User> users;

    private static UserManager instance = null;
    public static UserManager getInstance() {
        if (instance == null)
            instance = new UserManager();
        return instance;
    }

    private User currentUser = null;
    private Order currentOrder = null;

    public User GetCurrentUser() {
        return currentUser;
    }

    public void SetCurrentUser(User user) {
        currentUser = user;
    }

    public Order GetCurrentOrder() {
        return currentOrder;
    }

    public void SetCurrentOrder(Order order) {
        currentOrder = order;
    }

    private class UserManagerError extends LoggedError {
        public UserManagerError(String source, String message, String exception) {
            super(source, message, exception);
            Name = "UserManagerError";
        }

        @Override
        public void Handle() {
            super.Handle();
            System.out.println(this);
        }
    }

    /*
     * Initialize the user manager using a specific data manager for writing/reading user data
     */
    public void InitializeUsingFileManager() {
        dataManager = (UserData) FileManager.getInstance();
        initialized = true;
    }

    public void Save() {
        if (!initialized) {
            new UserManagerError("UserManager", "User manager not initialized", "User manager must be initialized before saving data").Handle();
            return;
        }
        dataManager.SaveUsers(users);
    }

    public void Load() {
        if (!initialized) {
            new UserManagerError("UserManager", "User manager not initialized", "User manager must be initialized before loading data").Handle();
            return;
        }
        users = dataManager.GetUsers();
        loaded = true;
    }

    public UserValidationResult RegisterUser(User user) {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before registering a user").Handle();
            return UserValidationResult.Error;
        }
        UserValidationResult result = ValidateUserInfo(user);
        if (result != UserValidationResult.Valid) {
            return result;
        }
        users.add(user);
        if (forceSave)
            Save();
        return UserValidationResult.Valid;
    }

    public void RemoveUser(User user) {
        users.remove(user);
        if (forceSave)
            Save();
    }

    public User GetUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(java.util.UUID.fromString(id)))
                return user;
        }
        return null;
    }

    public User GetUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email))
                return user;
        }
        return null;
    }

    public void RemoveUserById(String id) {
        for (User user : users) {
            if (user.getId().equals(java.util.UUID.fromString(id))) {
                users.remove(user);
                if (forceSave)
                    Save();
                return;
            }
        }
    }

    public void UpdateUserById(String id, User newUser) {
        for (User user : users) {
            if (user.getId().equals(java.util.UUID.fromString(id))) {
                users.remove(user);
                users.add(newUser);
                if (forceSave)
                    Save();
                return;
            }
        }
    }

    public UserValidationResult ValidateUserInfo(User user) {
        if (!(user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$") && user.getEmail().length() > 3 && user.getEmail().length() < 254)) {
            return UserValidationResult.InvalidEmail;
        }
        if (user.getPassword().length() < 8) {
            return UserValidationResult.PasswordTooShort;
        }
        if (user.getPassword().length() > 128) {
            return UserValidationResult.PasswordTooLong;
        }
        if (user.getFirstName().length() > 128 || user.getLastName().length() > 128) {
            return UserValidationResult.NameLengthExceeded;
        }
        if (user.getAddress().length() > 256 || user.getPhone().length() > 256) {
            return UserValidationResult.InfoLengthExceeded;
        }

        if (GetUserByEmail(user.getEmail()) != null) {
            return UserValidationResult.EmailAlreadyExists;
        }

        return UserValidationResult.Valid;
    }

    public boolean CanUserLogin(String email, String password) {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before logging in a user").Handle();
            return false;
        }
        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(password))
                return true;
        }
        return false;
    }

    public void PlaceOrder(Order order) {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before placing an order").Handle();
            return;
        }
        for (User user : users) {
            if (user.getId().equals(order.getUserId())) {
                user.orders.add(order);
                if (forceSave)
                    Save();
                return;
            }
        }
    }
}
