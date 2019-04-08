package com.organization.jeptagsalpha.ui.activity.product;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
import com.organization.jeptagsalpha.ui.activity.main.MainActivity;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerAdapter;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight2;
import com.organization.jeptagsalpha.wordpress.model.User;
import com.organization.jeptagsalpha.wordpress.model.wc.Country;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSellerAddress extends BaseActivity implements View.OnClickListener {
   /* @Bind(R.id.store_name)
    MyEditTextLight store_name;
    @Bind(R.id.store_id)
    MyEditTextLight store_id;
    @Bind(R.id.company_name)
    MyEditTextLight company_name;
    @Bind(R.id.contract_person_name)
    MyEditTextLight contract_person_name;
    @Bind(R.id.telephone)
    MyEditTextLight telephone;
    @Bind(R.id.fax)
    MyEditTextLight fax;
    @Bind(R.id.streat_address)
    MyEditTextLight streat_address;
    @Bind(R.id.city_edittext)
    MyEditTextLight city;
    @Bind(R.id.state)
    MyEditTextLight state_edittext;
    @Bind(R.id.postalcode_edittext)
    MyEditTextLight postalcode;*/
    @Bind(R.id.country_spinner)
   MySpinnerLight2 country_spinner;




    private String selectedCountry = "";
    private User currentUser;
    ProgressDialog pDialog;
    private List<Country> countryList;
    String GetId, GetStoreName, GetStoreID, GetCompanyName, GetContractPersonName, GetTelephone,
            GetFax, GetStreatAddress, Getcity, GetState, Getpostal, Getcountry,  GetLat,  GetLong;
    public boolean locationDetected = false;
    ProgressDialog locationProgressDialog;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    String lat = "";
    String lang = "";
    Timer timer1;
    TimerTask timerTask;
    int count = 0;
    Context context;
    EditText pr_longitute;
    EditText pr_latitute, postalcode, fax, company_name;
    EditText contract_person_name, store_name, store_id, telephone, streat_address, city, state_edittext;
    BlueButton gpsLocation;
    Location myLocation;
    String storename="";
    String storeid="";
    String cpn="";
    String countryId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seller_address_add);
        pr_latitute = (EditText) findViewById(R.id.latitude);
        pr_longitute = (EditText) findViewById(R.id.longitude);
        contract_person_name = (EditText) findViewById(R.id.contract_person_name);
        store_name = (EditText) findViewById(R.id.store_name);
        store_id = (EditText) findViewById(R.id.store_id);
        telephone = (EditText) findViewById(R.id.telephone);
        streat_address = (EditText) findViewById(R.id.street);
        city = (EditText) findViewById(R.id.city);
        state_edittext = (EditText) findViewById(R.id.state);
        fax = (EditText) findViewById(R.id.fax);
        company_name = (EditText) findViewById(R.id.company_name);
        postalcode = (EditText) findViewById(R.id.post_code);
        TextView textContractInfo = (TextView)findViewById(R.id.contract_information);
        TextView textAddress = (TextView)findViewById(R.id.add);
        SpannableString content1 = new SpannableString(getString(R.string.contract_information));
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
     //   textContractInfo.setText(content1);
        SpannableString content2 = new SpannableString(getString(R.string.address));
        content2.setSpan(new UnderlineSpan(), 0, content2.length(), 0);
      //  textAddress.setText(content2);
      //  gpsLocation = (BlueButton) findViewById(R.id.gpsLocation);
       // gpsLocation.setOnClickListener(this);
        alert = new AlertDialog.Builder(this);
        locationProgressDialog = new ProgressDialog(this);
        locationProgressDialog.setMessage("Getting location\n Please wait");
        pDialog = new ProgressDialog(AddSellerAddress.this);
        pDialog.setCancelable(false);
        context = this;
        ButterKnife.bind(this);
        countryList = Country.getCountryList();
        final MySpinnerAdapter countryAdapter = new MySpinnerAdapter(this, countryList);
        country_spinner.setAdapter(countryAdapter);
        country_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                countryId = countryList.get(position).getId();
            }
        });
        String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
        new GetSellerStore().execute(sellerId);
       // getGpsLocation();

    }

    public boolean validateAddress() {
        String var_storeName = store_name.getText().toString();
        String var_storeID = store_id.getText().toString();
        String var_contract_person = contract_person_name.getText().toString();
        String var_phoneno = telephone.getText().toString();
        String var_street_address = streat_address.getText().toString();
        String var_city = city.getText().toString();
        String var_state = state_edittext.getText().toString();
        String var_postalcode = postalcode.getText().toString();
        String var_country = country_spinner.getText().toString();


        boolean validate = true;
        if (var_storeName.isEmpty()) {
            store_name.setError(getString(R.string.validate_storename));
            validate = false;
        }
        if (var_storeID.isEmpty()) {
            store_id.setError(getString(R.string.validate_storeId));
            validate = false;
        }
        if (!pr_latitute.getText().toString().equals("")) {
            if (Double.parseDouble(pr_latitute.getText().toString()) == 0) {
                pr_latitute.setError(getString(R.string.validate_gps));
                validate = false;
            }
        } else {
            pr_latitute.setError(getString(R.string.validate_gps));
            validate = false;
        }
        if (!pr_longitute.getText().toString().equals("")) {
            if (Double.parseDouble(pr_longitute.getText().toString()) == 0) {
                pr_longitute.setError(getString(R.string.validate_gps));
                validate = false;
            }
        } else {
            pr_longitute.setError(getString(R.string.validate_gps));
            validate = false;
        }
        if (var_contract_person.isEmpty()) {
            contract_person_name.setError(getString(R.string.validate_person_name));
            validate = false;
        }
        if (var_street_address.isEmpty()) {
            streat_address.setError(getString(R.string.validate_streat));
            validate = false;
        }
        if (var_city.isEmpty()) {
            city.setError(getString(R.string.validate_city));
            validate = false;
        }
        if (var_state.isEmpty()) {
            state_edittext.setError(getString(R.string.validate_state));
            validate = false;
        }
        if (var_country.isEmpty()) {
            country_spinner.setError(getString(R.string.validate_country));
            validate= false;
        }
        if (var_postalcode.isEmpty()) {
            postalcode.setError(getString(R.string.validate_postal_code));
            validate = false;
        }
        if (var_phoneno.isEmpty()) {
            telephone.setError(getString(R.string.validate_phoneno));
            validate = false;
        }
        return validate;
    }

    @Override
    public void onClick(View view) {
       // getGpsLocation();
    }
    @OnClick(R.id.saveAddress)
    public void onClickSignup(View view) {
            if (App.isOnline(context))

                saveAddress();
        }

    public void getDataFromEditText(){
        GetStoreName = store_name.getText().toString().trim();
        GetStoreID=store_id.getText().toString().trim();
        GetCompanyName=company_name.getText().toString().trim();
        GetContractPersonName=contract_person_name.getText().toString().trim();
        GetTelephone = telephone.getText().toString().trim();
        GetFax = fax.getText().toString().trim();
        GetStreatAddress = streat_address.getText().toString().trim();
        Getcity = city.getText().toString().trim();
        GetState = state_edittext.getText().toString().trim();
        Getpostal = postalcode.getText().toString().trim();
        Getcountry = country_spinner.getText().toString().trim();
        GetLat = pr_latitute.getText().toString().trim();
        GetLong = pr_longitute.getText().toString().trim();
    }

    void saveAddress() {
        getDataFromEditText();
        UserLoginDetails userLoginDetails = BasePreferenceHelper.getLoginDetails(context);
        String sellerId = userLoginDetails.getEntity_id();
        if (validateAddress()) {
            if (App.isOnline(context))
                sendProfileDataToServer(sellerId, GetStoreName, GetStoreID, GetCompanyName, GetContractPersonName,
                        GetTelephone, GetFax, GetStreatAddress, Getcity, GetState, Getpostal, String.valueOf(countryId), GetLat, GetLong);
        }
    }
    public void sendProfileDataToServer(final String seller_id,final String storeName, final String storeID,
                                        final String companyName, final String contractPersonName,
                                        final String telephone, final  String fax, final String streetAddress,
                                        final String city, final String state,  final String postal,
                                        final String country, final String latitude, final String longitude ){

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(AddSellerAddress.this);
                pDialog.setMessage(getString(R.string.saving_add));
                pDialog.setCancelable(true);
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String return_text="";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.accumulate("seller_id", seller_id);
                    jsonObject.accumulate("storename",storeName);
                    jsonObject.accumulate("storeid",storeID);
                    jsonObject.accumulate("company",companyName);
                    jsonObject.accumulate("contact_person_name",contractPersonName);
                    jsonObject.accumulate("telephone",telephone);
                    jsonObject.accumulate("fax",fax);
                    jsonObject.accumulate("street",streetAddress);
                    jsonObject.accumulate("city",city);
                    jsonObject.accumulate("state",state);
                    jsonObject.accumulate("postcode",postal);
                    jsonObject.accumulate("country_id",country);
                    jsonObject.accumulate("latitude",latitude);
                    jsonObject.accumulate("longitude",longitude);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Urls.update_store_address);
                    StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                   // httpPost.setEntity(httpiEntity);
                    httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                    HttpResponse response = httpClient.execute(httpPost);
                    return_text= readResponse(response);
                    return return_text;
                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return return_text;
            }
            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);
                JSONObject jsonObject = null;
                if(pDialog.isShowing()) pDialog.dismiss();
                try {
                    jsonObject = new JSONObject(result);
                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                        boolean isSave = jsonObject.getBoolean("message");
                        if(isSave)
                        {
                            Toast.makeText(getApplicationContext(), getString(R.string.address_add_success), Toast.LENGTH_LONG).show();
                            Intent iAddress= new Intent(context, MainActivity.class);
                            startActivity(iAddress);
                            finish();
                        }else   Toast.makeText(getApplicationContext(), getString(R.string.address_not_add), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        if(App.isOnline(context))
            sendPostReqAsyncTask.execute();
    }

    private void getGpsLocation() {

        locationProgressDialog.show();
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           /* alert.setTitle("GPS");
            alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS...");*/
            alert.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            });
            alert.setView(R.layout.gps_message);
            alert.setPositiveButton(R.string.allow_gps,
                    new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog,
                                            int whichButton) {

                            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                return;
                            }
                            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                                    (float) 0.01, (android.location.LocationListener) listener);
                            setCriteria();

                            mlocManager.requestLocationUpdates(
                                    LocationManager.NETWORK_PROVIDER, 0, (float)
                                            0.01, (android.location.LocationListener) listener);

                            Intent I = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(I);

                        }
                    });
            alert.show();


        } else {

//            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
//                    (float) 0.01, (android.location.LocationListener) listener);
//            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
//                    (float) 0.01, (android.location.LocationListener) listener);
        }
        count=0;
        startTimer();

    }


    private void startTimer(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>20)
                        {
                            if(locationProgressDialog.isShowing()) locationProgressDialog.dismiss();
                            if (myLocation==null){
                                //Toast.makeText(getApplicationContext(), "Please check your Internet Connection", Toast.LENGTH_LONG).show();
                                timer1.cancel();
                            }
                        }
                    }
                });
            }
        };

        timer1.schedule(timerTask, 1000, 1000);
    }


    private final android.location.LocationListener listener = new android.location.LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            myLocation=location;


            if (location.getLatitude() > 0.0) {
                DecimalFormat df= new DecimalFormat("#.00000000");

                lat=String.valueOf(df.format(location.getLatitude()));
                lang=String.valueOf(df.format(location.getLongitude()));
                locationProgressDialog.setMessage("lat: "+lat+"\n"+"long:"+lang+"\n"+"accuracy:"+location.getAccuracy());
                if (location.getAccuracy()>0 && location.getAccuracy()<100) {
                    if(locationProgressDialog.isShowing()) locationProgressDialog.dismiss();
                    // Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + lang, Toast.LENGTH_LONG).show();
                   /* if (Double.valueOf(lat).equals(",")|| Double.valueOf(lang).equals(",")){
                        lat=".";
                        lang=".";
                    }*/
                   lat=lat.replaceAll(",",".");

                    lang=lang.replaceAll(",",".");
//                    char[] array1 = lat.toCharArray();
//                    char[] array2 = lang.toCharArray();
//                    for(int i = 0; i < array1.length; i++){
//                        if(array1[i] == ','){
//                            String latV=lat.replaceAll(",",".");
//                            pr_latitute.setText(latV);
//                        }
//                    }
//                    for(int i = 0; i < array2.length; i++){
//                        if(array2[i] == ','){
//                            String langV=lat.replace(',','.');
//                            pr_longitute.setText(langV);
//                        }
//                    }

                       // String langV=lat.replace(',','.');
                       // pr_longitute.setText(langV);


                   // String replaceString=lat.replace(',','.');
                    pr_longitute.setText(lang);
                    pr_latitute.setText(lat);
                }
                else
                {


                }
            }
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }

    };

    public String setCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_MEDIUM);
        String provider = mlocManager.getBestProvider(criteria, true);
        return provider;
    }
    class GetSellerStore extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Getting Store information");
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_seller_store);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id", params[0]);
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
            Log.d("result===", "" + result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONObject object = jsonObject.getJSONObject("message");
                    {
                        if (object != null) {
                            cpn = object.getString("contact_person_name");
                            contract_person_name.setText(cpn);
                        }
                    }
                }else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
