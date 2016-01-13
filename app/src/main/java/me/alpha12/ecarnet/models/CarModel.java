package me.alpha12.ecarnet.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;
import android.text.TextUtils;

import java.util.ArrayList;

import me.alpha12.ecarnet.Utils;
import me.alpha12.ecarnet.database.DatabaseManager;

public class CarModel implements Parcelable {

    private int id = -1;
    private String internalId;
    private String brand;
    private String model;
    private String version;
    private int generation = -1;
    private String energy;
    private String body;
    private String gearboxType;
    private int gears = -1;
    private int ratedHP = -1;
    private String minesType;

    /* Constructors */

    public CarModel(int id, String internalId, String brand, String model, String version, int generation,
                    String energy, String body, String gearboxType, int gears, int ratedHP, String minesType ) {
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

    /* Public methods */

    public String toString() {
        return Utils.ucWords(brand + " " + getFullModel());
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
                //String.valueOf(this.ratedHP)
                //this.minesType
        };

        return TextUtils.join(" ", array).toLowerCase();
    }

    public void persist() {
        ContentValues newValues = new ContentValues();

        if (this.id > 0)
            newValues.put(DBModel.C_ID, this.id);

        newValues.put(DBModel.C_INTERNAL_ID, this.internalId);
        newValues.put(DBModel.C_BRAND, this.brand);
        newValues.put(DBModel.C_MODEL, this.model);
        newValues.put(DBModel.C_VERSION, this.version);
        if (this.generation > 0)
            newValues.put(DBModel.C_GENERATION, this.generation);
        newValues.put(DBModel.C_ENERGY, this.energy);
        newValues.put(DBModel.C_BODY, this.body);
        newValues.put(DBModel.C_GEARBOX_TYPE, this.gearboxType);
        if (this.gears > 0)
            newValues.put(DBModel.C_GEARS, this.gears);
        if (this.ratedHP > 0)
            newValues.put(DBModel.C_RATED_HP, this.ratedHP);
        newValues.put(DBModel.C_MINES_TYPE, this.minesType);

        DatabaseManager.getCurrentDatabase().insert(DBModel.TABLE_NAME, null, newValues);
    }

    /* Static methods */

    public static ArrayList<String> findBrands() {
        return findBrands(false);
    }

    public static ArrayList<String> findBrands(boolean formatString) {
        ArrayList<String> brands = new ArrayList<>();
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT DISTINCT(" + DBModel.C_BRAND + ") FROM " + DBModel.TABLE_NAME + " ORDER BY " + DBModel.C_BRAND + ";",
                null
        );
        while(cursor.moveToNext()) {
            if (formatString)
                brands.add(Utils.ucWords(DatabaseManager.extractString(cursor, DBModel.C_BRAND)));
            else
                brands.add(DatabaseManager.extractString(cursor, DBModel.C_BRAND));
        }
        return brands;
    }

    public static ArrayList<String> findModelsByBrand(String brand) {
        return findModelsByBrand(brand, false);
    }

    public static ArrayList<String> findModelsByBrand(String brand, boolean formatString) {
        ArrayList<String>models = new ArrayList<>();

        String[] args = {brand};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT DISTINCT("+ DBModel.C_MODEL +") FROM "+ DBModel.TABLE_NAME +" WHERE "+ DBModel.C_BRAND +" = ? ORDER BY "+ DBModel.C_MODEL +";",
                args
        );

        while(cursor.moveToNext()) {
            if (formatString)
                models.add(Utils.ucWords(DatabaseManager.extractString(cursor, DBModel.C_MODEL)));
            else
                models.add(DatabaseManager.extractString(cursor, DBModel.C_MODEL));
        }

        return models;
    }

    public static ArrayList<CarModel> findByBrandModel(String brand, String model) {
        ArrayList<CarModel> result = new ArrayList<>();

        String[] args = {brand, model};
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * "+
                "FROM "+ DBModel.TABLE_NAME + " " +
                "WHERE "+ DBModel.C_BRAND +" = ? AND "+ DBModel.C_MODEL +" = ? "+
                "GROUP BY `brand`, `model`, `version`, `generation` " +
                "ORDER BY "+ DBModel.C_MODEL +", "+ DBModel.C_GENERATION +", "+ DBModel.C_VERSION +";",
                args
        );

        while(cursor.moveToNext()) {
            result.add(DBModel.extractFromCursor(cursor));
        }

        return result;
    }


    public static CarModel findById(int id) {
        String[] args = { Integer.toString(id) };
        Cursor cursor = DatabaseManager.getCurrentDatabase().rawQuery(
                "SELECT * FROM "+ DBModel.TABLE_NAME +" WHERE id = ?;",
                args
        );

        if(cursor.moveToNext()) {
            return DBModel.extractFromCursor(cursor);
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
    public static abstract class DBModel implements BaseColumns {
        public static final String TABLE_NAME = "car_models";
        public static final String C_ID = "id";
        public static final String C_INTERNAL_ID = "internal_id";
        public static final String C_BRAND = "brand";
        public static final String C_MODEL = "model";
        public static final String C_VERSION = "version";
        public static final String C_GENERATION = "generation";
        public static final String C_ENERGY = "fuel_type";
        public static final String C_BODY = "body";
        public static final String C_GEARBOX_TYPE = "gearbox_type";
        public static final String C_GEARS = "gears";
        public static final String C_RATED_HP = "rated_hp";
        public static final String C_MINES_TYPE = "mines_type";

        public static CarModel extractFromCursor(Cursor cursor) {
            return new CarModel(
                    DatabaseManager.extractInt(cursor, DBModel.C_ID),
                    DatabaseManager.extractString(cursor, DBModel.C_INTERNAL_ID),
                    DatabaseManager.extractString(cursor, DBModel.C_BRAND),
                    DatabaseManager.extractString(cursor, DBModel.C_MODEL),
                    DatabaseManager.extractString(cursor, DBModel.C_VERSION),
                    DatabaseManager.extractInt(cursor, DBModel.C_GENERATION),
                    DatabaseManager.extractString(cursor, DBModel.C_ENERGY),
                    DatabaseManager.extractString(cursor, DBModel.C_BODY),
                    DatabaseManager.extractString(cursor, DBModel.C_GEARBOX_TYPE),
                    DatabaseManager.extractInt(cursor, DBModel.C_GEARS),
                    DatabaseManager.extractInt(cursor, DBModel.C_RATED_HP),
                    DatabaseManager.extractString(cursor, DBModel.C_MINES_TYPE)
            );
        }
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

    public String getFullModel() {
        if (generation > 1) {
            return model+" "+Utils.toRoman(generation);
        }

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
}
