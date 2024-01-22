package com.driveasy.Controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Cars.CarManager;
import com.driveasy.Core.Cars.CarStatus;
import com.driveasy.Core.Orders.Order;
import com.driveasy.Core.Orders.OrderStatus;
import com.driveasy.Core.Users.User;
import com.driveasy.Core.Users.UserManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import java.util.List;

public class ManagerController implements IController {

    @FXML
    private VBox carsContent;

    @FXML
    private ScrollPane carsScroll;

    @FXML
    private VBox ordersContent;

    @FXML
    private ScrollPane ordersScroll;

    @FXML
    private VBox usersContent;

    @FXML
    private ScrollPane usersScroll;

    private void displayCarCard(Car car) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(30.0);
        panel.setStyle("-fx-background-color: #ffffff;");
        CornerRadii cornerRadii = new CornerRadii(3);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        VBox carInfo = new VBox();
        Label carId = new Label();
        carId.setText("ID: " + car.getId());
        carInfo.getChildren().add(carId);

        Label carName = new Label();
        carName.setText(car.getBrand() + " " + car.getModel());
        carInfo.getChildren().add(carName);

        Label carStatus = new Label();
        carStatus.setText("Status: " + car.getStatus().toString());
        carInfo.getChildren().add(carStatus);

        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox rightmostInfo = new VBox();
        Button removeButton = new Button();
        removeButton.setText("Delete");
        removeButton.setPrefWidth(70.0);
        removeButton.setPrefHeight(20.0);
        removeButton.setOnAction((event) -> {
            CarManager.getInstance().DeleteCar(car);
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });
        VBox.setMargin(removeButton, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));

        Button repairButton = new Button();
        repairButton.setText("Repair");
        repairButton.setPrefWidth(70.0);
        repairButton.setPrefHeight(20.0);
        repairButton.setOnAction((event) -> {
            car.setStatus(CarStatus.Repairing);
            CarManager.getInstance().UpdateCarById(car.getId(), car);
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });
        VBox.setMargin(repairButton, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));

        Button bringButton = new Button();
        bringButton.setText("Bring Back");
        bringButton.setPrefWidth(70.0);
        bringButton.setPrefHeight(20.0);
        bringButton.setOnAction((event) -> {
            car.setStatus(CarStatus.Available);
            CarManager.getInstance().UpdateCarById(car.getId(), car);
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });
        VBox.setMargin(bringButton, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));

        if (car.getStatus() != CarStatus.Available)
            repairButton.setDisable(true);
        
        rightmostInfo.getChildren().add(removeButton);
        if (car.getStatus() == CarStatus.Repairing)
            rightmostInfo.getChildren().add(bringButton);
        else
            rightmostInfo.getChildren().add(repairButton);

        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostInfo, 5.0);
        VBox.setMargin(leftmostInfo, new javafx.geometry.Insets(5.0, 0.0, 5.0, 5.0));
        VBox.setMargin(rightmostInfo, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        leftmostInfo.getChildren().add(carInfo);
        panel.getChildren().add(leftmostInfo);
        panel.getChildren().add(rightmostInfo);

        carsContent.getChildren().add(panel);
    }

    private void displayUserCard(User user) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(30.0);
        panel.setStyle("-fx-background-color: #ffffff;");
        CornerRadii cornerRadii = new CornerRadii(3);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        VBox userInfo = new VBox();
        Label userId = new Label();
        userId.setText("ID: " + user.getId());
        userInfo.getChildren().add(userId);

        Label userName = new Label();
        userName.setText(user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        userInfo.getChildren().add(userName);

        Label userData = new Label();
        userData.setText("Phone: " + user.getPhone() + " | Address: " + user.getAddress());
        userInfo.getChildren().add(userData);

        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox rightmostInfo = new VBox();
        Button removeButton = new Button();
        removeButton.setText("Delete");
        removeButton.setPrefWidth(70.0);
        removeButton.setPrefHeight(20.0);
        removeButton.setOnAction((event) -> {
            UserManager.getInstance().RemoveUser(user);
            LoadUsers();
        });
        VBox.setMargin(removeButton, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        
        rightmostInfo.getChildren().add(removeButton);

        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostInfo, 5.0);
        VBox.setMargin(leftmostInfo, new javafx.geometry.Insets(5.0, 0.0, 5.0, 5.0));
        VBox.setMargin(rightmostInfo, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        leftmostInfo.getChildren().add(userInfo);
        panel.getChildren().add(leftmostInfo);
        panel.getChildren().add(rightmostInfo);

        usersContent.getChildren().add(panel);
    }

    private void displayOrderCard(Order order) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(30.0);
        panel.setStyle("-fx-background-color: #ffffff;");
        CornerRadii cornerRadii = new CornerRadii(3);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        VBox orderInfo = new VBox();
        Label orderId = new Label();
        orderId.setText("ID: " + order.getId());
        orderInfo.getChildren().add(orderId);

        User user = UserManager.getInstance().GetUserById(order.getUserId());
        if (user != null) {
            Label orderUser = new Label();
            orderUser.setText("User: " + user.getFirstName() + " " + user.getLastName() + " (id: " + user.getId() + ")");
            orderInfo.getChildren().add(orderUser);
        }

        Car car = CarManager.getInstance().GetCarById(order.getCarId());
        if (car != null) {
            Label carName = new Label();
            carName.setText(car.getBrand() + " " + car.getModel() + " (id: " + car.getId() + ")");
            orderInfo.getChildren().add(carName);
        }
        
        Label carStatus = new Label();
        carStatus.setText("Status: " + order.getStatus().toString());
        orderInfo.getChildren().add(carStatus);

        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox rightmostInfo = new VBox();
        Button removeButton = new Button();
        removeButton.setText("Delete");
        removeButton.setPrefWidth(70.0);
        removeButton.setPrefHeight(20.0);
        removeButton.setOnAction((event) -> {
            User u = UserManager.getInstance().GetUserById(order.getUserId());
            if (u == null)
                return;
            u.orders.remove(order);
            if (car != null) {
                car.setStatus(CarStatus.Available);
                CarManager.getInstance().UpdateCarById(car.getId(), car);
            }
            UserManager.getInstance().Save();
            LoadOrders();
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });
        VBox.setMargin(removeButton, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        
        rightmostInfo.getChildren().add(removeButton);

        Button acceptButton = new Button();
        acceptButton.setText("Accept");
        acceptButton.setPrefWidth(70.0);
        acceptButton.setPrefHeight(20.0);
        acceptButton.setOnAction((event) -> {
            User u = UserManager.getInstance().GetUserById(order.getUserId());
            if (u == null)
                return;
            for (Order o : u.orders) {
                if (o.getId().equals(order.getId())) {
                    o.setStatus(OrderStatus.ACCEPTED);
                    break;
                }
            }
            UserManager.getInstance().Save();
            car.setStatus(CarStatus.Renting);
            CarManager.getInstance().UpdateCarById(car.getId(), car);
            LoadOrders();
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });
        VBox.setMargin(acceptButton, new javafx.geometry.Insets(0.0, 5.0, 5.0, 0.0));
        acceptButton.setDisable(order.getStatus() != OrderStatus.PENDING);
        rightmostInfo.getChildren().add(acceptButton);

        Button rejectButton = new Button();
        rejectButton.setText("Reject");
        rejectButton.setPrefWidth(70.0);
        rejectButton.setPrefHeight(20.0);
        rejectButton.setOnAction((event) -> {
            User u = UserManager.getInstance().GetUserById(order.getUserId());
            if (u == null)
                return;
            for (Order o : u.orders) {
                if (o.getId().equals(order.getId())) {
                    o.setStatus(OrderStatus.REJECTED);
                    break;
                }
            }
            UserManager.getInstance().Save();
            car.setStatus(CarStatus.Available);
            CarManager.getInstance().UpdateCarById(car.getId(), car);
            LoadOrders();
            LoadCars();
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
        });

        VBox.setMargin(rejectButton, new javafx.geometry.Insets(0.0, 5.0, 5.0, 0.0));
        rejectButton.setDisable(order.getStatus() != OrderStatus.PENDING);
        rightmostInfo.getChildren().add(rejectButton);

        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostInfo, 5.0);
        VBox.setMargin(leftmostInfo, new javafx.geometry.Insets(5.0, 0.0, 5.0, 5.0));
        VBox.setMargin(rightmostInfo, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        leftmostInfo.getChildren().add(orderInfo);
        panel.getChildren().add(leftmostInfo);
        panel.getChildren().add(rightmostInfo);

        ordersContent.getChildren().add(panel);
    }

    public void LoadCars() {
        carsContent.getChildren().clear();
        carsScroll.setFitToWidth(true);
        CarManager manager = CarManager.getInstance();
        if (!manager.IsLoaded())
            manager.Load();
        for (Car car : manager.GetCars()) {
            displayCarCard(car);
        }
    }

    public void LoadUsers() {
        usersContent.getChildren().clear();
        usersScroll.setFitToWidth(true);
        UserManager manager = UserManager.getInstance();
        if (!manager.IsLoaded())
            manager.Load();
        for (User user : manager.GetUsers()) {
            displayUserCard(user);
        }
    }

    public void LoadOrders() {
        ordersContent.getChildren().clear();
        ordersScroll.setFitToWidth(true);
        UserManager manager = UserManager.getInstance();
        if (!manager.IsLoaded())
            manager.Load();
        for (User user : manager.GetUsers()) {
            if (user.orders == null)
                continue;
            for (Order order : user.orders) {
                displayOrderCard(order);
            }
        }
    }

    @Override
    public void onActivate() {
        LoadCars();
        LoadOrders();
        LoadUsers();
    }

    @FXML
    void onAddCar(ActionEvent event) {
        if (!SceneManager.getInstance().isWindowShown("AddCar")) {
            SceneManager.getInstance().openPopupWindow("AddCar", "AddCar", "Add Car", true, false);
        } else {
            SceneManager.getInstance().closePopupWindow("AddCar");
        }
    }

}
