package com.driveasy.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Cars.CarManager;
import com.driveasy.Core.Cars.LuggageCapacity;
import com.driveasy.Core.Cars.PickupLocation;
import com.driveasy.Core.Orders.Order;
import com.driveasy.Core.Orders.OrderStatus;
import com.driveasy.Core.Users.User;
import com.driveasy.Core.Users.UserManager;
import com.driveasy.Core.Users.UserValidationResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

public class MainController implements IController {
    @FXML
    private Button buttonLogOut;

    @FXML
    private Label nameLabel;

    @FXML
    private Label profileAddress;

    @FXML
    private Label profileEmail;

    @FXML
    private Label profileFirstName;

    @FXML
    private Label profileLastName;

    @FXML
    private Label profilePhoneNumber;

    // Car Browser
    @FXML
    private CheckBox categoryBig;

    @FXML
    private CheckBox categoryMedium;

    @FXML
    private CheckBox categoryMinivan;

    @FXML
    private CheckBox categoryPremium;

    @FXML
    private CheckBox categorySUV;

    @FXML
    private CheckBox categorySmall;

    @FXML
    private CheckBox pickupAirport;

    @FXML
    private CheckBox pickupBus;

    @FXML
    private CheckBox pickupRental;

    @FXML
    private CheckBox pickupTrain;

    @FXML
    private CheckBox transmissionAutomatic;

    @FXML
    private CheckBox transmissionManual;

    @FXML
    private RadioButton sortAscend;

    @FXML
    private RadioButton sortDescend;

    @FXML
    private RadioButton sortName;

    @FXML
    private RadioButton sortPrice;

    @FXML
    private RadioButton sortRating;

    @FXML
    private VBox browserContent;

    @FXML
    private ScrollPane browserScroll;

    @FXML
    private ScrollPane ordersScroll;

    @FXML
    private VBox ordersContent;

    public void loadProfile() {
        User user = UserManager.getInstance().GetCurrentUser();
        if (user != null) {
            profileAddress.setText(user.getAddress());
            profileEmail.setText(user.getEmail());
            profileFirstName.setText(user.getFirstName());
            profileLastName.setText(user.getLastName());
            profilePhoneNumber.setText(user.getPhone());
            nameLabel.setText("Logged in as "+ user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        }
    }

    public void loadOrders() {
        ordersScroll.setFitToWidth(true);
        ordersContent.getChildren().clear();
        User user = UserManager.getInstance().GetCurrentUser();
        if (user == null) {
            return;
        }
        for (Order order : user.orders) {
            displayOrderCard(order);
        }
    }

    private void displayOrderCard(Order order) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(100.0);
        panel.setStyle("-fx-background-color: #ffffff;");
        CornerRadii cornerRadii = new CornerRadii(5);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        Car car = CarManager.getInstance().GetCarById(order.getCarId());
        if (car == null) {
            System.out.println("car not found for order " + order.getId() + "");
            return;
        }
        VBox carInfo = new VBox();
        Label carName = new Label();
        carName.setText(car.getBrand() + " " + car.getModel());
        carInfo.getChildren().add(carName);

        Label orderStatus = new Label();
        orderStatus.setText("Status: " + order.getStatus().toString());
        carInfo.getChildren().add(orderStatus);

        Label carStartDate = new Label();
        carStartDate.setText("Start date: " + order.getStartDate());
        carInfo.getChildren().add(carStartDate);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ENGLISH);
        LocalDate startDate = LocalDate.parse(order.getStartDate(), formatter);
        LocalDate endDate = LocalDate.parse(order.getEndDate(), formatter);
        Label carDuration = new Label();
        int daysElapsed = (int)java.time.temporal.ChronoUnit.DAYS.between( startDate , endDate ) ;
        carDuration.setText("Duration: " + (daysElapsed) + " days");
        carInfo.getChildren().add(carDuration);

        Label carPrice = new Label();
        carPrice.setText("Total price: " + (car.getPrice() * daysElapsed) + "$");
        carInfo.getChildren().add(carPrice);


        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox rightmostInfo = new VBox();
        Button cancelButton = new Button();
        cancelButton.setText("Cancel Order");
        cancelButton.setLayoutX(14.0);
        cancelButton.setLayoutY(14.0);
        cancelButton.setPrefWidth(100.0);
        cancelButton.setPrefHeight(30.0);
        cancelButton.setOnAction((event) -> {
            
        });
        if (order.getStatus() != OrderStatus.PENDING) {
            cancelButton.setDisable(false);
        } else {
            cancelButton.setDisable(true);
        }
        leftmostInfo.getChildren().add(carName);
        rightmostInfo.getChildren().add(cancelButton);

        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostInfo, 5.0);
        VBox.setMargin(leftmostInfo, new javafx.geometry.Insets(5.0, 0.0, 5.0, 5.0));
        VBox.setMargin(rightmostInfo, new javafx.geometry.Insets(5.0, 5.0, 5.0, 0.0));
        leftmostInfo.getChildren().add(carInfo);
        panel.getChildren().add(leftmostInfo);
        panel.getChildren().add(rightmostInfo);

        ordersContent.getChildren().add(panel);
    }

    public void displayCarCard(Car car) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(200.0);
        panel.setStyle("-fx-background-color: #ffffff;");
        CornerRadii cornerRadii = new CornerRadii(5);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        HBox carInfo = new HBox();
        ImageView carImage = new ImageView();
        carImage.setImage(new Image(car.getImageUrl()));
        carInfo.getChildren().add(carImage);

        // leftmost info
        VBox leftmostInfo = new VBox();
        Label carName = new Label();
        carName.setText(car.getModel() + " " + car.getBrand());
        carName.setLayoutX(14.0);
        carName.setLayoutY(14.0);

        Button rentButton = new Button();
        rentButton.setText("Rent");
        rentButton.setLayoutX(14.0);
        rentButton.setLayoutY(14.0);
        rentButton.setPrefWidth(100.0);
        rentButton.setPrefHeight(30.0);
        rentButton.setOnAction((event) -> {
            if (!SceneManager.getInstance().isWindowShown("OrderPage")) {
                Order o = new Order();
                o.setCarId(car.getId());
                o.setUserId(UserManager.getInstance().GetCurrentUser().getId());
                UserManager.getInstance().SetCurrentOrder(o);
                SceneManager.getInstance().openPopupWindow("OrderPage", "OrderPage", "Rent Car", true, false);
            } else {
                SceneManager.getInstance().closePopupWindow("OrderPage");
            }
        });
        leftmostInfo.getChildren().add(rentButton);
        leftmostInfo.getChildren().add(carName);
        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        carInfo.getChildren().add(leftmostInfo);
        panel.getChildren().add(carInfo);

        browserContent.getChildren().add(panel);
    }

    public void loadCars() {
        browserScroll.setFitToWidth(true);
        browserContent.getChildren().clear();
        CarManager manager = CarManager.getInstance();
        manager.InitializeUsingFileManager();
        manager.Load();
        for (Car car : manager.GetCars()) {
            List<PickupLocation> locations = new ArrayList<PickupLocation>();
            locations.add(PickupLocation.Airport);
            locations.add(PickupLocation.BusStation);
            car.setPickupLocation(locations);
            car.setLuggageCapacity(LuggageCapacity.Medium);
            car.setPrice((int)(100.0 + (Math.random() * 400.0)));
            displayCarCard(car);
        }
        manager.Save();
        System.out.println("saved cars");
    }

    public void onActivate() {
        loadProfile();
        loadCars();
        loadOrders();
    }

    @FXML
    void onLogOut(ActionEvent event) {
        SceneManager.getInstance().activate("LoginPage");
    }

    @FXML
    void onEditProfile() {
        if (!SceneManager.getInstance().isWindowShown("editProfileWindow")) {
            SceneManager.getInstance().openPopupWindow("editProfileWindow", "EditProfile", "Edit Profile", true, false);
        } else {
            SceneManager.getInstance().closePopupWindow("editProfileWindow");
        }
    }


    @FXML
    void onCategoryChange(ActionEvent event) {

    }

    @FXML
    void onPickupChange(ActionEvent event) {

    }

    @FXML
    void onTransmissionChange(ActionEvent event) {

    }

    @FXML
    void onSortDirection(ActionEvent event) {

    }

    @FXML
    void onSortType(ActionEvent event) {

    }
}