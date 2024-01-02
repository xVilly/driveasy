package com.driveasy.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Scanner;

/*
 * File Manager represents database (reading and writing data to a text file)
 */
public class FileManager {
    private static FileManager instance = null;

    public static FileManager getInstance() {
        if (instance == null)
            instance = new FileManager();
        return instance;
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
            FileWriter myWriter = new FileWriter(fileName, true);
            myWriter.write(data);
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    // Generic method to read array of objects from a file
    public <T> ArrayList<T> ReadObjects(String fileName) {
        ArrayList<T> data = new ArrayList<T>();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            data = (ArrayList<T>) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println("Error: "+e.getMessage());
        }
        return data;
    }
    
}
