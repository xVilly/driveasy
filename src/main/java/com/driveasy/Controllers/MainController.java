package com.driveasy.Controllers;

import java.net.URL;

import javax.imageio.ImageIO;

import com.driveasy.Core.Cars.Car;
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

    public void displayCarCard(Car car) {
        AnchorPane panel = new AnchorPane();
        panel.setPrefHeight(200.0);
        CornerRadii cornerRadii = new CornerRadii(5);
        panel.setBorder(new Border(new BorderStroke(Paint.valueOf("#000000"), BorderStrokeStyle.SOLID, cornerRadii, BorderWidths.DEFAULT)));
        VBox.setMargin(panel, new javafx.geometry.Insets(0.0, 0.0, 5.0, 0.0));
        
        HBox carInfo = new HBox();
        ImageView carImage = new ImageView();
        carImage.setImage(new Image("/Images/Fiat500.jpg"));
        carInfo.getChildren().add(carImage);

        // leftmost info
        VBox leftmostInfo = new VBox();
        Label carName = new Label();
        carName.setText(car.getModel() + " " + car.getBrand());
        carName.setLayoutX(14.0);
        carName.setLayoutY(14.0);
        leftmostInfo.getChildren().add(carName);
        AnchorPane.setLeftAnchor(leftmostInfo, 5.0);
        carInfo.getChildren().add(leftmostInfo);
        panel.getChildren().add(carInfo);

        browserContent.getChildren().add(panel);
    }

    public void loadCars() {
        browserScroll.setFitToWidth(true);
        browserContent.getChildren().clear();
        for (int i = 0; i < 10; i++) {
            Car car = new Car();
            car.setBrand("BMW");
            car.setModel("M3");
            displayCarCard(car);
        }
    }

    public void onActivate() {
        loadProfile();
        loadCars();
    }

    @FXML
    void onLogOut(ActionEvent event) {
        SceneManager.getInstance().activate("LoginPage");
    }

    @FXML
    void onEditProfile() {
        if (!SceneManager.getInstance().isWindowShown("editProfileWindow")) {
            SceneManager.getInstance().openPopupWindow("editProfileWindow", "EditProfile", "Edit Profile", true);
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