package com.driveasy.Controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Cars.CarCategory;
import com.driveasy.Core.Cars.CarManager;
import com.driveasy.Core.Cars.CarStatus;
import com.driveasy.Core.Cars.LuggageCapacity;
import com.driveasy.Core.Cars.PickupLocation;
import com.driveasy.Core.Cars.TransmissionType;
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
import javafx.scene.text.Font;

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

    @FXML
    private TextField priceFrom;

    @FXML
    private TextField priceTo;

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

        Label pickupLocation = new Label();
        pickupLocation.setText("Pickup location: "+ order.getPickupLocation());
        carInfo.getChildren().add(pickupLocation);


        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox rightmostInfo = new VBox();
        Button cancelButton = new Button();
        cancelButton.setText("Cancel Order");
        cancelButton.setPrefWidth(100.0);
        cancelButton.setPrefHeight(30.0);
        cancelButton.setOnAction((event) -> {
            User currentUser = UserManager.getInstance().GetCurrentUser();
            if (currentUser == null) {
                return;
            }
            for (Order o : currentUser.orders) {
                if (o.getId().equals(order.getId())) {
                    o.setStatus(OrderStatus.CANCELLED);
                    break;
                }
            }
            UserManager.getInstance().Save();
            loadOrders();
            loadCars();
            ManagerController _managerController = (ManagerController)SceneManager.getInstance().getController("ManagerPanel");
            if (_managerController != null) {
                _managerController.LoadCars();
                _managerController.LoadOrders();
            }
        });
        if (order.getStatus() != OrderStatus.PENDING) {
            cancelButton.setDisable(true);
        } else {
            cancelButton.setDisable(false);
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
        
        AnchorPane carInfo = new AnchorPane();
        AnchorPane.setLeftAnchor(carInfo, 2.0);
        AnchorPane.setRightAnchor(carInfo, 2.0);
        AnchorPane.setBottomAnchor(carInfo, 2.0);
        AnchorPane.setTopAnchor(carInfo, 2.0);
        ImageView carImage = new ImageView();
        carImage.setImage(new Image(car.getImageUrl()));
        HBox.setMargin(carImage, new javafx.geometry.Insets(5.0, 5.0, 5.0, 5.0));
        AnchorPane.setLeftAnchor(carImage, 5.0);
        AnchorPane.setTopAnchor(carImage, 5.0);
        carInfo.getChildren().add(carImage);

        AnchorPane carDetails = new AnchorPane();
        AnchorPane.setLeftAnchor(carDetails, carImage.getImage().getWidth() + 5.0);
        AnchorPane.setRightAnchor(carDetails, 0.0);
        AnchorPane.setBottomAnchor(carDetails, 0.0);
        AnchorPane.setTopAnchor(carDetails, 0.0);
        

        // leftmost info
        VBox leftmostInfo = new VBox();
        VBox leftmostBottomInfo = new VBox();
        VBox rightmostInfo = new VBox();
        VBox rightmostBottomInfo = new VBox();
        Label carName = new Label();
        carName.setText(car.getBrand() + " " + car.getModel());
        carName.setFont(new Font("JetBrains Mono Medium", 24.0));
        leftmostInfo.getChildren().add(carName);

        Label seatsDoors = new Label();
        seatsDoors.setText(car.getSeats() + " seats, " + car.getDoors() + " doors");
        seatsDoors.setFont(new Font("JetBrains Mono Medium", 14.0));
        seatsDoors.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostInfo.getChildren().add(seatsDoors);

        Label luggageCapacity = new Label();
        luggageCapacity.setText("Luggage capacity: " + car.getLuggageCapacity().toString());
        luggageCapacity.setFont(new Font("JetBrains Mono Medium", 12.0));
        luggageCapacity.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostInfo.getChildren().add(luggageCapacity);

        Label transmissionType = new Label();
        String transmissionTypeString = "";
        for (int i = 0; i < car.getTransmissionType().size(); i++) {
            transmissionTypeString += car.getTransmissionType().get(i).toString();
            if (i != car.getTransmissionType().size() - 1) {
                transmissionTypeString += ", ";
            }
        }
        transmissionType.setText("Transmission: " + transmissionTypeString.toLowerCase());
        transmissionType.setFont(new Font("JetBrains Mono Medium", 12.0));
        transmissionType.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostInfo.getChildren().add(transmissionType);

        Label pickupLocation = new Label();
        String pickupLocationString = "";
        for (int i = 0; i < car.getPickupLocation().size(); i++) {
            pickupLocationString += car.getPickupLocation().get(i).toString();
            if (i != car.getPickupLocation().size() - 1) {
                pickupLocationString += ", ";
            }
        }
        pickupLocation.setText("Available pickup locations: " + pickupLocationString.toLowerCase());
        pickupLocation.setFont(new Font("JetBrains Mono Medium", 12.0));
        pickupLocation.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostInfo.getChildren().add(pickupLocation);

        Label distanceLimit = new Label();
        distanceLimit.setText("Distance limit: " + car.getDistanceLimit() + " km");
        distanceLimit.setFont(new Font("JetBrains Mono Medium", 12.0));
        distanceLimit.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostInfo.getChildren().add(distanceLimit);

        Label priceLabel = new Label();
        priceLabel.setText("Price: " + car.getPrice() + "$ per day");
        priceLabel.setFont(new Font("JetBrains Mono Medium", 16.0));
        priceLabel.setTextFill(Paint.valueOf("#4f4e4e"));
        leftmostBottomInfo.getChildren().add(priceLabel);

        Label ratingLabel = new Label();
        ratingLabel.setText("Rating: " + car.getRating() + "⭐ / 5⭐");
        ratingLabel.setFont(new Font("JetBrains Mono Medium", 14.0));
        ratingLabel.setTextFill(Paint.valueOf("#6e6e6e"));
        leftmostBottomInfo.getChildren().add(ratingLabel);


        Button rentButton = new Button();
        rentButton.setText("Rent Car");
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
        rightmostBottomInfo.getChildren().add(rentButton);
        
        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        AnchorPane.setBottomAnchor(leftmostBottomInfo, 5.0);
        AnchorPane.setLeftAnchor(leftmostBottomInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostInfo, 5.0);
        AnchorPane.setBottomAnchor(rightmostBottomInfo, 5.0);
        AnchorPane.setRightAnchor(rightmostBottomInfo, 5.0);
        carDetails.getChildren().add(leftmostInfo);
        carDetails.getChildren().add(leftmostBottomInfo);
        carDetails.getChildren().add(rightmostInfo);
        carDetails.getChildren().add(rightmostBottomInfo);
        carInfo.getChildren().add(carDetails);
        panel.getChildren().add(carInfo);

        browserContent.getChildren().add(panel);
    }

    public void loadCars() {
        browserScroll.setFitToWidth(true);
        browserContent.getChildren().clear();
        CarManager manager = CarManager.getInstance();
        if (!manager.IsInitialized())
            manager.InitializeUsingFileManager();
        if (!manager.IsLoaded())
            manager.Load();
        List<Car> cars = manager.GetCars();
        if (cars == null) {
            return;
        }
        if (sortName.isSelected()) {
            cars.sort(new Comparator<Car>(){
                public int compare(Car o1, Car o2){
                    if (!sortAscend.isSelected())
                        return o2.getBrand().compareTo(o1.getBrand());
                    else
                        return o1.getBrand().compareTo(o2.getBrand());
                }
            });
        } else if (sortPrice.isSelected()) {
            cars.sort(new Comparator<Car>(){
                public int compare(Car o1, Car o2){
                    if (!sortAscend.isSelected())
                        return o2.getPrice() - o1.getPrice();
                    else
                        return o1.getPrice() - o2.getPrice();
                }
            });
        } else if (sortRating.isSelected()) {
            cars.sort(new Comparator<Car>(){
                public int compare(Car o1, Car o2){
                    if (!sortAscend.isSelected())
                        return (int)(o2.getRating() - o1.getRating());
                    else
                        return (int)(o1.getRating() - o2.getRating());
                }
            });
        }
        for (Car car : cars) {
            if (car.getStatus() != CarStatus.Available) {
                continue;
            }
            if (categoryBig.isSelected() && !car.getCategory().contains(CarCategory.Big)) {
                continue;
            }
            if (categoryMedium.isSelected() && !car.getCategory().contains(CarCategory.Medium)) {
                continue;
            }
            if (categoryMinivan.isSelected() && !car.getCategory().contains(CarCategory.Minivan)) {
                continue;
            }
            if (categoryPremium.isSelected() && !car.getCategory().contains(CarCategory.Premium)) {
                continue;
            }
            if (categorySUV.isSelected() && !car.getCategory().contains(CarCategory.SUV)) {
                continue;
            }
            if (categorySmall.isSelected() && !car.getCategory().contains(CarCategory.Small)) {
                continue;
            }
            if (pickupAirport.isSelected() && !car.getPickupLocation().contains(PickupLocation.Airport)) {
                continue;
            }
            if (pickupBus.isSelected() && !car.getPickupLocation().contains(PickupLocation.BusStation)) {
                continue;
            }
            if (pickupRental.isSelected() && !car.getPickupLocation().contains(PickupLocation.Rental)) {
                continue;
            }
            if (transmissionAutomatic.isSelected() && !(car.getTransmissionType().contains(TransmissionType.Automatic) || car.getTransmissionType().contains(TransmissionType.Any))) {
                continue;
            }
            if (transmissionManual.isSelected() && !(car.getTransmissionType().contains(TransmissionType.Manual) || car.getTransmissionType().contains(TransmissionType.Any))) {
                continue;
            }
            if (!priceFrom.getText().isEmpty() && !priceTo.getText().isEmpty()) {
                try {
                    int from = Integer.parseInt(priceFrom.getText());
                    int to = Integer.parseInt(priceTo.getText());
                    if (car.getPrice() < from || car.getPrice() > to) {
                        continue;
                    }
                } catch (Exception e) {
                    continue;
                }
            }
            displayCarCard(car);
        }
    }

    public void onActivate() {
        sortAscend.setSelected(true);

        loadProfile();
        loadCars();
        loadOrders();

        User cu = UserManager.getInstance().GetCurrentUser();
        if (cu != null && cu.isAdmin()) {
            if (!SceneManager.getInstance().isWindowShown("ManagerPanel")) {
                SceneManager.getInstance().openPopupWindow("ManagerPanel", "ManagerPanel", "Manager Panel", false, true);
            }
        }
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
        loadCars();
    }

    @FXML
    void onPickupChange(ActionEvent event) {
        loadCars();
    }

    @FXML
    void onTransmissionChange(ActionEvent event) {
        loadCars();
    }

    @FXML
    void onSortDirection(ActionEvent event) {
        if (event.getSource() == sortAscend)
            sortDescend.setSelected(false);
        else
            sortAscend.setSelected(false);
        loadCars();
    }

    @FXML
    void onSortType(ActionEvent event) {
        if (event.getSource() == sortName){
            sortPrice.setSelected(false);
            sortRating.setSelected(false);
        }
        else if (event.getSource() == sortPrice){
            sortName.setSelected(false);
            sortRating.setSelected(false);
        } else {
            sortName.setSelected(false);
            sortPrice.setSelected(false);
        }
        loadCars();
    }

    @FXML
    void onPriceChange(ActionEvent event) {
        if (priceFrom.getText().isEmpty() || priceTo.getText().isEmpty()) {
            return;
        }
        try {
            int from = Integer.parseInt(priceFrom.getText());
            int to = Integer.parseInt(priceTo.getText());
            if (from > to) {
                return;
            }
        } catch (Exception e) {
            return;
        }
        loadCars();
    }
}