package com.organization.jeptagsalpha.ui.activity.product;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight2;
import com.organization.jeptagsalpha.wordpress.model.wc.Category;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {
    String subCatId=null;
    String categoryId=null;
    String quantity;
    ImageView back;
    String qty;
    TextView prod_name,  prod_desc, prod_weight, prod_price, prod_quantity, prod_category;
    ImageView pr_image1, pr_image2, pr_image3, pr_image4, pr_image5, big_image;
    byte[] image1, image2, image3, image4, image5;
    private ArrayList<Category> categoriesList;
    MySpinnerLight2 prod_subcategory, brand, itemCondition;
    String language;
    List<SubCategory> subCategoryList;
    ProgressDialog pDialog;
    List<String> catIdList;
    List<String> subCatIdList;
    String productId;
    String imageUrl;
    int catId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        pr_image1=(ImageView)findViewById(R.id.image1);
        pr_image2=(ImageView)findViewById(R.id.image2);
        pr_image3=(ImageView)findViewById(R.id.image3);
        pr_image4=(ImageView)findViewById(R.id.image4);
        pr_image5=(ImageView)findViewById(R.id.image5);
        big_image=(ImageView)findViewById(R.id.bigImage);
        prod_name=(TextView)findViewById(R.id.pd_name);
        prod_desc=(TextView)findViewById(R.id.description);
        prod_category=(TextView)findViewById(R.id.category);
        prod_quantity=(TextView)findViewById(R.id.qty);
        back=(ImageView)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        productId = getIntent().getStringExtra("product_id");


        pr_image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(ProductListActivity.context).load(imageUrl).into(big_image);
            }
        });
        pr_image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(ProductListActivity.context).load(imageUrl).into(big_image);
            }
        });
        pr_image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(ProductListActivity.context).load(imageUrl).into(big_image);
            }
        });
        pr_image4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(ProductListActivity.context).load(imageUrl).into(big_image);
            }
        });

        pr_image5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Picasso.with(ProductListActivity.context).load(imageUrl).into(big_image);
            }
        });

        if (language=="") {
            language = "EN";
            new GetEditDetail().execute(productId, language);
        }else {
            new GetEditDetail().execute(productId, language);
        }
    }

    class GetEditDetail extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ProductDetailsActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.load_pr_details));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/edit_products");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("product_id",params[0]);
                jsonObject.accumulate("lang",params[1]);
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
            Log.d("result===", "" + result);
            pdLoading.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONObject object = jsonObject.getJSONObject("message");
                    if (object != null) {
                        String name = object.getString("name");
                        categoryId = object.getString("category");
                        subCatId = object.getString("subcategory");
                        String description = object.getString("description");
                        String weight = object.getString("weight");
                        String price = object.getString("price");
                        String quantity = object.getString("qty");
                        JSONArray jsonArray=object.getJSONArray("image");
                        for(int i=0;i<jsonArray.length();i++) {
                            imageUrl = jsonArray.get(i).toString();
                            if(imageUrl!="") {
                                switch (i + 1) {
                                    case 1: getImage(imageUrl, pr_image1, 1);
                                        Picasso.with(getApplicationContext()).load(imageUrl).into(big_image);
                                    break;
                                    case 2: getImage(imageUrl, pr_image2, 2); break;
                                    case 3: getImage(imageUrl, pr_image3, 3); break;
                                    case 4: getImage(imageUrl, pr_image4, 4); break;
                                    case 5: getImage(imageUrl, pr_image5, 5); break;
                                }
                            }
                        }
                        if (language=="") {
                            language = "EN";
                            new GetCategories().execute(language);
                        }else {
                            new GetCategories().execute(language);
                        }
                        prod_name.setText(name);
                        prod_desc.setText(description);
                        prod_quantity.setText(quantity);
                        //avc

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void  getImage(String url, final ImageView imageView, final int i)
    {
        RequestQueue requestAdministrator = null;
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if(bitmap!=null) imageView.setImageBitmap(bitmap);
                        switch (i)
                        {
                            case 1: image1=convertBitmapToByte(bitmap); break;
                            case 2: image2=convertBitmapToByte(bitmap); break;
                            case 3: image3=convertBitmapToByte(bitmap); break;
                            case 4: image4=convertBitmapToByte(bitmap); break;
                            case 5: image5=convertBitmapToByte(bitmap); break;
                        }

                    }
                },0,0,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(ProductListActivity.context).addToRequestQueue(request);
    }
    private byte[] convertBitmapToByte(Bitmap bitmap)
    {
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        return stream.toByteArray();
    }

    private class GetCategories extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductDetailsActivity.this);
            pDialog.setMessage(getString(R.string.load_category));
            pDialog.setCancelable(false);
            pDialog.show();
            categoriesList = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/get_seller_categories");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("lang",params[0]);
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
            catIdList=new ArrayList<>();
            if(pDialog.isShowing()) pDialog.dismiss();
            if (result != null) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject catObj = jsonArray.getJSONObject(i);
                        int id=catObj.getInt("cat_id");
                        String name=catObj.getString("name");
                        if(id>0 && name!=null) {
                            Category cat = new Category(id,name);
                            categoriesList.add(cat);
                            catIdList.add(String.valueOf(cat.getId()));
                            populateSpinner();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }

    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
        prod_category.setText("");

        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getName());
        }
    /*    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.catlist_item, lables);
        prod_category.setAdapter(spinnerAdapter);
        if(lables.size()>0) {
            prod_category.setEnabled(true);*/
            int index=catIdList.indexOf(categoryId);
            if(index>=0) {
                prod_category.setText(categoriesList.get(index).getName());
                catId=categoriesList.get(index).getId();

            }
        }

    }
