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
        ConnectionManager manager = ConnectionManager.getInstance();
        manager.setup( "jdbc:postgresql://localhost:5432/driveasy?useUnicode=yes&characterEncoding=UTF-8", "postgres", "lol123", 5);
        manager.connect();
        Connection connection = manager.getConnection();
        try {
            System.out.println(manager.isConnected());
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM users;", new String[] { "id", "name", "email", "password" });
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                System.out.println(result.getString("id") + " " + result.getString("name") +" " + result.getString("email") +" " + result.getString("password"));
            }
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        manager.disconnect();
        launch();
    }
}
