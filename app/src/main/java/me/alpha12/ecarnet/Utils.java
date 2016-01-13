package me.alpha12.ecarnet;

import android.text.TextUtils;

import java.util.Arrays;

/**
 * Created by paul on 13/01/16.
 */
public class Utils {
    public static final String[] ROMAN_DIGITS = {"", "I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};

    public static String ucFirst(String str) {
        return str.substring(0, 1).toUpperCase()+ str.substring(1).toLowerCase();
    }

    public static String ucWords(String str) {
        String[] array = str.split(" ");

        for (int i = 0; i < array.length; i++) {
            if (!Arrays.asList(ROMAN_DIGITS).contains(array[i]))
                array[i] = ucFirst(array[i]);
        }

        return TextUtils.join(" ", array);
    }

    public static String toRoman(int value) {
        if (value > 0 && value < 10)
            return ROMAN_DIGITS[value];

        return String.valueOf(value);
    }
}
