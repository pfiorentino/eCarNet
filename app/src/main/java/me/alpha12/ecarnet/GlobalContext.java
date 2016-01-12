package me.alpha12.ecarnet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by paul on 11/01/2016.
 */
public class GlobalContext extends Application {
    public static final int RESULT_CLOSE_ALL = 123;

    public static final String PREFS_NAME = "user_prefs_file";
    public static final String PREFS_SAVED_CAR_KEY = "current_car";
    public static final String PREFS_CAR_MODELS_IMPORTED = "are_car_models_imported";

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

    public static boolean areCarModelsImported() {
        SharedPreferences settings = getInstance().getSharedPreferences(PREFS_NAME, 0);
        return settings.getBoolean(PREFS_CAR_MODELS_IMPORTED, false);
    }

    public static void setCarModelsImported() {
        SharedPreferences settings = getInstance().getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putBoolean(PREFS_CAR_MODELS_IMPORTED, true).commit();
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
