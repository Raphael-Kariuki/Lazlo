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
}