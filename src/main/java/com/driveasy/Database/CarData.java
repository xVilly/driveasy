package com.driveasy.Database;

import java.util.ArrayList;

import com.driveasy.Core.Cars.Car;

/*
 * Interface that defines the methods for saving and retrieving user data
 * Should be implemented by any manager class that supports this functionality
 */
public interface CarData {
    public final String carDestinationName = "Cars";

    public void SaveCars(ArrayList<Car> cars);
    public ArrayList<Car> GetCars();
}
