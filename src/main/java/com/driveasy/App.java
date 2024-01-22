package com.driveasy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.driveasy.Database.FileManager;
import com.driveasy.Database.UserData;

import java.util.ArrayList;
import java.util.List;

import com.driveasy.Controllers.LoginController;
import com.driveasy.Controllers.SceneManager;
import com.driveasy.Core.Users.User;
import com.driveasy.Core.Users.UserManager;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager sceneManager = SceneManager.getInstance();
        primaryStage.setTitle("driveasy");
        sceneManager.Initialize(primaryStage);
        sceneManager.LoadScenes();
        primaryStage.show();
    }

    public static void main(String[] args) {
        UserManager userManager = UserManager.getInstance();
        userManager.InitializeUsingFileManager();
        userManager.Load();
        launch();
    }
}
