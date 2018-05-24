package com.organization.jeptagsalpha.Temperature;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.organization.jeptagsalpha.Database.DBManager;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.main.TemperatureHome;

public class SettingActivity extends AppCompatActivity {
    RadioButton tempRadioButton, timeZoneRadioButton;
    RadioGroup radiogroup1, radiogroup2;
    EditText hh_editext, mm_editext, ss_editext, upper_edittext, lower_edittext;
    Button saveChange;
    private DBManager dbManager;
    String GetSQliteQuery;
    ImageView back;
    String activity;
    String interval, hh,mm,ss, upper_limit, lower_limit, temperature, time_zone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageView)findViewById(R.id.back);
        activity=getIntent().getStringExtra("activity");
        radiogroup1=(RadioGroup)findViewById(R.id.radiogroup1);
        radiogroup2=(RadioGroup)findViewById(R.id.radiogroup2);
        hh_editext=(EditText)findViewById(R.id.hh);
        mm_editext=(EditText)findViewById(R.id.mm);
        ss_editext=(EditText)findViewById(R.id.ss);

        upper_edittext=(EditText)findViewById(R.id.upper_edittext);
        lower_edittext=(EditText)findViewById(R.id.lower_edittext);
        saveChange=(Button)findViewById(R.id.savechange);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activity.equals("1")){
                    startActivity(new Intent(getApplicationContext(), TemperatureHome.class));
                    finish();
                }else {
                    finish();
                }
            }
        });
        dbManager = new DBManager(this);
        dbManager.open();
       /* radiogroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                tempRadioButton = (RadioButton) findViewById(selectedId);

            }
        });
        radiogroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                // get selected radio button from radioGroup
                int selectedId2 = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                timeZoneRadioButton = (RadioButton) findViewById(selectedId2);

            }
        });*/

        saveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                hh= hh_editext.getText().toString();
                mm= mm_editext.getText().toString();
                ss= ss_editext.getText().toString();
                int HH = Integer.parseInt(hh)*60*60;
                int MM = Integer.parseInt(mm)*60;
                int SS = HH+MM+Integer.parseInt(ss);
                interval = String.valueOf(SS);
                upper_limit= upper_edittext.getText().toString();
                lower_limit= lower_edittext.getText().toString();
                int selectedId1 = radiogroup1.getCheckedRadioButtonId();
                tempRadioButton = (RadioButton) findViewById(selectedId1);
                temperature = tempRadioButton.getText().toString();
                int selectedId2 = radiogroup2.getCheckedRadioButtonId();
                timeZoneRadioButton = (RadioButton) findViewById(selectedId2);
                time_zone = timeZoneRadioButton.getText().toString();
               // dbManager.insert(interval, upper_limit, lower_limit, temperature, time_zone);


                editor.putString("interval", interval);
                editor.putString("upper_limit", upper_limit);
                editor.putString("lower_limit", lower_limit);
                editor.putString("temperature", temperature);
                editor.putString("time_zone", time_zone);
                editor.commit();
                Toast.makeText(getApplicationContext(), "Change successfully", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activity.equals("1")){
            startActivity(new Intent(getApplicationContext(), TemperatureHome.class));
            finish();
        }else {
            finish();
        }
    }
}
