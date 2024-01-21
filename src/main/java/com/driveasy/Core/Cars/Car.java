package com.driveasy.Core.Cars;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Car implements Serializable {
    private UUID id;
    private String model;
    private String brand;
    private String imageUrl;
    
    private List<CarCategory> category;
    private List<TransmissionType> transmissionType;
    private List<PickupLocation> pickupLocation;
    private int seats;
    private int doors;
    private LuggageCapacity luggageCapacity;
    private int distanceLimit;
    private int price;
    private double rating;
    private CarStatus status;

    public Car() {
        this.id = java.util.UUID.randomUUID();
        this.transmissionType = new ArrayList<TransmissionType>();
        this.pickupLocation = new ArrayList<PickupLocation>();
        this.category = new ArrayList<CarCategory>();
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public List<CarCategory> getCategory() {
        return category;
    }
    public void setCategory(List<CarCategory> category) {
        this.category = category;
    }
    public List<TransmissionType> getTransmissionType() {
        return transmissionType;
    }
    public void setTransmissionType(List<TransmissionType> transmissionType) {
        this.transmissionType = transmissionType;
    }
    public List<PickupLocation> getPickupLocation() {
        return pickupLocation;
    }
    public void setPickupLocation(List<PickupLocation> pickupLocation) {
        this.pickupLocation = pickupLocation;
    }
    public int getSeats() {
        return seats;
    }
    public void setSeats(int seats) {
        this.seats = seats;
    }
    public int getDoors() {
        return doors;
    }
    public void setDoors(int doors) {
        this.doors = doors;
    }
    public LuggageCapacity getLuggageCapacity() {
        return luggageCapacity;
    }
    public void setLuggageCapacity(LuggageCapacity luggageCapacity) {
        this.luggageCapacity = luggageCapacity;
    }
    public int getDistanceLimit() {
        return distanceLimit;
    }
    public void setDistanceLimit(int distanceLimit) {
        this.distanceLimit = distanceLimit;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public CarStatus getStatus() {
        return status;
    }
    public void setStatus(CarStatus status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
