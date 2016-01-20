package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DatabaseManager;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.User;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_MIN_DURATION = 250;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        new PrefetchData().execute();
    }

    /**
     * Async Task to init data
     */
    private class PrefetchData extends AsyncTask<Void, Void, Void> {
        private User currentUser;
        private HashMap<Integer, Car> cars;

        private Handler handler = new Handler();
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent intent;
                if(currentUser == null) {
                    intent = new Intent(SplashScreenActivity.this, AddUserActivity.class);
                    startActivity(intent);
                    finish();
                } else if (cars == null || cars.size() == 0) {
                    intent = new Intent(SplashScreenActivity.this, IncompleteConfigActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            DatabaseManager.initialize();
            this.currentUser = User.getUser();
            this.cars = Car.findAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handler.postDelayed(runnable, SPLASH_MIN_DURATION);
        }
    }
}
