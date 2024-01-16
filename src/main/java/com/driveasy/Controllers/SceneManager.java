package com.driveasy.Controllers;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class SceneManager {
    public static final ArrayList<String> scenes = new ArrayList<String>() {{
        add("LoginPage");
        add("RegisterPage");
        add("MainPage");
        add("EditProfile");
    }};

    public static final String startingScene = "LoginPage";

    private HashMap<String, Parent> screenMap = new HashMap<>();
    private HashMap<String, IController> controllerMap = new HashMap<>();
    private HashMap<String, Stage> openWindows = new HashMap<>();
    private Stage primaryStage;

    private static SceneManager instance = null;
    public static SceneManager getInstance() {
        if (instance == null)
            instance = new SceneManager();
        return instance;
    }

    public void Initialize(Stage primaryStage) {
        this.primaryStage = primaryStage;
        openWindows.put("primaryStage", primaryStage);
    }

    public void addScreen(String name, Parent root){
         screenMap.put(name, root);
    }

    public void addController(String name, IController controller){
        controllerMap.put(name, controller);
    }

    public void removeScreen(String name){
        screenMap.remove(name);
    }

    public void removeController(String name){
        controllerMap.remove(name);
    }

    public void activate(String name){
        Parent root = screenMap.get(name);
        IController controller = controllerMap.get(name);
        if (root == null) {
            System.out.println("Scene "+name+" not found");
            return;
        }
        if (primaryStage.getScene() == null)
            primaryStage.setScene(new Scene(root));
        else
            primaryStage.getScene().setRoot(root);

        controller.onActivate();
    }

    public void openPopupWindow(String windowName, String rootName, String title, boolean alwaysOnTop) {
        if (isWindowShown(windowName))
            return;
        if (isWindowLoaded(windowName))
            closePopupWindow(windowName); // make sure its gone
        Parent root = screenMap.get(rootName);
        IController controller = controllerMap.get(rootName);
        if (root == null) {
            System.out.println("Scene "+rootName+" not found");
            return;
        }
        Stage s = new Stage();
        openWindows.put(windowName, s);
        s.setScene(new Scene(root));
        s.setAlwaysOnTop(alwaysOnTop);
        s.setTitle(title);
        controller.onActivate();
        s.show();
    }

    public boolean isWindowShown(String windowName) {
        Stage s = openWindows.get(windowName);
        return s != null && s.isShowing();
    }

    public boolean isWindowLoaded(String windowName) {
        return openWindows.get(windowName) != null;
    }

    public void closePopupWindow(String windowName) {
        Stage s = openWindows.get(windowName);
        if (s != null){
            s.close();
            Parent p = new FlowPane(); // create a temporary root
            s.getScene().setRoot(p);
            s.setScene(null);
        }
        openWindows.remove(windowName);
    }

    public <ControllerType> ControllerType getController(String name) {
        IController controller = controllerMap.get(name);
        if (controller != null && controller.getClass().isInstance(controller))
            return (ControllerType) controller;
        return null;
    }

    public void LoadScenes() {
        for (String sceneName : scenes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/"+sceneName+".fxml"));
                Parent root = loader.load();
                IController controller = loader.getController();
                addScreen(sceneName, root);
                addController(sceneName, controller);
            } catch (Exception e) {
                System.out.println("Error loading scene "+sceneName);
                e.printStackTrace();
            }
        }

        activate(startingScene);
    }
}
