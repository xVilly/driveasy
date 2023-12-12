package com.driveasy;

import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Label label = new Label("Hello World!");
        StackPane root = new StackPane();
        root.getChildren().add(label);
        Scene scene = new Scene(root, 300, 250);
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Connection connection = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/driveasy","postgres","lol123");
            if (connection != null)
                System.out.println("Connection successful");
            else
                System.out.println("Connection failed");
            connection.close();
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
        launch();
    }
}
