package com.example.lazlo;



import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HouseOfCommons {
    //method to parse date input from adding task
    public static LocalDateTime getDateFromString(String string, DateTimeFormatter dateTimeFormatter){
        return LocalDateTime.parse(string, dateTimeFormatter);
    }
    //function that generates a random number between 0 and 1000 formatted to 4 decimal places
    public static Double generateRandomId() {
        Double entropy = Math.random() * 1000;
        String pattern = "###.####";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String formattedEntropy = decimalFormat.format(entropy);
        return Double.parseDouble(formattedEntropy);
    }
    //this method converts the time into 12hr format and assigns am or pm. Essential for populating a view
    public static String FormatTime(int hour, int minute) {

        String time;
        String formattedMinute;

        if (minute / 10 == 0) {
            formattedMinute = "0" + minute;
        } else {
            formattedMinute = "" + minute;
        }


        if (hour == 0) {
            time = "12" + ":" + formattedMinute + " AM";
        } else if (hour < 12) {
            time = hour + ":" + formattedMinute + " AM";
        } else if (hour == 12) {
            time = "12" + ":" + formattedMinute + " PM";
        } else {
            int temp = hour - 12;
            time = temp + ":" + formattedMinute + " PM";
        }


        return time;
    }

    /*
    * this function is useful in editing of tasks.
    * The dateFormatter somehow formats the date in the format yyyy-MM-dd even after explicitly stating that it should
    * format as dd-MM-yyyy, so the date selection is formatted as dd-MM-yyyy however on insertion into db, as it has to be formatted it's
    * stored as yyyy-MM-dd
    * While editing  a task, the user can change the date or not.
    * On the editing view,the date is populated straight from the db, so it appears as yyyy-MM-dd
    * The formatter does mistakes while itself it doesn't tolerate mistakes
    * To be on the safe side of a standard that stretches on a good part of the already done code, adding to the fact that one of the challenges
    * of this project has been date formatting, a solution has to be put in place to ensure that dates presented to the unforgiving dateFormatter
    * must be in the format dd-MM-yyyy*/
    public static String parseDate(String toDecideOn){
        String regex = "";
        String new_day = "",new_month = "",new_year = "",new_date;
        if(toDecideOn.contains("-")){
            regex = "-";
        }
        String[] date = toDecideOn.split(regex,3);

        if (Integer.parseInt(date[0]) > 31){
            new_year = date[0];
            new_month = date[1];
            new_day = date[2];
        }else if(Integer.parseInt(date[0]) < 31){
            new_day = date[0];
            new_month = date[1];
            new_year = date[2];
        }
        new_date = new_day + "-" + new_month + "-" + new_year;
        return new_date;
    }
    //function that ensures price input in task creation only contains allowed characters
    public static boolean priceCheck(String passphrase){
        String regex = "^(?=.*[0-9]).{1,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    //function that ensures duration input in task creation only contains allowed characters
    public static boolean durationCheck(String passphrase){
        String regex = "^(?=.*[0-9]).{1,5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    //function that ensures date input in task creation only contains allowed characters
    public static boolean dateCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[-]).{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    //function that ensures time input in task creation only contains allowed characters
    public static boolean timeCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[:]).{3,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    //function that encrypts the input with sha-512 algorithm and returns the hex value as a string
    public static String crypto(String passphrase) throws NoSuchAlgorithmException {

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] getBytes = md.digest(passphrase.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : getBytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /*function that checks that the password matches a specific criteria
    1. Must be at-least 8 characters long and not longer than 20
    2. Must contain digits 0-9
    3. Must contain characters a-z,A-Z
    4. Must contain other characters such as ? , # and the rest
    * */
    public static boolean passwordCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_=+<>?.;,:'|/`]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    //function converts a localDateTime to date, further used in returnFormattedDeadline
    public static Date getDateFromLocalDateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.of("GMT+3")).toInstant());
    }
    //function that formats milliseconds to  a specific date format
    public static String returnDate(long epochDate){
        String pattern = "EEE, LLL dd yyyy HH:mm:ss ";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern, new Locale("en", "KE"));
        return simpleDateFormat.format(new Date(epochDate));
    }
    /*
    *function that implements 2 functions
    * 1st obtains a date from a localDateTime
    * 2nd format the obtained date to a specific date format. Used in populating views. IndividualCompletedTask
     */
    public static String returnFormattedDeadline(String localDateTimeDeadline){
        Date newDate = getDateFromLocalDateTime(LocalDateTime.parse(localDateTimeDeadline));
        return returnDate(newDate.getTime());
    }

    /*
    * Function receives the duration and duration units and formats in the form duration:durationUnits
    * The function's output is the directly used while inserting a record*/
    public static String processPredictedDuration(String predictedDuration, String predictedDurationUnits){
        String duration;
        switch (predictedDurationUnits){
            case "hrs":
                duration = String.format(new Locale("en", "KE"),"%s:%s",(Long.parseLong(predictedDuration) * 3600000),0);
                break;
            case "min":
                duration = String.format(new Locale("en", "KE"),"%s:%s",(Long.parseLong(predictedDuration) * 60000),1);
                break;
            case "sec":
                duration = String.format(new Locale("en", "KE"),"%s:%s",(Long.parseLong(predictedDuration) * 1000),2);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + predictedDurationUnits);
        }
        return duration;
    }
    /*
    * This function receives a value that represents the duration a task was carried out. Formats the value to a presentable format
    * for populating the completed task details. Used in populating view in individual task
    * */
    public static String returnDuration(long duration){
        String formattedDuration;
        if (duration > 3600000){
            long hours = duration/3600000;
            long minutes = duration/60000;
            long seconds = duration/1000;
            formattedDuration  = hours + "hr " + minutes + " min " + seconds + " secs";

        }else if(duration > 600000 ){
            long minutes = duration/60000;
            long seconds = duration/1000;
            formattedDuration  = minutes + " min " + seconds + " secs";
        }else if(duration > 1000){
            long seconds = duration/1000;
            formattedDuration  = seconds + " secs";
        }else {
            formattedDuration = "less than 1 sec";
        }
        return formattedDuration;
    }

    /*
    * predicted duration is stored in the db in the format a:b, where a is the value of the predicted duration in milliseconds
    * , b is a integer between 0 - 2
    * 0 -> hrs
    * 1 -> min
    * 2 -> sec
    * This was for storage convenience, however the values have to be repopulated while editing thus
    * the value is pulled from db as a string then split and reformatted as the user previous input.
    * If the value after the : is 0, then the value before the : is divided to hours and so on.
    * The return type of the method is string array of length 2 i.e duration:units
    * */
    public static String[] processPredictedTaskDurationForPopulation(String predictedTaskDuration){
        String[] duration = predictedTaskDuration.split(":",2);
        String actualDurationUnits = null,actualDuration = null;
        String[] actualDurationCombined = new String[2];
        switch (duration[1]) {
            case "0":
                actualDuration = String.valueOf(Long.parseLong(duration[0]) / 3600000);
                actualDurationUnits = "hrs";
                break;
            case "1":
                actualDuration = String.valueOf(Long.parseLong(duration[0]) / 60000);
                actualDurationUnits = "min";
                break;
            case "2":
                actualDuration = String.valueOf(Long.parseLong(duration[0]) / 1000);
                actualDurationUnits = "sec";
                break;
        }
            actualDurationCombined[0] = actualDuration;
            actualDurationCombined[1] = actualDurationUnits;
            return actualDurationCombined;
        }

        public static final NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("en","KE"));
        public static final Locale locale = new Locale("en", "KE");

        //function that converts a string to date using a formatter
    public static LocalDateTime stringToDate(String date) {
        LocalDateTime newDate = null;
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("d-L-yyyy HH:mm");
        try {
            newDate = getDateFromString(date, dateTimeFormatter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newDate;
    }
    public String[] obtainDayRange(){
        Calendar calendar = Calendar.getInstance(HouseOfCommons.locale);
        int mMonth = calendar.get(Calendar.MONTH);
        int dDay = calendar.get(Calendar.DAY_OF_MONTH);
        int yYear = calendar.get(Calendar.YEAR);

        String startOfDay = String.format(HouseOfCommons.locale,"%d-%02d-%02d%s%02d:%02d",yYear,mMonth,dDay,"T",0,1);
        String endOfDay = String.format(HouseOfCommons.locale,"%d-%02d-%02d%s%02d:%02d",yYear,mMonth,dDay,"T",0,0);

        String[] dayRange = new String[2];
        dayRange[0] = startOfDay;
        dayRange[1] = endOfDay;

        return dayRange;

    }
}