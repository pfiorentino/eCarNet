package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Model implements Parcelable{

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


    private static Cursor exq;
    private DatabaseManager helper;


    public Model(int id, String brand, String model, int year, String energy, String engine, int ratedHP, double consumption, int doors, String subModel) {
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

    public Model()
    {

    }

    public Model(String brand, String model, String engine) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
    }




    public static ArrayList<String> getBrands(SQLiteDatabase bdd)
    {
        ArrayList<String>brands = new ArrayList<String>();
        exq = bdd.rawQuery("SELECT DISTINCT(brand) FROM Model ORDER BY brand ASC;", null);
        while(exq.moveToNext()) {
            brands.add(getString(DatabaseManager.C_MODEL_BRAND));
        }
        return brands;
    }


    public static ArrayList<String> getModelFromBrand(SQLiteDatabase bdd, String brand)
    {
        ArrayList<String>models = new ArrayList<String>();
        String[]args = new String[1];
        args[0] = brand;
        exq = bdd.rawQuery("SELECT DISTINCT(model) FROM Model WHERE brand = ? ORDER BY model ASC;", args);
        while(exq.moveToNext()) {
            models.add(getString(DatabaseManager.C_MODEL_MODEL));
        }
        return models;
    }


    public static ArrayList<String> getYearsFromBrandAndModel(SQLiteDatabase bdd, String brand, String model)
    {
        ArrayList<String>years = new ArrayList<String>();
        String[]args = new String[2];
        args[0] = brand;
        args[1] = model;
        exq = bdd.rawQuery("SELECT DISTINCT(year) FROM Model WHERE brand = ? AND model = ? ORDER BY year ASC;", args);
        while(exq.moveToNext()) {
            years.add(Integer.toString(getInt(DatabaseManager.C_MODEL_YEAR)));
        }
        return years;
    }


    public static ArrayList<String> getRatedHPFromBrandModel(SQLiteDatabase bdd, String brand, String model)
    {
        ArrayList<String>ratedHP = new ArrayList<String>();
        String[]args = new String[2];
        args[0] = brand;
        args[1] = model;
        exq = bdd.rawQuery("SELECT DISTINCT(rated_hp) FROM Model WHERE brand = ? AND model = ? ORDER BY rated_hp ASC;", args);
        while(exq.moveToNext()) {
            ratedHP.add(getString(DatabaseManager.C_MODEL_RATED_HP));
        }
        return ratedHP;
    }





    public static void add(Model model, SQLiteDatabase db) {
        if (model.getId() == 0){
            int id = 0;
            String query = "SELECT MAX(id) as max FROM Model";
            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                id = cursor.getInt(0);
            }
            model.setId(id+1);
        }
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseManager.C_MODEL_ID, model.getId());
        newValues.put(DatabaseManager.C_MODEL_BRAND, model.getBrand());
        newValues.put(DatabaseManager.C_MODEL_MODEL, model.getModel());
        newValues.put(DatabaseManager.C_MODEL_CONSUMPTION, model.getConsumption());
        newValues.put(DatabaseManager.C_MODEL_ENERGY, model.getEnergy());
        newValues.put(DatabaseManager.C_MODEL_YEAR, model.getYear());
        newValues.put(DatabaseManager.C_MODEL_RATED_HP, model.getRatedHP());
        newValues.put(DatabaseManager.C_MODEL_ENGINE, model.getEngine());
        newValues.put(DatabaseManager.C_MODEL_DOORS, model.getDoors());
        newValues.put(DatabaseManager.C_MODEL_SUB_MODEL, model.getSubModel());

        db.insert(DatabaseManager.T_MODEL, null, newValues);
    }

    public static int getNumOfModel(SQLiteDatabase bdd)
    {
        try {
            exq = bdd.rawQuery("SELECT COUNT (*) FROM Model;", null);
            exq.moveToFirst();
            return exq.getInt(0);
        }
        catch (Exception e)
        {
            System.out.println(e);
            return 0;
        }
    }

    public static ArrayList<Model> getAllModel(SQLiteDatabase bdd)
    {
        ArrayList<Model>models = new ArrayList<Model>();
        exq = bdd.rawQuery("SELECT id, brand, model, year, energy, engine, rated_hp, consumption, doors, sub_model FROM Model ORDER BY BRAND, MODEL ASC, YEAR DESC;", null);
        while(exq.moveToNext()) {
            int idModel = getInt(DatabaseManager.C_MODEL_ID);
            String brandname = getString(DatabaseManager.C_MODEL_BRAND);
            String modelname = getString(DatabaseManager.C_MODEL_MODEL);
            int yearmodel = getInt(DatabaseManager.C_MODEL_YEAR);
            String energy = getString(DatabaseManager.C_MODEL_ENERGY);
            String engine = getString(DatabaseManager.C_MODEL_ENGINE);
            int rated_hp = getInt(DatabaseManager.C_MODEL_RATED_HP);
            double consumption = getDouble(DatabaseManager.C_MODEL_CONSUMPTION);
            int doors = getInt(DatabaseManager.C_MODEL_DOORS);
            String submodel = getString(DatabaseManager.C_MODEL_SUB_MODEL);

            models.add(new Model(idModel, brandname, modelname, yearmodel, energy, engine, rated_hp, consumption, doors, submodel));
        }
        return models;
    }


    public static Model getModelById(SQLiteDatabase bdd, int id)
    {
        String[]args = new String[1];
        args[0] = Integer.toString(id);
        exq = bdd.rawQuery("SELECT * FROM Model WHERE id = ?;", args);
        if(exq.moveToNext()) {
            int idModel = getInt(DatabaseManager.C_MODEL_ID);
            String brandname = getString(DatabaseManager.C_MODEL_BRAND);
            String modelname = getString(DatabaseManager.C_MODEL_MODEL);
            int yearmodel = getInt(DatabaseManager.C_MODEL_YEAR);
            String energy = getString(DatabaseManager.C_MODEL_ENERGY);
            String engine = getString(DatabaseManager.C_MODEL_ENGINE);
            int rated_hp = getInt(DatabaseManager.C_MODEL_RATED_HP);
            double consumption = getDouble(DatabaseManager.C_MODEL_CONSUMPTION);
            int doors = getInt(DatabaseManager.C_MODEL_DOORS);
            String submodel = getString(DatabaseManager.C_MODEL_SUB_MODEL);
            return new Model(idModel, brandname, modelname, yearmodel, energy, engine, rated_hp, consumption, doors, submodel);
        }
        else return null;
    }


    public static ArrayList<Model> getModelFromBrandModel(SQLiteDatabase bdd, String brand, String model)
    {
        ArrayList<Model>models = new ArrayList<Model>();
        String[]args = new String[2];
        args[0] = brand;
        args[1] = model;
        exq = bdd.rawQuery("SELECT * FROM Model WHERE brand = ? AND model = ?;", args);
        while(exq.moveToNext()) {
            int idModel = getInt(DatabaseManager.C_MODEL_ID);
            String brandname = getString(DatabaseManager.C_MODEL_BRAND);
            String modelname = getString(DatabaseManager.C_MODEL_MODEL);
            int yearmodel = getInt(DatabaseManager.C_MODEL_YEAR);
            String energy = getString(DatabaseManager.C_MODEL_ENERGY);
            String engine = getString(DatabaseManager.C_MODEL_ENGINE);
            int rated_hp = getInt(DatabaseManager.C_MODEL_RATED_HP);
            double consumption = getDouble(DatabaseManager.C_MODEL_CONSUMPTION);
            int doors = getInt(DatabaseManager.C_MODEL_DOORS);
            String submodel = getString(DatabaseManager.C_MODEL_SUB_MODEL);

            models.add(new Model(idModel, brandname, modelname, yearmodel, energy, engine, rated_hp, consumption, doors, submodel));
        }
        return models;
    }

    public String toString()
    {
        return subModel + " " + engine + " - " + doors + " portes - " + energy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getEnergy() {
        return energy;
    }

    public void setEnergy(String energy) {
        this.energy = energy;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public int getRatedHP() {
        return ratedHP;
    }

    public void setRatedHP(int ratedHP) {
        this.ratedHP = ratedHP;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public int getDoors() {
        return doors;
    }

    public void setDoors(int doors) {
        this.doors = doors;
    }

    public String getSubModel() {
        return subModel;
    }

    public void setSubModel(String subModel) {
        this.subModel = subModel;
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
}
