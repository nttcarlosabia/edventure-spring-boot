package com.example.demo.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    private Utils() {}

    public static String getCurrentDateAsString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(new Date());
    }
}
