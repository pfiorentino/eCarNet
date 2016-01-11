package me.alpha12.ecarnet.database; /**
 * Created by guilhem on 05/10/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.CarModel;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.User;

public class DatabaseManager extends SQLiteOpenHelper {
    //Table Use
    public static final String T_USE = "use";
    public static final String C_USE_CAR_ID = "car_id";
    public static final String C_USE_USER_ID = "user_id";
    public static final String C_USE_OWN = "own";
    private static final String SQL_CREATE_TABLE_USE =
            "CREATE TABLE " + T_USE + "("
                    + C_USE_CAR_ID + " INTEGER NOT NULL,"
                    + C_USE_USER_ID + " INTEGER NOT NULL,"
                    + C_USE_OWN + " NUMERIC NOT NULL"
                    + ");";

    private static final String BDD_NAME = "ecarnet.db";
    private static final int BDD_VERSION = 2;

    private SQLiteDatabase db;

    private static DatabaseManager instance;

    public static DatabaseManager getInstance() {
        if (instance == null) {
            Log.d("Database", "New Instance");
            instance = new DatabaseManager(GlobalContext.getInstance());
        }

        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static SQLiteDatabase getCurrentDatabase() {
        return getInstance().db;
    }

    public static int extractInt(Cursor cur, String columnName){
        return cur.getInt(cur.getColumnIndex(columnName));
    }

    public static double extractDouble(Cursor cur, String columnName){
        return cur.getDouble(cur.getColumnIndex(columnName));
    }

    public static String extractString(Cursor cur, String columnName){
        return cur.getString(cur.getColumnIndex(columnName));
    }

    public static Date extractDate(Cursor cur, String ColumnName) {
        return new Date(cur.getLong(cur.getColumnIndex(ColumnName))*1000);
    }

    private DatabaseManager(Context context) {
        super(context, BDD_NAME, null, BDD_VERSION);
        this.db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "Database Initialisation");

        db.execSQL(Car.DBModel.SQL_CREATE_TABLE);
        db.execSQL(Intervention.DBModel.SQL_CREATE_TABLE);
        db.execSQL(CarModel.DBModel.SQL_CREATE_TABLE);
        db.execSQL(User.DBModel.SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE_USE);

        Log.d("Database", "Initialisation completed");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int nextVersion) {
        Log.d("database", "Database Upgrade");

        db.execSQL("DROP TABLE IF EXISTS " + Car.DBModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Intervention.DBModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CarModel.DBModel.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + User.DBModel.TABLE_NAME);

        onCreate(db);
    }
}

