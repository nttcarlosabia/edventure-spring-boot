package com.example.demo.utils;

public class Utils {
    public static long formatId(String input) {
        String lastFiveDigits = input.substring(Math.max(0, input.length() - 5));
        long result = Long.parseLong(lastFiveDigits);
        return result;
    }
}
