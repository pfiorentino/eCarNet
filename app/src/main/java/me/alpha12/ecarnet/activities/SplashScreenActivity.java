package me.alpha12.ecarnet.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.HashMap;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.EcarnetHelper;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.User;

public class SplashScreenActivity extends AppCompatActivity {
    private static int SPLASH_MIN_DURATION = 1000;

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
        private HashMap<String, Car> cars;

        private Handler handler = new Handler();
        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Intent i;
                if(currentUser == null) {
                    i = new Intent(SplashScreenActivity.this, AddUserActivity.class);
                    startActivity(i);
                    finish();
                } else if (cars == null || cars.size() == 0) {
                    i = new Intent(SplashScreenActivity.this, AddCarActivity.class);
                    startActivityForResult(i, 0);
                    finish();
                } else {
                    i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
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
            //------------database set--------------
            EcarnetHelper ecarnetHelper = new EcarnetHelper(SplashScreenActivity.this);
            ecarnetHelper.open();
            if(!ecarnetHelper.isInitialized()) {
                ecarnetHelper.init(SplashScreenActivity.this, false);
            }

            this.currentUser = User.getUser(ecarnetHelper.bdd);
            this.cars = Car.getAllCars(ecarnetHelper.bdd);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handler.postDelayed(runnable, SPLASH_MIN_DURATION);
        }

    }
}
