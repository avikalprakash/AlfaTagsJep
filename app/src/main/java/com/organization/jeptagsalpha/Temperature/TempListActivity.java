package com.organization.jeptagsalpha.Temperature;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ams.as39513App.AS39513AppActivity;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.main.TemperatureHome;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TempListActivity extends AppCompatActivity {
    public static ArrayList<TempListDetails> tempListDetailsArrayList = new ArrayList<>();
    String seller_id;
    ListView prodList;
    ImageView back;
    public static Context context;
    ProgressDialog pDialog;
    TextView control, info, list, chart;
    ImageView setting;
    String activity="3";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_list);
        setting = (ImageView) findViewById(R.id.setting);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                i.putExtra("activity", activity);
                startActivity(i);
            }
        });
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        prodList=(ListView)findViewById(R.id.prodList);
        info=(TextView)findViewById(R.id.info);
        list=(TextView)findViewById(R.id.list);
        chart=(TextView)findViewById(R.id.chart);
        control=(TextView)findViewById(R.id.control);
        list.setTypeface(list.getTypeface(), Typeface.BOLD);
        context=this;
        UserLoginDetails userLoginDetails= BasePreferenceHelper.getLoginDetails(context);
        seller_id=userLoginDetails.getEntity_id();
        new GetProductList().execute();

        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), TemperatureHome.class));
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
                startActivity(new Intent(getApplicationContext(), ChartActivity.class));
                finish();
            }
        });
    }


    class GetProductList extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TempListActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();
            tempListDetailsArrayList.clear();

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Urls.temp_list);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("seller_id", seller_id);


                StringEntity stringEntity = new StringEntity(jsonObject.toString());
                httpPost.setEntity(stringEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                s = readResponse(httpResponse);
                Log.d("tag1", " " + s);
            } catch (Exception exception) {
                exception.printStackTrace();

                Log.d("espone",exception.toString());

            }

            return s;

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
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            pDialog.dismiss();
            String messagedeptment="";
            try {
                JSONObject jsonObject=new JSONObject(json);
                boolean error=jsonObject.getBoolean("error");

                try {
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray("message");


                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("object", "kk1111xxxxx " + jsonArray.getJSONObject(i).toString());
                            JSONObject jobject = jsonArray.getJSONObject(i);

                            TempListDetails tempListDetails = new TempListDetails();
                            tempListDetails.setId(jobject.getString("id"));
                            tempListDetails.setName(jobject.getString("name"));
                            tempListDetails.setEmail(jobject.getString("email"));
                            tempListDetails.setAddress(jobject.getString("address"));
                            tempListDetails.setPhone(jobject.getString("phone"));
                            tempListDetails.setStart_now(jobject.getString("start_now"));
                            tempListDetails.setTiming_now(jobject.getString("timing_now"));
                            tempListDetails.setDelay_time_start(jobject.getString("delay_time_start"));
                            tempListDetails.setManually_start(jobject.getString("manually_start"));

                            tempListDetails.setTag_id(jobject.getString("tag_id"));
                            tempListDetails.setProfile_name(jobject.getString("profile_name"));
                            tempListDetails.setInterval_time(jobject.getString("interval_time"));
                            tempListDetails.setUpper_limit(jobject.getString("upper_limit"));
                            tempListDetails.setLower_limit(jobject.getString("lower_limit"));
                            tempListDetails.setRecord_rule(jobject.getString("record_rule"));

                            tempListDetailsArrayList.add(tempListDetails);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            TemperatureListAdaptor adapter3=new TemperatureListAdaptor(TempListActivity.this,tempListDetailsArrayList);
            prodList.setAdapter(adapter3);
        }
    }

}
