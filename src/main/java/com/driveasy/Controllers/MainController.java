package com.driveasy.Controllers;

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
import javafx.scene.control.TextField;
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

    public void onActivate() {
        loadProfile();
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