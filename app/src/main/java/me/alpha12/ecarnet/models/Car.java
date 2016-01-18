package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import me.alpha12.ecarnet.GlobalContext;
import me.alpha12.ecarnet.R;
import me.alpha12.ecarnet.Utils;
import me.alpha12.ecarnet.database.DatabaseManager;

public class Car {

    private int id;
    private CarModel model;
    private int kilometers;
    private Date buyingDate;
    private Date circulationDate;
    private double averageConsumption;
    private User owner;
    private ArrayList<User> sharedPeople;
    private String plateNum;

    /* Constructors */

    public Car(String plateNum, int kilometers, Date circulationDate, CarModel model) {
        this.plateNum = plateNum;
        this.kilometers = kilometers;
        this.circulationDate = circulationDate;
        this.model = model;
    }


    public Car(int id, CarModel model, int kilometers, Date buyingDate, Date circulationDate, String plateNum, double averageConsumption, User owner, ArrayList<User> sharedPeople) {
        this.id = id;
        this.model = model;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.averageConsumption = averageConsumption;
        this.owner = owner;
        this.sharedPeople = sharedPeople;
        this.plateNum = plateNum;
    }


    public Car(int id, CarModel model, int kilometers, Date buyingDate, Date circulationDate, String plateNum, double averageConsumption) {
        this.id = id;
        this.kilometers = kilometers;
        this.buyingDate = buyingDate;
        this.circulationDate = circulationDate;
        this.model = model;
        this.plateNum = plateNum;
        this.averageConsumption = averageConsumption;
    }

    /* Public methods */

    @Override
    public String toString() {
        return Utils.ucWords(this.model.getBrand()+" "+this.model.getModel())+" - "+this.plateNum;
    }

    public void persist() {
        persist(false);
    }

    public void update() {
        persist(true);
    }

    public void persist(boolean update) {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);
        else
            update = false;

        newValues.put(DBModel.C_KILOMETERS, this.kilometers);

        if (this.buyingDate != null)
            newValues.put(DBModel.C_BUYING_DATE, this.buyingDate.getTime());

        if (this.circulationDate != null)
            newValues.put(DBModel.C_CIRCULATION_DATE, this.circulationDate.getTime());

        newValues.put(DBModel.C_AVERAGE_CONSUMPTION, this.averageConsumption);
            newValues.put(DBModel.C_PLATE_NUMBER, this.plateNum);
            newValues.put(DBModel.C_MODEL_ID, this.model.getId());

        if (update){
            DatabaseManager.getCurrentDatabase().update(DBModel.TABLE_NAME, newValues, DBModel.C_ID+"="+this.id, null);
        } else {
            long insertedId = DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);

            if (this.id <= 0)
                this.id = (int) insertedId;
        }
    }

    public Drawable getCarPicture(Context ctx) {
        File imgFile = new  File(GlobalContext.getAppPicturePath() + String.valueOf(getId()) +  "_picture.jpg");

        // Fallback to default image
        if (!imgFile.exists())
            return ContextCompat.getDrawable(ctx, R.drawable.ic_car_profile_picture);


        int imageLength = (int)(72 * GlobalContext.getInstance().getResources().getDisplayMetrics().density);

        // Loading the bitmap
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath()).copy(Bitmap.Config.ARGB_8888, true);

        Bitmap output = Bitmap.createBitmap(imageLength, imageLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        // Cropping to a circle
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, imageLength, imageLength);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(output.getWidth() / 2, output.getHeight() / 2, output.getWidth() / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;

        // Returning drawable
        return new BitmapDrawable(GlobalContext.getInstance().getResources(), output);
        //return Drawable.createFromPath(imgFile.getAbsolutePath());
    }

    public Drawable getCarBanner(Context ctx) {
        File imgFile = new  File(GlobalContext.getAppPicturePath() + String.valueOf(getId()) +  "_cover.jpg");

        if(imgFile.exists()){
            return Drawable.createFromPath(imgFile.getAbsolutePath());
        }

        return ContextCompat.getDrawable(ctx, R.drawable.default_car_background);
    }



    /* Static methods */

    public static HashMap<Integer, Car> findAll() {
        HashMap<Integer, Car> result = new HashMap<Integer, Car>();

        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM " + DBModel.TABLE_NAME,
                null
        );
        while(cursor.moveToNext()) {
            result.put(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    new Car(
                            DatabaseManager.extractInt(cursor, DBModel.C_ID),
                            CarModel.findById(DatabaseManager.extractInt(cursor, DBModel.C_MODEL_ID)),
                            DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                            DatabaseManager.extractDate(cursor, DBModel.C_BUYING_DATE),
                            DatabaseManager.extractDate(cursor, DBModel.C_CIRCULATION_DATE),
                            DatabaseManager.extractString(cursor, DBModel.C_PLATE_NUMBER),
                            DatabaseManager.extractDouble(cursor, DBModel.C_AVERAGE_CONSUMPTION)
                    )
            );
        }
        return result;
    }

    /* Database Model */
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "cars";
        public static final String C_ID = "id";
        public static final String C_KILOMETERS = "kilometers";
        public static final String C_BUYING_DATE = "buying_date";
        public static final String C_CIRCULATION_DATE = "circulation_date";
        public static final String C_AVERAGE_CONSUMPTION = "average_consumption";
        public static final String C_PLATE_NUMBER = "plate_number";
        public static final String C_MODEL_ID = "model_id";

        public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                + C_ID + " INTEGER PRIMARY KEY,"
                + C_KILOMETERS + " INTEGER NOT NULL,"
                + C_BUYING_DATE + " INTEGER,"
                + C_CIRCULATION_DATE + " INTEGER,"
                + C_PLATE_NUMBER + " TEXT,"
                + C_AVERAGE_CONSUMPTION + " REAL NOT NULL,"
                + C_MODEL_ID + " INTEGER NOT NULL"
                + ");";
    }


    public static Car findCarById(int carId) {
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery("SELECT * FROM " + DBModel.TABLE_NAME + " WHERE " + DBModel.C_ID + " = " + carId, null);

        if (cursor.moveToNext()) {
            return new Car(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    CarModel.findById(DatabaseManager.extractInt(cursor, DBModel.C_MODEL_ID)),
                    DatabaseManager.extractInt(cursor, DBModel.C_KILOMETERS),
                    DatabaseManager.extractDate(cursor, DBModel.C_BUYING_DATE),
                    DatabaseManager.extractDate(cursor, DBModel.C_CIRCULATION_DATE),
                    DatabaseManager.extractString(cursor, DBModel.C_PLATE_NUMBER),
                    DatabaseManager.extractDouble(cursor, DBModel.C_AVERAGE_CONSUMPTION)
            );
        }

        return null;
    }

    /* Getters & Setters */
    public int getId() {
        return this.id;
    }

    public String getPlateNum() {
        return this.plateNum;
    }

    public CarModel getCarModel() {
        return this.model;
    }

    public int getKilometers() {
        return this.kilometers;
    }

    public String getStringCirculationDate() {
        SimpleDateFormat simpleDate =  new SimpleDateFormat("dd/MM/yyyy");
        return simpleDate.format(circulationDate);
    }

    public void setKilometers(int kilometers) {
        if (kilometers > this.kilometers)
            this.kilometers = kilometers;
    }
}
