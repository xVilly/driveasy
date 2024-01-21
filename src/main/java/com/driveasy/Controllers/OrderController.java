package com.driveasy.Controllers;

import java.util.Date;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Cars.CarManager;
import com.driveasy.Core.Orders.Order;
import com.driveasy.Core.Orders.OrderStatus;
import com.driveasy.Core.Users.User;
import com.driveasy.Core.Users.UserManager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;

public class OrderController implements IController {

    private Car selectedCar;

    @FXML
    private Label carName;

    @FXML
    private Button buttonConfirm;

    @FXML
    private Button buttonGoBack;

    @FXML
    private ImageView carImage;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label durationLabel;

    @FXML
    private Slider durationSlider;

    @FXML
    private Label errorLabel;

    @FXML
    private ChoiceBox<String> locationPicker;

    @FXML
    private Label priceLabel;

    @FXML
    void onChangeDate(ActionEvent event) {
        if (datePicker.getValue() == null)
            return;
        if (datePicker.getValue().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())) {
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            errorLabel.setText("Please select a date in the future.");
            errorLabel.setTextFill(Paint.valueOf("#ff0000"));
        }
        else {
            errorLabel.setVisible(false);
            errorLabel.setDisable(true);
        }
    }

    @FXML
    void onConfirm(ActionEvent event) {
        boolean error = false;
        String errorMessage = "";
        if (datePicker.getValue() == null) {
            error = true;
            errorMessage = "Please select a date.";
        }
        else if (datePicker.getValue().isBefore(new Date().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate())) {
            error = true;
            errorMessage = "Please select a date in the future.";
        }
        if (locationPicker.getValue() == null) {
            error = true;
            errorMessage = "Please select a location.";
        }
        if (error) {
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            errorLabel.setText(errorMessage);
            errorLabel.setTextFill(Paint.valueOf("#ff0000"));
            return;
        }
        Order currentOrder = UserManager.getInstance().GetCurrentOrder();
        currentOrder.setPickupLocation(locationPicker.getValue());
        currentOrder.setStartDate(datePicker.getValue().toString());
        currentOrder.setEndDate(datePicker.getValue().plusDays((int)durationSlider.getValue()).toString());
        currentOrder.setStatus(OrderStatus.PENDING);
        User currentUser = UserManager.getInstance().GetCurrentUser();
        currentUser.AddOrder(currentOrder);
        UserManager.getInstance().Save();
        SceneManager.getInstance().closePopupWindow("OrderPage");
    }

    @FXML
    void onGoBack(ActionEvent event) {
        SceneManager.getInstance().closePopupWindow("OrderPage");
    }

    void LoadCar() {
        if (selectedCar == null)
            return;
        carImage.setImage(new Image(selectedCar.getImageUrl()));
        double sliderValue = durationSlider.getValue();
        priceLabel.setText(((double)(selectedCar.getPrice() * (int)sliderValue)) + " $");
        locationPicker.getItems().clear();
        for (int i = 0; i < selectedCar.getPickupLocation().size(); i++) {
            locationPicker.getItems().add(selectedCar.getPickupLocation().get(i).toString());
        }
        carName.setText(selectedCar.getBrand() + " " + selectedCar.getModel());
    }

    void onUpdateSlider(double newSliderValue) {
        if ((int)newSliderValue == 1)
            durationLabel.setText((int)newSliderValue + " day");
        else
            durationLabel.setText((int)newSliderValue + " days");
        priceLabel.setText(((double)(selectedCar.getPrice() * (int)newSliderValue)) + " $");
    }

    @Override
    public void onActivate() {
        Order currentOrder = UserManager.getInstance().GetCurrentOrder();
        if (currentOrder == null) {
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            errorLabel.setText("Order not found.");
            errorLabel.setTextFill(Paint.valueOf("#ff0000"));
            return;
        }
        selectedCar = CarManager.getInstance().GetCarById(currentOrder.getCarId()); 
        if (selectedCar == null) {
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            errorLabel.setText("No car selected.");
            errorLabel.setTextFill(Paint.valueOf("#ff0000"));
            return;
        }
        durationSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            onUpdateSlider(newValue.intValue());
        });
        LoadCar();
    }

}
