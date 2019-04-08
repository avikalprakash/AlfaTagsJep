package com.organization.jeptagsalpha.Temperature;

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

import com.organization.jeptagsalpha.Database.DatabaseHandler;
import com.organization.jeptagsalpha.R;

public class SettingActivity extends AppCompatActivity {
    RadioButton tempRadioButton, timeZoneRadioButton;
    RadioButton celcious, fahrenheit, local, utc;
    RadioGroup radiogroup1, radiogroup2;
    EditText hh_editext, mm_editext, ss_editext, upper_edittext, lower_edittext, profile_name;
    Button saveChange, cancel;
    DatabaseHandler dbHandler = new DatabaseHandler(this);
    String id="";
    String GetSQliteQuery;
    ImageView back;
    String interval, hh,mm,ss, upper_limit, lower_limit, temperature, time_zone, pName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        back = (ImageView)findViewById(R.id.back);
        celcious=(RadioButton)findViewById(R.id.celcious);
        fahrenheit=(RadioButton)findViewById(R.id.fahrenheit);
        utc=(RadioButton)findViewById(R.id.utc);
        local=(RadioButton)findViewById(R.id.local);
        radiogroup1=(RadioGroup)findViewById(R.id.radiogroup1);
        radiogroup2=(RadioGroup)findViewById(R.id.radiogroup2);
        hh_editext=(EditText)findViewById(R.id.hh);
        mm_editext=(EditText)findViewById(R.id.mm);
        ss_editext=(EditText)findViewById(R.id.ss);
        profile_name=(EditText)findViewById(R.id.name_edittext);
        cancel=(Button)findViewById(R.id.cancel);
        upper_edittext=(EditText)findViewById(R.id.upper_edittext);
        lower_edittext=(EditText)findViewById(R.id.lower_edittext);
        saveChange=(Button)findViewById(R.id.savechange);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


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
                pName=profile_name.getText().toString();
            //    dbManager.insert(interval, upper_limit, lower_limit, temperature, time_zone);


              /*  editor.putString("interval", interval);
                editor.putString("upper_limit", upper_limit);
                editor.putString("lower_limit", lower_limit);
                editor.putString("temperature", temperature);
                editor.putString("time_zone", time_zone);
                editor.commit();*/
                dbHandler.Add_Contact(new TempPojo(pName,
                        interval, upper_limit, lower_limit, temperature, time_zone, hh, mm, ss));
                Toast.makeText(getApplicationContext(), "Save successfully", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        try {
            id = getIntent().getStringExtra("USER_ID");
            if (!id.equals("")) {
                saveChange.setVisibility(View.GONE);
                int USER_ID = Integer.parseInt(id);
                TempPojo c = dbHandler.Get_Contact(USER_ID);
                String name = c.getName();
                String interval = c.get_interval();
                String upper = c.get_upper();
                String lower = c.get_lower();
                String temp = c.get_temperature();
                String time = c.get_timezone();
                String hh = c.get_hh();
                String mm = c.get_mm();
                String ss = c.get_ss();
                upper_edittext.setText(upper);
                profile_name.setText(name);
                lower_edittext.setText(lower);
                hh_editext.setText(hh);
                mm_editext.setText(mm);
                ss_editext.setText(ss);
                if (temp.equals("celcious")){
                    celcious.setChecked(true);
                }else if(temp.equals("Fahrenheit")){
                    fahrenheit.setChecked(true);
                }
                if (time.equals("Local")){
                   local.setChecked(true);
                }else if (time.equals("UTC")){
                    utc.setChecked(true);
                }
            }
        }catch (Exception e){}
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

            finish();

    }
}
