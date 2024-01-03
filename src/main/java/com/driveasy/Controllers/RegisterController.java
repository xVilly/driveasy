package com.driveasy.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {

    @FXML
    private Button buttonRegister;

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

    @FXML
    void onLogin(ActionEvent event) {
        errorLabel.setText("Login");
        errorLabel.setVisible(true);
        errorLabel.setDisable(false);
    }

}