package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.database.DBObject;
import me.alpha12.ecarnet.database.DatabaseManager;

public class Car extends DBObject {
    private CarModel model;
    private int kilometers;
    private Date buyingDate;
    private Date circulationDate;
    private double averageConsumption;
    private User owner;
    private ArrayList<User> sharedPeople;
    private String plateNum;

    /* Constructors */

    public Car(String plateNum, int kilometers, Date circulationDate, CarModel model) {
        this.plateNum = plateNum;
        this.kilometers = kilometers;
        this.circulationDate = circulationDate;
        this.model = model;
    }


    public Car(int id, CarModel model, int kilometers, Date buyingDate, Date circulationDate, String plateNum, double averageConsumption, User owner, ArrayList<User> sharedPeople) {
        this.setId(id);
        this.model = model;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.averageConsumption = averageConsumption;
        this.owner = owner;
        this.sharedPeople = sharedPeople;
        this.plateNum = plateNum;
    }


    public Car(int id, CarModel model, int kilometers, Date buyingDate, Date circulationDate, String plateNum, double averageConsumption) {
        this.setId(id);
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.model = model;
        this.plateNum = plateNum;
        this.averageConsumption = averageConsumption;
    }

    /* Public methods */

    @Override
    public String toString() {
        if (this.model != null)
            return Utils.ucWords(this.model.getBrand()+" "+this.model.getModel())+" - "+this.plateNum;
        else
            return "Modèle inconnu - "+this.getPlateNum();
    }


    public String getModelString() {
        if (this.model != null)
            return this.model.toString();
        else
            return "Modèle inconnu";
    }

    @Override
    public boolean delete() {
        return false;
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

        newValues.put(DBModel.C_KILOMETERS, this.kilometers);

        if (this.buyingDate != null)
            newValues.put(DBModel.C_BUYING_DATE, this.buyingDate.getTime());

        if (this.circulationDate != null)
            newValues.put(DBModel.C_CIRCULATION_DATE, this.circulationDate.getTime());

        newValues.put(DBModel.C_AVERAGE_CONSUMPTION, this.averageConsumption);
        newValues.put(DBModel.C_PLATE_NUMBER, this.plateNum);

        if (this.model != null)
            newValues.put(DBModel.C_MODEL_ID, this.model.getId());

        if (update){
            DatabaseManager.getCurrentDatabase().update(DBModel.TABLE_NAME, newValues, DBModel.C_ID+"="+this.getId(), null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

            if (this.getId() <= 0)
                this.setId((int) insertedId);
        }

        return true;
    }

    public Drawable getCarPicture(Context ctx) {
        File imgFile = new File(GlobalContext.getAppPicturePath() + String.valueOf(getId()) +  "_picture.png");

        // Fallback to default image
        if (!imgFile.exists())
            return ContextCompat.getDrawable(ctx, R.drawable.ic_default_car);

        return Drawable.createFromPath(imgFile.getAbsolutePath());
    }

    public Drawable getCarBanner(Context ctx) {
        File imgFile = new File(GlobalContext.getAppPicturePath() + String.valueOf(getId()) +  "_cover.png");

        // Fallback to default image
        if(!imgFile.exists())
            return ContextCompat.getDrawable(ctx, R.drawable.pic_default_background);

        return Drawable.createFromPath(imgFile.getAbsolutePath());
    }



    /* Static methods */

    public static HashMap<Integer, Car> findAll() {
        HashMap<Integer, Car> result = new HashMap<Integer, Car>();

        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM " + DBModel.TABLE_NAME,
                null
        );
        while(cursor.moveToNext()) {
            result.put(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    new Car(
                            DatabaseManager.extractInt(cursor, DBModel.C_ID),
                            CarModel.findById(DatabaseManager.extractInt(cursor, DBModel.C_MODEL_ID)),
                            DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                            DatabaseManager.extractDate(cursor, DBModel.C_BUYING_DATE),
                            DatabaseManager.extractDate(cursor, DBModel.C_CIRCULATION_DATE),
                            DatabaseManager.extractString(cursor, DBModel.C_PLATE_NUMBER),
                            DatabaseManager.extractDouble(cursor, DBModel.C_AVERAGE_CONSUMPTION)
                    )
            );
        }
        return result;
    }

    public static Car get(int carId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_ID + " = " + carId, null);

        if (cursor.moveToNext()) {
            return new Car(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    CarModel.findById(DatabaseManager.extractInt(cursor, DBModel.C_MODEL_ID)),
                    DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                    DatabaseManager.extractDate(cursor, DBModel.C_BUYING_DATE),
                    DatabaseManager.extractDate(cursor, DBModel.C_CIRCULATION_DATE),
                    DatabaseManager.extractString(cursor, DBModel.C_PLATE_NUMBER),
                    DatabaseManager.extractDouble(cursor, DBModel.C_AVERAGE_CONSUMPTION)
            );
        }

        return null;
    }

    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "cars";
        public static final String C_ID = "id";
        public static final String C_KILOMETERS = "kilometers";
        public static final String C_BUYING_DATE = "buying_date";
        public static final String C_CIRCULATION_DATE = "circulation_date";
        public static final String C_AVERAGE_CONSUMPTION = "average_consumption";
        public static final String C_PLATE_NUMBER = "plate_number";
        public static final String C_MODEL_ID = "model_id";

        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + C_ID + " INTEGER PRIMARY KEY,"
                + C_KILOMETERS + " INTEGER NOT NULL,"
                + C_BUYING_DATE + " INTEGER,"
                + C_CIRCULATION_DATE + " INTEGER,"
                + C_PLATE_NUMBER + " TEXT,"
                + C_AVERAGE_CONSUMPTION + " REAL NOT NULL,"
                + C_MODEL_ID + " INTEGER"
                + ");";
    }

    /* Getters & Setters */
    public String getPlateNum() {
        if (this.plateNum != null)
            return this.plateNum;
        else
            return "Aucune plaque enregistrée";
    }

    public CarModel getCarModel() {
        return this.model;
    }

    public int getKilometers() {
        return this.kilometers;
    }

    public String getStringCirculationDate() {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        return simpleDate.format(circulationDate);
    }

    public void setKilometers(int kilometers) {
        if (kilometers > this.kilometers)
            this.kilometers = kilometers;
    }


}
