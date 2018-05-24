package com.organization.jeptagsalpha.ui.activity.product;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
import com.organization.jeptagsalpha.ui.activity.main.MainActivity;
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
import butterknife.ButterKnife;

public class ProductListActivity extends BaseActivity implements ProductListFragment.OnListFragmentInteractionListener{
    public static Context context;
   public static List<ProductDetails> productDetailsList;
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
    String catLang="EN";

   /* public static void start(Context context) {
        Intent intent = new Intent(context, ProductListActivity.class);
        context.startActivity(intent);
    }*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        addJepTagBtn=(AppCompatTextView)findViewById(R.id.addJepTagBtn);
        setActionBar(getTitle().toString());
        context = this;
        pDialog = new ProgressDialog(ProductListActivity.this);
        pDialog.setCancelable(false);
        UserLoginDetails userLoginDetails=BasePreferenceHelper.getLoginDetails(context);
        sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        if(App.isOnline(context))
            if (language=="") {
                language = "EN";
                new GetProductList().execute(userLoginDetails.getEntity_id(), language);
            }else {
                new GetProductList().execute(userLoginDetails.getEntity_id(), language);
            }
        ButterKnife.bind(this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addNewProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ProductSaveActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onListFragmentInteraction(ProductDetails item) {
      /* pr_id=item.getProduct_id();
        pr_name=item.getName();
        pr_url=item.getProduct_url();
        pr_qty=item.getProduct_qty();
        longi=item.getProduct_longitude();
        lati=item.getProduct_latitude();
        new VarifyProduct().execute(pr_id);*/
    }



    private class VarifyProduct extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(ProductListActivity.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage("Product approving...");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_status);
                JSONObject jsonObject=new JSONObject();
                Log.d("params====",""+params.toString());
                jsonObject.accumulate("product_id",params[0]);
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
                if(pdLoading.isShowing()) pdLoading.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    if (jsonObject.getString("message").equals("true")) ;
                    {
                        Intent adJepTagIntent=new Intent(context,AddJepTag.class);
                        adJepTagIntent.putExtra("pr_id", pr_id);
                        adJepTagIntent.putExtra("pr_name", pr_name);
                        adJepTagIntent.putExtra("pr_url", pr_url);
                        adJepTagIntent.putExtra("pr_qty", pr_qty);
                        adJepTagIntent.putExtra("longi", longi);
                        adJepTagIntent.putExtra("lati", lati);
                        startActivity(adJepTagIntent);
                        finish();
                    }
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.not_approved_msg), Toast.LENGTH_LONG).show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private class GetProductList extends AsyncTask<String, String, String>
    {
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_prod_list));
            pDialog.show();
            productDetailsList=new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.seller_products_list);
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
            productDetailsList=new ArrayList<>();
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
                            productDetailsList.add(new ProductDetails((JSONObject)jsonArray.get(i)));
                        }
                        if (productDetailsList.size()>0) {

                            Collections.sort(productDetailsList, new Comparator<ProductDetails>()
                            {
                                @Override
                                public int compare(ProductDetails lhs, ProductDetails rhs) {
                                    return (rhs.getProduct_id()).compareTo(lhs.getProduct_id());
                                }
                            });
                        }
                    }
                    ProductListFragment productListFragment=new ProductListFragment();
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
