package com.driveasy.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Users.User;
import com.driveasy.Tools.Error;
import com.driveasy.Tools.LoggedError;

import java.nio.file.Paths;
import java.nio.file.Files;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.reflect.TypeToken;

/*
 * File Manager represents database connection (reading and writing data to a text file)
 * Supports json serialization
 */
public class FileManager implements UserData, CarData {
    private static final String dataPath = "data";
    private static final String logPath = "logs";

    private static FileManager instance = null;
    private Gson gson;

    private class FileManagerError extends Error {
        public FileManagerError(String source, String message, String exception) {
            super(source, message, exception);
            Name = "FileManagerError";
        }

        @Override
        public void Handle() {
            System.out.println(this);
        }
    }

    public static FileManager getInstance() {
        if (instance == null)
            instance = new FileManager();
        return instance;
    }

    public FileManager() {
        gson = new Gson();
    }

    public ArrayList<String> ReadFile(String fileName) {
        ArrayList<String> data = new ArrayList<String>();
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              data.add(myReader.nextLine());
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            FileManagerError error = new FileManagerError("ReadFile", "Failed to read file.", e.getMessage());
            error.Handle();
        }
        return data;
    }

    public void WriteFile(String fileName, String data) {
        // append to a text file, create if doesn't exist
        try {
            Files.createDirectories(Paths.get(dataPath));
            Files.createDirectories(Paths.get(logPath));
        } catch (IOException e) {
            FileManagerError error = new FileManagerError("WriteFile", "Failed to create required directory structure.", e.getMessage());
            error.Handle();
        }
        try {
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            FileManagerError error = new FileManagerError("WriteFile", "Failed to write to file.", e.getMessage());
            error.Handle();
        }
    }

    // ReadFile but into a single string
    public String ReadFileAsString(String fileName) {
        String data = "";
        try {
            File myObj = new File(fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
              data += myReader.nextLine();
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            FileManagerError error = new FileManagerError("ReadFileAsString", "Failed to read file as string.", e.getMessage());
            error.Handle();
        }
        return data;
    }

    // Clean a text file
    public void CleanFile(String fileName) {
        try {
            FileWriter myWriter = new FileWriter(fileName);
            myWriter.write("");
            myWriter.close();
        } catch (IOException e) {
            FileManagerError error = new FileManagerError("CleanFile", "Failed to clean file.", e.getMessage());
            error.Handle();
        }
    }

    private String GetFilePath(String fileName) {
        return dataPath+"/"+fileName;
    }

    private String GetJSONFilePath(String fileName) {
        return GetFilePath(fileName)+".json";
    }

    @Override
    public void SaveUsers(ArrayList<User> users) {
        String path = GetJSONFilePath(userDestinationName);
        try {
            String json = gson.toJson(users);
            CleanFile(path);
            WriteFile(path, json);
        } catch (Exception e) {
            FileManagerError error = new FileManagerError("SaveUsers", "Failed to save users to a text file.", e.getMessage());
            error.Handle();
        }
    }

    @Override
    public ArrayList<User> GetUsers() {
        String path = GetJSONFilePath(userDestinationName);
        try {
            String rawData = ReadFileAsString(path);
            ArrayList<User> data = gson.fromJson(rawData, new TypeToken<List<User>>(){}.getType());
            if (data == null) // for some reason gson.fromJson returns null instead of throwing an exception
                throw new JsonIOException("Failed to parse json.");
            return data;
        } catch (Exception e) {
            FileManagerError error = new FileManagerError("GetUsers", "Failed to read users from a text file.", e.getMessage());
            error.Handle();
        }
        return new ArrayList<User>();
    }

    @Override
    public void SaveCars(ArrayList<Car> cars) {
        String path = GetJSONFilePath(carDestinationName);
        try {
            String json = gson.toJson(cars);
            CleanFile(path);
            WriteFile(path, json);
        } catch (Exception e) {
            FileManagerError error = new FileManagerError("SaveCars", "Failed to save cars to a text file.", e.getMessage());
            error.Handle();
        }
    }

    @Override
    public ArrayList<Car> GetCars() {
        String path = GetJSONFilePath(carDestinationName);
        try {
            String rawData = ReadFileAsString(path);
            ArrayList<Car> data = gson.fromJson(rawData, new TypeToken<List<Car>>(){}.getType());
            if (data == null) // for some reason gson.fromJson returns null instead of throwing an exception
                throw new JsonIOException("Failed to parse json.");
            return data;
        } catch (Exception e) {
            FileManagerError error = new FileManagerError("GetCars", "Failed to read cars from a text file.", e.getMessage());
            error.Handle();
        }
        return new ArrayList<Car>();
    }
}
