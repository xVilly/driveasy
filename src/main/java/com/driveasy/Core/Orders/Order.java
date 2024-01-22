package com.driveasy.Core.Orders;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.driveasy.Core.Cars.PickupLocation;

public class Order implements Serializable {
    private UUID id;
    private UUID carId;
    private UUID userId;
    private String startDate;
    private String endDate;
    private String pickupLocation;
    private OrderStatus status;

    public Order() {
        this.id = java.util.UUID.randomUUID();
    }

    public UUID getId() {
        return id;
    }
    public UUID getCarId() {
        return carId;
    }
    public void setCarId(UUID carId) {
        this.carId = carId;
    }
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
    public String getStartDate() {
        return startDate;
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate;
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public String getPickupLocation() {
        return pickupLocation;
    }
    public void setPickupLocation(String pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    public OrderStatus getStatus() {
        return status;
    }
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
}
