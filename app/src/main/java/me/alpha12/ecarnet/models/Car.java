package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 25/10/2015.
 */


public class Car {


    //attributes used by database

    public int uuid;
    public Model model;
    private int kilometers;
    private Date buyingDate;
    private Date circulationDate;
    private double averageConsumption;
    private User owner;
    private ArrayList<User> sharedPeople;
    private ArrayList<Intervention> myInterventions;
    private String plateNum;


    //Attributes used for processing
    private static Cursor exq;
    private DatabaseManager helper;
    private String[] columns = {
            helper.C_CAR_ID,
            helper.C_CAR_KILOMETERS,
            helper.C_CAR_BUYING_DATE,
            helper.C_CAR_AVERAGE_CONSUMPTION,
            helper.C_CAR_PLATE_NUMBER
    };
    private static final SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");




    //-------Contructors----------------------------------------------------


    public Car(int uuid, String plateNum, Model model) {
        this.uuid = uuid;
        this.plateNum = plateNum;
        this.model = model;
    }


    public Car(int uuid, Model model, int kilometers, Date buyingDate, Date circulationDate, double averageConsumption, User owner, ArrayList<User> sharedPeople, ArrayList<Intervention> myInterventions, String plateNum) {
        this.uuid = uuid;
        this.model = model;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.averageConsumption = averageConsumption;
        this.owner = owner;
        this.sharedPeople = sharedPeople;
        this.myInterventions = myInterventions;
        this.plateNum = plateNum;
    }


    public Car(int uuid, int kilometers, Date buyingDate, Date circulationDate, Model model, String plateNum, double averageConsumption) {
        this.uuid = uuid;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.model = model;
        this.plateNum = plateNum;
        this.averageConsumption = averageConsumption;
    }



    //---Database accessors---------------------------------------------------------

    public static Car getSimpleCarById(SQLiteDatabase bdd, int id)
    {
        String[] args = {};
        args[0] = Integer.toString(id);
        exq = bdd.rawQuery("SELECT * FROM Car WHERE id = ?;", args);
        if(exq.moveToFirst())
        {
            int ident = getInt(DatabaseManager.C_CAR_ID);
            int kilometers = getInt(DatabaseManager.C_CAR_KILOMETERS);
            double averageConsumption = getDouble(DatabaseManager.C_CAR_AVERAGE_CONSUMPTION);
            Date buyingDate = getDate(DatabaseManager.C_CAR_BUYING_DATE);
            Date circulationDate = getDate(DatabaseManager.C_CAR_CIRCULATION_DATE);
            String plateNBR = getString(DatabaseManager.C_CAR_PLATE_NUMBER);
            return new Car(ident, null, kilometers, buyingDate, circulationDate, averageConsumption, null, null, null, plateNBR);
        }
        else
        {
            return null;
        }
    }


    public void getSharedPeopleFromCar(SQLiteDatabase bdd, int id)
    {
        String[] args = {};
        args[0] = Integer.toString(id);
        ArrayList<User> users = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM User u, Use s WHERE s.id_user = u.user AND s.id_car = ? AND s.own = 0;", args);
        while(exq.moveToNext()) {
            int idUser = getInt(DatabaseManager.C_USE_USER_ID);
            String email = getString(DatabaseManager.C_USER_EMAIL);
            String firstName = getString(DatabaseManager.C_USER_FIRSTNAME);
            String lastName = getString(DatabaseManager.C_USER_LASTNAME);
            users.add(new User(idUser, firstName, lastName, email, null));
        }
        this.sharedPeople = users;
    }


    public void getOwner(SQLiteDatabase bdd, int id)
    {
        String[] args = {};
        args[0] = Integer.toString(id);
        exq = bdd.rawQuery("SELECT * FROM User u, Use s WHERE s.id_car = ? AND s.id_user = u.id_user AND own = 1;", args);
        if(exq.moveToFirst())
        {
            int idUser = getInt(DatabaseManager.C_USE_USER_ID);
            String email = getString(DatabaseManager.C_USER_EMAIL);
            String firstName = getString(DatabaseManager.C_USER_FIRSTNAME);
            String lastName = getString(DatabaseManager.C_USER_LASTNAME);
            this.owner = new User(idUser, firstName, lastName, email, null);
        }
    }


    public static HashMap<String, Car> getAllCars(SQLiteDatabase bdd)
    {
        String uidd = "uidd_10";
        int i = 0;
        HashMap<String, Car> cars = new HashMap<String, Car>();
        exq = bdd.rawQuery("SELECT * FROM car", null);
        while(exq.moveToNext())
        {
            int ident = getInt(DatabaseManager.C_CAR_ID);
            int kilometers = getInt(DatabaseManager.C_CAR_KILOMETERS);
            double averageConsumption = getDouble(DatabaseManager.C_CAR_AVERAGE_CONSUMPTION);
            Date buyingDate = getDate(DatabaseManager.C_CAR_BUYING_DATE);
            Date circulationDate = getDate(DatabaseManager.C_CAR_CIRCULATION_DATE);
            String plateNBR = getString(DatabaseManager.C_CAR_PLATE_NUMBER);
            int id_model = getInt(DatabaseManager.C_CAR_MODEL_ID);
            uidd += i;
            cars.put(uidd, new Car(ident, Model.getModelById(bdd, id_model), kilometers, buyingDate, circulationDate, averageConsumption, null, null, null, plateNBR));
            i++;
        }
        return cars;
    }


    public void getModelFromCar(SQLiteDatabase bdd, int id) {
        String[] args = {};
        args[0] = Integer.toString(id);
        exq = bdd.rawQuery("SELECT * FROM Car c, Model m WHERE c.id_car = ?", args);
        if(exq.moveToFirst()) {
            int idModel = getInt(DatabaseManager.C_CAR_MODEL_ID);
            String brand = getString(DatabaseManager.C_MODEL_BRAND);
            String model = getString(DatabaseManager.C_MODEL_MODEL);
            int year = getInt(DatabaseManager.C_MODEL_YEAR);
            String energy = getString(DatabaseManager.C_MODEL_ENERGY);
            String engine = getString(DatabaseManager.C_MODEL_ENGINE);
            int ratedHP = getInt(DatabaseManager.C_MODEL_RATED_HP);
            double consumption = getDouble(DatabaseManager.C_MODEL_CONSUMPTION);
            int doors = getInt(DatabaseManager.C_MODEL_DOORS);
            String sub_model = getString(DatabaseManager.C_MODEL_SUB_MODEL);
            Model mod = new Model(idModel, brand, model, year, energy, engine, ratedHP, consumption, doors, sub_model);
            this.model = mod;
        }
    }


    public static void addCar(Car car, SQLiteDatabase db) {
        if (car.uuid == 0){
            int id = 0;
            String query = "SELECT MAX(id) as max FROM Car";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
            car.uuid = id + 1;
        }

        if(car.getBuyingDate() == null)
        {
            car.setBuyingDate(new Date());
        }
        if(car.getCirculationDate() == null)
        {
            car.setCirculationDate(new Date());
        }
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseManager.C_CAR_ID, car.uuid);
        newValues.put(DatabaseManager.C_CAR_KILOMETERS, car.getKilometers());
        newValues.put(DatabaseManager.C_CAR_BUYING_DATE, car.getBuyingDate().getTime());
        newValues.put(DatabaseManager.C_CAR_CIRCULATION_DATE, car.getCirculationDate().getTime());
        newValues.put(DatabaseManager.C_CAR_AVERAGE_CONSUMPTION, car.getAverageConsumption());
        newValues.put(DatabaseManager.C_CAR_PLATE_NUMBER, car.getPlateNum());
        newValues.put(DatabaseManager.C_CAR_MODEL_ID, car.getModel().getId());
        db.insert(DatabaseManager.T_CAR, null, newValues);
    }


    public static int getNumOfCarr(SQLiteDatabase bdd)
    {
        try {
            exq = bdd.rawQuery("SELECT COUNT (*) FROM Car;", null);
            exq.moveToFirst();
            return exq.getInt(0);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return 0;
        }
    }





    //--------Getters and setters----------------------------------------

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public Date getBuyingDate() {
        return buyingDate;
    }

    public void setBuyingDate(Date buyingDate) {
        this.buyingDate = buyingDate;
    }

    public Date getCirculationDate() {
        return circulationDate;
    }

    public void setCirculationDate(Date circulationDate) {
        this.circulationDate = circulationDate;
    }

    public double getAverageConsumption() {
        return averageConsumption;
    }

    public void setAverageConsumption(double averageConsumption) {
        this.averageConsumption = averageConsumption;
    }

    public ArrayList<User> getSharedPeople() {
        return sharedPeople;
    }

    public void setSharedPeople(ArrayList<User> sharedPeople) {
        this.sharedPeople = sharedPeople;
    }

    public Drawable getCarPicture(Context ctx) {
        String filePath = "/sdcard/Images/test_image.jpg";
        File imgFile = new  File(filePath);

        if(imgFile.exists()){
            return Drawable.createFromPath(imgFile.getAbsolutePath());
        }

        return ContextCompat.getDrawable(ctx, R.drawable.ic_car_profile_picture);
    }

    public Drawable getCarBanner(Context ctx) {
        String filePath = "/sdcard/Images/test_image.jpg";
        File imgFile = new  File(filePath);

        if(imgFile.exists()){
            return Drawable.createFromPath(imgFile.getAbsolutePath());
        }

        return ContextCompat.getDrawable(ctx, R.drawable.default_car_background);
    }



    //------functions for processing------------------------------------------

    public static int getInt(String ColumnName) {
        return exq.getInt(exq.getColumnIndex(ColumnName));
    }

    public static String getString(String ColumnName) {
        return exq.getString(exq.getColumnIndex(ColumnName));
    }

    public static double getDouble(String ColumnName) {
        return exq.getDouble((exq.getColumnIndex(ColumnName)));
    }

    public static Date getDate(String ColumnName) {
        return new Date(exq.getLong(exq.getColumnIndex(ColumnName))*1000);
    }
}
