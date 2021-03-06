package me.alpha12.ecarnet.database; /**
 * Created by guilhem on 05/10/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.zip.ZipInputStream;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.models.Car;
import me.alpha12.ecarnet.models.Intervention;
import me.alpha12.ecarnet.models.Reminder;
import me.alpha12.ecarnet.models.NFCTag;
import me.alpha12.ecarnet.models.User;

public class DatabaseManager extends SQLiteOpenHelper {
    //Table Use

    public static final String T_USE = "use";
    public static final String C_USE_CAR_ID = "car_id";
    public static final String C_USE_USER_ID = "user_id";
    public static final String C_USE_OWN = "own";
    private static final String SQL_CREATE_TABLE_USE =
            "CREATE TABLE " + T_USE + "("
                    + C_USE_CAR_ID + " INTEGER NOT NULL,"
                    + C_USE_USER_ID + " INTEGER NOT NULL,"
                    + C_USE_OWN + " NUMERIC NOT NULL"
                    + ");";

    private static final String DATABASE_NAME = GlobalContext.getInstance().getPackageName() + ".db";
    private static final int DATABASE_VERSION = 1;
    private String DATABASE_PATH; // Defined in constructor

    private SQLiteDatabase db;
    private Context ctx;

    private static DatabaseManager instance;


    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager(GlobalContext.getInstance());
        }
        return instance;
    }


    public void open() {
        this.db = getInstance().getWritableDatabase();
    }

    public void close() {
        if (getInstance() != null) {
            this.getInstance().close();
        }
    }

    public static void initialize() {
        getInstance();
    }

    public static SQLiteDatabase getCurrentDatabase() {
        return getInstance().db;
    }

    private DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.ctx = context;

        String filesDir = context.getFilesDir().getPath();
        DATABASE_PATH = filesDir.substring(0, filesDir.lastIndexOf("/")) + "/databases/";

        if (!databaseExists()) {
            initDatabase();
        }

        this.db = getWritableDatabase();
    }


    private boolean databaseExists() {
        File dbfile = new File(DATABASE_PATH + DATABASE_NAME);
        return dbfile.exists();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database", "This method isn't used anymore");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int prevVersion, int nextVersion) {
        Log.d("Database", "onUpgrade()");
        if (prevVersion < nextVersion){
            Log.w("Database", "Warning: this manager doesn't support database upgrade. Please contact the dev team for more details.");
            this.ctx.deleteDatabase(DATABASE_NAME);
            System.exit(1);
        }
    }

    private void initDatabase() {
        Log.d("Database", "Database initialization");
        ZipInputStream zipInputFile;
        try {
            InputStream inputFile = ctx.getAssets().open("ecarnet.db.zip");
            zipInputFile = new ZipInputStream(new BufferedInputStream(inputFile));
            zipInputFile.getNextEntry();

            File pathFile = new File(DATABASE_PATH);
            if(!pathFile.exists()) {
                if(!pathFile.mkdirs()) {
                    Log.e("DatabaseManager", "Error : copydatabase(), pathFile.mkdirs()");
                    return;
                }
            }

            OutputStream ouputFile = new FileOutputStream(DATABASE_PATH + DATABASE_NAME);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = zipInputFile.read(buffer)) > 0) {
                ouputFile.write(buffer, 0, length);
            }

            ouputFile.flush();
            ouputFile.close();
            inputFile.close();
            zipInputFile.close();
        }
        catch (IOException e) {
            Log.e("DatabaseManager", "Error : copydatabase()");
            e.printStackTrace();
        }

        try {
            SQLiteDatabase checkdb = SQLiteDatabase.openDatabase(DATABASE_PATH + DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE);
            checkdb.setVersion(DATABASE_VERSION);
        } catch(SQLiteException e) {
            Log.e("DatabaseManager", "Error : Version number can't be set");
            e.printStackTrace();
        }

        // Additional tables creation
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(Car.DBModel.SQL_CREATE_TABLE);
        db.execSQL(Intervention.DBModel.SQL_CREATE_TABLE);
        db.execSQL(User.DBModel.SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE_USE);
        db.execSQL(Reminder.DBModel.SQL_CREATE_TABLE);
        db.execSQL(NFCTag.DBModel.SQL_CREATE_TABLE);
    }


    public static int extractInt(Cursor cur, String columnName){
        return cur.getInt(cur.getColumnIndex(columnName));
    }

    @Deprecated
    public static double extractDouble(Cursor cur, String columnName){
        return cur.getDouble(cur.getColumnIndex(columnName));
    }

    public static float extractFloat(Cursor cur, String columnName){
        return cur.getFloat(cur.getColumnIndex(columnName));
    }

    public static String extractString(Cursor cur, String columnName){
        return cur.getString(cur.getColumnIndex(columnName));
    }

    public static Date extractDate(Cursor cur, String columnName) {
        long timestamp = cur.getLong(cur.getColumnIndex(columnName));

        if (timestamp > 0) {
            return new Date(timestamp);
        } else {
            return null;
        }
    }

    public static Calendar extractCalendar(Cursor cur, String columName) {
        Calendar cal = Calendar.getInstance();
        Date date = extractDate(cur, columName);
        if (date != null) {
            cal.setTime(date);
            return cal;
        } else {
            return null;
        }
    }
}

