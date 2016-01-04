package me.alpha12.ecarnet.database;

/**
 * Created by guilhem on 09/11/2015.
 */


    import android.content.Context;
    import android.database.Cursor;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.io.BufferedReader;
    import java.io.IOException;
    import java.io.InputStream;
    import java.io.InputStreamReader;

    import me.alpha12.ecarnet.models.Model;

/**
     * Created by paul on 13/06/2015.
     */
    public class EcarnetHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ecarnet.db";

        public static SQLiteDatabase bdd;
        private DatabaseManager helper;

        public EcarnetHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
            helper = new DatabaseManager(context);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


    public void init(Context context, boolean reset) {
        if (reset) {
            this.onUpgrade(bdd, 1, 1);
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("models.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            String result = stringBuilder.toString();
            JSONObject jsonResult = new JSONObject(result);
            JSONObject jsonObject = jsonResult.getJSONObject("document");
            JSONArray jsonArray = jsonObject.getJSONArray("model");

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject current = jsonArray.getJSONObject(i);
                    Model model = new Model();
                    model.setId(current.getInt("id"));
                    model.setBrand(current.getString("brand"));
                    model.setModel(current.getString("model"));
                    model.setYear(current.getInt("year"));
                    model.setConsumption(current.getDouble("consumption"));
                    model.setEnergy(current.getString("energy"));
                    model.setRatedHP(current.getInt("ratedHP"));
                    model.setEngine(current.getString("engine"));
                    model.setDoors(current.getInt("doors"));
                    model.setSubModel((current.getString("submodel")));
                    Model.add(model, bdd);
                } catch (JSONException e) {
                    System.out.println("##### JSONException (for): " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("##### IOException (init): " + e.getMessage());
        } catch (JSONException e) {
            System.out.println("##### JSONException (init): " + e.getMessage());
        } finally {
            try {
                if (inputStream != null) inputStream.close();
            } catch (Exception e) {
                System.out.println("##### Exception (init): " + e.getMessage());
            }
        }
        System.out.println("---------" + Model.getNumOfModel(bdd) + " présents en bdd");
    }


    public boolean isInitialized()
        {
            System.out.println("---------" + Model.getNumOfModel(bdd) + " présents en bdd");
            return Model.getNumOfModel(bdd) > 0;
        }

        public void open() throws SQLException {
            bdd = helper.getWritableDatabase();
        }


        public void close() {
            helper.close();
        }
}
