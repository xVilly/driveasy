package com.driveasy.Controllers;

import com.driveasy.Core.Users.User;
import com.driveasy.Core.Users.UserManager;
import com.driveasy.Core.Users.UserValidationResult;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
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
}