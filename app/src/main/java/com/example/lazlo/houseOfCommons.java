package com.example.lazlo;



import java.text.DecimalFormat;

public class houseOfCommons {


    //function that generates a random number between 0 and 1000 formatted to 4 decimal places
    public Double generateRandomId() {
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
    public String parseDate(String toDecideOn){
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

}