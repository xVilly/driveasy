package com.driveasy.Controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {
    public static final ArrayList<String> scenes = new ArrayList<String>() {{
        add("LoginPage");
        add("RegisterPage");
    }};

    public static final String startingScene = "LoginPage";

    private HashMap<String, Scene> screenMap = new HashMap<>();
    private Stage primaryStage;

    private static SceneManager instance = null;
    public static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return instance;
    }

    public void Initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void addScreen(String name, Scene scene){
         screenMap.put(name, scene);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void activate(String name){
        Scene scene = screenMap.get(name);
        if (scene == null) {
            System.out.println("Scene "+name+" not found");
            return;
        }
        primaryStage.setScene(scene);
    }

    public void LoadScenes() {
        for (String sceneName : scenes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/"+sceneName+".fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                addScreen(sceneName, scene);
            } catch (Exception e) {
                System.out.println("Error loading scene "+sceneName);
                e.printStackTrace();
            }
        }

        activate(startingScene);
    }
}
