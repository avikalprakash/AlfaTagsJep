package com.organization.jeptagsalpha.ui.activity.product;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.base.NfcActivity;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight2;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight2;
import com.organization.jeptagsalpha.ui.model.NfcModel;
import com.organization.jeptagsalpha.utils.NfcConvertor;
import com.organization.jeptagsalpha.utils.PictureProcess;
import com.organization.jeptagsalpha.wordpress.model.wc.Attribute;
import com.organization.jeptagsalpha.wordpress.model.wc.Category;
import com.organization.jeptagsalpha.wordpress.model.wc.Product;
import com.organization.jeptagsalpha.wordpress.rest.HttpServerErrorResponse;
import com.organization.jeptagsalpha.wordpress.rest.WordPressRestResponse;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.tajchert.nammu.Nammu;
import pl.tajchert.nammu.PermissionCallback;
import static com.organization.jeptagsalpha.MyConstants.WCService;
import static com.organization.jeptagsalpha.MyConstants.removeHtmlTags;

public class ProductSaveActivity extends NfcActivity implements PictureProcess.PictureProcessInf, View.OnClickListener {
    @Bind(R.id.prod_name)
    MyEditTextLight2 prod_name;
    @Bind(R.id.prod_price)
    MyEditTextLight2 prod_price;
    @Bind(R.id.prod_desc)
    MyEditTextLight2 prod_desc;
    @Bind(R.id.prod_category)
    MySpinnerLight2 prod_category;
    @Bind(R.id.prod_sub_category)
    MySpinnerLight2 prod_sub_category;
    @Bind(R.id.prod_weight)
    MyEditTextLight2 prod_weight;
    @Bind(R.id.prod_qty)
    MyEditTextLight2 prod_qty;
   /* @Bind(R.id.btnAddProduct)
    public BlueButton addProduct;*/
    @Bind(R.id.prod_image1)
    ImageView prod_image1;
    @Bind(R.id.prod_image2)
    ImageView prod_image2;
    @Bind(R.id.prod_image3)
    ImageView prod_image3;
    @Bind(R.id.prod_image4)
    ImageView prod_image4;
    @Bind(R.id.prod_image5)
    ImageView prod_image5;
  /*  @Bind(R.id.btn_cancel)
    Button btn_cancel;*/
    TextView pr_address;
    MyEditTextLight2 website;
   // TextView pr_latitute;
    TextView barcodeResultT , barcodeResultF;
    Location myLocation;
    LocationManager mlocManager;
    AlertDialog.Builder alert;
    byte[] image1, image2, image3, image4, image5;
    String  GetBrandname,  Getname, Getcategory, Getsubcategory, Getdescription, Getweight, Getprice,
            Getquantity, GetBarcode, GetAddress, GetWebsite;
    private ArrayList<Category> categoriesList;
    ProgressDialog pDialog;
    RadioButton checkOnline, checkOffline, checkAddress1, checkAddress2, checkAddress3;
    Timer timer1;
    Button barcodeScan, btnAddProduct, btn_cancel;
    TimerTask timerTask;
    int conValue;
    int count = 0;
    int l=2;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
   // private String URL_CATEGORIES = "http://54.67.107.248/jeptags/apirest/get_categories";
   // String DataParseUrl = "http://54.67.107.248/jeptags/apirest/add_products";
    private static final String TAG = ProductSaveActivity.class.getSimpleName();
    int catId;
    int brandId;
    int conditionId;
    public Product product;
    public Product oldProduct;
    public int selectedImage = 0;
    PictureProcess mPictureProcess;
    ProgressDialog locationProgressDialog;
    String lat = "";
    String lang = "";
    String country_id;
   // String city;
    String postcode;
    String log;
    Context context;
    List<SubCategory> subCategoryList;
    List<BrandList> brandNameList;
    List<ConditionList> conditionNameList;
    int subCatId;
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String language;
    String catLang="EN";
    boolean flagSale;
    String[] product_condition = {"New","Refurbished"};

    final int GETADDRESS_CODE=99;

    String fname="";
    String lname="";
    String storename="";
    String store_id="";
    String company_name="";
    String c_person_name;
    String telephone="";
    String fax="";
    MySpinnerLight2 itemCondition;
    MySpinnerLight2 brand;
    String seller_address;

    String s_address="";
    String city="";
    String state="";
    String postal="";
    String country="";
    String longitude="";
    String latitude="";
    String en="en";
    SharedPreferences prefs ;
    private static final String PREFS_NAME = "name";
  //  String itemConditionText;
    private GoogleApiClient client;
    public static void start(final Context context) {
        Intent intent = new Intent(context, ProductSaveActivity.class);
        ((Activity) context).startActivityForResult(intent, 1);
    }
    @OnClick(R.id.prod_image1)
    public void onClickAddProductPic1(View view) {
        selectedImage = 1;
        getImageSelection();
    }
    @OnClick(R.id.prod_image2)
    public void onClickAddProductPic2(View view) {
        selectedImage = 2;
        getImageSelection();
    }
    @OnClick(R.id.prod_image3)
    public void onClickAddProductPic3(View view) {
        selectedImage = 3;
        getImageSelection();
    }
    @OnClick(R.id.prod_image4)
    public void onClickAddProductPic4(View view) {
        selectedImage = 4;
        getImageSelection();
    }
    @OnClick(R.id.prod_image5)
    public void onClickAddProductPic5(View view) {
        selectedImage = 5;
        getImageSelection();
    }
    private void getImageSelection() {
        final AppCompatDialog dialog = new AppCompatDialog(context);
        dialog.setContentView(R.layout.upload_doc_dialog);
        LinearLayout cameraBtnLayout = (LinearLayout) dialog.findViewById(R.id.cameraBtnLayout);
        LinearLayout galleryBtnLayout = (LinearLayout) dialog.findViewById(R.id.galleryBtnLayout);
        dialog.setTitle(getString(R.string.select_image));
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
    }
    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 3);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                barcodeResultT.setText(contents);
                barcodeResultF.setText("BARCODE : " + format);
            }
        }
        if (mPictureProcess != null) {
            mPictureProcess.onActivityResult(requestCode, resultCode, intent);
        }
        if (requestCode == 1 && resultCode == 2) {
            setResult(2);
            finish();
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            Uri fullPhotoUri = intent.getData();
            Bitmap image = null;
            try {
                image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fullPhotoUri);
                if (selectedImage == 1) {
                    prod_image1.setImageBitmap(image);
                    image1 = convertBitmapToByte(image);
                } else if (selectedImage == 2) {
                    prod_image2.setImageBitmap(image);
                    image2 = convertBitmapToByte(image);
                } else if (selectedImage == 3) {
                    prod_image3.setImageBitmap(image);
                    image3 = convertBitmapToByte(image);
                } else if (selectedImage == 4) {
                    prod_image4.setImageBitmap(image);
                    image4 = convertBitmapToByte(image);
                } else if (selectedImage == 5) {
                    prod_image5.setImageBitmap(image);
                    image5 = convertBitmapToByte(image);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (requestCode == GETADDRESS_CODE) {
            try {
                Bundle bundle = intent.getBundleExtra(MyConstants.ADDRESS);

                if (bundle != null) {
                    try {
                        //Getting New Address through Intent
                        storename = bundle.getString(MyConstants.STORE_NAME);
                        store_id = bundle.getString(MyConstants.STORE_ID);
                        company_name = bundle.getString(MyConstants.COMPANY_NAME);
                        c_person_name = bundle.getString(MyConstants.CONTRACT_PERSON_NAME);
                        telephone = bundle.getString(MyConstants.TELEPHONE);
                        fax = bundle.getString(MyConstants.FAX);
                        s_address = bundle.getString(MyConstants.STREET_ADDRESS);
                        city = bundle.getString(MyConstants.CITY);
                        city = bundle.getString(MyConstants.STATE);
                        postal = bundle.getString(MyConstants.POSTALCODE);
                        country = bundle.getString(MyConstants.COUNTRY);
                        latitude = bundle.getString(MyConstants.LATITUDE);
                        longitude = bundle.getString(MyConstants.LONGITUDE);
                        //Setting all fields in one TextView

                            if (longitude  == null && latitude == null && store_id ==null) {
                                longitude = " ";
                                latitude =" ";
                                store_id=" ";
                                pr_address.setText(storename + " " + store_id + ", " + s_address + ", " + city + ", " + postal + ", " + country + ", " + telephone + ", " + latitude + ", " + longitude);
                                pr_address.setVisibility(View.VISIBLE);
                            }else {
                                pr_address.setText(storename + " " + store_id + ", " + s_address + ", " + city + ", " + postal + ", " + country + ", " + telephone + ", " + latitude + ", " + longitude);
                                pr_address.setVisibility(View.VISIBLE);
                            }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } if (resultCode == Activity.RESULT_CANCELED) {
            finish();
        }
    }
    private byte[] convertBitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        return stream.toByteArray();
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            for (String message : getNfcMessages()) {
                NfcModel nfcModel = NfcConvertor.decode(message);
                if (nfcModel == null) {
                    showToast(getString(R.string.tag_not_recogniz_error));
                    return;
                }
                if (nfcModel.getProductId().isEmpty()) {
                    showToast(getString(R.string.productid_empty_error));
                    return;
                }
                onGetProductId(nfcModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            showToast(e.getMessage());
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_save);
        pr_address = (TextView) findViewById(R.id.pr_address);
        barcodeResultT = (TextView) findViewById(R.id.barcodeResultT);
        barcodeResultF = (TextView) findViewById(R.id.barcodeResultF);
        barcodeScan=(Button)findViewById(R.id.barcodeScan);
        btnAddProduct=(Button)findViewById(R.id.btnAddProduct);
        btn_cancel=(Button)findViewById(R.id.btn_cancel);
        itemCondition=(MySpinnerLight2)findViewById(R.id.itemCondition);
        brand=(MySpinnerLight2)findViewById(R.id.brand);
        website=(MyEditTextLight2)findViewById(R.id.prod_website);
        barcodeScan.setOnClickListener(this);
        context = this;

        pDialog = new ProgressDialog(ProductSaveActivity.this);
       /* pDialog.setMessage(getString(R.string.load_category));
        pDialog.show();
        pDialog.setCancelable(false);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                if(pDialog.isShowing()) pDialog.dismiss();
            }
        }, 8000);*/
        sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
/*        if (App.isOnline(context))
            new GetConditionList().execute();
            new GetBrandList().execute();*/
        if (language=="") {
            language="EN";
            if (App.isOnline(context))
            new GetConditionList().execute(language);
        }else {
            if (App.isOnline(context))
            new GetConditionList().execute(language);
        }
        ButterKnife.bind(this);
        mPictureProcess = new PictureProcess(this, this);
        alert = new AlertDialog.Builder(this);
        /*locationProgressDialog = new ProgressDialog(this);
        locationProgressDialog.setMessage(getString(R.string.get_location));*/
        context = this;
        prod_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                catId = categoriesList.get(position).getId();
                if (App.isOnline(context))
                    if (language=="") {
                        language="EN";
                        new GetSubCategory().execute(String.valueOf(catId), language);
                    }else {
                        new GetSubCategory().execute(String.valueOf(catId), language);
                    }
            }
        });
        itemCondition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                conditionId = conditionNameList.get(position).getValue();
            }
        });
        brand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                brandId = brandNameList.get(position).getId();
            }
        });
        prod_sub_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                subCatId = subCategoryList.get(position).getId();
            }
        });
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
       // if (App.isOnline(context))
          //  new getWholeAddress().execute(sellerId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.barcodeScan:
                try {
                    Intent intent = new Intent(ACTION_SCAN);
                    intent.putExtra("SCAN_MODE", "PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException anfe) {
                    showDialog(ProductSaveActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
                }
        }
    }
    private static android.app.AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        android.app.AlertDialog.Builder downloadDialog = new android.app.AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        try {
            downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });
        }catch (Exception e){}


        return downloadDialog.show();
    }

    private class GetSubCategory extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_subcategory));
            pDialog.show();
            pDialog.setCancelable(false);
            subCategoryList = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_subcategories);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("category_id",params[0]);
                jsonObject.accumulate("lang",params[1]);
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
            subCategoryList=new ArrayList<>();
            if(pDialog.isShowing()) pDialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            subCategoryList.add(new SubCategory((JSONObject) jsonArray.get(i)));
                        }
                        List<String> subCatString = new ArrayList<>();
                        for (SubCategory subCategory : subCategoryList) {
                            subCatString.add(subCategory.getName());
                        }
                        if (subCatString.size() > 0) {
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                                    R.layout.catlist_item, subCatString);
                            prod_sub_category.setAdapter(spinnerAdapter);
                            prod_sub_category.setVisibility(View.VISIBLE);
                        }
                        String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
                        new getWholeAddress().execute(sellerId);
                    }
                } catch (JSONException e) {
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }

    private class GetBrandList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_category));
            pDialog.show();
            pDialog.setCancelable(false);
            brandNameList = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_brands);
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
            brandNameList = new ArrayList<>();
            if(pDialog.isShowing()) pDialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            brandNameList.add(new BrandList((JSONObject) jsonArray.get(i)));
                        }
                        List<String> brString = new ArrayList<>();
                        for (BrandList br : brandNameList) {
                            brString.add(String.valueOf(br.getName()));
                        }
                        if (brString.size() > 0) {
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                                    R.layout.catlist_item, brString);
                            brand.setAdapter(spinnerAdapter);
                        }
                        if (language=="") {
                            language="EN";
                            if (App.isOnline(context))
                                new GetCategories().execute(language);
                        }else {
                            if (App.isOnline(context))
                                new GetCategories().execute(language);
                        }
                    }
                } catch (JSONException e) {
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
            }
        }
    }

    private class GetConditionList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_category));
            pDialog.show();
            pDialog.setCancelable(false);
            conditionNameList = new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_conditions);
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
            conditionNameList = new ArrayList<>();
            if(pDialog.isShowing()) pDialog.dismiss();
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    boolean error = jsonObject.getBoolean("error");
                    if (!error) {
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            conditionNameList.add(new ConditionList((JSONObject) jsonArray.get(i)));
                        }
                        List<String> brString = new ArrayList<>();
                        for (ConditionList br : conditionNameList) {
                            brString.add(String.valueOf(br.getLabel()));
                        }
                        if (brString.size() > 0) {
                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context,
                                    R.layout.catlist_item, brString);
                            itemCondition.setAdapter(spinnerAdapter);
                        }
                        if (language=="") {
                            language="EN";
                            if (App.isOnline(context))
                                new GetBrandList().execute(en);
                        }else {
                            if (App.isOnline(context))
                                new GetBrandList().execute(en);
                        }
                    }
                } catch (JSONException e) {
                }
            } else {
                Log.e("JSON Data", "Didn't receive any data from server!");
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
        /*float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;*/
    /*    if (actualHeight > maxHeight || actualWidth > maxWidth) {
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
    public void addImageToUpload(File oldfile, final int selectedImage) {


        if (oldfile != null) {

            // showProgress(getString(R.string.wait_image_uploading));
            String newFilePath = compressImage(oldfile.getAbsolutePath());
            if (!newFilePath.isEmpty()) {
                oldfile = new File(newFilePath);
            }
            final File file = oldfile;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            switch (selectedImage) {
                case 1:
                    prod_image1.setImageBitmap(bitmap);
                    image1 = convertBitmapToByte(bitmap);
                    break;
                case 2:
                    prod_image2.setImageBitmap(bitmap);
                    image2 = convertBitmapToByte(bitmap);
                    break;
                case 3:
                    prod_image3.setImageBitmap(bitmap);
                    image3 = convertBitmapToByte(bitmap);
                    break;
                case 4:
                    prod_image4.setImageBitmap(bitmap);
                    image4 = convertBitmapToByte(bitmap);
                    break;
                case 5:
                    prod_image5.setImageBitmap(bitmap);
                    image5 = convertBitmapToByte(bitmap);
                    break;
            }
        }
    }
    public void showProductError(String message) {
        showAlert(message, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
    }
    public void onGetProductId(NfcModel nfcModel) {
        hideProgress();
        showProgress(getString(R.string.wait_product_data));
        WCService.getProduct(nfcModel.getProductId(), new WordPressRestResponse<Product>() {
            @Override
            public void onSuccess(Product result) {
                hideProgress();
                boolean isNfcCatProduct = false;
                List<Category> listCategories = result.getCategories();
                if (listCategories != null && listCategories.size() > 0) {
                    for (Category category : listCategories) {
                        if (String.valueOf(category.getId()).equals(MyConstants.NFC_TAG_CAT_ID)) {
                            isNfcCatProduct = true;
                        }

                    }
                }
                if (!isNfcCatProduct) {
                    boolean userAttribFound = false;
                    if (result.getAttributes() != null) {
                        for (Attribute attribute : result.getAttributes()) {
                            if (String.valueOf(attribute.getId()).equals(MyConstants.ATTRIBUTE_CUST_ID)) {
                                userAttribFound = true;
                                if (attribute.getOptions().length > 0) {
                                    if (!String.valueOf(attribute.getOptions()[0]).equals(loginAccountHelper.getUserId())) {
                                        showProductError(getString(R.string.unauthroized_edit_product));
                                        return;
                                    }
                                } else {
                                    showProductError(getString(R.string.error_no_domestic_customer_found));
                                    return;
                                }
                            }
                        }
                        if (userAttribFound == false) {
                            showProductError(getString(R.string.error_product_data_empty));
                            return;
                        }
                    } else {
                        showProductError();
                        return;
                    }
                }
                product = result;
                oldProduct = product;
                prod_name.setText(product.getName());
                List<Category> categoryList = product.getCategories();
                for (Category category : categoryList) {
                }
                prod_price.setText(product.getRegular_price());
                prod_weight.setText(product.getWeight());
                prod_desc.setText(removeHtmlTags(product.getDescription()));
            }
            @Override
            public void onFailure(HttpServerErrorResponse errorResponse) {
                hideProgress();
                showHttpError(errorResponse);
            }
        });
    }
    @OnClick(R.id.btnAddProduct)
    public void onClickBtnAddProduct(View view) {
        sale();
        getDataFromEditText();
    }
    public void getDataFromEditText() {

        GetBrandname = brand.getText().toString().trim();
        Getname = prod_name.getText().toString().trim();
        Getcategory = prod_category.getText().toString().trim();
        Getsubcategory = prod_sub_category.getText().toString().trim();
        Getdescription = prod_desc.getText().toString().trim();
        Getweight = prod_weight.getText().toString().trim();
        Getprice = prod_price.getText().toString().trim();
        Getquantity = prod_qty.getText().toString().trim();
        GetBarcode = barcodeResultT.getText().toString().trim();
        GetAddress = pr_address.getText().toString().trim();
        GetWebsite=website.getText().toString().trim();
    }
    @OnClick(R.id.btn_cancel)
    public void onClickBtnCancelProduct(View view) {
        Intent i = new Intent(ProductSaveActivity.this, ProductListActivity.class);
        startActivity(i);
        finish();
    }

    public boolean validateProductOnline() {
        String var_prod_name = prod_name.getText().toString();
        String var_brand_name = brand.getText().toString();
        String var_prod_condition = itemCondition.getText().toString();
        String var_prod_category = prod_category.getText().toString();
        String var_prod_sub_category = prod_sub_category.getText().toString();
        String var_prod_desc = prod_desc.getText().toString();
        String var_barcode = barcodeResultT.getText().toString();
        String var_address = pr_address.getText().toString();
         boolean validate=true;
        if (var_prod_name.isEmpty()) {
            prod_name.setError(getString(R.string.validate_prod_name));
            validate= false;
        }
        if (var_brand_name.isEmpty()) {
            brand.setError(getString(R.string.validate_brand));
            validate= false;
        }
        if (var_prod_condition.isEmpty()) {
            itemCondition.setError(getString(R.string.validate_prod_condition));
            validate= false;
        }
        if (var_prod_category.isEmpty()) {
            prod_category.setError(getString(R.string.validate_prod_category));
            validate= false;
        }
        if (var_prod_sub_category.isEmpty()) {
            prod_sub_category.setError(getString(R.string.validate_prod_sub_category));
            validate= false;
        }
        if (var_prod_desc.isEmpty()) {
            prod_desc.setError(getString(R.string.validate_product_desc));
            validate= false;
        }
       /* if (var_barcode.isEmpty()) {
            barcodeResultT.setError(getString(R.string.validate_barcode));
            validate= false;
        }*/
        if (var_address.isEmpty()) {
            pr_address.setError(getString(R.string.validate_address));
            validate= false;
        }
        if(!prod_price.getText().toString().equals(""))
        {
            if(Double.parseDouble(prod_price.getText().toString())==0) {
                prod_price.setError(getString(R.string.validate_price));
                validate = false;
            }
        }
        else {
            prod_price.setError(getString(R.string.validate_price));
            validate= false;
        }
        if(!prod_qty.getText().toString().equals(""))
        {
            if(Double.parseDouble(prod_qty.getText().toString())==0) {
                prod_qty.setError(getString(R.string.validate_quantity1));
                validate = false;
            }
        }
        else {
            prod_qty.setError(getString(R.string.validate_quantity1));
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
    public boolean validateProductOffline() {
        String var_prod_name = prod_name.getText().toString();
        String var_brand_name = brand.getText().toString();
        String var_prod_condition = itemCondition.getText().toString();
        String var_prod_category = prod_category.getText().toString();
        String var_prod_sub_category = prod_sub_category.getText().toString();
        String var_prod_desc = prod_desc.getText().toString();
        String var_barcode = barcodeResultT.getText().toString();
        String var_address = pr_address.getText().toString();
        boolean validate=true;
        if (var_prod_name.isEmpty()) {
            prod_name.setError(getString(R.string.validate_prod_name));
            validate= false;
        }
        if (var_brand_name.isEmpty()) {
            brand.setError(getString(R.string.validate_brand));
            validate= false;
        }
        if (var_prod_condition.isEmpty()) {
            itemCondition.setError(getString(R.string.validate_prod_condition));
            validate= false;
        }
        if (var_prod_category.isEmpty()) {
            prod_category.setError(getString(R.string.validate_prod_category));
            validate= false;
        }
        if (var_prod_sub_category.isEmpty()) {
            prod_sub_category.setError(getString(R.string.validate_prod_sub_category));
            validate= false;
        }
        if (var_prod_desc.isEmpty()) {
            prod_desc.setError(getString(R.string.validate_product_desc));
            validate= false;
        }
        if (var_barcode.isEmpty()) {
            barcodeResultT.setError(getString(R.string.validate_barcode));
            validate= false;
        }
        if (var_address.isEmpty()) {
            pr_address.setError(getString(R.string.validate_address));
            validate= false;
        }
        if(!prod_qty.getText().toString().equals(""))
        {
            if(Double.parseDouble(prod_qty.getText().toString())==0) {
                prod_qty.setError(getString(R.string.validate_quantity1));
                validate = false;
            }
        }
        else {
            prod_qty.setError(getString(R.string.validate_quantity1));
            validate= false;
        }
        return validate;
    }
    public void showProductError() {
        showAlert(getString(R.string.product_category_load_error), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
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
                    showToast(getString(R.string.permission_denied));
                }
            });
        }
    }
    @Override
    public void onPicCapturedSuccess(File file) {
        addImageToUpload(file, selectedImage);
    }
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ProductEdit Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }
    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
 /*   public  void AddLocation(View view){
        *//*String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
        if (App.isOnline(context))
        new getWholeAddress().execute(sellerId);*//*
    }*/

    private void startTimer(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                      if(count>10)
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
    private void populateSpinner() {
        List<String> lables = new ArrayList<String>();
        prod_category.setText("");
        for (int i = 0; i < categoriesList.size(); i++) {
            lables.add(categoriesList.get(i).getName());
        }
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
             R.layout.catlist_item, lables);
      prod_category.setAdapter(spinnerAdapter);
        if(lables != null) {
            prod_category.setEnabled(true);
        }
        else {
            prod_category.setEnabled(false);
        }
    }
    private class GetCategories extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.load_category));
            pDialog.show();
            pDialog.setCancelable(false);
            categoriesList=new ArrayList<>();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_seller_categories);
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
            categoriesList=new ArrayList<>();
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

    class getWholeAddress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.get_location));
            pDialog.show();
            pDialog.setCancelable(false);
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.get_seller_store_address);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id", params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
               // httpPost.setEntity(httpiEntity);
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
            if (pDialog.isShowing()) pDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONObject object = jsonObject.getJSONObject("message");
                    {
                        if (object != null) {
                            s_address = object.getString("street");
                            city = object.getString("city");
                            state = object.getString("state");
                            postal = object.getString("postcode");
                            country = object.getString("country_id");
                            latitude = object.getString("geo_latitude");
                            longitude = object.getString("geo_longitude");
                            pr_address.setText(s_address + " " + city + ", " + state + ", " + postal + ", " + country + ", " + latitude + ", " + longitude);
                           // Toast.makeText(context, getString(R.string.toast_seller_location_msg), Toast.LENGTH_SHORT).show();
                        }
                    }
                }else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                    Toast.makeText(context, getString(R.string.error), Toast.LENGTH_LONG).show();
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
    public void sendDataToServer(final String sellerId, final String Brandname, final String itemConditionText, final String Name, final String Subcategory, final String Description, final String Weight,
                                 final String Price, final String Quantity, final String Barcode, final String Website, final String address,
                                 final byte[] image1, final byte[] image2, final byte[] image3, final byte[] image4, final byte[] image5, final  int mode){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.setMessage(getString(R.string.upload_product_details));
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String return_text="";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.accumulate("seller_id",sellerId);
                    jsonObject.accumulate("brand",Brandname);
                    jsonObject.accumulate("condition",itemConditionText);
                    jsonObject.accumulate("name",Name);
                    jsonObject.accumulate("category",Subcategory);
                    jsonObject.accumulate("description",Description);
                    jsonObject.accumulate("weight",Weight);
                    jsonObject.accumulate("price",Price);
                    jsonObject.accumulate("qty",Quantity);
                    jsonObject.accumulate("barcode",Barcode);
                    jsonObject.accumulate("website",Website);
                    jsonObject.accumulate("address",address);
                    jsonObject.accumulate("mode",mode);
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
                    HttpPost httpPost = new HttpPost(Urls.add_products);
                    StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                    httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                   // httpPost.setEntity(httpiEntity);
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
                if(pDialog.isShowing()) pDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean response=jsonObject.getBoolean("message");
                    if(response)
                    {
                        Toast.makeText(ProductSaveActivity.this, getString(R.string.toast_product_save), Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(ProductSaveActivity.this, ProductListActivity.class);
                        startActivity(iSave);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(ProductSaveActivity.this, getString(R.string.toast_product_not_save), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
            if(App.isOnline(context))
            sendPostReqAsyncTask.execute();
        }catch (Exception e){}
    }
    public void sale(){
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductSaveActivity.this);
        LayoutInflater layoutInflater = LayoutInflater.from(ProductSaveActivity.this);
        View eulaLayout = layoutInflater.inflate(R.layout.alert_sale_product, null);
        checkOnline=(RadioButton)eulaLayout.findViewById(R.id.checkOnline);
        checkOffline=(RadioButton)eulaLayout.findViewById(R.id.checkOffline);
        alertDialog.setView(eulaLayout);
        //alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkOnline.isChecked()) {
                    checkOffline.setVisibility(View.INVISIBLE);
                    String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
                    if (validateProductOnline()) {
                        sendDataToServer(sellerId, String.valueOf(brandId), String.valueOf(conditionId),  Getname, String.valueOf(subCatId), Getdescription, Getweight, Getprice, Getquantity,
                                 GetBarcode, GetWebsite, GetAddress, image1, image2, image3, image4, image5, 237);
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_sale_online), Toast.LENGTH_LONG).show();
                }
                else if (checkOffline.isChecked()) {
                    checkOnline.setVisibility(View.INVISIBLE);
                    String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
                    if (validateProductOffline()) {
                        sendDataToServer(sellerId, String.valueOf(brandId), String.valueOf(conditionId),  Getname, String.valueOf(subCatId), Getdescription, Getweight, Getprice, Getquantity,
                                 GetBarcode, GetWebsite, GetAddress, image1, image2, image3, image4, image5, 238);
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_sale_offline), Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.create().show();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* Intent iBack=new Intent(this, ProductListActivity.class);
        startActivity(iBack);*/
        finish();
    }

}
