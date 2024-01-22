package com.driveasy.Core.Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.driveasy.Core.Orders.Order;


public class User implements Serializable {
    private UUID id;
    private String firstName;
    private String lastName;
    private String passwordHash;
    private String passwordSalt;
    private String email;
    private String address;
    private String phone;
    private boolean isAdmin;
    

    public List<Order> orders;

    public User(String firstName, String lastName, String passwordHash, String passwordSalt, String email, String address, String phone) {
        this.id = java.util.UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.email = email;
        this.address = address;
        this.phone = phone;
        this.isAdmin = false;
        orders = new ArrayList<Order>();
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void AddOrder(Order order) {
        if (orders == null)
            orders = new ArrayList<Order>();
        orders.add(order);
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
