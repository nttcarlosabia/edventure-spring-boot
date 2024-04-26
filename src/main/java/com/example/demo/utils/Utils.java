package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {}

    public static String getCurrentDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(new Date());
    }
}
