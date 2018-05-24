package com.organization.jeptagsalpha.ui.activity.authorization;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import com.skyfishjy.library.RippleBackground;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;

import java.io.IOException;

import butterknife.ButterKnife;

public class SplashActivity extends BaseActivity {
    SharedPreferences sharedPreferences;
    private final String MY_lOGIN_PREFERENCE="my_login_preference";
    public static final String Login = "login";
    AlertDialog alertDialog;
    String loginstring="no";
    ImageView spaceshipImage;
   // private static int SPLASH_TIME_OUT = 3000;
    public static void start(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        context.startActivity(intent);
    }




    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ButterKnife.bind(this);

       // spaceshipImage = (ImageView) findViewById(R.id.splash_img);
      //  final RippleBackground rippleBackground=(RippleBackground)findViewById(R.id.content);
      //  rippleBackground.startRippleAnimation();
                sharedPreferences = getSharedPreferences(MY_lOGIN_PREFERENCE, Context.MODE_PRIVATE);
                String loginstring=sharedPreferences.getString( Login,"" );
        {
            if(loginAccountHelper.getUserLoggedIn())
            {
                login(loginAccountHelper.getUserName(),loginAccountHelper.getPassword(),true);
            }
            else
            {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.start(SplashActivity.this);
                        finish();
                    }
                },3000); // SPLASH_TIME_OUT);
            }
        }
    }
}
