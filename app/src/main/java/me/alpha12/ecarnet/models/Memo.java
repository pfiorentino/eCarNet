package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 13/01/2016.
 */
public class Memo {

    private int id;
    private String title;
    private Date dateNote;
    private Date limitDate;
    private int kilometers;
    private int isNotifSet;
    private int isDone;
    private int isArchived;
    private int carId;

    /* Contructors */

    public Memo(int id, String firstName, Date date, Date limit, int distance, boolean notif, boolean done, boolean archived, int idCar) {
        this.id = id;
        this.title = firstName;
        this.dateNote = date;
        Log.d("current date", dateNote.toString());
        this.limitDate = limit;
        Log.d("limit date", limitDate.toString());
        this.kilometers = distance;
        if(notif)
            this.isNotifSet = 1;
        else this.isNotifSet = 0;
        if(done)
            this.isDone = 1;
        else this.isDone = 0;
        if(archived)
            this.isArchived = 1;
        else this.isArchived = 0;
        this.carId = idCar;
    }

    public void persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBMemo.C_ID, this.id);
        else
            update = false;

        newValues.put(DBMemo.C_TITLE, this.title);

        if (this.limitDate != null)
            newValues.put(DBMemo.C_DATE_SET, this.dateNote.getTime());

        if (this.limitDate != null)
            newValues.put(DBMemo.C_DATE_CREATED, this.limitDate.getTime());

        newValues.put(DBMemo.C_KILOMETERS, this.kilometers);
        newValues.put(DBMemo.C_NOTIFICATION, this.isNotifSet);
        newValues.put(DBMemo.C_DONE, this.isDone());
        newValues.put(DBMemo.C_ARCHIVED, this.isArchived());
        newValues.put(DBMemo.C_CAR_ID, this.carId);


        if (update){
            DatabaseManager.getCurrentDatabase().update(DBMemo.TABLE_NAME, newValues, DBMemo.C_ID+"="+this.id, null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBMemo.TABLE_NAME, null, newValues);

            if (this.id <= 0)
                this.id = (int) insertedId;
        }
    }


    public static Memo getLastNote(int carId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBMemo.TABLE_NAME +
                " WHERE car_id = " + carId + " ORDER BY " + DBMemo.C_DATE_SET+", " +DBMemo.C_KILOMETERS + " DESC LIMIT 1", null);

        if (cursor.moveToNext()) {
            return new Memo(
                    DatabaseManager.extractInt(cursor, DBMemo.C_ID),
                    DatabaseManager.extractString(cursor, DBMemo.C_TITLE),
                    DatabaseManager.extractDate(cursor, DBMemo.C_DATE_SET),
                    DatabaseManager.extractDate(cursor, DBMemo.C_DATE_CREATED),
                    DatabaseManager.extractInt(cursor, DBMemo.C_KILOMETERS),
                    DatabaseManager.extractInt(cursor, DBMemo.C_NOTIFICATION) == 1,
                    DatabaseManager.extractInt(cursor, DBMemo.C_DONE) == 1,
                    DatabaseManager.extractInt(cursor, DBMemo.C_ARCHIVED) == 1,
                    DatabaseManager.extractInt(cursor, DBMemo.C_CAR_ID)
            );
        }
        return null;
    }





    public static abstract class DBMemo implements BaseColumns {
        public static final String TABLE_NAME = "memos";
        public static final String C_ID = "id";
        public static final String C_TITLE = "title";
        public static final String C_DATE_SET = "date_limit";
        public static final String C_DATE_CREATED = "creation_date";
        public static final String C_KILOMETERS = "kilometers";
        public static final String C_NOTIFICATION = "notification";
        public static final String C_DONE = "done";
        public static final String C_ARCHIVED = "archived";
        public static final String C_CAR_ID = "car_id";

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " ("
                        + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + C_TITLE + " TEXT NOT NULL,"
                        + C_DATE_SET + " NUMERIC,"
                        + C_DATE_CREATED + " NUMERIC NOT NULL,"
                        + C_KILOMETERS + " INTEGER,"
                        + C_NOTIFICATION + " INTEGER NOT NULL,"
                        + C_DONE + " INTEGER NOT NULL,"
                        + C_ARCHIVED + " INTEGER NOT NULL,"
                        + C_CAR_ID + " INTEGER NOT NULL"
                        + ");";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDateNote() {
        return dateNote;
    }

    public void setDateNote(Date dateNote) {
        this.dateNote = dateNote;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public int isNotifSet() {
        return isNotifSet;
    }

    public void setIsNotifSet(boolean isNotifSet) {
        if(isNotifSet)
        this.isNotifSet = 1;
        else this.isNotifSet = 0;
    }

    public Date getLimitDate() {
        return limitDate;
    }

    public void setLimitDate(Date limitDate) {
        this.limitDate = limitDate;
    }

    public int isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone)
    {
        if(isDone)
        this.isDone = 1;
        else this.isDone = 0;
    }

    public int isArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        if(isArchived)
        this.isArchived = 1;
        else this.isArchived = 0;
    }
}
