package main.model;
import main.apiOWM.OwmJapisCall;
import java.util.Vector;

public class DataLoader {


    public static Vector<Integer> getIndex(OwmJapisCall weather, String day) {

        String todayDate = weather.getHourlyWeatherDataList().get(0).getDateTime().toString();
        Vector<Integer> hourIndexes = new Vector<Integer>();
        Vector<Integer> hourIndexesNextDays = new Vector<Integer>();
        for (int i = 0; i < weather.getHourlyWeatherDataList().size(); i++) {
            String dateToCheck = weather.getHourlyWeatherDataList().get(i).getDateTime().toString();
            if (dateToCheck.substring(11, 13).equals("05") ||
                    dateToCheck.substring(11, 13).equals("11") ||
                    dateToCheck.substring(11, 13).equals("17") ||
                    dateToCheck.substring(11, 13).equals("23")) {

                if (dateToCheck.substring(0, 3).equals(todayDate.substring(0, 3))) hourIndexes.add(i);
                else {
                    hourIndexesNextDays.add(i);
                }
            }
        }

        if (day.equals("today")) return hourIndexes;
        else if (day.equals("nextDay")) return hourIndexesNextDays;
        return null;
    }
}
