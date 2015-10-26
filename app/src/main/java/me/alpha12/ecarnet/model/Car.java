package me.alpha12.ecarnet.model;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Car {

    private int id;
    private Model model;
    private int kilometers;
    private Date buyingDate;
    private Date circulationDate;
    private double averageConsumption;
    private User owner;
    private ArrayList<User> sharedPeople;
    private ArrayList<Intervention> myInterventions;

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
