package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DBObject;
import me.alpha12.ecarnet.database.DatabaseManager;

public class Intervention extends DBObject{
    public static final int TYPE_FILLUP = 1;
    public static final int TYPE_OTHER = 0;

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
            this.setId(id);
        this.carId          = carId;
        this.type           = type;
        this.description    = description;
        this.kilometers     = kilometers;
        this.date           = date;
        this.price          = price;
        this.quantity       = quantity;
    }

    public static double getTotalInterventionPrice(int carId, Date limit)
    {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT SUM("+ DBModel.C_PRICE+") FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId+ " AND "+ DBModel.C_TYPE + " = " + TYPE_OTHER + " AND " + DBModel.C_DATE +" > " +limit.getTime(),
                null
        );
        if(cursor.moveToFirst()) {
            return cursor.getDouble(0);
        }
        else return 0;
    }

    public static  Intervention get(int id)
    {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_ID+" = " + id+ " AND "+ DBModel.C_TYPE + " = " + TYPE_OTHER,
                null
        );
        if(cursor.moveToFirst()) {
            return new Intervention(cursor);
        }
        else return null;
    }



    public static double getAverageInterventionPrice(int carId)
    {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT AVG("+ DBModel.C_PRICE+") FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId+ " AND "+ DBModel.C_TYPE + " = " + TYPE_OTHER,
                null
        );
        if(cursor.moveToFirst()) {
            Log.d("value", ""+cursor.getDouble(0));
            return cursor.getDouble(0);
        }
        else return 0;
    }


    public static ArrayList<Intervention> findInterventionByNumericLimit(int carId, int limit){
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId+ " AND "+DBModel.C_TYPE +" = " + TYPE_OTHER + " LIMIT " +limit,
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }

    public Intervention(Cursor cursor) {
        this.setId(DatabaseManager.extractInt(cursor, DBModel.C_ID));
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
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId + " AND "+DBModel.C_TYPE + "=" + TYPE_FILLUP + " ORDER BY "+ DBModel.C_DATE+", "+DBModel.C_ID+" DESC",
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }

    public static ArrayList<Intervention> findOtherByCar(int carId) {
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId + " AND " + DBModel.C_TYPE + "=" + TYPE_OTHER + " ORDER BY " + DBModel.C_DATE + " DESC",
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }

    public static ArrayList<Intervention> find10ByCar(int carId) {
        ArrayList<Intervention> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_CAR_ID + " = " + carId + " ORDER BY " + DBModel.C_DATE + " ASC LIMIT 10",
                null
        );
        while(cursor.moveToNext()) {
            int id = DatabaseManager.extractInt(cursor, DBModel.C_ID);
            result.add(new Intervention(cursor));
        }
        return result;
    }

    public static ArrayList<Intervention> findInterventionByLimit(int carId, Date limit) {
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

    public static boolean deleteAllByCar(int carId) {
        return DatabaseManager.getCurrentDatabase().delete(DBModel.TABLE_NAME, DBModel.C_CAR_ID + " = " + carId, null) > 0;
    }


    @Override
    public boolean persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.getId() > 0 && update)
            newValues.put(DBModel.C_ID, this.getId());
        else if (this.getId() > 0)
            return false;
        else
            update = false;

        newValues.put(DBModel.C_CAR_ID, this.carId);
        newValues.put(DBModel.C_TYPE, this.type);
        newValues.put(DBModel.C_DESCRIPTION, this.description);
        newValues.put(DBModel.C_KILOMETERS, this.kilometers);

        if (this.date != null)
            newValues.put(DBModel.C_DATE, this.date.getTime());

        newValues.put(DBModel.C_PRICE, this.price);
        newValues.put(DBModel.C_QUANTITY, this.quantity);

        if (update){
            DatabaseManager.getCurrentDatabase().update(DBModel.TABLE_NAME, newValues, DBModel.C_ID+"="+this.getId(), null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

            if (this.getId() <= 0)
                this.setId((int) insertedId);
        }

        return true;
    }

    @Override
    public boolean delete() {
        if (this.getId() > 0) {
            return DatabaseManager.getCurrentDatabase().delete(DBModel.TABLE_NAME, DBModel.C_ID + " = " + this.getId(), null) > 0;
        } else {
            return false;
        }
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

    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public int getCarId() {
        return carId;
    }
}
