package main.convert;

public class DateConverter {

    public static String convertDayName(String dayName) {
        switch (dayName) {
            case "Mon":
                return "Poniedziałek";
            case "Tue":
                return "Wtorek";
            case "Wed":
                return "Środa";
            case "Thu":
                return "Czwartek";
            case "Fri":
                return "Piątek";
            case "Sat":
                return "Sobota";
            case "Sun":
                return "Niedziela";
        }
        return null;
    }

    public static String convertMonth(String month) {
        switch(month) {
            case "Jan": return "stycznia";
            case "Feb": return "lutego";
            case "Mar": return "marca";
            case "Apr": return "kwietnia";
            case "May": return "maja";
            case "Jun": return "czerwca";
            case "Jul": return "lipca";
            case "Aug": return "sierpnia";
            case "Sep": return "września";
            case "Oct": return "października";
            case "Nov": return "listopada";
            case "Dec": return "grudnia";
        }
        return null;
    }

    public static String getConvertedDate(String date) {
        String dayName = convertDayName(date.substring(0, 3));
        String month = convertMonth(date.substring(4, 7));
        String day = date.substring(8, 10);
        int dayNumber = Integer.parseInt(day);
        if (dayNumber < 10)
        {
            day = day.substring(1);
        }
        String year = date.substring(24);
        String convertedDate = dayName + ", " + day + " " + month + " " + year;
        return convertedDate;
    }
}
