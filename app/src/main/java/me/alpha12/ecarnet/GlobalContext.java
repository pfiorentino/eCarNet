package me.alpha12.ecarnet;

import android.app.Application;
import android.content.Context;

/**
 * Created by paul on 11/01/2016.
 */
public class GlobalContext extends Application {
    public static final int RESULT_CLOSE_ALL = 123;

    private static GlobalContext instance;

    public static GlobalContext getInstance() {
        return instance;
    }

    public static Context getContext(){
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        super.onCreate();
    }
}
