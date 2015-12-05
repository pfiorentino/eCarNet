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
    public static final String C_CAR_KILLOMETERS = "kilometers";
    public static final String C_CAR_BUYING_DATE = "buying_date";
    public static final String C_CAR_CIRCULATION_DATE = "circulation_date";
    public static final String C_CAR_AVERAGE_CONSUMPTION = "average_consumption";
    public static final String C_CAR_PLATE_NUMBER = "plate_number";
    private static final String INIT_BDD_CAR =
            "CREATE TABLE "
                    + T_CAR + "("
                    + C_CAR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_CAR_KILLOMETERS + " INTEGER NOT NULL,"
                    + C_CAR_BUYING_DATE + " NUMERIC NOT NULL,"
                    + C_CAR_CIRCULATION_DATE + "NUMERIC NOT NULL,"
                    + C_CAR_PLATE_NUMBER + "TEXT NOT NULL,"
                    + C_CAR_AVERAGE_CONSUMPTION + " REAL NOT NULL"
                    + ");";

    public String bidon;


    //Table Contains
    public static final String T_CONTAINS= "Contains";
    public static final String C_CONTAINS_ID_OPERATION = "id_operation";
    public static final String C_CONTAINS_ID_GROUP_OPERATION = "id_group_operation";
    private static final String INIT_BDD_CONTAINS =
            "CREATE TABLE "
                    + T_CONTAINS + "("
                    + C_CONTAINS_ID_OPERATION + " INTEGER NOT NULL,"
                    + C_CONTAINS_ID_GROUP_OPERATION + " INTEGER NOT NULL"
                    + ");";



    //Table Cycle
    public static final String T_CYCLE = "Cycle";
    public static final String C_CYCLE_ID_MODEL = "id_model";
    public static final String C_CYCLE_ID_OPERATION = "id_operation";
    public static final String C_CYCLE_KILLOMETERS = "killometers";
    public static final String C_CYCLE_TIME = "time";
    private static final String INIT_BDD_CYCLE =
            "CREATE TABLE "
                    + T_CYCLE + "("
                    + C_CYCLE_ID_MODEL + " INTEGER NOT NULL,"
                    + C_CYCLE_ID_OPERATION + " INTEGER NOT NULL,"
                    + C_CYCLE_KILLOMETERS + " INTEGER,"
                    + C_CYCLE_TIME + " INTEGER"
                    + ");";


    //Table GroupOperation
    public static final String T_GROUPOPERATION = "GroupOperation";
    public static final String C_GROUPEOPERATION_ID = "id";
    public static final String C_GROUPEOPERATION_NAME = "name";
    private static final String INIT_BDD_GROUPOPERATION =
            "CREATE TABLE "
                    + T_GROUPOPERATION+ "("
                    + C_GROUPEOPERATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_GROUPEOPERATION_NAME + " TEXT NOT NULL"
                    + ");";


    //Table Intervention
    public static final String T_INTERVENTION = "Intervention";
    public static final String C_INTERVENTION_ID = "id";
    public static final String C_INTERVENTION_KILLOMETERS = "kilometers";
    public static final String C_INTERVENTION_PRICE = "price";
    public static final String C_INTERVENTION_DATE_INTERVENTION = "date_intervention";
    public static final String C_INTERVENTION_ID_GROUP_OPERATION = "id_group_operation";
    public static final String C_INTERVENTION_GROUP_PRICE = "id_group_price";
    public static final String C_INTERVENTION_QUANTITY = "quantity";
    public static final String C_INTERVENTION_ID_CAR = "id_car";
    public static final String C_INTERVENTION_ID_OPERATION = "id_operation";
    private static final String INIT_BDD_INTERVENTION =
            "CREATE TABLE "
                    + T_INTERVENTION + "("
                    + C_INTERVENTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_INTERVENTION_KILLOMETERS + " INTEGER NOT NULL,"
                    + C_INTERVENTION_PRICE + " REAL NOT NULL,"
                    + C_INTERVENTION_DATE_INTERVENTION + " NUMERIC NOT NULL,"
                    + C_INTERVENTION_ID_GROUP_OPERATION + " INTEGER,"
                    + C_INTERVENTION_GROUP_PRICE + " DOUBLE,"
                    + C_INTERVENTION_QUANTITY + " REAL,"
                    + C_INTERVENTION_ID_CAR + " INTEGER NOT NULL,"
                    + C_INTERVENTION_ID_OPERATION + " INTEGER"
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


    //Table Operation
    public static final String T_OPERATION = "Operation";
    public static final String C_OPERATION_ID = "id";
    public static final String C_OPERATION_NAME = "name";
    public static final String C_OPERATION_TYPE = "type";
    public static final String C_OPERATION_PART = "part";
    private static final String INIT_BDD_OPERATION =
            "CREATE TABLE "
                    + T_OPERATION + "("
                    + C_OPERATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + C_OPERATION_NAME + " TEXT NOT NULL,"
                    + C_OPERATION_TYPE + " INTEGER NOT NULL,"
                    + C_OPERATION_PART + " TEXT"
                    + ");";

    //Table Own
    public static final String T_OWN = "Own";
    public static final String C_OWN_ID_USER = "id_user";
    public static final String C_OWN_ID_MODEL= "id_model";
    public static final String C_OWN_ID_CAR= "id_car";
    public static final String C_OWN_DATE = "date";
    private static final String INIT_BDD_OWN =
            "CREATE TABLE "
                    + T_OWN + "("
                    + C_OWN_ID_USER + " INTEGER NOT NULL,"
                    + C_OWN_ID_MODEL + " INTEGER NOT NULL,"
                    + C_OWN_ID_CAR + " INTEGER NOT NULL,"
                    + C_OWN_DATE + " NUMERIC NOT NULL"
                    + ");";

    //Table Share
    public static final String T_SHARE = "Share";
    public static final String C_SHARE_ID_USER = "id_user";
    public static final String C_SHARE_ID_CAR = "id_car";
    public static final String C_SHARE_ACTIVE = "active";
    public static final String C_SHARE_DATE = "date";
    private static final String INIT_BDD_SHARE =
            "CREATE TABLE "
                    + T_SHARE + "("
                    + C_SHARE_ID_USER + " INTEGER NOT NULL,"
                    + C_SHARE_ID_CAR + " INTEGER NOT NULL,"
                    + C_SHARE_ACTIVE + " INTEGER NOT NULL,"
                    + C_SHARE_DATE + " NUMERIC"
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
        bdd.execSQL(INIT_BDD_CONTAINS);
        bdd.execSQL(INIT_BDD_CYCLE);
        bdd.execSQL(INIT_BDD_GROUPOPERATION);
        bdd.execSQL(INIT_BDD_INTERVENTION);
        bdd.execSQL(INIT_BDD_MODEL);
        bdd.execSQL(INIT_BDD_OPERATION);
        bdd.execSQL(INIT_BDD_OWN);
        bdd.execSQL(INIT_BDD_SHARE);
        bdd.execSQL(INIT_BDD_USER);

    }

    @Override
    public void onUpgrade(SQLiteDatabase bdd, int prevVersion, int nextVersion) {
        onCreate(bdd);
    }
}

