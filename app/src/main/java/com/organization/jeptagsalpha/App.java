package com.organization.jeptagsalpha;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * Created by Lord Ganesha on 28/09/2016.
 */

public class App extends Application {
    private static App instance;
    public static App getInstance() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
//
//        instance = this;
//        AutoErrorReporter.get(this)
//                .setEmailAddresses("webstersys@gmail.com")
//                .setEmailSubject("JepTags-Alpha App Crash Report Version 1.0.0.0")
//                .start();

    }
    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
       // MultiDex.install(this);
    }

    public static void setStatusBarColor(Activity activity) {
        if (Build.VERSION.SDK_INT >= 21) {

            Window window = activity.getWindow();


            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            // finally change the color
            window.setStatusBarColor(Color.parseColor("#222222"));

        }
    }


        public static boolean isOnline(Context context) {

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();

            boolean isnet=(networkInfo != null && networkInfo.isConnected() == true);
            if(!isnet) Toast.makeText(context, context.getString(com.organization.jeptagsalpha.R.string.toast_internet_check), Toast.LENGTH_SHORT).show();
            return isnet;
        }

}
