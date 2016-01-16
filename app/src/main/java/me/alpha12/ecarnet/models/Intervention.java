package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

public class Intervention {

    private int id;
    private int kilometers;
    private double price;
    private Date dateIntervention;
    private double quantity;
    private int carId;

    /* Constructors */

    public Intervention(int id, int kilometers, double price, double quantity, Date dateIntervention, int carId) {
        this.id = id;
        this.kilometers = kilometers;
        this.price = price;
        this.dateIntervention = dateIntervention;
        this.quantity = quantity;
        this.carId = carId;
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
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId +" ORDER BY " +DBModel.C_DATE_INTERVENTION+ " DESC LIMIT 10",
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


    public static ArrayList<Intervention> findInterventionByLimit(int carId, Date limit) {
        Log.d("limite", limit.toString());
        ArrayList<Intervention> result = new ArrayList<>();
        java.sql.Date sqlLimit = new java.sql.Date(limit.getTime());
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId +" AND "+DBModel.C_DATE_INTERVENTION+" > "+sqlLimit ,
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


    public void persist() {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);

        newValues.put(DBModel.C_KILOMETERS, this.kilometers);

        if (this.dateIntervention != null)
            newValues.put(DBModel.C_DATE_INTERVENTION, this.dateIntervention.getTime());

        newValues.put(DBModel.C_PRICE, this.getPrice());
        newValues.put(DBModel.C_QUANTITY, this.quantity);
        newValues.put(DBModel.C_TYPE, 1);
        newValues.put(DBModel.C_OPERATION, 0);
        newValues.put(DBModel.C_CAR_ID, this.getCarId());

        long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

        if (this.id <= 0)
            this.id = (int) insertedId;
    }




    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "interventions";
        public static final String C_ID = "id";
        public static final String C_KILOMETERS = "kilometers";
        public static final String C_PRICE = "price";
        public static final String C_DATE_INTERVENTION = "date_intervention";
        public static final String C_OPERATION = "operation";
        public static final String C_QUANTITY = "quantity";
        public static final String C_TYPE = "type";
        public static final String C_CAR_ID = "car_id";

        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + C_ID + " INTEGER PRIMARY KEY,"
                + C_KILOMETERS + " INTEGER NOT NULL,"
                + C_PRICE + " REAL NOT NULL,"
                + C_DATE_INTERVENTION + " NUMERIC NOT NULL,"
                + C_OPERATION + " TEXT NOT NULL,"
                + C_TYPE + " NUMERIC NOT NULL,"
                + C_QUANTITY + " REAL,"
                + C_CAR_ID + " INTEGER NOT NULL"
                + ");";
    }


    public int getId() {
        return id;
    }

    public int getCarId() {
        return carId;
    }

    /* Getters & Setters */
    public int getKilometers() {
        return this.kilometers;
    }

    public Date getDateIntervention() {
        return this.dateIntervention;
    }

    public double getQuantity() {
        return this.quantity;
    }

    public double getPrice() {
        return this.price;
    }
}
