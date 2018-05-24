package com.organization.jeptagsalpha.ui.activity.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.ListView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
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

public class ProductPendingListActivity extends BaseActivity {
    public static ArrayList<ProductDetails> productDetailsArrayList = new ArrayList<>();
    String seller_id;
    ListView prodList;
    public static Context context;
    ProgressDialog pDialog;
    String pr_id="";
    String pr_name="";
    String pr_url="";
    String pr_qty="";
    String longi="";
    String lati="";
    AppCompatTextView addJepTagBtn;
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_pending_list);
        prodList=(ListView)findViewById(R.id.prodList);
        context = this;
        UserLoginDetails userLoginDetails= BasePreferenceHelper.getLoginDetails(context);
        seller_id=userLoginDetails.getEntity_id();
        sharedpreferences=getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        new GetProductList().execute();
    }

    class GetProductList extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductPendingListActivity.this);
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
                HttpPost httpPost = new HttpPost(Urls.pending_product_list);
                httpPost.setHeader("Content-type", "application/json");
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("seller_id", seller_id);
                jsonObject.accumulate("lang", language);

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

                        ProductDetails productDetails = new ProductDetails();
                        productDetails.setProduct_id(jobject.getString("id"));
                        productDetails.setName(jobject.getString("name"));
                        productDetails.setProduct_qty(jobject.getString("qty"));
                        productDetails.setImage(jobject.getString("image"));
                        productDetails.setProduct_url(jobject.getString("product_url"));
                        productDetails.setCategory(jobject.getString("category"));
                        productDetails.setApproved(jobject.getString("approved"));
                        productDetails.setProduct_latitude(jobject.getString("latitude"));
                        productDetails.setProduct_longitude(jobject.getString("longitude"));

                        productDetailsArrayList.add(productDetails);
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProductPendingListAdaptor adapter3=new ProductPendingListAdaptor(ProductPendingListActivity.this,productDetailsArrayList);
            prodList.setAdapter(adapter3);
        }
    }
}
