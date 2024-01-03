package com.driveasy;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.driveasy.Core.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.driveasy.Database.ConnectionManager;
import com.driveasy.Database.FileManager;
import com.driveasy.Database.UserData;

import java.util.ArrayList;
import java.util.List;

import com.driveasy.Controllers.LoginController;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Scenes/LoginPage.fxml"));
        Parent root = loader.load();
        LoginController controller = loader.getController();
        controller.setMainWindow(primaryStage);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("driveasy");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() {
        ConnectionManager manager = ConnectionManager.getInstance();
        manager.disconnect();
    }

    public static void main(String[] args) {
        UserData manager = (UserData) FileManager.getInstance();
        /*User u1 = new User("John", "Doe", "password", "123@", "125", "3523");
        User u2 = new User("Walter", "White", "password", "wwhite@", "newmexico", "3523");
        ArrayList<User> users = new ArrayList<User>();
        users.add(u1);
        users.add(u2);
        manager.SaveUsers(users);*/
        ArrayList<User> users = manager.GetUsers();
        for (User user : users) {
            System.out.println(user.getFirstName());
        }
        launch();
    }
}
