package main.apiOWM;

import net.aksingh.owmjapis.api.APIException;
import net.aksingh.owmjapis.core.OWM;
import net.aksingh.owmjapis.model.CurrentWeather;
import net.aksingh.owmjapis.model.HourlyWeatherForecast;
import net.aksingh.owmjapis.model.param.Weather;
import net.aksingh.owmjapis.model.param.WeatherData;
import java.util.List;

public class OwmJapisCall {

    private OWM owm = new OWM("YOUR_OWM_APIKEY");
    private CurrentWeather currentWeather;
    private HourlyWeatherForecast hourlyWeatherForecast;

    public OwmJapisCall(int id) throws APIException {
        owm.setUnit(OWM.Unit.METRIC);
        owm.setLanguage(OWM.Language.POLISH);

        currentWeather = getCurrentWeather(id);
        hourlyWeatherForecast = getHourlyWeather(id);
    }

    private CurrentWeather getCurrentWeather(int cityId) throws APIException {
        CurrentWeather currentWeatherCall = owm.currentWeatherByCityId(cityId);
        if (currentWeatherCall.hasRespCode() && currentWeatherCall.getRespCode() == 200) {
            return currentWeatherCall;
        } else return null;
    }

    private HourlyWeatherForecast getHourlyWeather(int cityId) throws APIException {
        return owm.hourlyWeatherForecastByCityId(cityId);
    }

    public String getCityName() {
        return (currentWeather.hasCityName()) ? currentWeather.getCityName() : null;
    }

    public String getCountryCode() {
        return (hourlyWeatherForecast.hasCityData()) ? hourlyWeatherForecast.getCityData().getCountryCode() : null;
    }

    public String getCurrentDateTime() {
        return (currentWeather.hasDateTime()) ? currentWeather.getDateTime().toString() : null;
    }

    public String roundTemperature(double temp){
        Double roundedTemp = Math.round(temp * 10.0) / 10.0;
        String temperature = roundedTemp.toString();
        return temperature;
    }
    public String roundPressure(double press){
        Long roundedPress = Math.round(press);
        String pressure = roundedPress.toString();
        return pressure;
    }

    public String getCurrentTemperature() {
        return (currentWeather.getMainData().hasTemp()) ? roundTemperature(currentWeather.getMainData().getTemp()) : null;
    }

    public int getCurrentCondition() {
        Weather currentWeatherData = getCurrentWeatherList().get(0);
        return (currentWeatherData.hasConditionId()) ? currentWeatherData.getConditionId(): null;
    }

    public String getSunsetDateTime() {
        return (currentWeather.getSystemData().hasSunsetDateTime()) ? currentWeather.getSystemData().getSunsetDateTime().toString() :
                null;
    }

    public String getSunriseDateTime() {
        return (currentWeather.getSystemData().hasSunriseDateTime()) ? currentWeather.getSystemData().getSunriseDateTime().toString() :
                null;
    }

    public String getCurrentHumidity() {
        return (currentWeather.getMainData().hasHumidity()) ? currentWeather.getMainData().getHumidity().toString() : null;
    }

    public String getCurrentPressure() {
        return (currentWeather.getMainData().hasPressure()) ? roundPressure(currentWeather.getMainData().getPressure()) : null;
    }

    public String getMinTemperature() {
        return (currentWeather.getMainData().hasTempMin()) ? roundTemperature(currentWeather.getMainData().getTempMin()) : null;
    }

    public String getMaxTemperature() {
        return (currentWeather.getMainData().hasTempMax()) ? roundTemperature(currentWeather.getMainData().getTempMax()) : null;
    }

    public List<Weather> getCurrentWeatherList() {
        return (currentWeather.hasWeatherList()) ? currentWeather.getWeatherList() : null;
    }

    public String getCurrentIcon() {
        Weather currentWeatherData = getCurrentWeatherList().get(0);
        return (currentWeatherData.hasIconLink()) ? currentWeatherData.getIconLink() : null;
    }

    public List<WeatherData> getHourlyWeatherDataList() {
        return (hourlyWeatherForecast.hasDataList()) ? hourlyWeatherForecast.getDataList() : null;
    }

    public WeatherData getHourlyWeatherData(int hourIndex) {
        return getHourlyWeatherDataList().get(hourIndex);
    }

    public String getHourlyDateTime(int hourIndex) {
        return (getHourlyWeatherData(hourIndex).hasDateTime()) ? getHourlyWeatherData(hourIndex).getDateTime().toString() : null;
    }

    public String getHourlyTemperature(int hourIndex) {
        return (getHourlyWeatherData(hourIndex).getMainData().hasTemp()) ? roundTemperature(getHourlyWeatherData(hourIndex).getMainData().getTemp()) : null;
    }
    public String getHourlyHumidity(int hourIndex) {
        return (getHourlyWeatherData(hourIndex).getMainData().hasHumidity()) ? getHourlyWeatherData(hourIndex).getMainData().getHumidity().toString() : null;
    }

    public List<Weather> getHourlyWeatherList(int hourIndex) {
        return (getHourlyWeatherData(hourIndex).hasWeatherList()) ? getHourlyWeatherData(hourIndex).getWeatherList() : null;
    }

    public String getHourlyIcon(int hourIndex) {
        Weather hourlyWeatherData = getHourlyWeatherList(hourIndex).get(0);
        return (hourlyWeatherData.hasIconLink()) ? hourlyWeatherData.getIconLink() : null;
    }
}
