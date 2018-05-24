package com.organization.jeptagsalpha.ui.activity.product;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Fujitsu on 03-02-2018.
 */

public class AddJepTags extends AppCompatActivity implements View.OnClickListener, JepTagsFragment.OnListFragmentInteractionListener {
    ImageView imageView;
    String sellerId="";
    String productQuantity="";
    String productId="";
    String tagId="";
    String productUrl="";
    String productName="";
    String image;
    ProgressDialog progressDialog;
    Button add, exit, stop;
    JepTagsFragment jepTagsFragment;
    private NfcAdapter mNfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter[] mIntentFilters;
    private String[][] mTechLists;
    public static List<JepTagList> jepTagNFCList;
    public static List<JepTagList> jepTagList;
    AddJepTags addJepTag;
    int listCount;
    TextView quantity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_jeptag_layout);
        imageView=(ImageView)findViewById(R.id.prod_image);
        progressDialog=new ProgressDialog(this);
        add=(Button)findViewById(R.id.add);
        exit=(Button)findViewById(R.id.exit);
        stop=(Button)findViewById(R.id.stop);
        quantity=(TextView) findViewById(R.id.prod_qty);
        add.setOnClickListener(this);
        stop.setOnClickListener(this);
        exit.setOnClickListener(this);
        try {
            productId = getIntent().getStringExtra("pr_id");
            productName = getIntent().getStringExtra("pr_name");
            productUrl = getIntent().getStringExtra("pr_url");
            productQuantity = getIntent().getStringExtra("pr_qty");
            image = getIntent().getStringExtra("image");
            Picasso.with(ProductListActivity.context).load(image).into(imageView);

        }catch (Exception e){}



       new LoadJepTag().execute(productId);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:

                break;

            case R.id.stop:

                break;

            case R.id.exit:
              finish();
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(JepTagList item) {

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
                                add.setEnabled(false);
                                stop.setEnabled(false);
                             //   dialog.dismiss();
                            }else {
                                add.setEnabled(true);
                                stop.setEnabled(true);
                            }
                        }
                    }
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
}
