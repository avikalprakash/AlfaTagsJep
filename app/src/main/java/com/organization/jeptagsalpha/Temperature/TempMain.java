package com.organization.jeptagsalpha.Temperature;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.ams.as39513App.AS39513AppActivity;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.main.MainActivity;

public class TempMain extends TabActivity {
    /** Called when the activity is first created. */
    TextView control, info, list, chart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_main);
        control=(TextView)findViewById(R.id.control);
        info=(TextView)findViewById(R.id.info);
        list=(TextView)findViewById(R.id.list);
        chart=(TextView)findViewById(R.id.chart);

    }
}