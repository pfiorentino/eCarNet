package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;

import java.util.ArrayList;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 25/10/2015.
 */
public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private static Cursor exq;

    public User(int id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }



    public User()
    {

    }


    public static User getUser(SQLiteDatabase bdd)
    {
        exq = bdd.rawQuery("SELECT * FROM User;", null);
        if (exq.moveToNext()) {
            int id = getInt(DatabaseManager.C_USER_ID);
            String firstName = getString(DatabaseManager.C_USER_FIRSTNAME);
            String lastName = getString(DatabaseManager.C_USER_LASTNAME);
            String email = getString(DatabaseManager.C_USER_EMAIL);
            return new User(id, firstName, lastName, email, null);
        }
        return null;
    }


    public static void addUser(User user, SQLiteDatabase db) {
        if (user.getId() == 0){
            int id = 0;
            user.setId(id+1);
        }
        ContentValues newValues = new ContentValues();
        newValues.put(DatabaseManager.C_USER_ID, user.getId());
        newValues.put(DatabaseManager.C_USER_FIRSTNAME, user.getFirstName());
        newValues.put(DatabaseManager.C_USER_LASTNAME, user.getLastName());
        newValues.put(DatabaseManager.C_USER_EMAIL, user.getEmail());
        newValues.put(DatabaseManager.C_USER_PASSWORD, user.getPassword());

        System.out.println(DatabaseManager.T_USER);
        System.out.println(newValues.toString());
        db.insert(DatabaseManager.T_USER, null, newValues);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getId()
    {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static int getInt(String ColumnName) {
        return exq.getInt(exq.getColumnIndex(ColumnName));
    }

    public static String getString(String ColumnName) {
        return exq.getString(exq.getColumnIndex(ColumnName));
    }
}
