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
    private String password;
    private String email;
    private String address;
    private String phone;
    public List<Order> orders;

    public User(String firstName, String lastName, String password, String email, String address, String phone) {
        this.id = java.util.UUID.randomUUID();
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.phone = phone;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
