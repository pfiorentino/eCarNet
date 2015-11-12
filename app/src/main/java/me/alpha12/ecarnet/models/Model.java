package me.alpha12.ecarnet.models;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Model {

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


    public Model(String brand, String model, String engine) {
        this.brand = brand;
        this.model = model;
        this.engine = engine;
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
}
