package me.alpha12.ecarnet.database; /**
 * Created by guilhem on 05/10/2015.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager extends SQLiteOpenHelper {


    //Table CAR
    public static final String T_CAR = "Car";
    public static final String C_CAR_ID = "id";
    public static final String C_CAR_KILOMETERS = "kilometers";
    public static final String C_CAR_BUYING_DATE = "buying_date";
    public static final String C_CAR_CIRCULATION_DATE = "circulation_date";
    public static final String C_CAR_AVERAGE_CONSUMPTION = "average_consumption";
    public static final String C_CAR_PLATE_NUMBER = "plate_number";
    public static final String C_CAR_MODEL_ID = "model_id";
    private static final String INIT_BDD_CAR =
            "CREATE TABLE "
                    + T_CAR + "("
                    + C_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_CAR_KILOMETERS + " INTEGER NOT NULL,"
                    + C_CAR_BUYING_DATE + " NUMERIC NOT NULL,"
                    + C_CAR_CIRCULATION_DATE + " NUMERIC NOT NULL,"
                    + C_CAR_PLATE_NUMBER + " TEXT NOT NULL,"
                    + C_CAR_AVERAGE_CONSUMPTION + " REAL NOT NULL,"
                    + C_CAR_MODEL_ID + " INTEGER NOT NULL"
                    + ");";

    //Table Use
    public static final String T_USE = "Use";
    public static final String C_USE_CAR_ID = "car_id";
    public static final String C_USE_USER_ID = "user_id";
    public static final String C_USE_OWN = "own";
    private static final String INIT_BDD_USE =
            "CREATE TABLE "
                    + T_USE + "("
                    + C_USE_CAR_ID + " INTEGER NOT NULL,"
                    + C_USE_USER_ID + " INTEGER NOT NULL,"
                    + C_USE_OWN + " NUMERIC NOT NULL"
                    + ");";


    //Table Intervention
    public static final String T_INTERVENTION = "Intervention";
    public static final String C_INTERVENTION_ID = "id";
    public static final String C_INTERVENTION_KILOMETERS = "kilometers";
    public static final String C_INTERVENTION_PRICE = "price";
    public static final String C_INTERVENTION_DATE_INTERVENTION = "date_intervention";
    public static final String C_INTERVENTION_OPERATION = "operation";
    public static final String C_INTERVENTION_QUANTITY = "quantity";
    public static final String C_INTERVENTION_TYPE = "type";
    public static final String C_INTERVENTION_ID_CAR = "id_car";
    private static final String INIT_BDD_INTERVENTION =
            "CREATE TABLE "
                    + T_INTERVENTION + "("
                    + C_INTERVENTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_INTERVENTION_KILOMETERS + " INTEGER NOT NULL,"
                    + C_INTERVENTION_PRICE + " REAL NOT NULL,"
                    + C_INTERVENTION_DATE_INTERVENTION + " NUMERIC NOT NULL,"
                    + C_INTERVENTION_OPERATION + " TEXT NOT NULL,"
                    + C_INTERVENTION_TYPE + " NUMERIC NOT NULL,"
                    + C_INTERVENTION_QUANTITY + " REAL,"
                    + C_INTERVENTION_ID_CAR + " INTEGER NOT NULL,"
                    + ");";



    //Table Model
    public static final String T_MODEL = "Model";
    public static final String C_MODEL_ID = "id";
    public static final String C_MODEL_BRAND = "brand";
    public static final String C_MODEL_MODEL = "model";
    public static final String C_MODEL_YEAR = "year";
    public static final String C_MODEL_ENERGY = "energy";
    public static final String C_MODEL_ENGINE = "engine";
    public static final String C_MODEL_RATED_HP = "rated_hp";
    public static final String C_MODEL_CONSUMPTION = "consumption";
    public static final String C_MODEL_DOORS = "doors";
    public static final String C_MODEL_SUB_MODEL = "sub_model";
    private static final String INIT_BDD_MODEL =
            "CREATE TABLE "
                    + T_MODEL + "("
                    + C_MODEL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_MODEL_BRAND + " TEXT NOT NULL,"
                    + C_MODEL_MODEL + " TEXT NOT NULL,"
                    + C_MODEL_YEAR + " INTEGER NOT NULL,"
                    + C_MODEL_ENERGY + " TEXT NOT NULL,"
                    + C_MODEL_ENGINE + " TEXT NOT NULL,"
                    + C_MODEL_RATED_HP + " INTEGER NOT NULL,"
                    + C_MODEL_CONSUMPTION + " REAL,"
                    + C_MODEL_DOORS + " INTEGER NOT NULL,"
                    + C_MODEL_SUB_MODEL + " TEXT"
                    + ");";

    //Table User
    public static final String T_USER = "User";
    public static final String C_USER_ID = "id";
    public static final String C_USER_EMAIL = "email";
    public static final String C_USER_FIRSTNAME = "firstname";
    public static final String C_USER_LASTNAME = "lastname";
    public static final String C_USER_PASSWORD = "password";
    private static final String INIT_BDD_USER =
            "CREATE TABLE "
                    + T_USER + "("
                    + C_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_USER_EMAIL + " TEXT NOT NULL,"
                    + C_USER_FIRSTNAME + " TEXT NOT NULL,"
                    + C_USER_LASTNAME + " TEXT NOT NULL,"
                    + C_USER_PASSWORD + "TEXT NOT NULL"
                    + ");";



    private static final String BDD_NAME = "eCarnet.db";
    private static final int BDD_VERSION = 1;

    public DatabaseManager(Context context) {
        super(context, BDD_NAME, null, BDD_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase bdd) {
        System.out.println("##### INIT_BDD");
        bdd.execSQL(INIT_BDD_CAR);
        bdd.execSQL(INIT_BDD_INTERVENTION);
        bdd.execSQL(INIT_BDD_MODEL);
        bdd.execSQL(INIT_BDD_USER);
        bdd.execSQL(INIT_BDD_USE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase bdd, int prevVersion, int nextVersion) {
        onCreate(bdd);
    }
}

