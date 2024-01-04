package com.driveasy.Controllers;

import java.lang.ModuleLayer.Controller;

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

public class RegisterController implements IController {

    @FXML
    private Button buttonRegister;

    @FXML
    private Button buttonGoBack;

    @FXML
    private TextField textValueAddress;

    @FXML
    private TextField textValueEmail;

    @FXML
    private TextField textValueFirstName;

    @FXML
    private TextField textValueLastName;

    @FXML
    private PasswordField textValuePassword;

    @FXML
    private TextField textValuePhone;

    @FXML
    private Label errorLabel;

    // Interface methods
    public void onActivate() {
        errorLabel.setVisible(false);
        errorLabel.setDisable(true);
        textValueAddress.setText("");
        textValueEmail.setText("");
        textValueFirstName.setText("");
        textValueLastName.setText("");
        textValuePassword.setText("");
        textValuePhone.setText("");
    }

    @FXML
    void onCreateAccount(ActionEvent event) {
        if (textValueFirstName.getText().isEmpty() || textValueLastName.getText().isEmpty() || textValuePassword.getText().isEmpty() ||
                textValueEmail.getText().isEmpty() || textValueAddress.getText().isEmpty() || textValuePhone.getText().isEmpty()) {
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            errorLabel.setText("Please fill in all the fields.");
            errorLabel.setTextFill(Paint.valueOf("#ff0000"));
            return;
        }
        UserManager manager = UserManager.getInstance();
        User newUser = new User(textValueFirstName.getText(), textValueLastName.getText(), textValuePassword.getText(),
                textValueEmail.getText(), textValueAddress.getText(), textValuePhone.getText());
        UserValidationResult result = manager.RegisterUser(newUser);
        errorLabel.setVisible(true);
        errorLabel.setDisable(false);
        switch (result) {
            case InvalidEmail:
                errorLabel.setText("Email address is not formatted properly.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case PasswordTooShort:
                errorLabel.setText("Password should be at least 8 characters long.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case PasswordTooLong:
                errorLabel.setText("Password should be at most 128 characters long.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case NameLengthExceeded:
                errorLabel.setText("Name should be at most 128 characters long.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case InfoLengthExceeded:
                errorLabel.setText("Info should be at most 256 characters long.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case EmailAlreadyExists:
                errorLabel.setText("User with that email address already exists.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            case Error:
                errorLabel.setText("An error occured while creating the account.");
                errorLabel.setTextFill(Paint.valueOf("#ff0000"));
                break;
            default:
            case Valid:
                errorLabel.setText("Account has been created successfully. Please login to continue.");
                errorLabel.setTextFill(Paint.valueOf("#43ba0b"));
                break;
        }
    }

    @FXML
    void onGoBack(ActionEvent event) {
        SceneManager.getInstance().activate("LoginPage");
    }

}