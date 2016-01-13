package me.alpha12.ecarnet.models;

import android.database.Cursor;
import android.provider.BaseColumns;

import java.util.Date;

import me.alpha12.ecarnet.database.DatabaseManager;

/**
 * Created by guilhem on 13/01/2016.
 */
public class Note {

    private int id;
    private String title;
    private Date dateNote;
    private int kilometers;
    private boolean isNotifSet;
    private boolean isDone;
    private boolean isArchived;
    private int carId;

    /* Contructors */

    public Note(int id, String firstName, Date date, int distance, boolean notif, boolean done, boolean archived, int idCar) {
        this.id = id;
        this.title = firstName;
        this.dateNote = date;
        this.kilometers = distance;
        this.isNotifSet = notif;
        this.isDone = done;
        this.isArchived = archived;
        this.carId = idCar;
    }



    public static Note getLastNote(int carId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME +
                " WHERE car_id = " + carId + " ORDER BY date DESC LIMIT 1", null);

        if (cursor.moveToNext()) {
            return new Note(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    DatabaseManager.extractString(cursor, DBModel.C_TITLE),
                    DatabaseManager.extractDate(cursor, DBModel.C_DATE_SET),
                    DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                    DatabaseManager.extractInt(cursor, DBModel.C_NOTIFICATION) == 1,
                    DatabaseManager.extractInt(cursor, DBModel.C_DONE) == 1,
                    DatabaseManager.extractInt(cursor, DBModel.C_ARCHIVED) == 1,
                    DatabaseManager.extractInt(cursor, DBModel.C_CAR_ID)
            );
        }
        return null;
    }





    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "note";
        public static final String C_ID = "id";
        public static final String C_TITLE = "title";
        public static final String C_DATE_SET = "date";
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

    public boolean isNotifSet() {
        return isNotifSet;
    }

    public void setIsNotifSet(boolean isNotifSet) {
        this.isNotifSet = isNotifSet;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean isDone) {
        this.isDone = isDone;
    }

    public boolean isArchived() {
        return isArchived;
    }

    public void setIsArchived(boolean isArchived) {
        this.isArchived = isArchived;
    }
}
