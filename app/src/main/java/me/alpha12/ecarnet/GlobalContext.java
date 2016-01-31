package me.alpha12.ecarnet;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.text.format.DateUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

import me.alpha12.ecarnet.activities.MainActivity;

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

    public static String getFormattedDate(Date d) {
        return DateUtils.formatDateTime(getInstance(), d.getTime(),
                DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
    }

    public static String getFormattedDate(Calendar c) {
        return DateUtils.formatDateTime(getInstance(), c.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_WEEKDAY | DateUtils.FORMAT_ABBREV_MONTH);
    }

    public static String getFormattedMediumDate(long millis, boolean longMonth) {
        if (longMonth) {
            return DateUtils.formatDateTime(getInstance(), millis,
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR);
        } else {
            return DateUtils.formatDateTime(getInstance(), millis,
                    DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_MONTH);
        }
    }

    public static String getFormattedMediumDate(Date date, boolean longMonth) {
        return getFormattedMediumDate(date.getTime(), longMonth);
    }

    public static String getFormattedMediumDate(Calendar date, boolean longMonth) {
        return getFormattedMediumDate(date.getTimeInMillis(), longMonth);
    }

    public static String getFormattedMediumDate(Date date) {
        return getFormattedMediumDate(date.getTime(), false);
    }

    public static String getFormattedMediumDate(Calendar date) {
        return DateUtils.formatDateTime(getInstance(), date.getTimeInMillis(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR | DateUtils.FORMAT_ABBREV_MONTH);
    }

    public static String getFormattedSmallDate(Date date) {
        return DateUtils.formatDateTime(getInstance(), date.getTime(),
                DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_NO_YEAR | DateUtils.FORMAT_ABBREV_MONTH);
    }

    public static String getAppPicturePath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator  + "Android" + File.separator
                + "data" + File.separator + getInstance().getPackageName()
                + File.separator + "cars_pictures" + File.separator;
    }

    public static void pushNotification(String title, String message) {
        Context ctx = getInstance();

        PendingIntent pi = PendingIntent.getActivity(ctx, 0, new Intent(ctx, MainActivity.class), 0);
        Notification notification = new NotificationCompat.Builder(ctx)
                .setTicker(title)
                .setSmallIcon(R.drawable.ic_directions_car_white_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) ctx.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
