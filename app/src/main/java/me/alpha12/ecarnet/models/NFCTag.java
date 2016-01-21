package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 13/01/2016.
 */
public class NFCTag {
    public static final String MIME_ADD_FILLUP = "application/ecarnet.fillup";

    private int id;
    private String name;
    private String mimeType;
    private String message;

    /* Contructors */

    public NFCTag(int id, String name, String mimeType, String message) {
        this.id = id;
        this.name = name;
        this.mimeType = mimeType;
        this.message = message;
    }

    public NFCTag(Cursor cursor) {
        this.id         = DatabaseManager.extractInt(cursor, DBModel.C_ID);
        this.name       = DatabaseManager.extractString(cursor, DBModel.C_NAME);
        this.mimeType   = DatabaseManager.extractString(cursor, DBModel.C_MIME);
        this.message    = DatabaseManager.extractString(cursor, DBModel.C_MESSAGE);
    }

    /* Public methods */

    public void persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);
        else
            update = false;

        newValues.put(DBModel.C_NAME, this.name);
        newValues.put(DBModel.C_MIME, this.mimeType);
        newValues.put(DBModel.C_MESSAGE, this.message);


        if (update){
            DatabaseManager.getCurrentDatabase().update(DBModel.TABLE_NAME, newValues, DBModel.C_ID+"="+this.id, null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

            if (this.id <= 0)
                this.id = (int) insertedId;
        }
    }

    /* Static methods */

    public static ArrayList<NFCTag> findAll() {
        ArrayList<NFCTag> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME,
                null
        );

        while(cursor.moveToNext()) {
            result.add(new NFCTag(cursor));
        }

        return result;
    }

    /* Database Model */

    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "nfc_tags";
        public static final String C_ID = "id";
        public static final String C_NAME = "name";
        public static final String C_MIME = "mime_type";
        public static final String C_MESSAGE = "message";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + C_ID + " INTEGER PRIMARY KEY,"
                        + C_NAME + " TEXT NOT NULL,"
                        + C_MIME + " TEXT NOT NULL,"
                        + C_MESSAGE + " TEXT NOT NULL"
                        + ");";
    }
}
