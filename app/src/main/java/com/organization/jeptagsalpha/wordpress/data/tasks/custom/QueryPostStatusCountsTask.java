package com.organization.jeptagsalpha.wordpress.data.tasks.custom;

import android.content.Context;
import android.database.Cursor;

import com.organization.jeptagsalpha.wordpress.data.WordPressContract;
import com.organization.jeptagsalpha.wordpress.data.tasks.WpAsyncTask;
import com.organization.jeptagsalpha.wordpress.data.tasks.callback.WpTaskCallback;
import com.organization.jeptagsalpha.wordpress.util.WordpressPreferenceHelper;

import java.util.HashMap;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/03/04.
 */
public class QueryPostStatusCountsTask extends WpAsyncTask<Void, Void, HashMap<String, Integer>> {

    public QueryPostStatusCountsTask(Context context, WpTaskCallback<HashMap<String, Integer>> callback) {
        super(context, callback);
    }

    @Override
    protected HashMap<String, Integer> exec() throws Exception {
        HashMap<String, Integer> map = new HashMap<>();

        String query = "SELECT " + WordPressContract.Posts.STATUS + ", "
                + " COUNT(" + WordPressContract.Posts.STATUS + ") AS status_counts "
                + " FROM " + WordPressContract.Posts.TABLE_NAME
                + " WHERE " + WordPressContract.Posts.WP_AUTHOR_ID + "=?"
                + " GROUP BY " + WordPressContract.Posts.STATUS;

        long authorId = WordpressPreferenceHelper.with(context).getWordPressUserId();
        String[] selectionArgs = {authorId + ""};

        Cursor cursor = getReadableDatabase().rawQuery(query, selectionArgs);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String status = cursor.getString(0);
                int count = cursor.getInt(1);

                map.put(status, count);
            }
            cursor.close();
        }

        return map;
    }
}
