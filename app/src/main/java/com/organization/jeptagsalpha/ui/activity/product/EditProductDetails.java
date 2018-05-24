package com.organization.jeptagsalpha.ui.activity.product;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.NfcActivity;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight2;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight2;
import com.organization.jeptagsalpha.utils.PictureProcess;
import com.organization.jeptagsalpha.wordpress.model.wc.Category;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import butterknife.Bind;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;

import static com.organization.jeptagsalpha.ui.activity.product.ServiceHandler.response;

public class EditProductDetails extends NfcActivity implements PictureProcess.PictureProcessInf{
    private static final String TAG = ProductSaveActivity.class.getSimpleName();
    MyEditTextLight2 prod_name,  prod_desc, prod_weight, prod_price, prod_quantity;
    ImageView pr_image1, pr_image2, pr_image3, pr_image4, pr_image5;
    private GoogleApiClient client;
    MySpinnerLight2 prod_category, prod_subcategory, brand, itemCondition;
    String DataUpdateUrl = "http://54.67.107.248/jeptags/apirest/update_products";
    String subCatId=null;
    String categoryId=null;
    List<String> catIdList;
    List<String> subCatIdList;
    public static List<ProductDetails> productDetailsList;
    List<SubCategory> subCategoryList;
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;
    String GetId, Getname, Getcategory, Getsubcategory, Getdescription,GetWebsite, Getweight, Getprice, Getquantity, Getlongitute, Getlatitute;
    private String URL_CATEGORIES = "http://54.67.107.248/jeptags/apirest/get_categories";
    @Bind(R.id.btn_cancel)
    Button btn_cancel;
    AlertDialog.Builder alert;
    TextView pr_id;
    String productId="";
    String productQuantity="";
    String jepTagListCount="";
    byte[] image1, image2, image3, image4, image5;
    public int selectedImage = 0;
    PictureProcess mPictureProcess;
    Context context;
    String imageName="";
    int catId;
    Button updateBtn;
    String quantity, website;
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String language;
    String catLang="EN";
    String getName;
    String getDescription;
    byte descriptionByte[];
    byte nameByte[];
    MyEditTextLight2 prod_website;
    String UPDATE_PRODUCT="http://54.67.107.248/jeptags/apirest/update_products";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_product_details);
        pr_id=(TextView)findViewById(R.id.pr_id);
        pr_image1=(ImageView)findViewById(R.id.pr_image1);
        pr_image2=(ImageView)findViewById(R.id.pr_image2);
        pr_image3=(ImageView)findViewById(R.id.pr_image3);
        pr_image4=(ImageView)findViewById(R.id.pr_image4);
        pr_image5=(ImageView)findViewById(R.id.pr_image5);
        prod_name=(MyEditTextLight2) findViewById(R.id.prod_name);
        prod_category=(MySpinnerLight2) findViewById(R.id.prod_category);
        prod_subcategory=(MySpinnerLight2) findViewById(R.id.prod_subcategory);
        prod_desc=(MyEditTextLight2) findViewById(R.id.prod_desc);
        prod_weight=(MyEditTextLight2) findViewById(R.id.prod_weight);
        prod_price=(MyEditTextLight2) findViewById(R.id.prod_price);
        prod_quantity=(MyEditTextLight2) findViewById(R.id.prod_quantity);
        prod_website=(MyEditTextLight2) findViewById(R.id.prod_website);
        updateBtn=(Button)findViewById(R.id.updateBtn);
        mPictureProcess = new PictureProcess(this, this);
        alert=new AlertDialog.Builder(this);
        context=this;
        sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        try {
            productId = getIntent().getStringExtra(MyConstants.PRODUCT_ID);
            productQuantity = getIntent().getStringExtra(MyConstants.PRODUCT_QUANTITY);
            pr_id.setText(getString(R.string.prod_id) + productId);
            if(App.isOnline(context))
                if (language=="") {
                    language = "EN";
                    new GetEditDetail().execute(productId, language);
                }else {
                    new GetEditDetail().execute(productId, language);
                }
        }catch (Exception e){}

        pr_image1.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             selectedImage = 1;
                                             imageName="image1";
                                             getImageSelection();
                                         }
                                     }
        );
        pr_image2.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             selectedImage = 2;
                                             imageName="image2";
                                             getImageSelection();
                                         }
                                     }
        );
        pr_image3.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             selectedImage = 3;
                                             imageName="image3";
                                             getImageSelection();
                                         }
                                     }
        );
        pr_image4.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             selectedImage = 4;
                                             imageName="image4";
                                             getImageSelection();
                                         }
                                     }

        );
        pr_image5.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             selectedImage = 5;
                                             imageName="image5";
                                             getImageSelection();
                                         }
                                     }

        );
        prod_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catId=categoriesList.get(position).getId();
                new GetSubCategory().execute(String.valueOf(catId), language);
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });
    }
    private class GetCategories extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductDetails.this);
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
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                    R.layout.catlist_item, lables);
            prod_category.setAdapter(spinnerAdapter);
            if(lables.size()>0) {
                 prod_category.setEnabled(true);
                int index=catIdList.indexOf(categoryId);
               if(index>=0) {
                   prod_category.setText(categoriesList.get(index).getName());
                   catId=categoriesList.get(index).getId();
                   if (language=="") {
                       language = "EN";
                       new GetSubCategory().execute(String.valueOf(catId), language);
                   }else {
                       new GetSubCategory().execute(String.valueOf(catId), language);
                   }
               }
            }
            else prod_category.setEnabled(false);
        }
            class GetSubCategory extends AsyncTask<String, String, String> {
                @Override
                protected void onPreExecute() {
                    super.onPreExecute();
                }
                @Override
                protected String doInBackground(String... params) {
                    String return_text = "";
                    try {
                        HttpClient httpClient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost("http://54.67.107.248/jeptags/apirest/get_subcategories");
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.accumulate("category_id", params[0]);
                        jsonObject.accumulate("lang", params[1]);
                        StringEntity httpiEntity = new StringEntity(jsonObject.toString());
                        httpPost.setEntity(httpiEntity);
                        HttpResponse httpResponse = httpClient.execute(httpPost);
                        return_text = readResponse(httpResponse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return return_text;
                }
                @Override
                protected void onPostExecute(String result) {

                    subCategoryList = new ArrayList<>();

                    if (result != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(result);
                            boolean error = jsonObject.getBoolean("error");
                            if (!error) {
                                JSONArray jsonArray = jsonObject.getJSONArray("message");
                                subCatIdList=new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    SubCategory subCategory=new SubCategory((JSONObject) jsonArray.get(i));
                                    subCategoryList.add(subCategory);
                                    subCatIdList.add(String.valueOf(subCategory.getId()));
                                }
                                List<String> subCatString = new ArrayList<>();
                                for (SubCategory subCategory : subCategoryList) {
                                    subCatString.add(subCategory.getName());
                                }
                                if (subCatString.size() > 0) {
                                    ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                                            R.layout.catlist_item, subCatString);
                                    prod_subcategory.setAdapter(spinnerAdapter);
                                    if(subCatId!=null) {
                                        prod_subcategory.setEnabled(true);
                                        int index = subCatIdList.indexOf(subCatId);
                                        if (index >= 0)
                                            prod_subcategory.setText(subCategoryList.get(index).getName());
                                    }else prod_subcategory.setEnabled(false);
                                }
                            }
                        } catch (JSONException e) {
                        }
                    }
                }
            }
            class GetEditDetail extends AsyncTask<String, String, String> {
                ProgressDialog pdLoading = new ProgressDialog(EditProductDetails.this);
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
                                quantity = object.getString("qty");
                                website = object.getString("website_name");
                                JSONArray jsonArray=object.getJSONArray("image");
                                for(int i=0;i<jsonArray.length();i++) {
                                    String imageUrl = jsonArray.get(i).toString();
                                    if(imageUrl!="") {
                                        switch (i + 1) {
                                            case 1: getImage(imageUrl, pr_image1, 1); break;
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
                                prod_weight.setText(weight);
                                prod_price.setText(price);
                                prod_quantity.setText(quantity);
                                if (!website.equals("null")) {
                                    prod_website.setText(website);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

    public boolean validateProduct() {
        String var_prod_name = prod_name.getText().toString();
        String var_prod_desc = prod_desc.getText().toString();
        String var_prod_qty = prod_quantity.getText().toString();
        boolean validate=true;
        if (var_prod_name.isEmpty()) {
            prod_name.setError(getString(R.string.validate_prod_name));
            validate= false;
        }
        if (var_prod_desc.isEmpty()) {
            prod_desc.setError(getString(R.string.validate_product_desc));
            validate= false;
        }
        if(!prod_price.getText().toString().equals(""))
        {
            if(Float.parseFloat(prod_price.getText().toString())==0) {
                prod_price.setError(getString(R.string.validate_price));
                validate = false;
            }
        }
        else {
            prod_price.setError(getString(R.string.validate_price));
            validate= false;
        }
        if(!prod_quantity.getText().toString().equals(""))
        {
            if(Double.parseDouble(prod_quantity.getText().toString())==0) {
                prod_quantity.setError(getString(R.string.validate_quantity1));
                validate = false;
            }
        }
        else {
            prod_quantity.setError(getString(R.string.validate_quantity1));
            validate= false;
        }
        if(!prod_quantity.getText().toString().equals(""))
        {
            if(Integer.parseInt(prod_quantity.getText().toString()) < Integer.parseInt(quantity)) {
                prod_quantity.setError(getString(R.string.validate_quantity2));
                validate = false;
            }
        }
        else {
            prod_quantity.setError(getString(R.string.validate_quantity2));
            validate= false;
        }

        if(!prod_weight.getText().toString().equals(""))
        {
            if(Double.parseDouble(prod_weight.getText().toString())==0) {
                prod_weight.setError(getString(R.string.validate_weight));
                validate = false;
            }
        }
        else {
            prod_weight.setError(getString(R.string.validate_weight));
            validate= false;
        }
        return validate;
    }

    public void cancel(View view) {
      /*  Intent i=new Intent(EditProductDetails.this, ProductListActivity.class);
        i.putExtra("pr_id", productId);
        i.putExtra("pr_qty", productQuantity);
        startActivity(i);*/
        finish();
    }
    public  void remove(String image){
        if (selectedImage==1){
            image1=null;
            pr_image1.setImageResource(android.R.drawable.ic_input_add);
            Toast.makeText(getApplicationContext(), "Image 1 removed", Toast.LENGTH_SHORT).show();
        }
        if (selectedImage==2){
            image2=null;
            pr_image2.setImageResource(android.R.drawable.ic_input_add);
            Toast.makeText(getApplicationContext(), "Image 2 removed", Toast.LENGTH_SHORT).show();
        }
        if (selectedImage==3){
            image3=null;
            pr_image3.setImageResource(android.R.drawable.ic_input_add);
            Toast.makeText(getApplicationContext(), "Image 3 removed", Toast.LENGTH_SHORT).show();
        }
        if (selectedImage==4){
            image4=null;
            pr_image4.setImageResource(android.R.drawable.ic_input_add);
            Toast.makeText(getApplicationContext(), "Image 4 removed", Toast.LENGTH_SHORT).show();
        }
        if (selectedImage==5){
            image5=null;
            pr_image5.setImageResource(android.R.drawable.ic_input_add);
            Toast.makeText(getApplicationContext(), "Image 5 removed", Toast.LENGTH_SHORT).show();
    }
}
    public Bitmap byteToBitMap(byte[] encodedbyte){
        try{

            Bitmap bitmap= BitmapFactory.decodeByteArray(encodedbyte,0,encodedbyte.length);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }
    private byte[] convertBitmapToByte(Bitmap bitmap)
    {
        ByteArrayOutputStream stream= new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        return stream.toByteArray();
    }
    private void getImageSelection()
    {
        final AppCompatDialog dialog=new AppCompatDialog(context);
        dialog.setContentView(R.layout.edit_image_layout);
        LinearLayout cameraBtnLayout=(LinearLayout)dialog.findViewById(R.id.cameraBtnLayout);
        LinearLayout galleryBtnLayout=(LinearLayout)dialog.findViewById(R.id.galleryBtnLayout);
        LinearLayout removeBtnLayout=(LinearLayout)dialog.findViewById(R.id.removeBtnLayout);

        if(selectedImage==1) if(image1==null) removeBtnLayout.setVisibility(View.GONE);
        if(selectedImage==2) if(image2==null) removeBtnLayout.setVisibility(View.GONE);
        if(selectedImage==3) if(image3==null) removeBtnLayout.setVisibility(View.GONE);
        if(selectedImage==4) if(image4==null) removeBtnLayout.setVisibility(View.GONE);
        if(selectedImage==5) if(image5==null) removeBtnLayout.setVisibility(View.GONE);
        dialog.setTitle("Select Image");
        dialog.show();
        cameraBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenCameraGallary(selectedImage);

                dialog.dismiss();
            }
        });

        galleryBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
                dialog.dismiss();
            }
        });

        removeBtnLayout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                remove(imageName);
                dialog.dismiss();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Nammu.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void OpenCameraGallary(int selectedImage) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            mPictureProcess = new PictureProcess(this, this);
            mPictureProcess.captureImage();
        } else {
            Nammu.askForPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionCallback() {
                @Override
                public void permissionGranted() {
                    mPictureProcess.captureImage();
                }
                @Override
                public void permissionRefused() {
                    Toast.makeText(EditProductDetails.this, getString(R.string.permission_denied), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 3);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        super.onActivityResult(requestCode, resultCode, result);
        if (mPictureProcess != null) {
            mPictureProcess.onActivityResult(requestCode, resultCode, result);
        }
        if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            finish();
        }
        if(requestCode==3 && resultCode==RESULT_OK)
        {
            Uri fullPhotoUri = result.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);

                if(selectedImage==1)
                {
                    pr_image1.setImageBitmap(image);
                    image1=convertBitmapToByte(image);
                }
                else if(selectedImage==2){
                    pr_image2.setImageBitmap(image);
                    image2=convertBitmapToByte(image);
                }
                else if(selectedImage==3){
                    pr_image3.setImageBitmap(image);
                    image3=convertBitmapToByte(image);
                }
                else if(selectedImage==4){
                    pr_image4.setImageBitmap(image);
                    image4=convertBitmapToByte(image);
                }
                else if(selectedImage==5) {
                    pr_image5.setImageBitmap(image);
                    image5=convertBitmapToByte(image);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onPicCapturedSuccess(File file) {
        addImageToUpload(file, selectedImage);
    }
    public void addImageToUpload(File oldfile, final int selectedImage) {
        if (oldfile != null) {
            String newFilePath = compressImage(oldfile.getAbsolutePath());
            if (!newFilePath.isEmpty()) {
                oldfile = new File(newFilePath);
            }
            final File file = oldfile;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;
            Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath(),options);
            switch (selectedImage) {
                case 1:
                    pr_image1.setImageBitmap(bitmap);
                    image1=convertBitmapToByte(bitmap);
                    break;
                case 2:
                    pr_image2.setImageBitmap(bitmap);
                    image2=convertBitmapToByte(bitmap);
                    break;
                case 3:
                    pr_image3.setImageBitmap(bitmap);
                    image3=convertBitmapToByte(bitmap);
                    break;
                case 4:
                    pr_image4.setImageBitmap(bitmap);
                    image4=convertBitmapToByte(bitmap);
                    break;
                case 5:
                    pr_image5.setImageBitmap(bitmap);
                    image5=convertBitmapToByte(bitmap);
                    break;
            }
        }
    }
    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }
        return inSampleSize;
    }
    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }
    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "webstersys/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;
    }
    public String compressImage(String imageUri) {
        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);
        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
//      max Height and width values of the compressed image is taken as 816x612
/*        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;*/
//      width and height values are set maintaining the aspect ratio of the image
/*        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }*/
        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];
        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }
        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;
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
    public void getDataFromEditText(){
        GetId = pr_id.getText().toString().trim();
        Getname = prod_name.getText().toString().trim();
        Getdescription = prod_desc.getText().toString().trim();
       /* try {
           nameByte = Getname.getBytes(Charset.forName("UTF-8"));
            getName = new String(nameByte , "UTF-8");
            descriptionByte = Getdescription.getBytes(Charset.forName("UTF-8"));
            getDescription = new String(descriptionByte, "UTF-8");
           // URLEncoder.encode(Getdescription, "UTF-8")
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }*/
        Getcategory = prod_category.getText().toString().trim();
        Getsubcategory = prod_subcategory.getText().toString().trim();
        Getweight = prod_weight.getText().toString().trim();
        GetWebsite = prod_website.getText().toString().trim();
        Getprice = prod_price.getText().toString().trim();
        Getquantity = prod_quantity.getText().toString().trim();
    }
     void update() {
         getDataFromEditText();
         if (validateProduct()) {
             try {
                 sendDataToServer(productId, Getname, String.valueOf(catId), subCatId, Getdescription, Getweight, GetWebsite, Getprice, Getquantity,
                         language, image1, image2, image3, image4, image5);
             } catch (Exception e) {
                 e.printStackTrace();
             }
         }
     }
    public void sendDataToServer(final String id ,final String Name, final String category, final String Subcategory, final String Description, final String Weight,
                                 final String website, final String Price, final String Quantity, final  String language, final byte[] image1, final byte[] image2,
                                 final  byte[] image3, final byte[] image4, final byte[] image5){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog = new ProgressDialog(EditProductDetails.this);
                pDialog.setMessage(getString(R.string.upload_product_details));
                pDialog.setCancelable(true);
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String return_text="";
                String url = null;
                URL sourceUrl = null;
               // UPDATE_PRODUCT = UPDATE_PRODUCT.replaceAll("", " ");
                try {
                    sourceUrl = new URL(UPDATE_PRODUCT);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                url = sourceUrl.toString();

                JSONObject jsonObject=new JSONObject();
                try {

                    jsonObject.accumulate("product_id",id);
                    jsonObject.accumulate("name",Name);
                    jsonObject.accumulate("category",category);
                    jsonObject.accumulate("subcategory",Subcategory);
                    jsonObject.accumulate("description",Description);
                    jsonObject.accumulate("weight",Weight);
                    jsonObject.accumulate("website",website);
                    jsonObject.accumulate("price",Price);
                    jsonObject.accumulate("qty",Quantity);
                    jsonObject.accumulate("lang", language);

                    String imageString="";
                    if(image1!=null)imageString+=convertToBase64(image1);
                    if(image2!=null)imageString+=","+convertToBase64(image2);
                    if(image3!=null)imageString+=","+convertToBase64(image3);
                    if(image4!=null)imageString+=","+convertToBase64(image4);
                    if(image5!=null)imageString+=","+convertToBase64(image5);
                    jsonObject.accumulate("image",imageString);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(url);
                    StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                    httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                  //  httpPost.setEntity(httpiEntity);
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
                        boolean isUpdated = jsonObject.getBoolean("message");
                        if(isUpdated)
                        {
                            Toast.makeText(getApplicationContext(), "Product Updated Successfully", Toast.LENGTH_LONG).show();
                          /*  Intent iSave=new Intent(getApplicationContext(), ApprovedProductList.class);
                            startActivity(iSave);*/
                            finish();
                        }else   Toast.makeText(getApplicationContext(), "Product not Updated", Toast.LENGTH_LONG).show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent iBack=new Intent(context, ProductListActivity.class);
        iBack.putExtra("pr_id", productId);
        iBack.putExtra("pr_qty", productQuantity);
        startActivity(iBack);*/
        finish();
    }
}
