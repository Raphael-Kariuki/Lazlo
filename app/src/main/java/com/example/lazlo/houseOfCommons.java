package com.example.lazlo;

import java.text.DecimalFormat;

public class houseOfCommons {
    public Double generateRandomId(){
        Double entropy = Math.random() * 1000;
        String pattern = "#.####";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        String formattedEntropy = decimalFormat.format(entropy);
        return Double.parseDouble(formattedEntropy);
    }
}
