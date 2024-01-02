package com.driveasy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.driveasy.Database.ConnectionManager;
import com.driveasy.Database.FileManager;
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
        FileManager manager = FileManager.getInstance();
        manager.ReadFile("text.txt");
        manager.WriteFile("text.txt", "Hello World\n");
        manager.ReadFile("text.txt");
        launch();
    }
}
