package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

import java.util.ArrayList;

import me.alpha12.ecarnet.database.DatabaseManager;

public class CarModelOld implements Parcelable {

    private int id;
    private String brand;
    private String model;
    private int year;
    private String energy;
    private String engine;
    private int ratedHP;
    private double consumption;
    private int doors;
    private String subModel;

    /* Constructors */

    public CarModelOld(String brand, String model, String engine) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
    }

    public CarModelOld(int id, String brand, String model, int year, String energy, String engine, int ratedHP, int doors, double consumption, String subModel) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.energy = energy;
        this.engine = engine;
        this.ratedHP = ratedHP;
        this.consumption = consumption;
        this.doors = doors;
        this.subModel = subModel;
    }

    /* Public methods */

    public String toString() {
        return subModel + " " + engine + " - " + doors + " portes - " + energy;
    }

    public void persist() {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);

        newValues.put(DBModel.C_BRAND, this.brand);
        newValues.put(DBModel.C_MODEL, this.model);
        newValues.put(DBModel.C_CONSUMPTION, this.consumption);
        newValues.put(DBModel.C_ENERGY, this.energy);
        newValues.put(DBModel.C_YEAR, this.year);
        newValues.put(DBModel.C_RATED_HP, this.ratedHP);
        newValues.put(DBModel.C_ENGINE, this.engine);
        newValues.put(DBModel.C_DOORS, this.doors);
        newValues.put(DBModel.C_SUB_MODEL, this.subModel);

        DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);
    }

    /* Static methods */

    public static ArrayList<String> findBrands() {
        ArrayList<String> brands = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT DISTINCT(" + DBModel.C_BRAND + ") FROM " + DBModel.TABLE_NAME + " ORDER BY " + DBModel.C_BRAND + ";",
                null
        );
        while(cursor.moveToNext()) {
            brands.add(DatabaseManager.extractString(cursor, DBModel.C_BRAND));
        }
        return brands;
    }


    public static ArrayList<String> findModelsByBrand(String brand) {
        ArrayList<String>models = new ArrayList<>();

        String[] args = {brand};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT DISTINCT("+ DBModel.C_MODEL +") FROM "+ DBModel.TABLE_NAME +" WHERE "+ DBModel.C_BRAND +" = ? ORDER BY "+ DBModel.C_MODEL +";",
                args
        );

        while(cursor.moveToNext()) {
            models.add(DatabaseManager.extractString(cursor, DBModel.C_MODEL));
        }

        return models;
    }

    public static ArrayList<CarModelOld> findByBrandModel(String brand, String model) {
        ArrayList<CarModelOld> result = new ArrayList<>();

        String[] args = {brand, model};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+ DBModel.TABLE_NAME +" WHERE "+ DBModel.C_BRAND +" = ? AND "+ DBModel.C_MODEL +" = ? ORDER BY "+ DBModel.C_MODEL +";",
                args
        );

        while(cursor.moveToNext()) {
            result.add(
                    new CarModelOld(
                            DatabaseManager.extractInt(cursor, DBModel.C_ID),
                            DatabaseManager.extractString(cursor, DBModel.C_BRAND),
                            DatabaseManager.extractString(cursor, DBModel.C_MODEL),
                            DatabaseManager.extractInt(cursor, DBModel.C_YEAR),
                            DatabaseManager.extractString(cursor, DBModel.C_ENERGY),
                            DatabaseManager.extractString(cursor, DBModel.C_ENGINE),
                            DatabaseManager.extractInt(cursor, DBModel.C_RATED_HP),
                            DatabaseManager.extractInt(cursor, DBModel.C_DOORS),
                            DatabaseManager.extractDouble(cursor, DBModel.C_CONSUMPTION),
                            DatabaseManager.extractString(cursor, DBModel.C_SUB_MODEL)
                    )
            );
        }

        return result;
    }


    public static CarModelOld findById(int id) {
        String[] args = { Integer.toString(id) };
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+ DBModel.TABLE_NAME +" WHERE id = ?;",
                args
        );

        if(cursor.moveToNext()) {
            return new CarModelOld(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    DatabaseManager.extractString(cursor, DBModel.C_BRAND),
                    DatabaseManager.extractString(cursor, DBModel.C_MODEL),
                    DatabaseManager.extractInt(cursor, DBModel.C_YEAR),
                    DatabaseManager.extractString(cursor, DBModel.C_ENERGY),
                    DatabaseManager.extractString(cursor, DBModel.C_ENGINE),
                    DatabaseManager.extractInt(cursor, DBModel.C_RATED_HP),
                    DatabaseManager.extractInt(cursor, DBModel.C_DOORS),
                    DatabaseManager.extractDouble(cursor, DBModel.C_CONSUMPTION),
                    DatabaseManager.extractString(cursor, DBModel.C_SUB_MODEL)
            );
        }

        return null;
    }

    /* Parcelable implementation */

    public static final Creator<CarModelOld> CREATOR = new Creator<CarModelOld>() {
        @Override
        public CarModelOld createFromParcel(Parcel in) {
            return new CarModelOld(in);
        }

        @Override
        public CarModelOld[] newArray(int size) {
            return new CarModelOld[size];
        }
    };

    protected CarModelOld(Parcel in) {
        id = in.readInt();
        brand = in.readString();
        model = in.readString();
        year = in.readInt();
        energy = in.readString();
        engine = in.readString();
        ratedHP = in.readInt();
        consumption = in.readDouble();
        doors = in.readInt();
        subModel = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeInt(this.year);
        dest.writeString(this.energy);
        dest.writeString(this.engine);
        dest.writeInt(this.ratedHP);
        dest.writeDouble(this.consumption);
        dest.writeInt(this.doors);
        dest.writeString(this.subModel);
    }

    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "car_model";
        public static final String C_ID = "id";
        public static final String C_BRAND = "brand";
        public static final String C_MODEL = "model";
        public static final String C_YEAR = "year";
        public static final String C_ENERGY = "energy";
        public static final String C_ENGINE = "engine";
        public static final String C_RATED_HP = "rated_hp";
        public static final String C_CONSUMPTION = "consumption";
        public static final String C_DOORS = "doors";
        public static final String C_SUB_MODEL = "sub_model";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + "("
                        + C_ID + " INTEGER PRIMARY KEY,"
                        + C_BRAND + " TEXT NOT NULL,"
                        + C_MODEL + " TEXT NOT NULL,"
                        + C_YEAR + " INTEGER NOT NULL,"
                        + C_ENERGY + " TEXT NOT NULL,"
                        + C_ENGINE + " TEXT NOT NULL,"
                        + C_RATED_HP + " INTEGER NOT NULL,"
                        + C_CONSUMPTION + " REAL,"
                        + C_DOORS + " INTEGER NOT NULL,"
                        + C_SUB_MODEL + " TEXT"
                        + ");";
    }

    /* Getters & setters */

    public int getId() {
        return this.id;
    }
    public int getYear() {
        return this.year;
    }
    public int getRatedHP() {
        return this.ratedHP;
    }
    public String getBrand() {
        return this.brand;
    }
    public String getModel() {
        return this.model;
    }
    public String getEngine() {
        return this.engine;
    }
    public String getEnergy() {
        return this.energy;
    }
    public String getSubModel() {
        return this.subModel;
    }
}
