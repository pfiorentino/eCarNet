package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Date;

import me.alpha12.ecarnet.database.DBObject;
import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 13/01/2016.
 */
public class Memo extends DBObject {
    private String title;
    private Date dateNote;
    private Date limitDate;
    private Date modifDate;
    private int kilometers;
    private boolean notifSet;
    private boolean archived;
    private int carId;

    /* Contructors */
    public Memo(int id, String title, Date date, Date modif, Date limit, int distance, boolean notifSet, boolean archived, int idCar) {
        this.setId(id);

        this.title = title;
        this.dateNote = date;
        this.limitDate = limit;
        this.modifDate = modif;
        this.kilometers = distance;
        this.notifSet = notifSet;
        this.archived = archived;
        this.carId = idCar;
    }

    public Memo(Cursor cursor) {
        this.setId(DatabaseManager.extractInt(cursor, DBModel.C_ID));

        this.carId      = DatabaseManager.extractInt(cursor, DBModel.C_CAR_ID);
        this.title      = DatabaseManager.extractString(cursor, DBModel.C_TITLE);
        this.kilometers = DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS);
        this.dateNote   = DatabaseManager.extractDate(cursor, DBModel.C_DATE_CREATED);
        this.modifDate =  DatabaseManager.extractDate(cursor, DBModel.C_LAST_MODIFICATION);
        this.limitDate  = DatabaseManager.extractDate(cursor, DBModel.C_DATE_LIMIT_SET);
        this.notifSet   = DatabaseManager.extractInt(cursor, DBModel.C_NOTIFICATION) == 1;
        this.archived   = DatabaseManager.extractInt(cursor, DBModel.C_ARCHIVED) == 1;
    }

    public void persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.getId() > 0)
            newValues.put(DBModel.C_ID, this.getId());
        else
            update = false;

        newValues.put(DBModel.C_TITLE, this.title);

        if (this.dateNote != null)
            newValues.put(DBModel.C_DATE_CREATED, this.dateNote.getTime());

        if (this.limitDate != null)
            newValues.put(DBModel.C_DATE_LIMIT_SET, this.limitDate.getTime());

        if(this.modifDate != null)
            newValues.put(DBModel.C_LAST_MODIFICATION, this.modifDate.getTime());

        newValues.put(DBModel.C_KILOMETERS, this.kilometers);
        newValues.put(DBModel.C_NOTIFICATION, this.notifSet);
        newValues.put(DBModel.C_ARCHIVED, this.archived);
        newValues.put(DBModel.C_CAR_ID, this.carId);

        if (update){
            DatabaseManager.getCurrentDatabase().update(DBModel.TABLE_NAME, newValues, DBModel.C_ID+"="+this.getId(), null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

            if (this.getId() <= 0)
                this.setId((int) insertedId);
        }
    }

    @Override
    public boolean delete() {
        if (this.getId() > 0) {
            return DatabaseManager.getCurrentDatabase().delete(DBModel.TABLE_NAME, DBModel.C_ID + " = " + this.getId(), null) > 0;
        } else {
            return false;
        }
    }

    public static ArrayList<Memo> findAllByCar(int carId) {
        ArrayList<Memo> result = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+DBModel.TABLE_NAME+" WHERE "+DBModel.C_CAR_ID+" = " + carId,
                null
        );
        while(cursor.moveToNext()) {
            result.add(new Memo(cursor));
        }
        return result;
    }

    public static Memo getLastByCar(int carId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME +
                " WHERE car_id = " + carId + " ORDER BY " + DBModel.C_DATE_LIMIT_SET +", " +DBModel.C_KILOMETERS + " DESC LIMIT 1", null);

        if (cursor.moveToNext()) {
            return new Memo(cursor);
        }

        return null;
    }

    public static Memo get(int memoId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME +
                " WHERE " +DBModel.C_ID+" = " + memoId, null);

        if (cursor.moveToNext()) {
            return new Memo(cursor);
        }
        return null;
    }

    public String getTitle() {
        return title;
    }

    public Date getDateNote() {
        return dateNote;
    }

    public int getKilometers() {
        return kilometers;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public Date getModifDate() {
        return modifDate;
    }

    public int getCarId() {
        return carId;
    }

    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "memos";
        public static final String C_ID = "id";
        public static final String C_TITLE = "title";
        public static final String C_DATE_LIMIT_SET = "date_limit";
        public static final String C_DATE_CREATED = "creation_date";
        public static final String C_LAST_MODIFICATION = "mod_date";
        public static final String C_KILOMETERS = "kilometers";
        public static final String C_NOTIFICATION = "notification";
        public static final String C_ARCHIVED = "archived";
        public static final String C_CAR_ID = "car_id";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + C_TITLE + " TEXT NOT NULL,"
                        + C_DATE_LIMIT_SET + " NUMERIC,"
                        + C_DATE_CREATED + " NUMERIC NOT NULL,"
                        + C_LAST_MODIFICATION + " NUMERIC,"
                        + C_KILOMETERS + " INTEGER,"
                        + C_NOTIFICATION + " INTEGER NOT NULL,"
                        + C_ARCHIVED + " INTEGER NOT NULL,"
                        + C_CAR_ID + " INTEGER NOT NULL"
                        + ");";
    }
}
