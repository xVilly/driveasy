package com.driveasy.Core.Users;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.driveasy.Core.Orders.Order;
import com.driveasy.Database.FileManager;
import com.driveasy.Database.UserData;
import com.driveasy.Tools.LoggedError;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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

    public boolean IsLoaded() {
        return loaded;
    }

    public List<User> GetUsers() {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before getting data").Handle();
            return null;
        }
        return users;
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

    public UserValidationResult RegisterUser(User user, String inputPassword) {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before registering a user").Handle();
            return UserValidationResult.Error;
        }
        UserValidationResult result = ValidateUserInfo(user, inputPassword);
        if (result != UserValidationResult.Valid) {
            return result;
        }
        users.add(user);
        if (forceSave)
            Save();
        FileManager manager = FileManager.getInstance();
        manager.WriteFile("logs/actions.txt", "["+LocalDateTime.now()+"] User "+user.getEmail()+" has registered. Assigned id: "+user.getId());
        return UserValidationResult.Valid;
    }

    public void RemoveUser(User user) {
        if (!loaded) {
            new UserManagerError("UserManager", "User manager not loaded", "User manager must be loaded before removing a user").Handle();
            return;
        }
        users.remove(user);
        if (forceSave)
            Save();
        FileManager manager = FileManager.getInstance();
        manager.WriteFile("logs/actions.txt", "["+LocalDateTime.now()+"] User "+user.getEmail()+" has been deleted.");
    }

    public User GetUserById(UUID id) {
        for (User user : users) {
            if (user.getId().equals(id))
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
                FileManager manager = FileManager.getInstance();
                manager.WriteFile("logs/actions.txt", "["+LocalDateTime.now()+"] User "+user.getEmail()+" has been updated.");
                return;
            }
        }
    }

    public UserValidationResult ValidateUserInfo(User user, String inputPassword) {
        if (!(user.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$") && user.getEmail().length() > 3 && user.getEmail().length() < 254)) {
            return UserValidationResult.InvalidEmail;
        }
        if (inputPassword.length() < 8) {
            return UserValidationResult.PasswordTooShort;
        }
        if (inputPassword.length() > 128) {
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
            if (user.getEmail().equals(email) && user.getPasswordHash() != null && user.getPasswordSalt() != null) {
                if (user.getPasswordHash().equals(hashSHA512(password, user.getPasswordSalt())))
                    return true;
            }
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
                FileManager manager = FileManager.getInstance();
                manager.WriteFile("logs/actions.txt", "["+LocalDateTime.now()+"] User "+user.getEmail()+" has placed order id "+order.getId()+"");
                return;
            }
        }
    }

    public String hashSHA512(String passwordToHash, String salt){
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt.getBytes(StandardCharsets.UTF_8));
            byte[] bytes = md.digest(passwordToHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String randomString(int targetStringLength) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

        return generatedString;
    }
}
