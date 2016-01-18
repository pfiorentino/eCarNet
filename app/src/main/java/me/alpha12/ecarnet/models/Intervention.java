package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

public class Intervention {
    public static final int TYPE_FILLUP = 1;
    public static final int TYPE_OTHER = 0;

    private int id;
    private int carId;
    private int type;
    private String description;
    private int kilometers;
    private Date date;
    private double price;
    private double quantity;


    /* Constructors */
    public Intervention(int id, int carId, int type, String description, int kilometers, Date date, double price, double quantity) {
        if (id > 0)
            this.id         = id;
        this.carId          = carId;
        this.type           = type;
        this.description    = description;
        this.kilometers     = kilometers;
        this.date           = date;
        this.price          = price;
        this.quantity       = quantity;
    }

    public Intervention(Cursor cursor) {
        this.id         = DatabaseManager.extractInt(cursor, DBModel.C_ID);
        this.carId      = DatabaseManager.extractInt(cursor, DBModel.C_CAR_ID);
        this.type       = DatabaseManager.extractInt(cursor, DBModel.C_TYPE);
        this.description = DatabaseManager.extractString(cursor, DBModel.C_DESCRIPTION);
        this.kilometers = DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS);
        this.date       = DatabaseManager.extractDate(cursor, DBModel.C_DATE);
        this.price      = DatabaseManager.extractDouble(cursor, DBModel.C_PRICE);
        this.quantity   = DatabaseManager.extractDouble(cursor, DBModel.C_QUANTITY);
    }

    /* Static methods */

    public static ArrayList<Intervention> findAllByCar(int carId) {
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId,
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }

    public static ArrayList<Intervention> findFillUpByCar(int carId) {
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId + " AND "+DBModel.C_QUANTITY+" > 0 ORDER BY " + DBModel.C_DATE_INTERVENTION+", "+DBModel.C_ID+" DESC",
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(
                    id,
                    DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                    DatabaseManager.extractDouble(cursor, DBModel.C_PRICE),
                    DatabaseManager.extractDouble(cursor, DBModel.C_QUANTITY),
                    DatabaseManager.extractDate(cursor, DBModel.C_DATE_INTERVENTION),
                    DatabaseManager.extractInt(cursor, DBModel.C_CAR_ID)
            ));
        }
        return result;
    }

    public static ArrayList<Intervention> find10ByCar(int carId) {
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_CAR_ID + " = " + carId + " ORDER BY " + DBModel.C_DATE + " DESC LIMIT 10",
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }


    public static ArrayList<Intervention> findInterventionByLimit(int carId, Date limit) {
        Log.d("limite", limit.toString());
        ArrayList<Intervention> result = new ArrayList<>();
        java.sql.Date sqlLimit = new java.sql.Date(limit.getTime());
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_CAR_ID + " = " + carId + " AND " + DBModel.C_DATE + " > " + sqlLimit,
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }


    public void persist() {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);

        newValues.put(DBModel.C_CAR_ID, this.carId);
        newValues.put(DBModel.C_TYPE, this.type);
        newValues.put(DBModel.C_DESCRIPTION, this.description);
        newValues.put(DBModel.C_KILOMETERS, this.kilometers);

        if (this.date != null)
            newValues.put(DBModel.C_DATE, this.date.getTime());

        newValues.put(DBModel.C_PRICE, this.price);
        newValues.put(DBModel.C_QUANTITY, this.quantity);

        long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

        if (this.id <= 0)
            this.id = (int) insertedId;
    }

    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME       = "interventions";
        public static final String C_ID             = "id";
        public static final String C_CAR_ID         = "car_id";
        public static final String C_TYPE           = "type";
        public static final String C_DESCRIPTION    = "description";
        public static final String C_KILOMETERS     = "kilometers";
        public static final String C_DATE           = "date";
        public static final String C_PRICE          = "price";
        public static final String C_QUANTITY       = "quantity";

        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + C_ID          + " INTEGER PRIMARY KEY,"
                + C_CAR_ID      + " INTEGER NOT NULL,"
                + C_TYPE        + " INTEGER NOT NULL,"
                + C_DESCRIPTION + " TEXT NOT NULL,"
                + C_KILOMETERS  + " INTEGER NOT NULL,"
                + C_DATE        + " INTEGER NOT NULL,"
                + C_PRICE       + " REAL NOT NULL,"
                + C_QUANTITY    + " REAL"
                + ");";
    }


    /* Getters & Setters */
    public int getId() {
        return id;
    }

    public int getKilometers() {
        return this.kilometers;
    }

    public Date getDate() {
        return this.date;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }
}
