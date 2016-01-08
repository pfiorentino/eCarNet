package me.alpha12.ecarnet.models;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by guilhem on 25/10/2015.
 */
public class Operation {

    private int id;
    private String name;
    private String type;
    private String piece;
    private int kilometersCycle;
    private int dateCycle;

    private static Cursor exq;


    public static ArrayList<Operation> getAllOperation(SQLiteDatabase bdd, int operationId)
    {
        ArrayList<Operation> opers = new ArrayList<>();
        exq = bdd.rawQuery("SELECT * FROM Operation WHERE id_operation = " + operationId, null);
        while(exq.moveToNext())
        {
            int id = getInt("id");
            String name = getString("name");
            String type = getString("type");
            String piece = getString("piece");
            int kilometersCycle = getInt("kilometersCycle");
            int dateCycle = getInt("dateCycle");
            opers.add(new Operation(id, name, type, piece, kilometersCycle, dateCycle));
        }
        return opers;
    }


    public Operation()
    {

    }
    public Operation(int id, String name, String type, String piece, int kilometersCycle, int dateCycle) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.piece = piece;
        this.kilometersCycle = kilometersCycle;
        this.dateCycle = dateCycle;
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

    public static java.util.Date getDate(String ColumnName)
    {
        return new java.util.Date(exq.getLong(exq.getColumnIndex(ColumnName))*1000);
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPiece() {
        return piece;
    }

    public void setPiece(String piece) {
        this.piece = piece;
    }

    public int getKilometersCycle() {
        return kilometersCycle;
    }

    public void setKilometersCycle(int kilometersCycle) {
        this.kilometersCycle = kilometersCycle;
    }

    public int getDateCycle() {
        return dateCycle;
    }

    public void setDateCycle(int dateCycle) {
        this.dateCycle = dateCycle;
    }
}
