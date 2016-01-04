package me.alpha12.ecarnet.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

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

    private static Cursor exq;


    private static ArrayList<Intervention> getAllIntervention(SQLiteDatabase bdd, int carId)
    {
        ArrayList<Intervention> inters = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM Intervention WHERE id_car = " + carId, null);
        while(exq.moveToNext())
        {
            int id = getInt("id");
            int kilometers = getInt("kilometers");
            double price = getDouble("price");
            double amount = getDouble("quantity");
            Date dateIntervention = getDate("date_intervention");
            inters.add(new Intervention(id, kilometers, price, dateIntervention, Operation.getAllOperation(bdd, id), amount));
        }
        return inters;
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


    public Intervention(int id, int kilometers, double price, Date dateIntervention, ArrayList<Operation> myOperations, double amount)
    {
        this.id = id;
        this.kilometers = kilometers;
        this.price = price;
        this.dateIntervention = dateIntervention;
        this.myOperations = myOperations;
        this.amount = amount;
    }


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
