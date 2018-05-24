package com.organization.jeptagsalpha.wordpress.data.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.organization.jeptagsalpha.wordpress.data.tasks.callback.WpTaskCallback;


/**
 * @author Jan-Louis Crafford
 *         Created on 2016/02/11.
 */
public class WpInsertTask extends WpAsyncTask<Void, Void, Long> {

    private String table;
    private ContentValues values;

    public WpInsertTask(Context context, String table, ContentValues values, WpTaskCallback<Long> callback) {
        super(context, callback);

        this.table = table;
        this.values = values;
    }

    @Override
    protected Long exec() throws Exception {
        SQLiteDatabase db = getWritableDatabase();

        return db.insert(table, null, values);
    }
}
