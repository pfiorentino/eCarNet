package me.alpha12.ecarnet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import java.util.Calendar;

/**
 * Created by paul on 11/01/2016.
 */
public class GlobalContext extends Application {
    public static final int RESULT_CLOSE_ALL = 123;

    public static final String PREFS_NAME = "user_prefs_file";
    public static final String PREFS_SAVED_CAR_KEY = "current_car";

    private static GlobalContext instance;

    public static GlobalContext getInstance() {
        return instance;
    }

    public static Context getContext() {
        return instance;
    }

    public static int getCurrentCar() {
        SharedPreferences settings = getInstance().getSharedPreferences(PREFS_NAME, 0);
        return settings.getInt(PREFS_SAVED_CAR_KEY, -1);
    }

    public static void setCurrentCar(int newCarId) {
        SharedPreferences settings = getInstance().getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putInt(PREFS_SAVED_CAR_KEY, newCarId).commit();
    }

    public static String getFormattedDate(Calendar c) {
        return DateUtils.formatDateTime(getInstance(), c.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
