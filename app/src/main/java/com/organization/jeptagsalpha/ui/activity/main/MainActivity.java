package com.organization.jeptagsalpha.ui.activity.main;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.as39513App.AS39513AppActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.Temperature.TempHome;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.base.NfcActivity;
import com.organization.jeptagsalpha.ui.activity.contact_us.ContactUsActivity;
import com.organization.jeptagsalpha.ui.activity.contact_us.Support;
import com.organization.jeptagsalpha.ui.activity.product.ApprovedProductList;
import com.organization.jeptagsalpha.ui.activity.product.JepTagEncode;
import com.organization.jeptagsalpha.ui.activity.product.ProductListActivity;
import com.organization.jeptagsalpha.ui.activity.product.ProductPendingListActivity;
import com.organization.jeptagsalpha.ui.activity.product.ProductSaveActivity;
import com.organization.jeptagsalpha.ui.activity.product.Report;
import com.organization.jeptagsalpha.ui.activity.profile.ProfileActivity;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.ContentValues.TAG;

public class MainActivity extends NfcActivity implements Drawer.OnDrawerItemClickListener, View.OnClickListener {
    TextView storeName, product, pending, addProduct, addJeptags, report, support, fullName;
    public static final int REQUEST_PERMISSION_CODE=1;
    ImageView back_btn;
    UserLoginDetails session;
    Drawer result;
    public static final String TAG = "NfcDemo";
    ProgressDialog progressDialog;
    Timer timer1;
    TimerTask timerTask;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    String tagId="";
    Context context;
    Tag tag;
    int nfcMode=0;
    int count=0;
    String  prod_id;
    String  prod_url;
    String  prod_name;
    String  latitude;
    String  longitude;
    String address;
    String  jeptag_id;
    String  alert_msg;
    RelativeLayout stopRelativeLayout;
    ImageView genuineJepTag;
    Button temp;
    boolean b;
    int check;
    TextView bought, used, pendingItem;
    int i;
    UserLoginDetails userLoginDetails;
    Dialog dialog;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    public static void start(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setActionBar(getString(R.string.profile) ,false);
        context = this;
        userLoginDetails = BasePreferenceHelper.getLoginDetails(this);
        ButterKnife.bind(this);
        session = new UserLoginDetails();
        storeName=(TextView)findViewById(R.id.company_name);
        product=(TextView)findViewById(R.id.product);
        pending=(TextView)findViewById(R.id.pending);
        addProduct=(TextView)findViewById(R.id.addProduct);
        addJeptags=(TextView)findViewById(R.id.addJeptags);
        report=(TextView)findViewById(R.id.report);
        support=(TextView)findViewById(R.id.support);
        temp=(Button)findViewById(R.id.temp);
        bought=(TextView)findViewById(R.id.bought);
        used=(TextView)findViewById(R.id.used);
        pendingItem=(TextView)findViewById(R.id.pendingItem);
       // back_btn=(ImageView)findViewById(R.id.back_btn);
        fullName=(TextView)findViewById(R.id.full_name);
        context = this;
        temp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Intent intent = new Intent(getApplicationContext(), ChartActivity.class);
                Intent intent = new Intent(getApplicationContext(), TempActivity.class);
                startActivity(intent);
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(MainActivity.this, ProductListActivity.class);
                startActivity(i);

            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), ProductPendingListActivity.class));
            }
        });
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ProductSaveActivity.class);
                startActivity(i);
            }
        });
        addJeptags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, ApprovedProductList.class);
                startActivity(i);
            }
        });
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                Intent i = new Intent(MainActivity.this, Report.class);
                startActivity(i);
            }
        });
        support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Support.class));
               // Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });
//        back_btn.setOnClickListener(this);
        userLoginDetails= BasePreferenceHelper.getLoginDetails(context);
        if(App.isOnline(context)) {
           // new GetProfileDetail().execute(userLoginDetails.getEntity_id());
            new GetDetails().execute();
        }
        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_background)
                .addProfiles(
                        new ProfileDrawerItem().withName(userLoginDetails.getFirstname())
                                .withEmail(userLoginDetails.getEmail()).withIcon(getResources().getDrawable(R.drawable.avtar))
                ).
                        withAlternativeProfileHeaderSwitching(false)
                .build();

        result = new DrawerBuilder().
                withActivity(this).
                withToolbar(toolbar).
                withActionBarDrawerToggle(true).
                withAccountHeader(headerResult).
                withOnDrawerItemClickListener(this).build();



        progressDialog=new ProgressDialog(this);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};

        String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
        new VarifyAddress().execute(sellerId);
        checkRunTimePermission();
    }


    private class VarifyAddress extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  pdLoading.setMessage("Address approving...");
          //  pdLoading.show();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/check_store_address_status");
                JSONObject jsonObject=new JSONObject();
                Log.d("params====",""+params.toString());
                jsonObject.accumulate("seller_id",params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            }catch(Exception e){
                e.printStackTrace();
            }
            return return_text;
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
        @Override
        protected void onPostExecute(String result) {
            try{
                dialog.dismiss();
               // if(pdLoading.isShowing()) pdLoading.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    if (jsonObject.getString("message").equals("true")) ;
                    {
                        i=1;
                        getMenus();
                    }
                }
                else{
                    i=0;
                    Toast.makeText(getApplicationContext(), getString(R.string.not_approved), Toast.LENGTH_LONG).show();
                   getMenus();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    class GetProfileDetail extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.loading));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/edit_sellers");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id",params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                //httpPost.setEntity(httpiEntity);
                HttpResponse httpResponse=httpClient.execute(httpPost);
                return_text=readResponse(httpResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("result===", "" + result);
            pdLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONObject object = jsonObject.getJSONObject("message");
                    if (object != null) {
                        String fname = object.getString("fullname");
                        String email = object.getString("email");
                        String storename = object.getString("storename");
                        String street = object.getString("street");
                        String state = object.getString("state");
                        String country = object.getString("country_id");
                        String u_city = object.getString("city");
                        String postal = object.getString("postcode");
                        String phone_no = object.getString("telephone");
                      /*  fullname.setText(fname);
                        email_id.setText(email);
                        store_name.setText(storename);
                        street_edittext.setText(street);
                        state_edittext.setText(state);
                        country_spinner.setText(country);
                        city.setText(u_city);
                        postalcode.setText(postal);
                        phoneno.setText(phone_no);*/
                        storeName.setText(storename);
                        fullName.setText(fname);
                    }
                    String total_quantity_of_product = jsonObject.getString("total qty of product");
                    bought.setText(total_quantity_of_product);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class GetDetails extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
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
                HttpPost httpPost = new HttpPost("http://54.67.107.248/jeptags/apirest/edit_sellers");
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("seller_id", userLoginDetails.getEntity_id());


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readadsResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }

            return s;

        }
        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            try {
                JSONObject objone = new JSONObject(json);
                boolean check  = objone.getBoolean("error");
                if(check) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Please Try Again")
                            .setNegativeButton("ok", null)
                            .create()
                            .show();
                }else{
                    JSONObject jsonObject = objone.getJSONObject("message");
                    if (jsonObject != null) {
                        String fname = jsonObject.getString("fullname");
                        String email = jsonObject.getString("email");
                        String storename = jsonObject.getString("storename");
                        String street = jsonObject.getString("street");
                        String state = jsonObject.getString("state");
                        String country = jsonObject.getString("country_id");
                        String u_city = jsonObject.getString("city");
                        String postal = jsonObject.getString("postcode");
                        String phone_no = jsonObject.getString("telephone");
                      /*  fullname.setText(fname);
                        email_id.setText(email);
                        store_name.setText(storename);
                        street_edittext.setText(street);
                        state_edittext.setText(state);
                        country_spinner.setText(country);
                        city.setText(u_city);
                        postalcode.setText(postal);
                        phoneno.setText(phone_no);*/
                        storeName.setText(storename);
                        fullName.setText(fname);
                    }
                    String total_quantity_of_product = objone.getString("total qty of product");
                    String total_tag = objone.getString("Total tags");
                    String total_bonus = objone.getString("Total bonus");
                    bought.setText(total_quantity_of_product);
                    used.setText(total_tag);
                    pendingItem.setText("$ "+total_bonus);
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

    public void getMenus()
    {
             addMenu(1, R.string.nav_scan_product, R.drawable.ic_barcode_scan, false);
        if (i==1){
            addMenu(2, R.string.nav_edit_product, R.drawable.ic_cube, false);
        }
             addMenu(97, R.string.contact_us, R.drawable.ic_information,true);
            // addMenu(98,R.string.nav_profile,R.drawable.ic_account_circle,true);
             addMenu(99, R.string.nav_logout, R.drawable.ic_logout,true);
        }
    public void addMenu(long id, int resId,int drawable,boolean isSticky)
    {
        if(isSticky)
        {
            result.addStickyFooterItem(new PrimaryDrawerItem().
                    withIdentifier(id).
                    withName(resId).
                    withTextColorRes(R.color.accent).
                    withIcon(drawable).
                    withTintSelectedIcon(true).
                    withIconColorRes(R.color.materialize_accent));
        }
        else {
            result.addItem(new PrimaryDrawerItem().
                    withIdentifier(id).
                    withName(resId).
                    withTextColorRes(R.color.accent).
                    withIcon(drawable).
                    withTintSelectedIcon(true).
                    withIconColorRes(R.color.accent));
        }
    }
    @Override
    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
        if(drawerItem != null)
        {
            switch (String.valueOf(drawerItem.getIdentifier()))
            {
                case "1":
                    stopRelativeLayout=(RelativeLayout)findViewById(R.id.stopRelativeLayout);
                    genuineJepTag=(ImageView) findViewById(R.id.genuineJepTag);
                    mAdapter = NfcAdapter.getDefaultAdapter(MainActivity.this);
                    if (mAdapter == null) {
                        Toast.makeText(MainActivity.this,"Your device do not support nfc",Toast.LENGTH_LONG).show();
                    } else {
                        mPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, new Intent(MainActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                        progressDialog.setMessage(getString(R.string.progress_bas_scan_msg));
                        progressDialog.setCancelable(true);
                        progressDialog.show();
                    }
                    check=2;
                    nfcMode=1;
                    break;
                case "2":
                    Intent i = new Intent(MainActivity.this, ProductListActivity.class);
                    startActivity(i);

                    break;
                case "97":
                    ContactUsActivity.start(this);
                    break;
                case "98":
                  //  ProfileActivity.start(this);
                    break;
                case "99":
                    Logout();
                    break;
                default:
                    break;
            }
        }
        result.closeDrawer();
        return  true;
    }
    private void startTimerTrue1(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>3)
                        {
                            stopRelativeLayout.setVisibility(View.INVISIBLE);
                            genuineJepTag.setVisibility(View.INVISIBLE);
                                timer1.cancel();
                            oldProductAlertDialog();
                        }
                    }
                });
            }
        };
        timer1.schedule(timerTask, 1000, 1000);
    }
    private void startTimerTrue2(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>3)
                        {
                            stopRelativeLayout.setVisibility(View.INVISIBLE);
                            genuineJepTag.setVisibility(View.INVISIBLE);
                            timer1.cancel();
                            newProductAlertDialog();
                        }
                    }
                });
            }
        };
        timer1.schedule(timerTask, 1000, 1000);
    }
    private void startTimerFalse(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>3)
                        {
                            stopRelativeLayout.setVisibility(View.INVISIBLE);
                            genuineJepTag.setVisibility(View.INVISIBLE);
                            timer1.cancel();
                        }
                    }
                });
            }
        };
        timer1.schedule(timerTask, 1000, 1000);
    }
    @Override
    public void onResume(){
        super.onResume();
        if (mNfcAdapter != null) {
            mNfcAdapter.enableForegroundDispatch((Activity)context, pendingIntent, mIntentFilters, mTechLists);
        }
    }
    @Override
    public void onPause(){
        super.onPause();
        if (mNfcAdapter != null)
        {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (check == 2) {
            if (nfcMode == 1) {
                String action = intent.getAction();
                if (intent != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

                    /*String message2 = new String(message.getRecords()[0].getPayload());
                    Log.d(TAG, "readFromNFC: "+message2);
                    Toast.makeText(getApplicationContext(), message2, Toast.LENGTH_LONG).show();*/

                    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

                        byte[] id = tag.getId();
                        if (id.length < 8) {
                            JepTagEncode.reverse(id);
                            tagId = JepTagEncode.encodeHexStr(id, id.length).toString();
                        } else tagId = JepTagEncode.encodeHexStr(id).toString();


                        // sending tag id to server for verification and getting Jeptag id.
                        if (check==2) {
                            if (nfcMode == 1) {
                                new ScanJepTagEncode().execute(tagId);
                            } else {
                                nfcMode = 1;
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                stopRelativeLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        openWebPage(prod_url);
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
                            prod_id = object1.getString("product_id");
                            prod_url = object1.getString("product_url");
                            prod_name = object1.getString("name");
                            latitude = object1.getString("latitude");
                            longitude = object1.getString("longitude");
                            address = object1.getString("address");
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                            genuineJepTag.setVisibility(View.VISIBLE);
                            startTimerTrue1();
                        }
                    } else   if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                        if (jsonObject.getString("type").equalsIgnoreCase("2")) {
                            JSONObject object2 = jsonObject.getJSONObject("message");
                            if (object2 != null) {
                                jeptag_id = object2.getString("jeptag_id");
                                alert_msg = object2.getString("alert");
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                                genuineJepTag.setVisibility(View.VISIBLE);
                                startTimerTrue2();
                            }
                        }
                    }
                }
                else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                    String msg = jsonObject.getString("message");
                    if(progressDialog.isShowing()) progressDialog.dismiss();
                    stopRelativeLayout.setVisibility(View.VISIBLE);
                    startTimerFalse();
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
    public void oldProductAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.alert_scan_tag, null);
        final TextView idText = (TextView) subView.findViewById(R.id.idText);
        final TextView urlText = (TextView)subView.findViewById(R.id.urlText);
        final TextView nameText = (TextView) subView.findViewById(R.id.nameText);
        final TextView addressLongitude = (TextView)subView.findViewById(R.id.addressLongitude);
        final TextView addressLatitude = (TextView)subView.findViewById(R.id.addressLatitude);
        final TextView add = (TextView)subView.findViewById(R.id.address);
        idText.setText(prod_id);
        urlText.setText(prod_url);
        nameText.setText(prod_name);
        addressLongitude.setText(longitude);
         addressLatitude.setText(latitude);
        add.setText(address);
        urlText.setOnClickListener(this);
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setView(subView);
        AlertDialog alertDialog = builder1.create();

        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, getString(R.string.toast_calcel), Toast.LENGTH_LONG).show();
                b=false;
            }
        });

        builder1.show();

    }
    public void newProductAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
        View subView = inflater.inflate(R.layout.alert_scan_tag2, null);
        final TextView idText = (TextView) subView.findViewById(R.id.idText);
        final TextView alertText = (TextView)subView.findViewById(R.id.urlText);
        idText.setText(jeptag_id);
        alertText.setText(alert_msg);
        AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
        builder2.setView(subView);
        AlertDialog alertDialog = builder2.create();

        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(MainActivity.this, getString(R.string.toast_calcel), Toast.LENGTH_LONG).show();
            }
        });

        builder2.show();
    }
    public void openWebPage(String url) {
        try {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }catch (Exception e){}
    }

    // Permission giving at runtime

    private void checkRunTimePermission() {

        if(checkPermission()){

            Toast.makeText(MainActivity.this, "All Permissions Granted Successfully", Toast.LENGTH_LONG).show();

        }
        else {

            requestPermission();
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]
                {
                        CAMERA,
                        ACCESS_COARSE_LOCATION,
                        READ_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE

                },  REQUEST_PERMISSION_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case  REQUEST_PERMISSION_CODE:

                if (grantResults.length > 0) {

                    boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//                    boolean ReadPhoneStatePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (CameraPermission && ReadContactsPermission ) {

                        //   Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_LONG).show();
                    }
                    else {
                        //   Toast.makeText(MainActivity.this,"Permission Denied", Toast.LENGTH_LONG).show();

                    }
                }

                break;
        }
    }

    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int FourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FourthPermissionResult == PackageManager.PERMISSION_GRANTED ;
    }
}






