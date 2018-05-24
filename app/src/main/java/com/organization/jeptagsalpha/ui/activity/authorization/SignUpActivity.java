package com.organization.jeptagsalpha.ui.activity.authorization;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignUpActivity extends BaseActivity {

    TextView txtSignin;

/*    @Bind(R.id.first_name_edittext)
    MyEditTextLight firstname;

    @Bind(R.id.middle_name_edittext)
    MyEditTextLight middlename;

    @Bind(R.id.last_name_edittext)
    MyEditTextLight lastname;


    @Bind(R.id.email_edittext)
    MyEditTextLight email;

*//*    @Bind(R.id.store_name_edittext)
    MyEditTextLight storename;

    @Bind(R.id.store_id_edittext)
    MyEditTextLight store_id;*//*


    @Bind(R.id.password_edittext)
    MyEditTextLight password;

    @Bind(R.id.repassword_edittext)
    MyEditTextLight repassword;*/

    private String selectedCountry = "";
    String  GetFirstname,  GetMiddlename, GetLastname, GetEmail, GetStorename, GetStoreId, GetPassword;
    ProgressDialog pDialog;
    EditText firstname, middlename, lastname, email, password, repassword;
    Context context;
    String message;
    public static void start(Context context) {
        Intent intent = new Intent(context, SignUpActivity.class);
        context.startActivity(intent);
    }
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        context = this;
        pDialog = new ProgressDialog(SignUpActivity.this);
        pDialog.setCancelable(false);
        ButterKnife.bind(this);
        txtSignin=(TextView)findViewById(R.id.txtSignin);
        firstname=(EditText) findViewById(R.id.first_name_edittext);
        middlename=(EditText) findViewById(R.id.middle_name_edittext);
        lastname=(EditText) findViewById(R.id.last_name_edittext);
        email=(EditText) findViewById(R.id.email_edittext);
        password=(EditText) findViewById(R.id.password_edittext);
        repassword=(EditText) findViewById(R.id.repassword_edittext);
        txtSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }

   public boolean validation(){
       String var_firstname = firstname.getText().toString();
       String var_lastname = lastname.getText().toString();
       String var_email = email.getText().toString();
     //  String var_storename = storename.getText().toString();
       String var_password = password.getText().toString();
       String var_repassword = repassword.getText().toString();
       boolean validate=true;
       if(var_firstname.isEmpty())
       {
           firstname.setError(getString(R.string.validate_firstname));
           validate= false;
       }
       if(var_lastname.isEmpty())
       {
           lastname.setError(getString(R.string.validate_lastname));
           validate= false;
       }
/*       if(var_storename.isEmpty())
       {
           storename.setError(getString(R.string.validate_storename));
           validate= false;
       }*/
       if(var_email.isEmpty())
       {
           email.setError(getString(R.string.validate_email));
           validate= false;
       }
       if(var_password.isEmpty())
       {
           password.setError(getString(R.string.validation_password));
           validate= false;
       }
       if(var_repassword.isEmpty())
       {
           repassword.setError(getString(R.string.validation_repassword));
           validate= false;
       }
       if(!var_password.equals(var_repassword))
       {
           repassword.setError(getString(R.string.validate_repassword_match));
           validate= false;
       }
       return validate;
   }
    @OnClick(R.id.signup)
    public void onClickSignup(View view)
    {
        register();

    }
    void  register() {
        getDataFromEditText();
        if (validation()) {
            if (App.isOnline(context))
                sendDataToServer(GetFirstname, GetMiddlename, GetLastname, GetEmail, GetPassword);
        }
    }
    public void getDataFromEditText() {
        GetFirstname = firstname.getText().toString().trim();
        GetMiddlename = middlename.getText().toString().trim();
        GetLastname = lastname.getText().toString().trim();
        GetEmail = email.getText().toString().trim();
      //  GetStorename = storename.getText().toString().trim();
      //  GetStoreId = store_id.getText().toString().trim();
        GetPassword = password.getText().toString().trim();
    }
    public void sendDataToServer(final String f_name, final String m_name, final String l_name, final String email,
                                 final String password){
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.setMessage(getString(R.string.registering));
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String return_text="";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.accumulate("firstname",f_name);
                    jsonObject.accumulate("middlename",m_name);
                    jsonObject.accumulate("lastname",l_name);
                    jsonObject.accumulate("email",email);
                  //  jsonObject.accumulate("storename",store_name);
                  //  jsonObject.accumulate("storeid",store_id);
                    jsonObject.accumulate("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Urls.register_seller);
                    StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                    httpPost.setEntity(new StringEntity(jsonObject.toString(), "UTF-8"));
                    //httpPost.setEntity(httpiEntity);
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
                    if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                        message=jsonObject.getString("message");
                            if (message != null) {
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                                Intent iSave = new Intent(SignUpActivity.this, LoginActivity.class);
                                startActivity(iSave);
                                finish();
                            }
                    }
                    else if (jsonObject.getString("error").equalsIgnoreCase("true")) {
                         message = jsonObject.getString("message");
                        if (message != null) {
                            {
                                Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_LONG).show();
                            }
                        }
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
}
