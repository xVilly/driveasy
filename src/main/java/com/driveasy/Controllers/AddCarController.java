package com.driveasy.Controllers;

import java.util.ArrayList;

import com.driveasy.Core.Cars.Car;
import com.driveasy.Core.Cars.CarCategory;
import com.driveasy.Core.Cars.CarManager;
import com.driveasy.Core.Cars.LuggageCapacity;
import com.driveasy.Core.Cars.PickupLocation;
import com.driveasy.Core.Cars.TransmissionType;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddCarController implements IController {

    @FXML
    private TextField brandName;

    @FXML
    private Button buttonAdd;

    @FXML
    private Button buttonGoBack;

    @FXML
    private ImageView carImage;

    @FXML
    private CheckBox catBig;

    @FXML
    private CheckBox catMedium;

    @FXML
    private CheckBox catMinivan;

    @FXML
    private CheckBox catPremium;

    @FXML
    private CheckBox catSUV;

    @FXML
    private CheckBox catSmall;

    @FXML
    private TextField distanceLimit;

    @FXML
    private TextField doors;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField imageSource;

    @FXML
    private ChoiceBox<String> luggageCapacity;

    @FXML
    private TextField modelName;

    @FXML
    private CheckBox pickupAirport;

    @FXML
    private CheckBox pickupBus;

    @FXML
    private CheckBox pickupRental;

    @FXML
    private CheckBox pickupTrain;

    @FXML
    private TextField pricePerDay;

    @FXML
    private TextField seats;

    @FXML
    private CheckBox transmissionAuto;

    @FXML
    private CheckBox transmissionManual;

    @FXML
    void onAdd(ActionEvent event) {
        // check if all fields are filled
        if (brandName.getText().isEmpty() || modelName.getText().isEmpty() || pricePerDay.getText().isEmpty() || imageSource.getText().isEmpty()) {
            errorLabel.setText("Please fill in all the fields.");
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            return;
        }
        try {
            Car car = new Car();
            car.setBrand(brandName.getText());
            car.setModel(modelName.getText());
            car.setPrice(Integer.parseInt(pricePerDay.getText()));
            car.setImageUrl(imageSource.getText());
            car.setDistanceLimit(Integer.parseInt(distanceLimit.getText()));
            car.setDoors(Integer.parseInt(doors.getText()));
            car.setSeats(Integer.parseInt(seats.getText()));
            ArrayList<CarCategory> categories = new ArrayList<CarCategory>();
            if (catBig.isSelected()) {
                categories.add(CarCategory.Big);
            }
            if (catMedium.isSelected()) {
                categories.add(CarCategory.Medium);
            }
            if (catMinivan.isSelected()) {
                categories.add(CarCategory.Minivan);
            }
            if (catPremium.isSelected()) {
                categories.add(CarCategory.Premium);
            }
            if (catSUV.isSelected()) {
                categories.add(CarCategory.SUV);
            }
            if (catSmall.isSelected()) {
                categories.add(CarCategory.Small);
            }
            car.setCategory(categories);
            ArrayList<PickupLocation> pickupLocations = new ArrayList<PickupLocation>();
            if (pickupAirport.isSelected()) {
                pickupLocations.add(PickupLocation.Airport);
            }
            if (pickupBus.isSelected()) {
                pickupLocations.add(PickupLocation.BusStation);
            }
            if (pickupRental.isSelected()) {
                pickupLocations.add(PickupLocation.Rental);
            }
            if (pickupTrain.isSelected()) {
                pickupLocations.add(PickupLocation.TrainStation);
            }
            car.setPickupLocation(pickupLocations);
            ArrayList<TransmissionType> transmissionTypes = new ArrayList<TransmissionType>();
            if (transmissionManual.isSelected()) {
                transmissionTypes.add(TransmissionType.Manual);
            }
            if (transmissionAuto.isSelected()) {
                transmissionTypes.add(TransmissionType.Automatic);
            }
            car.setTransmissionType(transmissionTypes);
            car.setLuggageCapacity(LuggageCapacity.valueOf(luggageCapacity.getValue()));

            CarManager.getInstance().AddCar(car);
            MainController _mainController = (MainController)SceneManager.getInstance().getController("MainPage");
            if (_mainController != null) {
                _mainController.loadCars();
                _mainController.loadOrders();
            }
            ManagerController _managerController = (ManagerController)SceneManager.getInstance().getController("ManagerPanel");
            if (_managerController != null) {
                _managerController.LoadCars();
                _managerController.LoadOrders();
            }

            SceneManager.getInstance().closePopupWindow("AddCar");
        }
        catch (Exception e) {
            errorLabel.setText("Invalid input.");
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            return;
        }
    }

    @FXML
    void onGoBack(ActionEvent event) {
        SceneManager.getInstance().closePopupWindow("AddCar");
    }

    @FXML
    void onImageChange(ActionEvent event) {
        String source = imageSource.getText();
        if (source.isEmpty()) {
            carImage.setImage(null);
            return;
        }
        try {
            Image image = new Image(source);
            if (image.isError()) {
                errorLabel.setText("Invalid image source.");
                errorLabel.setVisible(true);
                errorLabel.setDisable(false);
                return;
            } else {
                carImage.setImage(image);
            }
        } catch (Exception e) {
            errorLabel.setText("Invalid image source.");
            errorLabel.setVisible(true);
            errorLabel.setDisable(false);
            return;
        }
    }

    @Override
    public void onActivate() {
        luggageCapacity.getItems().clear();
        luggageCapacity.getItems().add("Small");
        luggageCapacity.getItems().add("Medium");
        luggageCapacity.getItems().add("Big");
    }

}
