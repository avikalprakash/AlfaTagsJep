package com.organization.jeptagsalpha.ui.activity.authorization;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.MyEditText;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    ProgressDialog pDialog;
    String email="";
    String otp1="";
    String otp2="";
    String password="";
    MyEditTextLight emailEt, pass_et, pass_confirm_et;
    BlueButton reset_password, update;
    Timer timer1;
    TimerTask timerTask;
    int count=0;
    Context context;

    RelativeLayout relativeLayoutEmail, relativeLayoutPassword, relativeIcon;
    public static void start(Context context) {
        Intent intent = new Intent(context, ForgotPassword.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        emailEt=(MyEditTextLight)findViewById(R.id.emailEt);
        pass_et=(MyEditTextLight)findViewById(R.id.pass_et);
        pass_confirm_et=(MyEditTextLight)findViewById(R.id.pass_confirm_et);
        relativeLayoutEmail=(RelativeLayout)findViewById(R.id.relativeLayoutEmail);
        relativeLayoutPassword=(RelativeLayout)findViewById(R.id.relativeLayoutPassword);
        relativeIcon=(RelativeLayout)findViewById(R.id.relativeIcon);
        reset_password=(BlueButton)findViewById(R.id.reset_password);
        update=(BlueButton)findViewById(R.id.update);
        reset_password.setOnClickListener(this);
        update.setOnClickListener(this);
        pDialog=new ProgressDialog(ForgotPassword.this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.reset_password:
                email=emailEt.getText().toString();
                if (email.isEmpty()) {
                    emailEt.setError(getString(R.string.toast_login_username));
                    return;
                }
                new SendEmail().execute(email);
                break;
            case R.id.update:
                password = pass_et.getText().toString().trim();
                if (validation()) {
                    new updatePassword().execute(email, password);
                }
                break;
        }
    }
    class SendEmail extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.check_email));
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.forgot_password_seller);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("email", params[0]);
                StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                httpPost.setEntity(httpiEntity);
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
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    JSONObject object1 = jsonObject.getJSONObject("message");
                    {
                        if (object1 !=null) {
                            Toast.makeText(ForgotPassword.this, getString(R.string.email_message), Toast.LENGTH_SHORT).show();
                            otp2= object1.getString("verification_code");
                            startTimer();
                        }
                    }
                }else {

                    Toast.makeText(getApplicationContext(), getString(R.string.email_error), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void startTimer(){
        timer1 = new Timer();
        timerTask = new TimerTask() {
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        count++;
                        if(count>5)
                        {
                            timer1.cancel();
                            OTP();
                        }
                    }
                });
            }
        };
        timer1.schedule(timerTask, 1000, 1000);
    }
    public void OTP(){
        final Dialog dialog = new Dialog(ForgotPassword.this);
        LayoutInflater inflater = LayoutInflater.from(ForgotPassword.this);
        View subView = inflater.inflate(R.layout.alert_otp, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(subView);
       // dialog.setTitle("Title...");
        Button dialogButton = (Button) dialog.findViewById(R.id.submit);
        final MyEditText otp=(MyEditText)dialog.findViewById(R.id.otp);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp1=otp.getText().toString();
                if (otp1.isEmpty()) {
                    otp.setError(getString(R.string.otp));
                    return;
                }
                if (otp1.equals(otp2)){
                    Toast.makeText(getApplicationContext(), getString(R.string.otp_match), Toast.LENGTH_LONG).show();
                    relativeLayoutEmail.setVisibility(View.INVISIBLE);
                    relativeLayoutPassword.setVisibility(View.VISIBLE);
                    relativeIcon.setVisibility(View.VISIBLE);
                    dialog.dismiss();
                }else {
                   Toast.makeText(getApplicationContext(), getString(R.string.otp_error), Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }


        class updatePassword extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                pDialog.setMessage("Updating password...");
                pDialog.show();
            }
            @Override
            protected String doInBackground(String... params) {
                String return_text="";
                JSONObject jsonObject=new JSONObject();
                try {
                    jsonObject.accumulate("email",email);
                    jsonObject.accumulate("password",password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(Urls.update_password_seller);
                    StringEntity httpiEntity=new StringEntity(jsonObject.toString());
                    httpPost.setEntity(httpiEntity);
                    HttpResponse response = httpClient.execute(httpPost);
                    return_text= readResponse(response);
                    return return_text;
                } catch (ClientProtocolException e) {

                } catch (IOException e) {
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
                super.onPostExecute(result);
                if(pDialog.isShowing()) pDialog.dismiss();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    boolean response=jsonObject.getBoolean("message");
                    if(response)
                    {
                        Toast.makeText(ForgotPassword.this, getString(R.string.pass_success), Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(ForgotPassword.this, LoginActivity.class);
                        startActivity(iSave);
                        finish();

                    }
                    else
                    {
                        Toast.makeText(ForgotPassword.this, getString(R.string.pass_error), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    public boolean validation(){
        String var_password = pass_et.getText().toString();
        String var_repassword = pass_confirm_et.getText().toString();
        boolean validate=true;
        if(var_password.isEmpty())
        {
            pass_et.setError(getString(R.string.validation_password));
            validate= false;
        }
        if(var_repassword.isEmpty())
        {
            pass_confirm_et.setError(getString(R.string.validation_repassword));
            validate= false;
        }
        if(!var_password.equals(var_repassword))
        {
            pass_confirm_et.setError(getString(R.string.validate_repassword_match));
            validate= false;
        }
        return validate;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i= new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
}
