package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Classe à refactorer complètement (ne contiendra toujours qu'un seul user)
 */

public class User {

    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    /* Contructors */

    public User(int id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    /* Public methods */

    public void persist() {
        SQLiteDatabase db = DatabaseManager.getCurrentDatabase();

        db.rawQuery("DELETE FROM " + DBModel.TABLE_NAME, null);
        db.rawQuery("DELETE FROM SQLITE_SEQUENCE WHERE name='" + DBModel.TABLE_NAME + "'", null);

        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);

        newValues.put(DBModel.C_FIRSTNAME, this.firstName);
        newValues.put(DBModel.C_LASTNAME, this.lastName);
        newValues.put(DBModel.C_EMAIL, this.email);
        newValues.put(DBModel.C_PASSWORD, this.password);

        db.insert(DBModel.TABLE_NAME, null, newValues);
    }

    /* Static methods */

    public static User getUser() {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME, null);

        if (cursor.moveToNext()) {
            return new User(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    DatabaseManager.extractString(cursor, DBModel.C_FIRSTNAME),
                    DatabaseManager.extractString(cursor, DBModel.C_LASTNAME),
                    DatabaseManager.extractString(cursor, DBModel.C_EMAIL),
                    null);
        }

        return null;
    }

    /* Database Model */

    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String C_ID = "id";
        public static final String C_EMAIL = "email";
        public static final String C_FIRSTNAME = "firstname";
        public static final String C_LASTNAME = "lastname";
        public static final String C_PASSWORD = "password";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + C_EMAIL + " TEXT NOT NULL,"
                        + C_FIRSTNAME + " TEXT NOT NULL,"
                        + C_LASTNAME + " TEXT NOT NULL,"
                        + C_PASSWORD + " TEXT NOT NULL"
                        + ");";
    }

    /* Getters & Setters */

    public int getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }
}
