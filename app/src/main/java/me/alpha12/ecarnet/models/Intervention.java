package me.alpha12.ecarnet.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

public class Intervention {

    private int id;
    private int kilometers;
    private double price;
    private Date dateIntervention;
    private double quantity;

    /* Constructors */

    public Intervention(int id, int kilometers, double price, double quantity, Date dateIntervention) {
        this.id = id;
        this.kilometers = kilometers;
        this.price = price;
        this.dateIntervention = dateIntervention;
        this.quantity = quantity;
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
                    DatabaseManager.extractDate(cursor, DBModel.C_DATE_INTERVENTION)
            ));
        }

        return result;
    }

    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "intervention";
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
