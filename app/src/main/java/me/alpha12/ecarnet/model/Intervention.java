package me.alpha12.ecarnet.model;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Intervention {

    private int id;
    private int kilometers;
    private double price;
    private Date dateIntervention;
    private ArrayList<Operation> myOperations;
    private double amount;

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getDateIntervention() {
        return dateIntervention;
    }

    public void setDateIntervention(Date dateIntervention) {
        this.dateIntervention = dateIntervention;
    }

    public ArrayList<Operation> getMyOperations() {
        return myOperations;
    }

    public void setMyOperations(ArrayList<Operation> myOperations) {
        this.myOperations = myOperations;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
