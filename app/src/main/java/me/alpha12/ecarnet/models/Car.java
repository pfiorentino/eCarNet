package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Car {

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


    private static final SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");

    public Car(int uuid, String plateNum, Model model) {
        this.uuid = uuid;
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


    public static Car getSimpleCarById(SQLiteDatabase bdd, int id)
    {
        exq = bdd.rawQuery("SELECT * FROM Car WHERE id = " + id, null);
        if(exq.moveToFirst())
        {
            int ident = getInt("id");
            int kilometers = getInt("kilometers");
            double averageConsumption = getDouble("averageConsumption");
            Date buyingDate = getDate("buying_date");
            Date circulationDate = getDate("circulation_date");
            String plateNBR = getString("plate_number");
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
        exq = bdd.rawQuery("SELECT * FROM User u, Share s WHERE s.id_user = u.user AND s.id_car = " + this.uuid, null);
        while(exq.moveToNext()) {
            int idUser = getInt("id_user");
            String email = getString("email");
            String firstName = getString("first_name");
            String lastName = getString("last_name");
            users.add(new User(idUser, firstName, lastName, email));
        }
        this.sharedPeople = users;
    }


    public void getOwner(SQLiteDatabase bdd)
    {
        exq = bdd.rawQuery("SELECT * FROM Share WHERE id_car = " + this.uuid, null);
        if(exq.moveToFirst())
        {
            int idUser = getInt("id_user");
            String email = getString("email");
            String firstName = getString("first_name");
            String lastName = getString("last_name");
            this.owner = new User(idUser, firstName, lastName, email);
        }
    }


    public static ArrayList<Car> getAllCars(SQLiteDatabase bdd)
    {
        ArrayList<Car> cars = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM car", null);
        while(exq.moveToNext())
        {
            int ident = getInt("id");
            int kilometers = getInt("kilometers");
            double averageConsumption = getDouble("averageConsumption");
            Date buyingDate = getDate("buying_date");
            Date circulationDate = getDate("circulation_date");
            String plateNBR = getString("plate_number");
            cars.add(new Car(ident, null, kilometers, buyingDate, circulationDate, averageConsumption, null, null, null, plateNBR));
        }
        return cars;
    }


    public void getModelFromCar(SQLiteDatabase bdd) {
        exq = bdd.rawQuery("SELECT * FROM Own o, Model m WHERE o.id_car = " + this.uuid, null);
        if(exq.moveToFirst()) {
            int idModel = getInt("m.id");
            String brand = getString("brand");
            String model = getString("model");
            int year = getInt("year");
            String energy = getString("energy");
            String engine = getString("engine");
            int ratedHP = getInt("rated_hp");
            double consumption = getDouble("consumption");
            int doors = getInt("doors");
            String sub_model = getString("sub_model");
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
        ContentValues newValues = new ContentValues();
        newValues.put("id", car.uuid);
        newValues.put("kilometers", car.getKilometers());
        newValues.put("buying_date", car.getBuyingDate().getTime());
        newValues.put("circulation_date", car.getCirculationDate().getTime());
        newValues.put("average_consumption", car.getAverageConsumption());
        newValues.put("plate_number", car.getPlateNum());
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
}
