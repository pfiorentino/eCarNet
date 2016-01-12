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
import me.alpha12.ecarnet.models.CarModel;
import me.alpha12.ecarnet.models.CarModelOld;
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
            //System.exit(0);
            //importCarModels();

            this.currentUser = User.getUser();
            this.cars = Car.findAll();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            handler.postDelayed(runnable, SPLASH_MIN_DURATION);
        }

        private void importCarModels() {
            Log.d("Database", "Import carModels");

            InputStream inputStream = null;
            try {
                //inputStream = GlobalContext.getInstance().getAssets().open("models.json");
                inputStream = GlobalContext.getInstance().getAssets().open("cars_full.json");

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line);
                }

                String result = stringBuilder.toString();
                JSONObject jsonResult = new JSONObject(result);
                /*JSONObject jsonObject = jsonResult.getJSONObject("document");
                JSONArray jsonArray = jsonObject.getJSONArray("model");*/
                JSONArray jsonArray = jsonResult.getJSONArray("models");

                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject current = jsonArray.getJSONObject(i);

                        /*CarModel carModel = new CarModel(
                                current.getInt("id"),
                                current.getString("brand"),
                                current.getString("model"),
                                current.getInt("year"),
                                current.getString("energy"),
                                current.getString("engine"),
                                current.getInt("doors"),
                                current.getInt("ratedHP"),
                                current.getDouble("consumption"),
                                current.getString("submodel")
                        );*/

                        CarModelOld carModel = new CarModelOld(
                                current.getInt("id"),
                                current.getString("brand"),
                                current.getString("model"),
                                0,
                                current.getString("fuel_type"),
                                current.getString("version"),
                                0,
                                current.getInt("rated_hp"),
                                0,
                                ""
                        );
                        carModel.persist();
                    } catch (JSONException e) {
                        Log.e("CarModels import", e.getMessage());
                    }
                }
            } catch (IOException e) {
                Log.e("CarModels import", e.getMessage());
            } catch (JSONException e) {
                Log.e("CarModels import", e.getMessage());
            } finally {
                try {
                    if (inputStream != null) inputStream.close();
                } catch (Exception e) {
                    Log.e("CarModels import", e.getMessage());
                }
            }
        }
    }
}
