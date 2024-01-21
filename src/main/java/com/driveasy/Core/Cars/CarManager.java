package com.driveasy.Core.Cars;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.driveasy.Core.Users.User;
import com.driveasy.Database.CarData;
import com.driveasy.Database.FileManager;
import com.driveasy.Database.UserData;
import com.driveasy.Tools.LoggedError;

public class CarManager {
    private static final boolean forceSave = true;
    private boolean initialized = false;
    private boolean loaded = false;

    private CarData dataManager;
    private ArrayList<Car> cars;

    private CarManager() {
        cars = new ArrayList<Car>();
    }

    private static CarManager instance = null;
    public static CarManager getInstance() {
        if (instance == null)
            instance = new CarManager();
        return instance;
    }

    private class CarManagerError extends LoggedError {
        public CarManagerError(String source, String message, String exception) {
            super(source, message, exception);
            Name = "CarManagerError";
        }

        @Override
        public void Handle() {
            super.Handle();
            System.out.println(this);
        }
    }

    public void InitializeUsingFileManager() {
        dataManager = (CarData) FileManager.getInstance();
        initialized = true;
    }

    public void Save() {
        if (!initialized) {
            new CarManagerError("CarManager", "Car manager not initialized", "Car manager must be initialized before saving data").Handle();
            return;
        }
        dataManager.SaveCars(cars);
    }

    public void Load() {
        if (!initialized) {
            new CarManagerError("CarManager", "Car manager not initialized", "Car manager must be initialized before loading data").Handle();
            return;
        }
        cars = dataManager.GetCars();
        loaded = true;
    }

    public void AddCar(Car car) {
        cars.add(car);
        if (forceSave)
            Save();
    }

    public Car GetCarById(UUID id) {
        if (!loaded) {
            new CarManagerError("CarManager", "Car manager not loaded", "Car manager must be loaded before getting data").Handle();
            return null;
        }
        for (Car car : cars) {
            if (car.getId().equals(id))
                return car;
        }
        return null;
    }

    public List<Car> GetCars() {
        if (!loaded) {
            new CarManagerError("CarManager", "Car manager not loaded", "Car manager must be loaded before getting data").Handle();
            return null;
        }
        return cars;
    }

    public void UpdateCarById(UUID id, Car newCar) {
        for (Car car : cars) {
            if (car.getId().equals(id)) {
                cars.remove(car);
                cars.add(newCar);
                if (forceSave)
                    Save();
                return;
            }
        }
    }

    public void DeleteCar(Car car) {
        cars.remove(car);
        if (forceSave)
            Save();
        FileManager manager = FileManager.getInstance();
        manager.WriteFile("logs/actions.txt", "["+LocalDate.now()+"] Car "+car.getId()+" has been deleted (name: "+car.getBrand()+" "+car.getModel()+")");
    }

    public boolean IsLoaded() {
        return loaded;
    }
}
