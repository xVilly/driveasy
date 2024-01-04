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

public class EditProfileController implements IController {
    


    @FXML
    private TextField profileEditAddress;

    @FXML
    private TextField profileEditEmail;

    @FXML
    private TextField profileEditFirstName;

    @FXML
    private TextField profileEditLastName;

    @FXML
    private TextField profileEditPhone;

    public void loadProfile() {
        User user = UserManager.getInstance().GetCurrentUser();
        if (user != null) {
            profileEditAddress.setText(user.getAddress());
            profileEditEmail.setText(user.getEmail());
            profileEditFirstName.setText(user.getFirstName());
            profileEditLastName.setText(user.getLastName());
            profileEditPhone.setText(user.getPhone());
        }
    }

    public void onActivate() {
        loadProfile();
    }

    @FXML
    void onCancel(ActionEvent event) {
        SceneManager.getInstance().closePopupWindow("editProfileWindow");
    }

    @FXML
    void onConfirm(ActionEvent event) {
        User u = UserManager.getInstance().GetCurrentUser();
        u.setAddress(profileEditAddress.getText());
        u.setEmail(profileEditEmail.getText());
        u.setFirstName(profileEditFirstName.getText());
        u.setLastName(profileEditLastName.getText());
        u.setPhone(profileEditPhone.getText());
        UserManager.getInstance().SetCurrentUser(u);
        UserManager.getInstance().UpdateUserById(u.getId().toString(), u);
        SceneManager.getInstance().closePopupWindow("editProfileWindow");
        MainController mainController = SceneManager.getInstance().getController("MainPage");
        if (mainController != null) {
            mainController.loadProfile();
        }
    }
}