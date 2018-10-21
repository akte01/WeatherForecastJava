package main.model;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import main.apiOWM.OwmJapisCall;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import main.citiesList.CityListReader;
import main.convert.DateConverter;
import net.aksingh.owmjapis.api.APIException;
import org.controlsfx.control.textfield.TextFields;

import java.net.NoRouteToHostException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.*;

public class ForecastFunctions {

    private final String DEGREE = "\u00b0";
    private final String LOADERROR = "Błąd ładowania listy!";
    private HashMap<String, String> cityNameWithCountryCode;
    private List<City> allCitiesList;

    TextField currentCity;
    TextField targetCity;

    public ForecastFunctions(TextField currentCity, TextField targetCity) {

        this.currentCity = currentCity;
        this.targetCity = targetCity;

        try {
            allCitiesList = new CityListReader().getCityList();
            cityNameWithCountryCode = new HashMap<>();
            int iterator = 0;
            for (City i : allCitiesList) {
                String city = allCitiesList.get(iterator).getCityName();
                String countryCode = allCitiesList.get(iterator).getCountryCode();
                cityNameWithCountryCode.put(city, city + ", " + countryCode);
                iterator++;
            }

            TextFields.bindAutoCompletion(currentCity, cityNameWithCountryCode.values());
            TextFields.bindAutoCompletion(targetCity, cityNameWithCountryCode.values());

        } catch (Exception ex) {
            currentCity.setPromptText(LOADERROR);
            targetCity.setPromptText(LOADERROR);
        }
    }

    public int getCityId(String introducedCity) {
        int cityId = 0;
        int iterator = 0;

        String[] splitedArray = null;
        splitedArray = introducedCity.split(",");
        introducedCity = splitedArray[0];

        if (cityNameWithCountryCode.containsKey(introducedCity)) {
            for (City i : allCitiesList) {
                if (allCitiesList.get(iterator).getCityName().equals(introducedCity)) {
                    cityId = allCitiesList.get(iterator).getCityId();
                }
                iterator++;
            }
        }
        return cityId;
    }

    public void loadDataForCurrentDay(TextField city, Label cityName, Label currentDate,
                                      Label currentTemperature, Label currentMinMaxTemperature, ImageView currentIcon,
                                      Label currentHumidity, Label currentPressure, GridPane background,
                                      HBox currentDayNextHoursData, GridPane nextDaysData) {

        String introducedCity = city.getText();

        int cityId = getCityId(introducedCity);

        try {
            if (cityId <= 0) throw new Exception();
            else {
                OwmJapisCall weather = new OwmJapisCall(cityId);

                cityName.setText(weather.getCityName() + ", " + weather.getCountryCode());

                String date = DateConverter.getConvertedDate(weather.getCurrentDateTime());
                currentDate.setText(date);

                currentTemperature.setText(weather.getCurrentTemperature() + DEGREE + "C");
                if ((weather.getCurrentTemperature().equals(weather.getMinTemperature())) &&
                        (weather.getCurrentTemperature().equals(weather.getMaxTemperature()))) {
                    currentMinMaxTemperature.setText("");
                } else {
                    currentMinMaxTemperature.setText("min: " + weather.getMinTemperature() + DEGREE + "C"
                            + " max: " + weather.getMaxTemperature() + DEGREE + "C");
                }
                currentHumidity.setText("Wilgotność: " + weather.getCurrentHumidity() + "%");
                currentPressure.setText("Ciśnienie: " + weather.getCurrentPressure() + " hPa");

                String pathCurrentIconCurrentCity = weather.getCurrentIcon();
                currentIcon.setImage(setIcon(pathCurrentIconCurrentCity));

                String imageURL = setBackgroundImage(weather.getCurrentCondition(),
                        weather.getCurrentDateTime(), weather.getSunriseDateTime(),
                        weather.getSunsetDateTime());
                String image = ForecastFunctions.class.getResource(imageURL).toExternalForm();
                background.setStyle("-fx-background-image: url('" + image + "'); -fx-background-position: center; " +
                        "-fx-background-size: cover;");
                Vector<Integer> hourIndexes = DataLoader.getIndex(weather, "today");
                Vector<Integer> hourIndexesNextDays = DataLoader.getIndex(weather, "nextDay");
                loadNextHoursDataForCurrentDay(currentDayNextHoursData, hourIndexes, weather);
                loadHoursDataForNextDays(nextDaysData, hourIndexesNextDays, weather);
            }

        } catch (APIException ex) {
            cityName.setText("Niepoprawne dane!");

        } catch (UnknownHostException ex) {
            cityName.setText("Brak połączenia z siecią!");

        } catch (NoRouteToHostException ex) {
            cityName.setText("Przerwano połączenie z siecią!");

        } catch (SocketTimeoutException stex) {
            cityName.setText("Serwer nie odpowiada!");

        } catch (Exception expn) {
            cityName.setText("Błędne ID miasta!");
        }
    }

    private static Image setIcon(String path) {
        return new Image(path);
    }

    private static String setBackgroundImage(int conditionId, String currentDateTime, String sunriseDateTime,
                                             String sunsetDateTime) {

        double sunriseHour = Double.parseDouble(sunriseDateTime.substring(11, 13) + "." + sunriseDateTime.substring(14,
                16) + sunriseDateTime.substring(17, 19));
        double sunsetHour = Double.parseDouble(sunsetDateTime.substring(11, 13) + "." + sunsetDateTime.substring(14,
                16) + sunsetDateTime.substring(17, 19));
        double currentHour = Double.parseDouble(currentDateTime.substring(11, 13) + "." + currentDateTime.substring(14,
                16) + currentDateTime.substring(17, 19));
        if ((currentHour > sunriseHour) && (currentHour < sunsetHour)) {
            if ((conditionId >= 200) && (conditionId <= 232)) {
                return "img/thunderstorm.jpg";
            } else if ((conditionId >= 300) && (conditionId <= 531)) {
                return "img/rain.jpg";
            } else if ((conditionId >= 600) && (conditionId <= 622)) {
                return "img/snow.jpg";
            } else if ((conditionId >= 701) && (conditionId <= 781)) {
                return "img/mist.jpg";
            } else if ((conditionId >= 801) && (conditionId <= 804)) {
                return "img/clouds.jpg";
            } else if ((conditionId == 800)) {
                return "img/sun.jpg";
            } else return "";
        } else {
            if ((conditionId >= 200) && (conditionId <= 232)) {
                return "img/thunderstorm_n.jpg";
            } else if ((conditionId >= 300) && (conditionId <= 531)) {
                return "img/rain.jpg";
            } else if ((conditionId >= 600) && (conditionId <= 622)) {
                return "img/snow_n.jpg";
            } else if ((conditionId >= 701) && (conditionId <= 781)) {
                return "img/mist.jpg";
            } else if ((conditionId >= 801) && (conditionId <= 804)) {
                return "img/clouds_n.jpg";
            } else if ((conditionId == 800)) {
                return "img/moon.jpg";
            } else return "";
        }
    }

    private void loadNextHoursDataForCurrentDay(HBox currentDayNextHoursData, Vector<Integer> hourIndexes,
                                                OwmJapisCall weather) {

        List<VBox> containerForHourData = new ArrayList<>();
        List<Label> selectedHoursForCurrentDay = new ArrayList<>();
        List<ImageView> hourlyIconForCurrentDay = new ArrayList<>();
        List<Label> hourlyTemperatureForCurrentDay = new ArrayList<>();
        List<Label> hourlyHumidityForCurrentDay = new ArrayList<>();


        for (int i = 0; i < hourIndexes.size(); i++) {
            VBox hourData = new VBox();
            hourData.setAlignment(Pos.CENTER);
            hourData.setPrefWidth(110);
            containerForHourData.add(hourData);

            Label currentDayHour = new Label();
            currentDayHour.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
            currentDayHour.setTextFill(Color.web(("fff")));
            currentDayHour.setFont(Font.font("DejaVu Sans", 12));

            selectedHoursForCurrentDay.add(currentDayHour);

            ImageView hourlyIconForCurrentDayImage = new ImageView();
            hourlyIconForCurrentDay.add(hourlyIconForCurrentDayImage);

            Label hourlyTemperatureForCurrentDayLabel = new Label();
            hourlyTemperatureForCurrentDayLabel.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
            hourlyTemperatureForCurrentDayLabel.setTextFill(Color.web(("fff")));
            hourlyTemperatureForCurrentDayLabel.setFont(Font.font("DejaVu Sans", 12));
            hourlyTemperatureForCurrentDay.add(hourlyTemperatureForCurrentDayLabel);

            Label hourlyHumidityForCurrentDayLabel = new Label();
            hourlyHumidityForCurrentDayLabel.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
            hourlyHumidityForCurrentDayLabel.setTextFill(Color.web(("fff")));
            hourlyHumidityForCurrentDayLabel.setFont(Font.font("DejaVu Sans", 12));
            hourlyHumidityForCurrentDay.add(hourlyHumidityForCurrentDayLabel);

            hourData.getChildren().addAll(currentDayHour, hourlyIconForCurrentDayImage,
                    hourlyTemperatureForCurrentDayLabel, hourlyHumidityForCurrentDayLabel);
            currentDayNextHoursData.getChildren().add(hourData);

            currentDayHour.setText(weather.getHourlyDateTime(hourIndexes.get(i)).substring(11, 16));
            String pathFirstDayIcon = weather.getHourlyIcon(hourIndexes.get(i));
            hourlyIconForCurrentDayImage.setImage(setIcon(pathFirstDayIcon));
            hourlyTemperatureForCurrentDayLabel.setText(weather.getHourlyTemperature(hourIndexes.get(i)) + " " +
                    DEGREE + "C");
            hourlyHumidityForCurrentDayLabel.setText(weather.getHourlyHumidity(hourIndexes.get(i)) + "%");
        }
    }

    private void loadHoursDataForNextDays(GridPane currentDayNextHoursData, Vector<Integer> hourIndexes,
                                          OwmJapisCall weather) {
        List<VBox> nextDays = new ArrayList<>();
        List<Label> nextDaysDate = new ArrayList<>();
        List<HBox> oneDayDataList = new ArrayList<>();
        List<VBox> hoursData = new ArrayList<>();
        List<Label> selectedHours = new ArrayList<>();
        List<ImageView> hourlyIcons = new ArrayList<>();
        List<Label> hourlyTemperatureData = new ArrayList<>();
        List<Label> hourlyHumidityData = new ArrayList<>();
        List<Separator> separators = new ArrayList<>();

        int index = 1;

        for (int i = 1; i <= 4; i++) {

            VBox nextDay = new VBox();
            nextDay.setAlignment(Pos.CENTER);
            nextDay.setSpacing(5);
            nextDays.add(nextDay);

            Label nextDayDate = new Label();
            nextDayDate.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
            nextDayDate.setTextFill(Color.web(("fff")));
            nextDayDate.setFont(Font.font("DejaVu Sans", 16));
            nextDayDate.setPadding(new Insets(0, 0, 10, 0));
            nextDaysDate.add(nextDayDate);

            HBox oneDayData = new HBox();
            oneDayData.setAlignment(Pos.CENTER);
            oneDayDataList.add(oneDayData);

            for (int j = 0; j < 4; j++) {
                VBox hourData = new VBox();
                hourData.setAlignment(Pos.CENTER);
                hourData.setPrefWidth(110);
                hourData.setSpacing(5);
                hoursData.add(hourData);

                Label selectedHour = new Label();
                selectedHour.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
                selectedHour.setTextFill(Color.web(("fff")));
                selectedHour.setFont(Font.font("DejaVu Sans", 12));
                selectedHours.add(selectedHour);

                ImageView hourlyIcon = new ImageView();
                hourlyIcons.add(hourlyIcon);

                Label hourlyTemperature = new Label();
                hourlyTemperature.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
                hourlyTemperature.setTextFill(Color.web(("fff")));
                hourlyTemperature.setFont(Font.font("DejaVu Sans", 12));
                hourlyTemperatureData.add(hourlyTemperature);

                Label hourlyHumidity = new Label();
                hourlyHumidity.setStyle("-fx-effect: dropshadow(two-pass-box, #000, 1, 1, 1, 1)");
                hourlyHumidity.setTextFill(Color.web(("fff")));
                hourlyHumidity.setFont(Font.font("DejaVu Sans", 12));
                hourlyHumidity.setPadding(new Insets(0, 0, 20, 0));
                hourlyHumidityData.add(hourlyHumidity);

                Separator separator = new Separator();
                separator.setOpacity(0.5);
                separator.prefWidth(200.0);
                separators.add(separator);

                hourData.getChildren().addAll(selectedHour, hourlyIcon, hourlyTemperature, hourlyHumidity, separator);
                oneDayData.getChildren().add(hourData);

                if (j == 0) {
                    String date = DateConverter.getConvertedDate(weather.getHourlyDateTime(hourIndexes.get(0)));
                    nextDayDate.setText(date);
                }

                selectedHour.setText(weather.getHourlyDateTime(hourIndexes.get(0)).substring(11, 16));
                String pathDayIcon = weather.getHourlyIcon(hourIndexes.get(0));
                hourlyIcon.setImage(setIcon(pathDayIcon));
                hourlyTemperature.setText(weather.getHourlyTemperature(hourIndexes.get(0)) + " " + DEGREE + "C");
                hourlyHumidity.setText(weather.getHourlyHumidity(hourIndexes.get(0)) + "%");
                hourIndexes.remove(0);
            }
            nextDay.getChildren().add(nextDayDate);
            nextDay.getChildren().add(oneDayData);
            currentDayNextHoursData.add(nextDay, 0, index);
            currentDayNextHoursData.setAlignment(Pos.CENTER);
            index++;

        }
    }
}