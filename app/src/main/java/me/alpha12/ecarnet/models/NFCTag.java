package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 13/01/2016.
 */
public class NFCTag implements Serializable {
    public static final String MIME_ADD_FILLUP      = "application/ecarnet.fillup";
    public static final String MIME_ADD_OPERATION   = "application/ecarnet.operation";
    public static final String MIME_ADD_MEMO        = "application/ecarnet.memo";
    public static final String MIME_CAR_INFO        = "application/ecarnet.carInfo";

    private int id;
    private String name;
    private String mimeType;
    private String message;

    private boolean selected;

    /* Contructors */

    public NFCTag(String name, String mimeType, String message) {
        this.name = name;
        this.mimeType = mimeType;
        this.message = message;
    }

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

    public void persist() {
        persist(false);
    }

    public boolean persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.id > 0 && update)
            newValues.put(DBModel.C_ID, this.id);
        else if (this.id > 0)
            return false;
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

        return true;
    }

    public boolean delete() {
        if (this.getId() > 0) {
            return DatabaseManager.getCurrentDatabase().delete(DBModel.TABLE_NAME, DBModel.C_ID + " = " + this.getId(), null) > 0;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name+" - "+this.mimeType+" - "+this.message;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public String getMimeType() {
        return mimeType;
    }

    public String getMimeTypeDesc() {
        switch (mimeType) {
            case MIME_ADD_FILLUP:
                return "Nouveau plein";
            case MIME_ADD_OPERATION:
                return "Nouvelle intervention";
            case MIME_ADD_MEMO:
                return "Nouveau mémo";
            case MIME_CAR_INFO:
                return "Infos du véhicule";
        }

        return null;
    }

    public Integer getMimeTypeIcon() {
        if(this.selected){
            return R.drawable.ic_check_tblack_24dp;
        } else {
            switch (mimeType) {
                case MIME_ADD_FILLUP:
                    return R.drawable.ic_local_gas_station_tblack_24dp;
                case MIME_ADD_OPERATION:
                    return R.drawable.ic_local_car_wash_tblack_24dp;
                case MIME_ADD_MEMO:
                    return R.drawable.ic_notifications_tblack_24dp;
                case MIME_CAR_INFO:
                    return R.drawable.ic_info_outline_tblack_24dp;
            }
        }

        return null;
    }

    public Car getCar() {
        try {
            int carId = Integer.parseInt(this.message);
            return Car.findCarById(carId);
        } catch (NumberFormatException e) {
            Log.e("eCarNet", "Invalid tag message \"" + message + "\"");
            return null;
        }
    }

    /* Static methods */

    public static NFCTag get(int tagId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_ID + " = " + tagId + " LIMIT 1", null);

        if (cursor.moveToNext()) {
            return new NFCTag(cursor);
        }

        return null;
    }

    public static ArrayList<NFCTag> findAll() {
        return findAll(null);
    }

    public static ArrayList<NFCTag> findAll(String order) {
        ArrayList<NFCTag> result = new ArrayList<>();

        String query = "SELECT * FROM "+DBModel.TABLE_NAME;
        if (order != null) {
            query += " ORDER BY "+order;
        }

        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                query,
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
