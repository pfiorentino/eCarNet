package me.alpha12.ecarnet.models;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;

import me.alpha12.ecarnet.classes.Utils;
import me.alpha12.ecarnet.database.DatabaseManager;

public class CarModel implements Parcelable {

    private int id;
    private String internalId;
    private String brand;
    private String model;
    private String version;
    private int generation;
    private String energy;
    private String body;
    private String gearboxType;
    private int gears;
    private int ratedHP;
    private String minesType;

    /* Constructors */

    public CarModel(int id, String internalId, String brand, String model, String version, int generation,
                    String energy, String body, String gearboxType, int gears, int ratedHP, String minesType) {
        if (id > 0)
            this.id = id;

        this.internalId = internalId;
        this.brand = brand;
        this.model = model;
        this.version = version;

        if (generation > 0)
            this.generation = generation;

        this.energy = energy;
        this.body = body;
        this.gearboxType = gearboxType;

        if (gears > 0)
            this.gears = gears;

        if (ratedHP > 0)
            this.ratedHP = ratedHP;

        this.minesType = minesType;
    }

    public CarModel(Cursor cursor) {
        this.id         = DatabaseManager.extractInt(cursor, DBCarModelDetails.C_ID);
        this.internalId = DatabaseManager.extractString(cursor, DBCarModelDetails.C_INTERNAL_ID);
        this.brand      = DatabaseManager.extractString(cursor, DBCarModelDetails.C_BRAND_NAME);
        this.model      = DatabaseManager.extractString(cursor, DBCarModelDetails.C_MODEL_NAME);
        this.version    = DatabaseManager.extractString(cursor, DBCarModelDetails.C_VERSION);
        this.generation = DatabaseManager.extractInt(cursor, DBCarModelDetails.C_GENERATION);
        this.energy     = DatabaseManager.extractString(cursor, DBCarModelDetails.C_ENERGY);
        this.body       = DatabaseManager.extractString(cursor, DBCarModelDetails.C_BODY);
        this.gearboxType = DatabaseManager.extractString(cursor, DBCarModelDetails.C_GEARBOX_TYPE);
        this.gears      = DatabaseManager.extractInt(cursor, DBCarModelDetails.C_GEARS);
        this.ratedHP    = DatabaseManager.extractInt(cursor, DBCarModelDetails.C_RATED_HP);
        this.minesType  = DatabaseManager.extractString(cursor, DBCarModelDetails.C_MINES_TYPE);
    }

    /* Public methods */

    public String toString() {
        return Utils.ucWords(brand + " " + model);
    }

    public String getSearchableString() {
        String[] array = new String[] {
                this.brand,
                this.model,
                this.version,
                String.valueOf(this.generation),
                this.energy,
                this.body,
                this.gearboxType,
                String.valueOf(this.gears)
        };

        return TextUtils.join(" ", array).toLowerCase();
    }

    /* Static methods */

    public static ArrayList<String> findBrands() {
        return findBrands(false);
    }

    public static ArrayList<String> findBrands(boolean formatString) {
        ArrayList<String> brands = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT " + DBCarBrand.C_LABEL + " FROM " + DBCarBrand.TABLE_NAME + " ORDER BY " + DBCarBrand.C_TOP + " DESC, " + DBCarBrand.C_LABEL + " ASC",
                null
        );
        while(cursor.moveToNext()) {
            if (formatString)
                brands.add(Utils.ucWords(DatabaseManager.extractString(cursor, DBCarBrand.C_LABEL)));
            else
                brands.add(DatabaseManager.extractString(cursor, DBCarBrand.C_LABEL));
        }
        return brands;
    }

    public static ArrayList<String> findModelsByBrand(String brand) {
        return findModelsByBrand(brand, false);
    }

    public static ArrayList<String> findModelsByBrand(String brand, boolean formatString) {
        ArrayList<String>models = new ArrayList<>();

        String[] args = {brand.toUpperCase()};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT m." + DBCarModel.C_LABEL + " " +
                "FROM " + DBCarModel.TABLE_NAME + " m " +
                    "JOIN " + DBCarBrand.TABLE_NAME + " b ON m." + DBCarModel.C_BRAND_ID + " = b." + DBCarBrand.C_ID + " " +
                "WHERE b." + DBCarBrand.C_LABEL + " = ? " +
                "ORDER BY m." + DBCarModel.C_TOP + " DESC, m." + DBCarModel.C_LABEL + " ASC",
                args
        );

        while(cursor.moveToNext()) {
            if (formatString)
                models.add(Utils.ucWords(DatabaseManager.extractString(cursor, DBCarModel.C_LABEL)));
            else
                models.add(DatabaseManager.extractString(cursor, DBCarModel.C_LABEL));
        }

        return models;
    }

    public static ArrayList<CarModel> findByBrandModel(String brand, String model) {
        ArrayList<CarModel> result = new ArrayList<>();

        String[] args = {brand.toUpperCase(), model.toUpperCase()};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT md.*, m." + DBCarModel.C_LABEL + " as " + DBCarModelDetails.C_MODEL_NAME + ", b." + DBCarBrand.C_LABEL + " as " + DBCarModelDetails.C_BRAND_NAME + " " +
                "FROM " + DBCarModelDetails.TABLE_NAME + " md " +
                    "JOIN " + DBCarModel.TABLE_NAME + " m ON md." + DBCarModelDetails.C_MODEL_ID + " = m." + DBCarModel.C_ID + " " +
                    "JOIN " + DBCarBrand.TABLE_NAME + " b ON m." + DBCarModel.C_BRAND_ID + " = b." + DBCarBrand.C_ID + " " +
                "WHERE b." + DBCarBrand.C_LABEL + " = ? AND m." + DBCarModel.C_LABEL + " = ? " +
                "GROUP BY md." + DBCarModelDetails.C_VERSION + " " +
                "ORDER BY m." + DBCarModel.C_LABEL + ", md." + DBCarModelDetails.C_VERSION,
                args
        );

        while(cursor.moveToNext()) {
            result.add(new CarModel(cursor));
        }

        return result;
    }


    public static boolean existWithTypeMine(String typeMine) {
        ArrayList<CarModel> result = new ArrayList<>();
        int number = 0;
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT " + DBCarModelDetails.C_ID + " FROM " + DBCarModelDetails.TABLE_NAME+
                        " WHERE " + DBCarModelDetails.C_MINES_TYPE + " LIKE '%"+typeMine.toUpperCase()+"%' LIMIT 1",
                null
        );
        return cursor.moveToNext();
    }

    public static ArrayList<CarModel> findByTypeMine(String typeMine) {
        ArrayList<CarModel> result = new ArrayList<>();

        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT md.*, m." + DBCarModel.C_LABEL + " as " + DBCarModelDetails.C_MODEL_NAME + ", b." + DBCarBrand.C_LABEL + " as " + DBCarModelDetails.C_BRAND_NAME + " " +
                        "FROM " + DBCarModelDetails.TABLE_NAME + " md " +
                        "JOIN " + DBCarModel.TABLE_NAME + " m ON md." + DBCarModelDetails.C_MODEL_ID + " = m." + DBCarModel.C_ID + " " +
                        "JOIN " + DBCarBrand.TABLE_NAME + " b ON m." + DBCarModel.C_BRAND_ID + " = b." + DBCarBrand.C_ID + " " +
                        "WHERE md." + DBCarModelDetails.C_MINES_TYPE + " LIKE '%"+typeMine.toUpperCase()+"%'" +
                        "GROUP BY md." + DBCarModelDetails.C_VERSION + " " +
                        "ORDER BY m." + DBCarModel.C_LABEL + ", md." + DBCarModelDetails.C_VERSION,
                null
        );

        while(cursor.moveToNext()) {
            result.add(new CarModel(cursor));
        }

        return result;
    }


    public static CarModel findById(int id) {
        String[] args = { Integer.toString(id) };
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT md.*, m.label as " + DBCarModelDetails.C_MODEL_NAME + ", b.label as " + DBCarModelDetails.C_BRAND_NAME + " " +
                "FROM " + DBCarModelDetails.TABLE_NAME + " md " +
                    "JOIN " + DBCarModel.TABLE_NAME + " m ON md." + DBCarModelDetails.C_MODEL_ID + " = m." + DBCarModel.C_ID + " " +
                    "JOIN " + DBCarBrand.TABLE_NAME + " b ON m." + DBCarModel.C_BRAND_ID + " = b." + DBCarBrand.C_ID + " " +
                "WHERE md." + DBCarModelDetails.C_ID + " = ? " +
                "LIMIT 1",
                args
        );

        if(cursor.moveToNext()) {
            return new CarModel(cursor);
        }

        return null;
    }

    /* Parcelable implementation */

    public static final Creator<CarModel> CREATOR = new Creator<CarModel>() {
        @Override
        public CarModel createFromParcel(Parcel in) {
            return new CarModel(in);
        }

        @Override
        public CarModel[] newArray(int size) {
            return new CarModel[size];
        }
    };

    protected CarModel(Parcel in) {
        id = in.readInt();
        internalId = in.readString();
        brand = in.readString();
        model = in.readString();
        version = in.readString();
        generation = in.readInt();
        energy = in.readString();
        body = in.readString();
        gearboxType = in.readString();
        gears = in.readInt();
        ratedHP = in.readInt();
        minesType = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.internalId);
        dest.writeString(this.brand);
        dest.writeString(this.model);
        dest.writeString(this.version);
        dest.writeInt(this.generation);
        dest.writeString(this.energy);
        dest.writeString(this.body);
        dest.writeString(this.gearboxType);
        dest.writeInt(this.gears);
        dest.writeInt(this.ratedHP);
        dest.writeString(this.minesType);
    }

    /* Database Model */
    public static abstract class DBCarBrand implements BaseColumns {
        public static final String TABLE_NAME = "car_brands";
        public static final String C_ID = "id";
        public static final String C_LABEL = "label";
        public static final String C_TOP = "top";
    }

    public static abstract class DBCarModel implements BaseColumns {
        public static final String TABLE_NAME = "car_models";
        public static final String C_ID = "id";
        public static final String C_BRAND_ID = "brand_id";
        public static final String C_LABEL = "label";
        public static final String C_TOP = "top";
    }

    public static abstract class DBCarModelDetails implements BaseColumns {
        public static final String TABLE_NAME = "car_models_details";
        public static final String C_ID = "id";
        public static final String C_INTERNAL_ID = "internal_id";
        public static final String C_BRAND_ID = "brand_id";
        public static final String C_BRAND_NAME = "brand_name";
        public static final String C_MODEL_ID = "model_id";
        public static final String C_MODEL_NAME = "model_name";
        public static final String C_VERSION = "version";
        public static final String C_GENERATION = "generation";
        public static final String C_GENERATION_ROMAN = "generation_roman";
        public static final String C_ENERGY = "fuel_type";
        public static final String C_BODY = "body";
        public static final String C_GEARBOX_TYPE = "gearbox_type";
        public static final String C_GEARS = "gears";
        public static final String C_RATED_HP = "rated_hp";
        public static final String C_MINES_TYPE = "mines_type";
    }

    /* Getters & setters */

    public int getId() {
        return id;
    }

    public int getRatedHP() {
        return ratedHP;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getVersion() {
        return version;
    }

    public String getEnergy() {
        return energy;
    }

    public String getBody() {
        return body;
    }

    public String getGearboxType() {
        return gearboxType;
    }

    public int getGears() {
        return gears;
    }

    public String getDetails() {
        return version+" - "+body+" - "+energy+" - "+gearboxType;
    }
}
