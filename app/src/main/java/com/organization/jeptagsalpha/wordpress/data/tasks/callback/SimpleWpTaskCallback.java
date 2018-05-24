package com.organization.jeptagsalpha.wordpress.data.tasks.callback;


import com.organization.jeptagsalpha.wordpress.data.tasks.WpAsyncTask;
import com.organization.jeptagsalpha.wordpress.util.LogUtils;

/**
 * @author Jan-Louis Crafford
 *         Created on 2016/03/15.
 */
public abstract class SimpleWpTaskCallback<Result> implements WpTaskCallback<Result> {

    @Override
    public void onTaskResultNull() {

    }

    @Override
    public void onTaskCancelled() {

    }

    @Override
    public void onTaskFailure(WpAsyncTask task, String error) {
        LogUtils.w("Task failure for (" + task.getClass().getSimpleName() + ") : " + error);
    }
}
