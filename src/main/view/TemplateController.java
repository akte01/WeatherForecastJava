package main.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import main.model.ForecastFunctions;


import java.net.URL;
import java.util.ResourceBundle;

public class TemplateController implements Initializable {

    @FXML
    private GridPane currentCityBackground;
    @FXML
    private TextField currentCity;
    @FXML
    private Label currentCityName;
    @FXML
    private Label currentDateForCurrentCity;
    @FXML
    private Label currentTemperatureForCurrentCity;
    @FXML
    private Label currentMinMaxTemperatureForCurrentCity;
    @FXML
    private ImageView currentIconForCurrentCity;
    @FXML
    private Label currentHumidityForCurrentCity;
    @FXML
    private Label currentPressureForCurrentCity;
    @FXML
    private HBox currentDayNextHoursDataForCurrentCity;
    @FXML
    private GridPane nextDaysDataForCurrentCity;

    @FXML
    private GridPane targetCityBackground;
    @FXML
    private TextField targetCity;
    @FXML
    private Label targetCityName;
    @FXML
    private Label currentDateForTargetCity;
    @FXML
    private Label currentTemperatureForTargetCity;
    @FXML
    private Label currentMinMaxTemperatureForTargetCity;
    @FXML
    private ImageView currentIconForTargetCity;
    @FXML
    private Label currentHumidityForTargetCity;
    @FXML
    private Label currentPressureForTargetCity;
    @FXML
    private HBox currentDayNextHoursDataForTargetCity;
    @FXML
    private GridPane nextDaysDataForTargetCity;

    ForecastFunctions functions;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        functions = new ForecastFunctions(currentCity, targetCity);

        Platform.runLater(() -> currentCity.requestFocus());
    }

    @FXML
    public void loadDataForCurrentCity(ActionEvent event) {

        deletePreviousData(currentCityName, currentDateForCurrentCity,
                currentTemperatureForCurrentCity, currentMinMaxTemperatureForCurrentCity, currentIconForCurrentCity,
                currentHumidityForCurrentCity, currentPressureForCurrentCity, currentCityBackground,
                currentDayNextHoursDataForCurrentCity, nextDaysDataForCurrentCity);

        functions.loadDataForCurrentDay(currentCity, currentCityName, currentDateForCurrentCity,
                currentTemperatureForCurrentCity, currentMinMaxTemperatureForCurrentCity, currentIconForCurrentCity,
                currentHumidityForCurrentCity, currentPressureForCurrentCity, currentCityBackground,
                currentDayNextHoursDataForCurrentCity, nextDaysDataForCurrentCity);
    }

    @FXML
    public void loadDataForTargetCity(ActionEvent event) {

        deletePreviousData(targetCityName, currentDateForTargetCity,
                currentTemperatureForTargetCity, currentMinMaxTemperatureForTargetCity, currentIconForTargetCity,
                currentHumidityForTargetCity, currentPressureForTargetCity, targetCityBackground,
                currentDayNextHoursDataForTargetCity, nextDaysDataForTargetCity);

        functions.loadDataForCurrentDay(targetCity, targetCityName, currentDateForTargetCity,
                currentTemperatureForTargetCity, currentMinMaxTemperatureForTargetCity, currentIconForTargetCity,
                currentHumidityForTargetCity, currentPressureForTargetCity, targetCityBackground,
                currentDayNextHoursDataForTargetCity, nextDaysDataForTargetCity);
    }

    public void deletePreviousData (Label cityName, Label currentDate, Label currentTemperature,
                                    Label currentMinMaxTemperature, ImageView currentIcon, Label currentHumidity,
                                    Label currentPressure, GridPane background, HBox currentDayNextHoursData,
                                    GridPane nextDaysData) {

        cityName.setText(null);
        currentDate.setText(null);
        currentTemperature.setText(null);
        currentMinMaxTemperature.setText(null);
        currentIcon.setImage(null);
        currentHumidity.setText(null);
        currentPressure.setText(null);
        background.setStyle(null);
        currentDate.setText(null);
        currentDayNextHoursData.getChildren().clear();
        nextDaysData.getChildren().clear();
    }
}
