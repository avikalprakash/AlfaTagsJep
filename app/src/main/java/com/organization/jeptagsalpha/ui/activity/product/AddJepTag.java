package com.organization.jeptagsalpha.ui.activity.product;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcV;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;


import be.appfoundry.nfclibrary.tasks.interfaces.AsyncOperationCallback;

public class AddJepTag extends AppCompatActivity  implements JepTagsFragment.OnListFragmentInteractionListener,View.OnClickListener{

    final String READ_TAG="Scanning to read NFC tag....\n Place NFC Tag near to scanning device";
    final String WRITE_TAG="Scanning to write NFC tag....\n Place NFC Tag near to scanning device";

    TextView pr_id,progressBarText, quantity;
    Bundle bundle = null;
    ProgressDialog pDialog;
    Button addTag, btnStopScan, btnEditDetails, btnDeleteProduct, continueBtn;
    int index = 0;
    int listCount;
    CardView progressCardView;
    ProgressBar progressBarRead,progressBarWrite;
    CheckBox checkBox;
    int k;
    String latV;
    String langV;
    TextView scanning;
  //  TextView descriptionP, descriptionM;
    RelativeLayout layout1;
   public static List<JepTagList> jepTagNFCList;
   public static List<JepTagList> jepTagList;

    JepTagsFragment jepTagsFragment;


    String jeptag_id;
    AsyncOperationCallback mAsyncOperationCallback;
    ProgressDialog progressDialog;
    AddJepTag addJepTag;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    String sellerId="";
    String productQuantity="";
    String productId="";
    String tagId="";
    String productUrl="";
    String productName="";
    String image;
    Tag tag;
    Dialog dialog;
    int nfcMode=0;
    ProgressDialog locationProgressDialog;
    LocationManager mlocManager;
    String lat = "";
    String lang = "";
    Timer timer1;
    TimerTask timerTask;
    int count = 0;
    int count1;
    Context context;
    TextView pro_name;
    Location myLocation;
    String msg1;
    AlertDialog.Builder builder;
    private NfcAdapter mAdapter;
    private PendingIntent mPendingIntent;
    public static final String LANGUAGE = "log";
    public static final String MyPref = "MyPref";
    SharedPreferences sharedpreferences;
    String language;
    ImageView imageView;
    String catLang="EN";
    RelativeLayout layout3;
    Button exit;
    TextView prod_name;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_jeptag_activity);
        dialog = new Dialog(this);
        exit=(Button)findViewById(R.id.exit);
        back=(ImageView) findViewById(R.id.back);
        scanning=(TextView)findViewById(R.id.scanning);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        context = this;
        addJepTag=this;
        jepTagNFCList=new ArrayList<>();
        progressDialog=new ProgressDialog(this);

        locationProgressDialog = new ProgressDialog(this);
        locationProgressDialog.setMessage("Getting location\n Please wait");
        continueBtn = (Button) findViewById(R.id.continueBtn);
        addTag = (Button) findViewById(R.id.addTag);
        btnStopScan = (Button) findViewById(R.id.stop);
        layout3 = (RelativeLayout) findViewById(R.id.layout3);
      //  btnEditDetails = (Button) findViewById(R.id.btnEditDetails);
        btnDeleteProduct = (Button) findViewById(R.id.btnDeleteProduct);
        progressCardView= (CardView) findViewById(R.id.progressCardView);
        progressBarText= (TextView) findViewById(R.id.progressBarText);
        progressBarRead=(ProgressBar)findViewById(R.id.progressBarRead);
        progressBarWrite=(ProgressBar)findViewById(R.id.progressBarWrite);
        prod_name=(TextView)findViewById(R.id.prod_name);
        imageView=(ImageView)findViewById(R.id.image);
    /*    descriptionP=(TextView)findViewById(R.id.descriptionP);
        descriptionM=(TextView)findViewById(R.id.descriptionM);
        descriptionP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout3.setVisibility(View.VISIBLE);
                descriptionM.setVisibility(View.VISIBLE);
                descriptionP.setVisibility(View.INVISIBLE);
            }
        });

        descriptionM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout3.setVisibility(View.INVISIBLE);
                descriptionM.setVisibility(View.INVISIBLE);
                descriptionP.setVisibility(View.VISIBLE);
            }
        });*/
        sharedpreferences=getSharedPreferences(MyPref,Context.MODE_PRIVATE);
        language=sharedpreferences.getString(LANGUAGE,"");
        pr_id = (TextView) findViewById(R.id.pr_id);
        quantity = (TextView) findViewById(R.id.quantity);
        pro_name = (TextView) findViewById(R.id.pro_name);
        addTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdapter = NfcAdapter.getDefaultAdapter(AddJepTag.this);
                if (mAdapter == null) {
                    Toast.makeText(AddJepTag.this,"Your device do not support nfc",Toast.LENGTH_LONG).show();
                } else {
                    // getGpsLocation();
                    dialog.show();
                  //  progressBarShowHide(READ_TAG,true,1);
                    nfcMode=1;

                    // btnEditDetails.setEnabled(false);
                    // btnDeleteProduct.setEnabled(false);
                    // btnEditDetails.setEnabled(false);
                   // btnDeleteProduct.setEnabled(false);

                    mPendingIntent = PendingIntent.getActivity(AddJepTag.this, 0, new Intent(AddJepTag.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                }
            }
        });
        btnStopScan.setOnClickListener(this);
        continueBtn.setOnClickListener(this);
       jepTagsFragment=new JepTagsFragment();
        FragmentTransaction fm=getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.listJepTags,jepTagsFragment);
        fm.commitAllowingStateLoss();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        mIntentFilters = new IntentFilter[]{new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)};
        mTechLists = new String[][]{new String[]{Ndef.class.getName()},
                new String[]{NdefFormatable.class.getName()}};

        try {
            productId = getIntent().getStringExtra("pr_id");
            productName = getIntent().getStringExtra("pr_name");
            if (!productName.equals("") && productName!=null){
                prod_name.setText(productName);
            }
            productUrl = getIntent().getStringExtra("pr_url");
            productQuantity = getIntent().getStringExtra("pr_qty");
            image = getIntent().getStringExtra("image");
            pro_name.setText(productName);
            Picasso.with(ProductListActivity.context).load(image).into(imageView);

        }catch (Exception e){}

        pr_id.setText(getString(R.string.prod_id) + productId);
        quantity.setText(getString(R.string.quantity) + productQuantity);
        new LoadJepTag().execute(productId);
        count = Integer.parseInt(productQuantity);
        if (count==0){
            addTag.setEnabled(false);
            dialog.dismiss();
            continueBtn.setEnabled(false);
            addTag.setBackgroundColor(Color.parseColor("#8b8b83"));
            addTag.setTextColor(Color.parseColor("#ffffff"));
            continueBtn.setBackgroundColor(Color.parseColor("#8b8b83"));
            continueBtn.setTextColor(Color.parseColor("#ffffff"));
        }else {
            addTag.setEnabled(true);
            continueBtn.setEnabled(true);
        }
        getGpsLocation();
    }


/*    public void delete(View view) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddJepTag.this);
        LayoutInflater layoutInflater = LayoutInflater.from(AddJepTag.this);
        View eulaLayout = layoutInflater.inflate(R.layout.alert_dialog, null);
        checkBox=(CheckBox)eulaLayout.findViewById(R.id.checkbox);
        alertDialog.setView(eulaLayout);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (checkBox.isChecked()) {
                    new DeleteData().execute(productId);
                }else {
                    alertDialog.setCancelable(false);
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_alert_delete_msg), Toast.LENGTH_LONG).show();
                }
            }
        });
        alertDialog.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.create().show();
    }*/


    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
           case R.id.addTag:
             /*  mAdapter = NfcAdapter.getDefaultAdapter(AddJepTag.this);
               if (mAdapter == null) {
                   Toast.makeText(AddJepTag.this,"Your device do not support nfc",Toast.LENGTH_LONG).show();
               } else {
                  // getGpsLocation();
                   progressBarShowHide(READ_TAG,true,1);
                   nfcMode=1;
                  // btnEditDetails.setEnabled(false);
                  // btnDeleteProduct.setEnabled(false);
                  // btnEditDetails.setEnabled(false);
                   btnDeleteProduct.setEnabled(false);
                   mPendingIntent = PendingIntent.getActivity(AddJepTag.this, 0, new Intent(AddJepTag.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
               }*/
            break;

            case R.id.stop:
                nfcMode=4; // to stop reading
              //  progressBarShowHide("",false,0);
               // btnEditDetails.setEnabled(true);
              //  btnDeleteProduct.setEnabled(true);
                dialog.dismiss();
                scanning.setVisibility(View.INVISIBLE);
                break;

            case R.id.continueBtn:
                scanning.setVisibility(View.VISIBLE);
                mAdapter = NfcAdapter.getDefaultAdapter(AddJepTag.this);
                if (mAdapter == null) {
                    Toast.makeText(AddJepTag.this,"Your device do not support nfc",Toast.LENGTH_LONG).show();
                } else {
                    dialog.show();
                    // getGpsLocation();
                  //  progressBarShowHide(READ_TAG,true,1);
                    nfcMode=1;
                    // btnEditDetails.setEnabled(false);
                    // btnDeleteProduct.setEnabled(false);
                    // btnEditDetails.setEnabled(false);
                    mPendingIntent = PendingIntent.getActivity(AddJepTag.this, 0, new Intent(AddJepTag.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
                }
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(JepTagList item) {

    }

    class  DeleteData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddJepTag.this);
            pDialog.setMessage(getString(R.string.pr_deleting));
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.delete_products);
                JSONObject jsonObject=new JSONObject();
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
            } catch (Exception e){}
            return return_text;
        }
        @Override
        protected void onPostExecute(String result) {
            try{
                if(pDialog.isShowing()) pDialog.dismiss();
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false")) {
                    if (jsonObject.getString("message").equals("true")) ;
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.delete_msg), Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(getApplicationContext(), ProductListActivity.class);
                        startActivity(iSave);
                        finish();
                    }
                }
                else Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
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
        if (count1 < Integer.parseInt(productQuantity)) {
            if (nfcMode == 1 || nfcMode == 2) {
                String action = intent.getAction();
                if (intent != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                        || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                        ) {

                    tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);


                    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                            ) {


                        byte[] id = tag.getId();
                        if (id.length < 8) {
                            JepTagEncode.reverse(id);
                            tagId = JepTagEncode.encodeHexStr(id, id.length).toString();
                        } else tagId = JepTagEncode.encodeHexStr(id).toString();

                        // sending tag id to server for verification and getting Jeptag id.
                        if (nfcMode == 1) {
                            UserLoginDetails userLoginDetails = BasePreferenceHelper.getLoginDetails(context);
                            sellerId = userLoginDetails.getEntity_id();
                            new GetJepTag().execute(sellerId, tagId, productId, lat, lang);
                        }

                        //.............. writing product id , lat & long to tag
                        if (nfcMode == 2) {
                           /* JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.accumulate("id", productId);
                                jsonObject.accumulate("lati", lat);
                                jsonObject.accumulate("longi", lang);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                            // NdefRecord record = NdefRecord.createMime("JepTag", productUrl.getBytes());
                            NdefRecord record = NdefRecord.createMime("application/com.organization.jeptags", productUrl.getBytes());
                            NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                            addJepTag.jepTagNFCList.add(new JepTagList(jeptag_id, getDateString()));
                            if (addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter != null)
                                addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.notifyDataSetChanged();
                            int len=  addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.getItemCount();
                            if (len != 0) {
                                quantity.setText(len + "/" + productQuantity);
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                            }
                            if (len==Integer.parseInt(productQuantity)){
                                addTag.setEnabled(false);
                                dialog.dismiss();
                                continueBtn.setEnabled(false);
                                addTag.setBackgroundColor(Color.parseColor("#8b8b83"));
                                addTag.setTextColor(Color.parseColor("#ffffff"));
                                continueBtn.setBackgroundColor(Color.parseColor("#8b8b83"));
                                continueBtn.setTextColor(Color.parseColor("#ffffff"));
                            }else {
                                addTag.setEnabled(true);
                                continueBtn.setEnabled(true);
                            }
                            /*if (writeTag(message, tag)) {
                                Toast.makeText(context, getString(R.string.tag_written_success), Toast.LENGTH_LONG).show();

                                addJepTag.jepTagNFCList.add(new JepTagList(jeptag_id, getDateString()));
                                if (addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter != null)
                                    addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.notifyDataSetChanged();

                             int len=  addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.getItemCount();
                                //for count jepTag added in list
                               // count1++;

                                if (len != 0) {
                                    pr_qty.setText(len + "/" + productQuantity);
                                    if (progressDialog.isShowing()) progressDialog.dismiss();
                                }
                                if (len==Integer.parseInt(productQuantity)){
                                    btnScanJepTag.setEnabled(false);
                                }else {
                                    btnScanJepTag.setEnabled(true);
                                }
                                //compare listValue and quantity
                                if (len == Integer.parseInt(productQuantity)) {  // pr_qty.setText(count+"/"+productQuantity);
                                    btnScanJepTag.setEnabled(false);
                                    progressBarShowHide("", false, 0);
                                } else


                                {
                                    nfcMode = 1;
                                    progressBarShowHide("", false, 0);
                                    progressBarShowHide(READ_TAG, true, 1);
                                    btnScanJepTag.setEnabled(true);
                                }
                            }*/
                        }
                    }
                }

            }
        }
    }
public void time(){

}
    public boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            NfcV nfcV= NfcV.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag not writable",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    Toast.makeText(getApplicationContext(),
                            "Error: tag too small",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;

            }
            else
            {
                return  false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    class GetJepTag extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(context);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Downloading Jeptag Id...");
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.encoding_verify);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id", params[0]);
                jsonObject.accumulate("tag_id", params[1]);
                jsonObject.accumulate("product_id", params[2]);
                jsonObject.accumulate("latitude", params[3]);
                jsonObject.accumulate("longitude", params[4]);
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
                    JSONArray jsonArray = jsonObject.getJSONArray("message");
                    if(jsonArray.length()>0)
                    {
                        JSONObject jsonObject1=jsonArray.getJSONObject(0);

                        if (jsonObject1 != null) {
                              jeptag_id = jsonObject1.getString("jeptag_id");
                            if(progressDialog.isShowing()) progressDialog.dismiss();
                           // nfcMode=2;

                          //  progressBarShowHide("",false,0);
                          //  progressBarShowHide(WRITE_TAG,true,2);
                            NdefRecord record = NdefRecord.createMime("application/com.organization.jeptags", productUrl.getBytes());
                            NdefMessage message = new NdefMessage(new NdefRecord[]{record});
                            addJepTag.jepTagNFCList.add(new JepTagList(jeptag_id, getDateString()));
                            if (addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter != null)
                                addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.notifyDataSetChanged();
                            int len=  addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.getItemCount();
                            if (len != 0) {
                                quantity.setText(len + "/" + productQuantity);
                                if (progressDialog.isShowing()) progressDialog.dismiss();
                            }
                            if (len==Integer.parseInt(productQuantity)){
                                addTag.setEnabled(false);
                                dialog.dismiss();
                                continueBtn.setEnabled(false);
                              //  progressBarShowHide("", false, 0);
                                addTag.setBackgroundColor(Color.parseColor("#8b8b83"));
                                addTag.setTextColor(Color.parseColor("#ffffff"));
                                continueBtn.setBackgroundColor(Color.parseColor("#8b8b83"));
                                continueBtn.setTextColor(Color.parseColor("#ffffff"));
                            }else {
                                addTag.setEnabled(true);
                                continueBtn.setEnabled(false);
                            }
                        }
                    }

                    k= jsonArray.length();
                }
                else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                    if (jsonObject.getString("type").equalsIgnoreCase("1")) {
                        msg1 = jsonObject.getString("message");
                       // Toast.makeText(context, msg1, Toast.LENGTH_LONG).show();
                        if (progressDialog.isShowing()) progressDialog.dismiss();
                      //  progressBarShowHide("", false, 0);
                        dialog.dismiss();
                        scanning.setVisibility(View.INVISIBLE);
                        jepTagAlertDialog();
                    }else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                        if (jsonObject.getString("type").equalsIgnoreCase("2")) {
                            String msg2 = jsonObject.getString("message");
                            Toast.makeText(context, msg2, Toast.LENGTH_LONG).show();
                            if (progressDialog.isShowing()) progressDialog.dismiss();
                          //  progressBarShowHide("", false, 0);
                            dialog.dismiss();
                            scanning.setVisibility(View.INVISIBLE);
                            btnEditDetails.setEnabled(true);
                            btnDeleteProduct.setEnabled(true);

                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void jepTagAlertDialog(){
        LayoutInflater inflater = LayoutInflater.from(AddJepTag.this);
        View subView = inflater.inflate(R.layout.jeptag_msg, null);
        final TextView addressText = (TextView) subView.findViewById(R.id.full_address);
        addressText.setText(msg1);
        if (progressDialog.isShowing()) progressDialog.dismiss();
         builder = new AlertDialog.Builder(this);
        builder.setView(subView);
        final AlertDialog closedialog= builder.create();

        closedialog.show();

        final Timer timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            public void run() {
                closedialog.dismiss();
                timer2.cancel(); //this will cancel the timer of the system
            }
        }, 5000); // the timer will count 5 seconds....

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
/*    private void progressBarShowHide(String message,boolean flag,int mode)
    {
        progressBarText.setText(message);
        try {
            progressDialog.setCancelable(false);
            if(mode==1)
            {
                progressBarWrite.setVisibility(View.GONE);
                progressBarRead.setVisibility(View.VISIBLE);
            }
            else if(mode==2) {
                progressBarWrite.setVisibility(View.VISIBLE);
                progressBarRead.setVisibility(View.GONE);
            }

            if (flag) progressCardView.setVisibility(View.VISIBLE);
            else progressCardView.setVisibility(View.GONE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent iBack=new Intent(context, ProductListActivity.class);
        startActivity(iBack);
        finish();
    }
    class LoadJepTag extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage(getString(R.string.prog_load_jeptagid));
            progressDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.encoding_get_product_tags);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("product_id", params[0]);
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
            jepTagList=new ArrayList<>();
            if(progressDialog.isShowing()) progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                        JSONArray jsonArray = jsonObject.getJSONArray("message");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            if (jsonObject1 != null) {
                                String jeptag_id = jsonObject1.getString("jeptag_id");
                                String updated_at = jsonObject1.getString("updated_at");
                                //to convert local time
                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                                Date myDate = simpleDateFormat.parse(updated_at);
                                //to remove GMT
                                simpleDateFormat.setTimeZone(TimeZone.getDefault());
                                String formattedDate = simpleDateFormat.format(myDate);
                                addJepTag.jepTagNFCList.add(new JepTagList(jeptag_id, formattedDate));
                                if (addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter != null)
                                    addJepTag.jepTagsFragment.jepTagRecyclerViewAdapter.notifyDataSetChanged();
                                 listCount=jsonArray.length();
                                if (listCount != 0) {
                                    quantity.setText(getString(R.string.quantity)+listCount + "/" + productQuantity);
                                    if (progressDialog.isShowing()) progressDialog.dismiss();
                                }
                                if (listCount==Integer.parseInt(productQuantity)){
                                    addTag.setEnabled(false);
                                    continueBtn.setEnabled(false);
                                    dialog.dismiss();
                                    addTag.setBackgroundColor(Color.parseColor("#8b8b83"));
                                    addTag.setTextColor(Color.parseColor("#ffffff"));
                                    continueBtn.setBackgroundColor(Color.parseColor("#8b8b83"));
                                    continueBtn.setTextColor(Color.parseColor("#ffffff"));
                                }else {
                                    addTag.setEnabled(true);
                                    continueBtn.setEnabled(true);
                                }
                            }
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void Edit(View v) {
        Intent iEdit = new Intent(AddJepTag.this, EditProductDetails.class);
        iEdit.putExtra("product_id", productId);
        iEdit.putExtra("qty", productQuantity);
        iEdit.putExtra("listCount", listCount);
        startActivity(iEdit);
        finish();
    }
    public static String getDateString() {
        SimpleDateFormat postFormater = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");

        String newDateStr = postFormater.format(Calendar.getInstance()
                .getTime());
        return newDateStr;
    }

    private void getGpsLocation() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
           // alert.setTitle("GPS");
           // alert.setMessage("GPS is turned OFF...\nDo U Want Turn On GPS..");
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

            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
                    (float) 0.01, (android.location.LocationListener) listener);
            mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,
                    (float) 0.01, (android.location.LocationListener) listener);
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
                        if(count>10)
                        {
                            if (myLocation==null){
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
                if (location.getAccuracy()>0 && location.getAccuracy()<500) {
                    if(locationProgressDialog.isShowing()) locationProgressDialog.dismiss();
                   // progressBarShowHide(READ_TAG,true,1);
                   // nfcMode=1;
                   // btnEditDetails.setEnabled(false);
                   // btnDeleteProduct.setEnabled(false);
                    lat=lat.replaceAll(",",".");
                    lang=lang.replaceAll(",",".");
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
}