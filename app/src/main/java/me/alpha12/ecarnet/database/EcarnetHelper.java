package me.alpha12.ecarnet.database;

/**
 * Created by guilhem on 09/11/2015.
 */


    import android.content.Context;
    import android.database.SQLException;
    import android.database.sqlite.SQLiteDatabase;
    import android.database.sqlite.SQLiteOpenHelper;

    /**
     * Created by paul on 13/06/2015.
     */
    public class EcarnetHelper extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "ecarnet.db";

        public EcarnetHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }


        public void open() throws SQLException {
            SQLiteDatabase bdd = this.getWritableDatabase();
        }


        public void close() {
            this.close();
        }
}
