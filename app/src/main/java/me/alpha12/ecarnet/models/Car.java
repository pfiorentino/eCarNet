package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Car {

    private int id;
    public Model model;
    private int kilometers;
    private Date buyingDate;
    private Date circulationDate;
    private double averageConsumption;
    private User owner;
    private ArrayList<User> sharedPeople;
    private ArrayList<Intervention> myInterventions;
    private String plateNum;


    private static final SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

    public Car(String plateNum, Model model) {
        this.plateNum = plateNum;
        this.model = model;
    }


    private static Cursor exq;
    private DatabaseManager helper;
    private String[] columns = {
            helper.C_CAR_ID,
            helper.C_CAR_KILLOMETERS,
            helper.C_CAR_BUYING_DATE,
            helper.C_CAR_AVERAGE_CONSUMPTION,
            helper.C_CAR_PLATE_NUMBER
    };


    public Car(int id, Model model, int kilometers, Date buyingDate, Date circulationDate, double averageConsumption, User owner, ArrayList<User> sharedPeople, ArrayList<Intervention> myInterventions, String plateNum) {
        this.id = id;
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


    public Car(int id, int kilometers, Date buyingDate, Date circulationDate, Model model, String plateNum, double averageConsumption) {
        this.id = id;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.model = model;
        this.plateNum = plateNum;
        this.averageConsumption = averageConsumption;
    }


    public static Car getSimpleCarById(SQLiteDatabase bdd, int id)
    {
        exq = bdd.rawQuery("SELECT * FROM Car WHERE id = " + id, null);
        if(exq.moveToFirst())
        {
            int ident = getInt(DatabaseManager.C_CAR_ID);
            int kilometers = getInt(DatabaseManager.C_CAR_KILLOMETERS);
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


    public void getSharedPeopleFromCar(SQLiteDatabase bdd)
    {
        ArrayList<User> users = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM User u, Share s WHERE s.id_user = u.user AND s.id_car = " + this.id, null);
        while(exq.moveToNext()) {
            int idUser = getInt(DatabaseManager.C_USER_ID);
            String email = getString(DatabaseManager.C_USER_EMAIL);
            String firstName = getString(DatabaseManager.C_USER_FIRSTNAME);
            String lastName = getString(DatabaseManager.C_USER_LASTNAME);
            users.add(new User(idUser, firstName, lastName, email));
        }
        this.sharedPeople = users;
    }


    public void getOwner(SQLiteDatabase bdd) {
        exq = bdd.rawQuery("SELECT * FROM Share WHERE id_car = " + this.id, null);
        if(exq.moveToFirst())
        {
            int idUser = getInt(DatabaseManager.C_USER_ID);
            String email = getString(DatabaseManager.C_USER_EMAIL);
            String firstName = getString(DatabaseManager.C_USER_FIRSTNAME);
            String lastName = getString(DatabaseManager.C_USER_LASTNAME);
            this.owner = new User(idUser, firstName, lastName, email);
        }
    }


    public static ArrayList<Car> getAllCars(SQLiteDatabase bdd) {
        ArrayList<Car> cars = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM car", null);
        while(exq.moveToNext())
        {
            int ident = getInt(DatabaseManager.C_CAR_ID);
            int kilometers = getInt(DatabaseManager.C_CAR_KILLOMETERS);
            double averageConsumption = getDouble(DatabaseManager.C_CAR_AVERAGE_CONSUMPTION);
            Date buyingDate = getDate(DatabaseManager.C_CAR_BUYING_DATE);
            Date circulationDate = getDate(DatabaseManager.C_CAR_CIRCULATION_DATE);
            String plateNBR = getString(DatabaseManager.C_CAR_PLATE_NUMBER);
            cars.add(new Car(ident, null, kilometers, buyingDate, circulationDate, averageConsumption, null, null, null, plateNBR));
        }
        return cars;
    }




    public void getModelFromCar(SQLiteDatabase bdd) {
        exq = bdd.rawQuery("SELECT * FROM Own o, Model m WHERE o.id_car = " + this.id, null);
        if(exq.moveToFirst()) {
            int idModel = getInt("m.id");
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
        if (car.getId() == 0){
            int id = 0;
            String query = "SELECT MAX(id) as max FROM Car";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
            car.setId(id + 1);
        }
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseManager.C_CAR_ID, car.uuid);
        newValues.put(DatabaseManager.C_CAR_KILLOMETERS, car.getKilometers());
        newValues.put(DatabaseManager.C_CAR_BUYING_DATE, car.getBuyingDate().getTime());
        newValues.put(DatabaseManager.C_CAR_CIRCULATION_DATE, car.getCirculationDate().getTime());
        newValues.put(DatabaseManager.C_CAR_AVERAGE_CONSUMPTION, car.getAverageConsumption());
        newValues.put(DatabaseManager.C_CAR_PLATE_NUMBER, car.getPlateNum());
        db.insert("Car", null, newValues);
    }










    public static int getInt(String ColumnName)
    {
        return exq.getInt(exq.getColumnIndex(ColumnName));
    }


    public static String getString(String ColumnName)
    {
        return exq.getString(exq.getColumnIndex(ColumnName));
    }

    public static double getDouble(String ColumnName)
    {
        return exq.getDouble((exq.getColumnIndex(ColumnName)));
    }

    public static Date getDate(String ColumnName)
    {
        return new Date(exq.getLong(exq.getColumnIndex(ColumnName))*1000);
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

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
}
