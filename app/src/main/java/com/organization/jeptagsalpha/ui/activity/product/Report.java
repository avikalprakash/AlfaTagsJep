package com.organization.jeptagsalpha.ui.activity.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
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

public class Report extends AppCompatActivity {
   ListView listProduct;
    String seller_id;
    public static Context context;
    public static ArrayList<ReportListDetails> productDetailsArrayList = new ArrayList<>();
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    int total_seller_product_count;
    int pending_product_count;
    String Total;
    String Chip_scan;
    String Remaining;
    SharedPreferences sharedpreferences;
    String language;
    TextView qty_product, qty_pending_product,chip_scan, remainning_scan, total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        context = this;
        listProduct=(ListView)findViewById(R.id.listProduct);
        qty_product=(TextView)findViewById(R.id.qty_product);
        qty_pending_product=(TextView)findViewById(R.id.qty_pending_product);
        chip_scan=(TextView)findViewById(R.id.chip_scan);
        remainning_scan=(TextView)findViewById(R.id.remainning_scan);
        total=(TextView)findViewById(R.id.total);
        UserLoginDetails userLoginDetails= BasePreferenceHelper.getLoginDetails(context);
        seller_id=userLoginDetails.getEntity_id();
        sharedpreferences=getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        new GetReport().execute();
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if (String.valueOf(total_seller_product_count) != null && !String.valueOf(total_seller_product_count).equals("")){
                    qty_product.setText(String.valueOf(total_seller_product_count));
                }

                if (String.valueOf(pending_product_count) != null && !String.valueOf(pending_product_count).equals("")){
                    qty_pending_product.setText(String.valueOf(pending_product_count));
                }

                if (String.valueOf(Total) != null && !String.valueOf(Total).equals("")){
                    total.setText(String.valueOf(Total));
                }
                if (String.valueOf(Chip_scan) != null && !String.valueOf(Chip_scan).equals("")){
                    chip_scan.setText(String.valueOf(Chip_scan));
                }
                if (String.valueOf(Remaining) != null && !String.valueOf(Remaining).equals("")){
                    remainning_scan.setText(String.valueOf(Remaining));
                }
            }
        }, 2500);

    }

    class GetReport extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Report.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            // pDialog.setCancelable(true);
            pDialog.show();

        }

        @Override
        protected String doInBackground(String... args) {
            String s = "";


            try {
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Urls.report);
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
                        total_seller_product_count=jsonObject.getInt("total_seller_product_count");
                        int approved_product_count=jsonObject.getInt("approved_product_count");
                        pending_product_count=jsonObject.getInt("pending_product_count");
                        JSONArray jsonArray = jsonObject.getJSONArray("product_tag_detail_array");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Log.d("object", "kk1111xxxxx " + jsonArray.getJSONObject(i).toString());
                            JSONObject jobject = jsonArray.getJSONObject(i);

                            ReportListDetails reportListDetails = new ReportListDetails();
                            reportListDetails.setProduct_name(jobject.getString("product_name"));
                            reportListDetails.setTotal_product(jobject.getString("Total_product"));
                            productDetailsArrayList.add(reportListDetails);
                        }

                        Total = jsonObject.getString("Total");
                        Chip_scan = jsonObject.getString("Chip_scan");
                        Remaining = jsonObject.getString("Remaining");

                    }


                }catch (Exception e){
                    e.printStackTrace();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProductReportListAdaptor adapter3=new ProductReportListAdaptor(Report.this,productDetailsArrayList);
            listProduct.setAdapter(adapter3);
        }
    }
}
