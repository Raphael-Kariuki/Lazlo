package com.example.lazlo;



import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HouseOfCommons {


    //function that generates a random number between 0 and 1000 formatted to 4 decimal places
    public static Double generateRandomId() {
        Double entropy = Math.random() * 1000;
        String pattern = "###.####";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String formattedEntropy = decimalFormat.format(entropy);
        return Double.parseDouble(formattedEntropy);
    }
    //this method converts the time into 12hr format and assigns am or pm
    public String FormatTime(int hour, int minute) {

        String time;
        time = "";
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
    public static boolean priceCheck(String passphrase){
        String regex = "^(?=.*[0-9]).{1,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public static boolean durationCheck(String passphrase){
        String regex = "^(?=.*[0-9]).{1,5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public static boolean dateCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[-]).{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public static boolean timeCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[:]).{3,9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }

    public static String crypto(String passphrase) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        byte[] getBytes = md.digest(passphrase.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : getBytes){
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
    public static boolean passwordCheck(String passphrase){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_=+<>?.;,:'|/`]).{8,20}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(passphrase);
        return matcher.matches();
    }
    public static Date getDateFromLocalDateTime(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.of("GMT+3")).toInstant());
        //Date.from(localDateTime.now(ZoneId.of("GMT+3")).atZone(ZoneId.systemDefault()).toInstant());
    }
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
            formattedDuration = "< 1s";
        }
        return formattedDuration;
    }
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
}