package com.organization.jeptagsalpha.ui.activity.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.ImageView;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ApprovedProductList extends AppCompatActivity implements ApprovedProductListFragment.OnListFragmentInteractionListener{
    public static List<ProductDetails> productDetailsArrayList = new ArrayList<>();
    String seller_id;
    public static Context context;
    ProgressDialog pDialog;
    String pr_id="";
    String pr_name="";
    String pr_url="";
    String pr_qty="";
    String longi="";
    String lati="";
    ImageView back;
    AppCompatTextView addJepTagBtn;
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String language;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_product_list);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        context = this;
        UserLoginDetails userLoginDetails= BasePreferenceHelper.getLoginDetails(context);
        seller_id=userLoginDetails.getEntity_id();
        sharedpreferences=getSharedPreferences(MyPref, Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        pDialog = new ProgressDialog(ApprovedProductList.this);
        pDialog.setCancelable(false);
        if(App.isOnline(context))
            if (language=="") {
                language = "EN";
                new GetProductList().execute(seller_id, language);
            }else {
                new GetProductList().execute(seller_id, language);
            }

    }

    @Override
    public void onListFragmentInteraction(ProductDetails item) {
        Intent iEdit = new Intent(ApprovedProductList.context, EditProductDetails.class);
        iEdit.putExtra("product_id", item.getProduct_id());
        iEdit.putExtra("qty", item.getProduct_qty());
        //  iEdit.putExtra("listCount", listCount);
        ApprovedProductList.context.startActivity(iEdit);
    }

  /*  class GetProductList extends AsyncTask<String, Void, String> {

        private ProgressDialog pDialog;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ApprovedProductList.this);
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
                HttpPost httpPost = new HttpPost(Urls.approved_product_list);
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
                            String s = productDetails.getProduct_qty();
                            productDetailsArrayList.add(productDetails);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            ProductApprovedListAdaptor adapter3=new ProductApprovedListAdaptor(ApprovedProductList.this,productDetailsArrayList);
            prodList.setAdapter(adapter3);
        }
    }*/

    private class GetProductList extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_prod_list));
            pDialog.show();
            productDetailsArrayList=new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.approved_product_list);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id",params[0]);
                jsonObject.accumulate("lang", params[1]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                //  httpPost.setEntity(httpiEntity);
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
            productDetailsArrayList=new ArrayList<>();
            if(pDialog.isShowing()) pDialog.dismiss();
            if(result!=null)
            {
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean error=jsonObject.getBoolean("error");

                    if(!error)
                    {
                        JSONArray jsonArray=jsonObject.getJSONArray("message");
                        for(int i=0;i<jsonArray.length();i++)
                        {
                            productDetailsArrayList.add(new ProductDetails((JSONObject)jsonArray.get(i)));
                        }
                        if (productDetailsArrayList.size()>0) {

                            Collections.sort(productDetailsArrayList, new Comparator<ProductDetails>()
                            {
                                @Override
                                public int compare(ProductDetails lhs, ProductDetails rhs) {
                                    return (rhs.getProduct_id()).compareTo(lhs.getProduct_id());
                                }
                            });
                        }
                    }
                    ApprovedProductListFragment productListFragment=new ApprovedProductListFragment();
                    FragmentTransaction fm=getSupportFragmentManager().beginTransaction();
                    fm.replace(R.id.listFrame,productListFragment);
                    fm.commitAllowingStateLoss();
                } catch (JSONException e) {}
            }
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
