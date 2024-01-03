package com.driveasy.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    // FXML fields
    
    @FXML
    private TextField textValueEmail;
    @FXML
    private PasswordField textValuePassword;

    // FXML events

    @FXML
    void onCreateAccount(ActionEvent event) {
        SceneManager.getInstance().activate("RegisterPage");
    }

    @FXML
    void onLogin(ActionEvent event) {
        System.out.println("Email: "+textValueEmail.getText());
        System.out.println("Password: "+textValuePassword.getText());
    }
}
