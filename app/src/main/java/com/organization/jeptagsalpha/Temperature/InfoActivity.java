package com.organization.jeptagsalpha.Temperature;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.main.TemperatureHome;

public class InfoActivity extends AppCompatActivity {
    TextView control, info, list, chart;
    ImageView setting;
    TextView uid, current_time, current_voltge, status, center_vertical, log_interval, num_reading, avg_temp,
            max_temp, min_temp, upperLimit, lowerLimit, start_time;
    LinearLayout linear_layout;
    String interval, hh,mm,ss, upper_limit, lower_limit, tag_id, time_start;
    public static final String INTERVAL = "interval";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String TAG_ID = "tag_id";
    public static final String TIME_NOW = "timing_now";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String activity="2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                i.putExtra("activity", activity);
                startActivity(i);
            }
        });
        control=(TextView)findViewById(R.id.control);
        info=(TextView)findViewById(R.id.info);
        list=(TextView)findViewById(R.id.list);
        chart=(TextView)findViewById(R.id.chart);
        info.setTypeface(info.getTypeface(), Typeface.BOLD);
        uid=(TextView)findViewById(R.id.uid);
        current_time=(TextView)findViewById(R.id.current_time);
        current_voltge=(TextView)findViewById(R.id.current_voltge);
        status=(TextView)findViewById(R.id.status);
        center_vertical=(TextView)findViewById(R.id.center_vertical);
        log_interval=(TextView)findViewById(R.id.log_interval);
        num_reading=(TextView)findViewById(R.id.num_reading);
        avg_temp=(TextView)findViewById(R.id.avg_temp);
        max_temp=(TextView)findViewById(R.id.max_temp);
        min_temp=(TextView)findViewById(R.id.min_temp);
        upperLimit=(TextView)findViewById(R.id.upper_limit);
        lowerLimit=(TextView)findViewById(R.id.lower_limit);
        start_time=(TextView)findViewById(R.id.start_time);
        linear_layout=(LinearLayout)findViewById(R.id.linear_layout);
        sharedpreferences=getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        interval=sharedpreferences.getString(INTERVAL,"");
        upper_limit=sharedpreferences.getString(UPPER_LIMIT,"");
        lower_limit=sharedpreferences.getString(LOWER_LIMIT,"");
        tag_id=sharedpreferences.getString(TAG_ID,"");
        time_start=sharedpreferences.getString(TIME_NOW,"");
       /* if (interval==null || upper_limit==null || lowerLimit==null ||
                tag_id==null || time_start==null ||interval.equals("") || upper_limit.equals("") ||
                lowerLimit.equals("") || tag_id.equals("") || time_start.equals("")){
            linear_layout.setVisibility(View.INVISIBLE);
        }else {
            linear_layout.setVisibility(View.VISIBLE);
        }*/
        if (!interval.equals("")){
            log_interval.setText("Log Interval(Sec) : "+interval);
        }else {
            log_interval.setText("");
        }
        if (!upper_limit.equals("")){
            upperLimit.setText("Upper Limit : "+upper_limit+"°C");
            max_temp.setText("Max. Temp. : "+upper_limit+"°C");
        }else {
            upperLimit.setText("");
            max_temp.setText("");
        }
        if (!lower_limit.equals("")){
            lowerLimit.setText("Lower Limit : "+lower_limit+"°C");
            min_temp.setText("Min Temp. : "+lower_limit+"°C");
            int avg = (Integer.parseInt(upper_limit)+Integer.parseInt(lower_limit))/2;
            avg_temp.setText("Avg. Temp. : "+String.valueOf(avg)+"°C");

        }else {
            lowerLimit.setText("");
            min_temp.setText("");
        }
        if (!tag_id.equals("")){
            uid.setText("UID : "+tag_id);
        }else {
            uid.setText(tag_id);
        }
        if (!time_start.equals("")){
            current_time.setText("Current Time : "+time_start);
            start_time.setText("Start Time : "+time_start);
        }else {
            current_time.setText("");
            start_time.setText("");
        }

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TempListActivity.class));
                finish();
            }
        });

        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TemperatureHome.class));
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                finish();
            }
        });
    }
}
