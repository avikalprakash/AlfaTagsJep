package com.organization.jeptagsalpha.ui.activity.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.icu.text.SimpleDateFormat;
import android.location.LocationManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.Database.DatabaseHandler;
import com.organization.jeptagsalpha.R;
//import com.organization.jeptagsalpha.Temperature.ChartActivity;
import com.organization.jeptagsalpha.Temperature.Help;
import com.organization.jeptagsalpha.Temperature.InfoActivity;
import com.organization.jeptagsalpha.Temperature.ManageProfiles;
import com.organization.jeptagsalpha.Temperature.QrScannerHandler;
import com.organization.jeptagsalpha.Temperature.SettingActivity;
import com.organization.jeptagsalpha.Temperature.TempHome;
import com.organization.jeptagsalpha.Temperature.TempListActivity;
import com.organization.jeptagsalpha.Temperature.TempPojo;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.product.JepTagEncode;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class TemperatureHome extends AppCompatActivity {
    EditText name_edittext, email_edittext, address_edittext, phone_edittext, profil_name_edittext,
            textInterval, upper_edittext, lower_edittext, record_rule_edittext, barcode_editText;
    Button start_record, stop_record, activeTag, scan;
    TextView todayTemp;
    String seller_id;
    Context context;
    private String[][] mTechLists;
    Tag tag;
    int nfcMode = 0;
    int count = 0;
    int check;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private static Map<String, Integer> speedUnits = new HashMap<>(3);
    private static Map<String, Integer> pressUnits = new HashMap<>(3);
    private static boolean mappingsInitialised = false;
    String start_now = "";
    String tag_id = "";
    String manually_start = "";
    String delay_time_start = "";
    String timing_now = "";
    private final int requestcode = 1;
    Typeface weatherFont;
    LocationManager locationManager;
    ProgressDialog progressDialog;
    private NfcAdapter mAdapter;
    Button deactiveTag;
    int a;
    private PendingIntent mPendingIntent;
    public static final int REQUEST_PERMISSION_CODE = 1;

    private String EVENT_DATE_TIME = "2018-12-31 10:30:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private LinearLayout linear_layout_1, linear_layout_2;
    private Handler handler = new Handler();
    private Runnable runnable;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String GetSQliteQuery;
    Cursor cursor;
    SQLiteDatabase sqLiteDatabase;

    TextView control, info, list, chart;
    public static final String INTERVAL = "interval";
    public static final String UPPER_LIMIT = "upper_limit";
    public static final String LOWER_LIMIT = "lower_limit";
    public static final String TEMPERATURE = "temperature";
    public static final String TIME_ZONE = "time_zone";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    ImageView setting;
    Button select_profile;
    String activity="1";
    SharedPreferences.Editor editor;
    String interval, upper_limit, lower_limit, temperature, time_zone;
    int p1=0;
    private Dialog dialog = null;



    static final int CUSTOM_DIALOG_ID = 0;
    Contact_Adapter cAdapter;
    ListView dialog_ListView;
    ArrayList<TempPojo> contact_data = new ArrayList<TempPojo>();
    DatabaseHandler db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature_home);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setting = (ImageView) findViewById(R.id.setting);
        deactiveTag = (Button)findViewById(R.id.deactiveTag);
        select_profile=(Button)findViewById(R.id.selectProfile);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ManageProfiles.class);
                i.putExtra("activity", activity);
                startActivity(i);
                finish();
            }
        });
        sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        interval=sharedpreferences.getString(INTERVAL,"");
        upper_limit=sharedpreferences.getString(UPPER_LIMIT,"");
        lower_limit=sharedpreferences.getString(LOWER_LIMIT,"");
        temperature=sharedpreferences.getString(TEMPERATURE,"");
        time_zone=sharedpreferences.getString(TIME_ZONE,"");
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        name_edittext = (EditText) findViewById(R.id.name_edittext);
        email_edittext = (EditText) findViewById(R.id.email_edittext);
        address_edittext = (EditText) findViewById(R.id.address_edittext);
        phone_edittext = (EditText) findViewById(R.id.phone_edittext);
        profil_name_edittext = (EditText) findViewById(R.id.profil_name_edittext);
        textInterval = (EditText) findViewById(R.id.textInterval);
        upper_edittext = (EditText) findViewById(R.id.upper_edittext);
        lower_edittext = (EditText) findViewById(R.id.lower_edittext);
        record_rule_edittext = (EditText) findViewById(R.id.record_rule_edittext);
        barcode_editText = (EditText) findViewById(R.id.barcode_edittext);
        start_record = (Button) findViewById(R.id.start_record);
        stop_record = (Button) findViewById(R.id.stop_record);
        scan = (Button) findViewById(R.id.scan);

        control=(TextView)findViewById(R.id.control);
        info=(TextView)findViewById(R.id.info);
        list=(TextView)findViewById(R.id.list);
        chart=(TextView)findViewById(R.id.chart);
        control.setTypeface(control.getTypeface(), Typeface.BOLD);
        activeTag = (Button) findViewById(R.id.activeTag);
        //chronometer=(Chronometer)findViewById(R.id.interval_edittext);
        ActivityCompat.requestPermissions(TemperatureHome.this, new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestcode);

        if (interval==null ||interval.equals("")){
            textInterval.setText("");
        }else {
            textInterval.setText(interval);
        }
        if (upper_limit==null ||upper_limit.equals("")){
            upper_edittext.setText("");
        }else {
            upper_edittext.setText(upper_limit);
        }
        if (lower_limit==null ||lower_limit.equals("")){
            lower_edittext.setText("");
        }else {
            lower_edittext.setText(lower_limit);
        }
        /*dbManager = new DBManager(this);
        dbManager.open();
        cursor = dbManager.fetch();
        databaseHelper = new DatabaseHelper(this);*/


        context = this;
        UserLoginDetails userLoginDetails = BasePreferenceHelper.getLoginDetails(context);
        seller_id = userLoginDetails.getEntity_id();

        progressDialog = new ProgressDialog(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};

        activeTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter = NfcAdapter.getDefaultAdapter(TemperatureHome.this);
                if (mAdapter == null) {
                    Toast.makeText(TemperatureHome.this, "Your device do not support nfc", Toast.LENGTH_LONG).show();
                } else {
                    mPendingIntent = PendingIntent.getActivity(TemperatureHome.this, 0, new Intent(TemperatureHome.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                    progressDialog.setMessage(getString(R.string.progress_bas_scan_msg));
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    check = 2;
                    nfcMode = 1;

                }
            }
        });
        select_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  AlertDialog();
                showDialog(CUSTOM_DIALOG_ID);
            }
        });
        deactiveTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter = NfcAdapter.getDefaultAdapter(TemperatureHome.this);
                if (mAdapter == null) {
                    Toast.makeText(TemperatureHome.this, "Your device do not support nfc", Toast.LENGTH_LONG).show();
                } else {
                    p1=0;
                    mPendingIntent = PendingIntent.getActivity(TemperatureHome.this, 0, new Intent(TemperatureHome.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                    progressDialog.setMessage(getString(R.string.progress_bas_scan_msg));
                    progressDialog.setCancelable(true);
                    progressDialog.show();
                    check = 4;
                    nfcMode = 2;

                }
            }
        });

        start_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    if (validate()){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            new StartRecord().execute();
                        }
                    }
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TemperatureHome.this, QrScannerHandler.class);
                startActivity(i);
            }
        });


        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TempListActivity.class));
                finish();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                  startActivity(new Intent(getApplicationContext(), InfoActivity.class));
            }
        });

        chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(new Intent(getApplicationContext(), ChartActivity.class));
              //  finish();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch((Activity) context, pendingIntent, mIntentFilters, mTechLists);
        }
        super.onResume();
        String Data = Help.DATA;
        if (Help.DATA.length() > 0) {

            barcode_editText.setText(Data);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (check == 2 || check == 4) {
            if (nfcMode == 1 || nfcMode == 2) {
              //  countDownStart();
                String action = intent.getAction();
                if (intent != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                      String s = "avikal prakash";
                      String s2 = "pro";
                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                    NdefRecord record = NdefRecord.createMime(s2,s.getBytes());
                    NdefMessage message = new NdefMessage(new NdefRecord[] { record });
                    if (writeTag(message, tag)) {
                        Toast.makeText(this, "Success: Wrote placeid to nfc tag", Toast.LENGTH_LONG)
                                .show();
                    }

                 /*   String message2 = new String(message.getRecords()[0].getPayload());
                    Log.d(TAG, "readFromNFC: "+message2);
                    Toast.makeText(getApplicationContext(), message2, Toast.LENGTH_LONG).show();*/

                 //   readFromTag(getIntent());

                    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                        byte[] id = tag.getId();
                        if (id.length < 8) {
                            JepTagEncode.reverse(id);
                            tag_id = JepTagEncode.encodeHexStr(id, id.length).toString();
                        } else tag_id = JepTagEncode.encodeHexStr(id).toString();
                       // Toast.makeText(getApplicationContext(), tag_id, Toast.LENGTH_LONG).show();
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                        // sending tag id to server for verification and getting Jeptag id.
                        if (check == 2) {
                            if (nfcMode == 1) {
                                p1=1;
                       //         new ScanJepTagEncode().execute(tag_id);
                                sharedpreferences= getSharedPreferences("MyPref", MODE_PRIVATE);
                                editor = sharedpreferences.edit();
                                editor.putString("p1",String.valueOf(p1));
                                editor.commit();
                            } else {
                                nfcMode = 1;
                             //   if (progressDialog.isShowing()) progressDialog.dismiss();
                            }
                        }

                        if (check == 4) {
                            if (nfcMode == 2) {
                                p1=0;
                                sharedpreferences= getSharedPreferences("MyPref", MODE_PRIVATE);
                                editor = sharedpreferences.edit();
                                editor.putString("p1",String.valueOf(p1));
                                editor.commit();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                     if (progressDialog.isShowing())progressDialog.dismiss();
                                     Toast.makeText(getApplicationContext(), "Tag Formated", Toast.LENGTH_LONG).show();
                                    }
                                },2000);
                               /* Parcelable[] rawMessages;
                                rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
                                if (rawMessages != null) {
                                    NdefMessage[] messages = new NdefMessage[rawMessages.length];
                                    for (int i = 0; i < rawMessages.length; i++) {
                                        NdefMessage ndefMessage = (NdefMessage) rawMessages[i];
                                        messages[i] = ndefMessage;
                                        String b = new String(messages[i].getRecords()[0].getPayload(), Charset.forName("UTF-8"));
                                        Log.e("..........scan decoded", b);
                                        if (formatNFC(tag, ndefMessage)) {
                                            if (progressDialog.isShowing())
                                                progressDialog.dismiss();
                                            Toast.makeText(context, getString(R.string.tag_format_success), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                }
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    class StartRecord extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;

        String name = name_edittext.getText().toString().trim();
        String email = email_edittext.getText().toString().trim();
        String address = address_edittext.getText().toString().trim();
        String phone = phone_edittext.getText().toString().trim();
        String profie_name = profil_name_edittext.getText().toString().trim();
        String interval = textInterval.getText().toString().trim();
        String lower = lower_edittext.getText().toString().trim()+"°C";
        String upper = upper_edittext.getText().toString().trim()+"°C";
        String record_rule = record_rule_edittext.getText().toString().trim();
        String barcode = barcode_editText.getText().toString().trim();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
        String timing_now = dateFormat1.format(new Date());



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TemperatureHome.this);
            pDialog.setMessage("Please Wait ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://54.67.107.248/jeptags/apirest/encoding/temp_record");
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("seller_id", seller_id);
                jsonObject.accumulate("name", name);
                jsonObject.accumulate("email", email);
                jsonObject.accumulate("address", address);
                jsonObject.accumulate("phone", phone);

                //   jsonObject.accumulate("barcode", barcode);

                jsonObject.accumulate("start_now", start_now);
                jsonObject.accumulate("timing_now", timing_now);
                jsonObject.accumulate("delay_time_start", delay_time_start);
                jsonObject.accumulate("manually_start", manually_start);
                jsonObject.accumulate("tag_id", tag_id);

                jsonObject.accumulate("profile_name", profie_name);
                jsonObject.accumulate("interval_time", interval);
                jsonObject.accumulate("upper_limit", upper);
                jsonObject.accumulate("lower_limit", lower);
                jsonObject.accumulate("record_rule", record_rule);
                jsonObject.accumulate("bar_code", barcode);


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone", exception.toString());

            }

            return s;

        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                boolean check = objone.getBoolean("error");
                if (check) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(TemperatureHome.this);
                    builder.setMessage("Please Try Again")
                            .setNegativeButton("ok", null)
                            .create()
                            .show();
                } else {
                    String message = objone.getString("message");
                    SharedPreferences pref = getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("interval", interval);
                    editor.putString("upper_limit", upper_limit);
                    editor.putString("lower_limit", lower_limit);
                    editor.putString("tag_id", tag_id);
                    SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");
                    String timing_now = dateFormat1.format(new Date());
                    editor.putString("timing_now", timing_now);
                    editor.commit();
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getApplicationContext(), TempActivity.class));
                    finish();
                    // Intent i=new Intent(Login.this,Home.class);
                    // session.createUserLoginSession(username);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private String readadsResponse(HttpResponse httpResponse) {

        InputStream is = null;
        String return_text = "";
        try {
            is = httpResponse.getEntity().getContent();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return_text = sb.toString();
            Log.d("return1230", "" + return_text);
        } catch (Exception e) {

        }
        return return_text;
    }


    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    class ScanJepTagEncode extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            check=1;
            progressDialog.setMessage(getString(R.string.progress_bar_verify_msg));
            progressDialog.show();
            progressDialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/encoding/encode_scan");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("tag_id", params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    if (jsonObject.getString("type").equalsIgnoreCase("1")) {
                        JSONObject object1 = jsonObject.getJSONObject("message");
                        if (object1 != null) {
                          Toast.makeText(getApplicationContext(), "Tag Activeted", Toast.LENGTH_LONG).show();
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                           // chronometer.stop();
                        }
                    } else   if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                        if (jsonObject.getString("type").equalsIgnoreCase("2")) {
                            JSONObject object2 = jsonObject.getJSONObject("message");
                            if (object2 != null) {
                              //  jeptag_id = object2.getString("jeptag_id");
                             String   alert_msg = object2.getString("alert");
                                Toast.makeText(getApplicationContext(), alert_msg, Toast.LENGTH_LONG).show();
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                              //  chronometer.stop();
                            }
                        }
                    }
                }
                else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                    String msg = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                 //   chronometer.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public String readResponse(HttpResponse res) {
        InputStream is=null;
        String return_text="";
        try {
            is=res.getEntity().getContent();
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(is));
            String line="";
            StringBuffer sb=new StringBuffer();
            while ((line=bufferedReader.readLine())!=null)
            {
                sb.append(line);
            }
            return_text=sb.toString();
        } catch (Exception e)
        {
        }
        return return_text;
    }

    public boolean validate() {
        String name = name_edittext.getText().toString().trim();
        String email = email_edittext.getText().toString().trim();
        String address = address_edittext.getText().toString().trim();
        String phone = phone_edittext.getText().toString().trim();
        String profie_name = profil_name_edittext.getText().toString().trim();
        String interval = textInterval.getText().toString().trim();
        String lower = lower_edittext.getText().toString().trim();
        String upper = upper_edittext.getText().toString().trim();
        String record_rule = record_rule_edittext.getText().toString().trim();
        String barcode = barcode_editText.getText().toString().trim();
        boolean validate=true;
        if (name.isEmpty()) {
            name_edittext.requestFocus();
            name_edittext.setError(getString(R.string.v_name));
            validate= false;
        }
          if (email.isEmpty()) {
            email_edittext.requestFocus();
            email_edittext.setError(getString(R.string.v_emailid));
            validate= false;
        }else if (!email.matches(emailPattern)){
            email_edittext.requestFocus();
            email_edittext.setError(getString(R.string.v_valid_emailid));
            validate= false;
        }
        if (address.isEmpty()) {
            address_edittext.requestFocus();
            address_edittext.setError(getString(R.string.v_address));
            validate= false;
        }
        if (phone.isEmpty()) {
            phone_edittext.requestFocus();
            phone_edittext.setError(getString(R.string.v_phone));
            validate= false;
        }
       if (profie_name.isEmpty()) {
            profil_name_edittext.requestFocus();
            profil_name_edittext.setError(getString(R.string.v_profile_name));
            validate= false;
        }
        if (interval.isEmpty()) {
            textInterval.requestFocus();
            textInterval.setError("Please set interval");
            validate= false;
        }
        if (lower.isEmpty()) {
            lower_edittext.requestFocus();
            lower_edittext.setError(getString(R.string.v_upper_limit));
            validate= false;
        }
        if (upper.isEmpty()) {
            upper_edittext.requestFocus();
            upper_edittext.setError(getString(R.string.v_lower_limit));
            validate= false;
        }
        if (record_rule.isEmpty()) {
            record_rule_edittext.requestFocus();
            record_rule_edittext.setError(getString(R.string.v_record_rule));
            validate= false;
        }
        if (barcode.isEmpty()) {
            barcode_editText.requestFocus();
            barcode_editText.setError(getString(R.string.validate_barcode_scan));
            validate= false;
        }
        if (tag_id.isEmpty()){
            activeTag.requestFocus();
            activeTag.setError(getString(R.string.v_interval));
            validate=false;
        }
        return validate;
    }


    /*
     * Writes an NdefMessage to a NFC tag
     */
    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean formatNFC(Tag tag,NdefMessage ndefMessage)
    {
        NdefFormatable format = NdefFormatable.get(tag);
        if (format != null) {
            try {
                format.connect();
                format.format(ndefMessage);
                Log.e("............","tag formatted");
                return true;
            } catch (IOException e) {
                return false;
            } catch (FormatException e) {
                e.printStackTrace();
                return  false;
            }
        } else {
            Log.e("............","tag format null");
            return false;
        }
    }

    public void AlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(TemperatureHome.this);
        View subView = inflater.inflate(R.layout.alert_profile_select, null);
        final TextView cancel = (TextView) subView.findViewById(R.id.cancel);
        final TextView clear = (TextView)subView.findViewById(R.id.clear);


        final AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(subView);
        final AlertDialog alertDialog = builder1.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        /*builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ;
            }
        });*/

//        builder1.show();

    }


    @Override
    protected Dialog onCreateDialog(int id) {


        contact_data.clear();
        db = new DatabaseHandler(this);
        ArrayList<TempPojo> contact_array_from_db = db.Get_Contacts();
        switch(id) {
            case CUSTOM_DIALOG_ID:
                dialog = new Dialog(TemperatureHome.this);
                dialog.setContentView(R.layout.dialoglayout);


                dialog.setCancelable(true);
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener(){

                    @Override
                    public void onCancel(DialogInterface dialog) {
// TODO Auto-generated method stub
                       dialog.dismiss();
                    }});

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener(){

                    @Override
                    public void onDismiss(DialogInterface dialog) {
// TODO Auto-generated method stub
                        dialog.dismiss();
                    }});

//Prepare ListView in dialog
                for (int i = 0; i < contact_array_from_db.size(); i++) {

                    int tidno = contact_array_from_db.get(i).getID();
                    String name = contact_array_from_db.get(i).getName();
                    String interval = contact_array_from_db.get(i).get_interval();
                    String upper = contact_array_from_db.get(i).get_upper();
                    String lower = contact_array_from_db.get(i).get_lower();
                    TempPojo cnt = new TempPojo();
                    cnt.setID(tidno);
                    cnt.setName(name);
                    cnt.set_interval(interval);
                    cnt.set_upper(upper);
                    cnt.set_lower(lower);

                    contact_data.add(cnt);
                }
                db.close();
                dialog_ListView = (ListView)dialog.findViewById(R.id.dialoglist);

                cAdapter = new Contact_Adapter(TemperatureHome.this, R.layout.listview_row_profile,
                        contact_data);
                dialog_ListView.setAdapter(cAdapter);

                dialog_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
// TODO Auto-generated method stub
                        Toast.makeText(TemperatureHome.this,
                                parent.getItemAtPosition(position).toString() + " clicked",
                                Toast.LENGTH_LONG).show();
                        dismissDialog(CUSTOM_DIALOG_ID);
                    }});


                break;
        }

        return dialog;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle bundle) {
// TODO Auto-generated method stub
        super.onPrepareDialog(id, dialog, bundle);

        switch(id) {
            case CUSTOM_DIALOG_ID:
//
                break;
        }
    }

    public class Contact_Adapter extends ArrayAdapter<TempPojo> {
        Activity activity;
        int layoutResourceId;
        TempPojo user;
        ArrayList<TempPojo> data = new ArrayList<TempPojo>();

        public Contact_Adapter(Activity act, int layoutResourceId,
                               ArrayList<TempPojo> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            Contact_Adapter.UserHolder holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new Contact_Adapter.UserHolder();
                holder.name = (TextView) row.findViewById(R.id.user_name_txt);

                holder.nameLayout = (LinearLayout) row.findViewById(R.id.nameLayout);
                row.setTag(holder);
            } else {
                holder = (Contact_Adapter.UserHolder) row.getTag();
            }
            user = data.get(position);
            holder.nameLayout.setTag(user.getID());
            holder.name.setText(user.getName());
         /*   holder.email.setText(user.getEmail());
            holder.number.setText(user.getPhoneNumber());*/

            holder.nameLayout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    DatabaseHandler dbHandler = new DatabaseHandler(TemperatureHome.this);
                    String id= v.getTag().toString();
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
                    textInterval.setText(interval);
                    profil_name_edittext.setText(name);
                    upper_edittext.setText(upper);
                    lower_edittext.setText(lower);
                    a=1;
                    dialog.dismiss();
                }
            });

            return row;

        }

        class UserHolder {
            TextView name;
            LinearLayout nameLayout;
        }

    }

    public void readFromTag(Intent intent){
       tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Ndef ndef = Ndef.get(tag);


        try{
            ndef.connect();


            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];

                byte[] payload = record.getPayload();
                String text = new String(payload);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();


                ndef.close();

            }
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Cannot Read From Tag.", Toast.LENGTH_LONG).show();
        }
    }

}
