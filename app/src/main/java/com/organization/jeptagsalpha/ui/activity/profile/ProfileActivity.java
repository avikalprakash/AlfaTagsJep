package com.organization.jeptagsalpha.ui.activity.profile;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.UserLoginDetails;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
import com.organization.jeptagsalpha.ui.activity.main.MainActivity;
import com.organization.jeptagsalpha.ui.activity.product.ProductListActivity;
import com.organization.jeptagsalpha.ui.activity.product.ProductSaveActivity;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;
import com.organization.jeptagsalpha.wordpress.model.User;
import com.organization.jeptagsalpha.wordpress.model.wc.Country;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {
        //implements View.OnClickListener {

/*    TextView storeName, product, pending, addProduct, addJeptags, report, support, fullName;
    ImageView back_btn;
*//*    @Bind(R.id.full_name)
    MyEditTextLight fullname;


    @Bind(R.id.email_edittext)
    MyEditTextLight email_id;

    @Bind(R.id.store_name)
    MyEditTextLight store_name;

    @Bind(R.id.street_edittext)
    MyEditTextLight street_edittext;

    @Bind(R.id.city_edittext)
    MyEditTextLight city;

    @Bind(R.id.state_edittext)
    MyEditTextLight state_edittext;

    @Bind(R.id.postalcode_edittext)
    MyEditTextLight postalcode;

    @Bind(R.id.country_spinner)
    MyEditTextLight country_spinner;

    @Bind(R.id.phone_edittext)
    MyEditTextLight phoneno;


    public static Context context;
    private List<Country> countryList;
    BlueButton ok;

*//*
     public static Context context;
    public static void start(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        storeName=(TextView)findViewById(R.id.company_name);
        product=(TextView)findViewById(R.id.product);
        pending=(TextView)findViewById(R.id.pending);
        addProduct=(TextView)findViewById(R.id.addProduct);
        addJeptags=(TextView)findViewById(R.id.addJeptags);
        report=(TextView)findViewById(R.id.report);
        support=(TextView)findViewById(R.id.support);
        back_btn=(ImageView)findViewById(R.id.back_btn);
        fullName=(TextView)findViewById(R.id.full_name);
        context = this;
        product.setOnClickListener(this);
        pending.setOnClickListener(this);
        addProduct.setOnClickListener(this);
        addJeptags.setOnClickListener(this);
        report.setOnClickListener(this);
        support.setOnClickListener(this);
        back_btn.setOnClickListener(this);
      *//*  TextView textStoreAdd = (TextView)findViewById(R.id.st_add);
        SpannableString content1 = new SpannableString(getString(R.string.store_address));
        content1.setSpan(new UnderlineSpan(), 0, content1.length(), 0);
        textStoreAdd.setText(content1);
        setActionBar(getTitle().toString());
        ok=(BlueButton)findViewById(R.id.ok);
        ok.setOnClickListener(this);
        UserLoginDetails userLoginDetails=BasePreferenceHelper.getLoginDetails(context);
        if(App.isOnline(context))
          //  new GetProfileDetail().execute(userLoginDetails.getEntity_id());
        ButterKnife.bind(this);
        countryList=Country.getCountryList();*//*
        UserLoginDetails userLoginDetails=BasePreferenceHelper.getLoginDetails(context);
        if(App.isOnline(context))
          new GetProfileDetail().execute(userLoginDetails.getEntity_id());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.product:
                ProductListActivity.start(this);
                break;
            case R.id.pending:
            Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.addProduct:
                Intent i = new Intent(ProfileActivity.this, ProductSaveActivity.class);
               startActivity(i);
                finish();
                break;
            case R.id.addJeptags:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.report:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.support:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                break;
            case R.id.back_btn:
                finish();
                break;
        }
    }



 *//*   @Override
    public void onClick(View view) {
        Intent iSave=new Intent(ProfileActivity.this, MainActivity.class);
        finish();
    }*//*


    class GetProfileDetail extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ProfileActivity.this);
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdLoading.setMessage(getString(R.string.loading));
            pdLoading.setCancelable(false);
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost("http://54.67.107.248/jeptags/apirest/edit_sellers");
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id",params[0]);
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
                        String fname = object.getString("fullname");
                        String email = object.getString("email");
                        String storename = object.getString("storename");
                        String street = object.getString("street");
                        String state = object.getString("state");
                        String country = object.getString("country_id");
                        String u_city = object.getString("city");
                        String postal = object.getString("postcode");
                        String phone_no = object.getString("telephone");
                      *//*  fullname.setText(fname);
                        email_id.setText(email);
                        store_name.setText(storename);
                        street_edittext.setText(street);
                        state_edittext.setText(state);
                        country_spinner.setText(country);
                        city.setText(u_city);
                        postalcode.setText(postal);
                        phoneno.setText(phone_no);*//*
                        storeName.setText(storename);
                        fullName.setText(fname);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/
}
