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
import com.driveasy.Tools.Error;
import java.nio.file.Paths;
import java.nio.file.Files;

import com.driveasy.Core.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/*
 * File Manager represents database (reading and writing data to a text file)
 */
public class FileManager implements UserData {
    private static final String dataPath = "data";

    private static FileManager instance = null;
    private Gson gson;

    private class FileManagerError extends Error {
        public FileManagerError(String source, String message, String exception) {
            super(source, message, exception);
            Name = "FileManagerError";
        }

        @Override
        public String toString() {
            return "["+ Name + "] "+source+": "+message+" (caused by "+exception+")";
        }

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
            System.out.println("Error: "+e.getMessage());
        }
        return data;
    }

    public void WriteFile(String fileName, String data) {
        // append to a text file, create if doesn't exist
        try {
            Files.createDirectories(Paths.get(dataPath));
        } catch (IOException e) {
            FileManagerError error = new FileManagerError("WriteFile", "Failed to create data directory.", e.getMessage());
            error.Handle();
        }
        try {
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
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
            System.out.println("Error: "+e.getMessage());
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
            System.out.println("Error: "+e.getMessage());
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
        String path = GetJSONFilePath(destinationName);
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
        String path = GetJSONFilePath(destinationName);
        try {
            String rawData = ReadFileAsString(path);
            ArrayList<User> data = gson.fromJson(rawData, new TypeToken<List<User>>(){}.getType());
            return data;
        } catch (Exception e) {
            FileManagerError error = new FileManagerError("GetUsers", "Failed to read users from a text file.", e.getMessage());
            error.Handle();
        }
        return new ArrayList<User>();
    }
}
