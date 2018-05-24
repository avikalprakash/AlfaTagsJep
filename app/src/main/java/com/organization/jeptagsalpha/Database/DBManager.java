package com.organization.jeptagsalpha.Database;

/**
 * Created by anupamchugh on 19/10/15.
 */
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {

    private DatabaseHelper dbHelper;

    private Context context;

    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(context);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(String interval, String upper_limit, String lower_limit, String temperature, String time_zone) {
        ContentValues contentValue = new ContentValues();
        contentValue.put(DatabaseHelper.INTERVAL, interval);
        contentValue.put(DatabaseHelper.UPPER_LIMIT, upper_limit);
        contentValue.put(DatabaseHelper.LOWER_LIMIT, lower_limit);
        contentValue.put(DatabaseHelper.TEMPERATURE, temperature);
        contentValue.put(DatabaseHelper.TIME_ZONE, time_zone);
        database.insert(DatabaseHelper.TABLE_NAME, null, contentValue);
    }

    public Cursor fetch() {
        String[] columns = new String[] { DatabaseHelper._ID, DatabaseHelper.INTERVAL, DatabaseHelper.UPPER_LIMIT, DatabaseHelper.LOWER_LIMIT
                , DatabaseHelper.TEMPERATURE, DatabaseHelper.TIME_ZONE};
        Cursor cursor = database.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int update(long _id, String interval, String upper_limit, String lower_limt, String temp, String time_zone) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseHelper.INTERVAL, interval);
        contentValues.put(DatabaseHelper.UPPER_LIMIT, upper_limit);
        contentValues.put(DatabaseHelper.LOWER_LIMIT, lower_limt);
        contentValues.put(DatabaseHelper.TEMPERATURE, temp);
        contentValues.put(DatabaseHelper.TIME_ZONE, time_zone);
        int i = database.update(DatabaseHelper.TABLE_NAME, contentValues, DatabaseHelper._ID + " = " + _id, null);
        return i;
    }

    public void delete(long _id) {
        database.delete(DatabaseHelper.TABLE_NAME, DatabaseHelper._ID + "=" + _id, null);
    }

}
