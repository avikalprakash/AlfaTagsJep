package com.organization.jeptagsalpha.Database;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Table Name
    public static final String TABLE_NAME = "Setting";

    // Table columns
    public static final String _ID = "_id";
    public static final String INTERVAL = "interval";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String TEMPERATURE = "temperature";
    public static final String TIME_ZONE = "time_zone";

    // Database Information
    static final String DB_NAME = "Jepshops.DB";

    // database version
    static final int DB_VERSION = 1;

    // Creating table query
    private static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + _ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + INTERVAL + " VARCHAR, " + UPPER_LIMIT + " VARCHAR, " + LOWER_LIMIT + " VARCHAR, " + TEMPERATURE + " VARCHAR" +
            ", " + TIME_ZONE + " VARCHAR);";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
