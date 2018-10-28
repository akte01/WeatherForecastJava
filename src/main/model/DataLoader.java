package main.model;
import main.apiOWM.OwmJapisCall;

import java.util.Vector;

public class DataLoader {

    public static String[] nextHoursLoader(OwmJapisCall weather) {
        for (int i = 0; i < weather.getHourlyWeatherDataList().size(); i++) {
            String dateToCheck = weather.getHourlyWeatherDataList().get(i).getDateTime().toString();
            if (dateToCheck.substring(11, 13).equals("04")) {
                String[] nextHours = {"04", "10", "16", "22"};
                return nextHours;
            } else if (dateToCheck.substring(11, 13).equals("05")) {
                String[] nextHours = {"05", "11", "17", "23"};
                return nextHours;
            }
        }
        return null;
    }

    public static Vector<Integer> getIndex(OwmJapisCall weather, String day) {

        String todayDate = weather.getHourlyWeatherDataList().get(0).getDateTime().toString();
        Vector<Integer> hourIndexes = new Vector<Integer>();
        Vector<Integer> hourIndexesNextDays = new Vector<Integer>();
        for (int i = 0; i < weather.getHourlyWeatherDataList().size(); i++) {
            String dateToCheck = weather.getHourlyWeatherDataList().get(i).getDateTime().toString();
            String[] nextHours = nextHoursLoader(weather);
            if (dateToCheck.substring(11, 13).equals(nextHours[0]) ||
                    dateToCheck.substring(11, 13).equals(nextHours[1]) ||
                    dateToCheck.substring(11, 13).equals(nextHours[2]) ||
                    dateToCheck.substring(11, 13).equals(nextHours[3])) {

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
