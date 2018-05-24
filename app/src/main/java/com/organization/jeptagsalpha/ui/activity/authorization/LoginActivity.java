package com.organization.jeptagsalpha.ui.activity.authorization;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.App;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.base.BaseActivity;
import com.organization.jeptagsalpha.ui.activity.main.LanguageDataBase;
import com.organization.jeptagsalpha.ui.activity.main.MainActivity;
import com.organization.jeptagsalpha.ui.activity.main.ReadLanguage;
import com.organization.jeptagsalpha.ui.activity.product.AddSellerAddress;
import com.organization.jeptagsalpha.ui.controls.BlueButton;
import com.organization.jeptagsalpha.ui.controls.MyEditTextLight;
import com.organization.jeptagsalpha.ui.controls.Spinner.MySpinnerLight;
import com.organization.jeptagsalpha.wordpress.util.BasePreferenceHelper;
import com.organization.jeptagsalpha.wordpress.util.LoginAccountHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    EditText etEmail;
    EditText etPassword;
    Button login;
    private CheckBox saveLoginCheckBox;
    private Boolean saveLogin;
    public LoginAccountHelper loginAccountHelper;
    ImageView signinback;
    Context context;
    String username;
    String password;
    String seller_id;
    String log="";
    ProgressDialog pDialog;
    MySpinnerLight lang;
    Dialog dialog;
    TextView forgot_password;
    SharedPreferences prefs ;
    private static final String PREFS_NAME = "name";
    String[] language = {"EN","VI"};
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
    @OnClick(R.id.signup)
    public void OnClickSignBack(View view)
    {
        SignUpActivity.start(this);
        finish();
    }
   /* @OnClick(R.id.forgot_password)
    public void OnClickForgotPass(View view)
    {
        ForgotPassword.start(this);
        finish();
    }*/
    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fill_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        context = this;
        ButterKnife.bind(this);
        loginAccountHelper = new LoginAccountHelper(this);
        etEmail = (EditText) findViewById(R.id.user_edittext);
        etPassword = (EditText) findViewById(R.id.password_edittext);
        login = (Button) findViewById(R.id.signin);
        forgot_password=(TextView) findViewById(R.id.forgot_password);
        saveLoginCheckBox = (CheckBox) findViewById(R.id.saveLoginCheckBox);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), ForgotPassword.class));
            }
        });
        login.setOnClickListener(this);
        pDialog = new ProgressDialog(LoginActivity.this);
        pDialog.setCancelable(false);
        UserLoginDetails userLoginDetails = BasePreferenceHelper.getLoginDetails(context);
        saveLogin = userLoginDetails.isSavelogin();
        if (saveLogin == true) {
            etEmail.setText(userLoginDetails.getEmail());
            etPassword.setText(userLoginDetails.getPassword());
            seller_id = userLoginDetails.getEntity_id();
            saveLoginCheckBox.setChecked(true);
        }

    }
    @Override
    public void onClick(View v) {
        if (v == login) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(etEmail.getWindowToken(), 0);
            username = etEmail.getText().toString();
            password = etPassword.getText().toString();

            if (username.isEmpty() && password.isEmpty()) {
                if (username.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_login_username), Toast.LENGTH_LONG).show();
                    return;
                }
                if (password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_login_pass), Toast.LENGTH_LONG).show();
                    return;
                }
                Toast.makeText(getApplicationContext(), getString(R.string.toast_login_userpass), Toast.LENGTH_LONG).show();
            }
            if (App.isOnline(context))
                new AsyncLogin().execute(username, password);
        }
    }
    private class AsyncLogin extends AsyncTask<String, String, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LoginActivity.this);
        HttpURLConnection conn;
        URL url = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
          //  pdLoading.setMessage(getString(R.string.loading));
          //  pdLoading.setCancelable(false);
          //  pdLoading.show();
            dialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text="";
            try{
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.login_seller);
                JSONObject jsonObject=new JSONObject();
                Log.d("params====",""+params.toString());
                jsonObject.accumulate("email",params[0]);
                jsonObject.accumulate("password",params[1]);
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
            Log.d("result===",""+result);
           // pdLoading.dismiss();
            dialog.dismiss();
            try{
                JSONObject jsonObject=new JSONObject(result);
                if(jsonObject.getString("error").equalsIgnoreCase("false"))
                {
                   JSONObject object=jsonObject.getJSONObject("message");
                    if (saveLoginCheckBox.isChecked()) saveLogin=true;
                    else  saveLogin=false;
                        if(object!=null)
                        {
                            String entity_id=object.getString("entity_id");
                            String firstname=object.getString("firstname");
                            String lastname=object.getString("lastname");
                            String email=object.getString("email");
                            UserLoginDetails userLoginDetails=
                                    new UserLoginDetails(entity_id,firstname,lastname,email,password,saveLogin);
                            BasePreferenceHelper.setLoginDetails(context,userLoginDetails);
                        }

                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_success), Toast.LENGTH_LONG).show();
                   // showCustomAlert("Login Successfully");
                    String sellerId = BasePreferenceHelper.getLoginDetails(context).getEntity_id();
                   new CheckAddress().execute(sellerId);
                }else if (jsonObject.getString("error").equalsIgnoreCase("true")){
                    Toast.makeText(LoginActivity.this, getString(R.string.toast_login_unsuccess), Toast.LENGTH_LONG).show();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void language(View view)
    {

        onchooselang();
    }
    private String getResString(int resId){
        return getResources().getString(resId);
    }

    public void onchooselang()
    {

        try {
            final String s[] = {getResString(R.string.en), getResString(R.string.vi)};
            AlertDialog.Builder adb = new AlertDialog.Builder(LoginActivity.this);
            //adb.setView(R.layout.layout_language2);
            adb.setTitle(R.string.txt_setting_changeyourlocale);
            final String lan = ReadLanguage.read(LoginActivity.this);
            adb.setAdapter(new ArrayAdapter(LoginActivity.this, R.layout.language_layout, s), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref = context.getSharedPreferences("MyPref", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    switch (which) {
                        case -1:
                            log = "EN";
                            editor.putString("log", log);
                            editor.commit();
                            break;
                        case 0:
                            log = "EN";
                            editor.putString("log", log);
                          editor.commit();
                            break;
                        case 1:
                            log = "VI";
                            editor.putString("log", log);
                            editor.commit();
                            break;
                        default:

                            break;
                    }

                    if (!log.equals(lan)) {
                        showRestartConfirmDlg(log);
                    }
                }
            });
            adb.show();
        }
        catch (Exception e) {}
    }

    public void showRestartConfirmDlg(final String log)
    {
      // final AlertDialog.Builder adb=new AlertDialog.Builder(LoginActivity.this);
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            adb.setView(R.layout.layout_language);
        }else {
             adb.setTitle("Language is changing");
              adb.setMessage(getResources().getString(R.string.msg_change_locale));
        }
        adb.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeLocale(log);
            }
        });
        adb.setNegativeButton("No",null);
        adb.show();
    }

    private void changeLocale(String language_code){
        Resources res = getResources();
        // Change locale settings in the app.
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.locale = new Locale(language_code.toLowerCase());
        res.updateConfiguration(conf, dm);

        LanguageDataBase languageDatabase=new LanguageDataBase(LoginActivity.this);
        languageDatabase.open();
        languageDatabase.update(language_code);
        languageDatabase.close();
        restartApp();
    }

    private void restartApp(){
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    class CheckAddress extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage(getString(R.string.checking));
            pDialog.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String return_text = "";
            try {
                HttpClient httpClient=new DefaultHttpClient();
                HttpPost httpPost=new HttpPost(Urls.check_seller_default_address);
                JSONObject jsonObject=new JSONObject();
                jsonObject.accumulate("seller_id", params[0]);
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
            Log.d("result===", "" + result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getString("error").equalsIgnoreCase("false")) {
                    boolean bCheckAddress = jsonObject.getBoolean("message");
                    {
                        if (bCheckAddress) {
                            Intent i1=new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i1);
                            finish();
                        }
                    }
                }else {
                            Intent i2 = new Intent(LoginActivity.this, AddSellerAddress.class);
                            startActivity(i2);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public void showCustomAlert(String value)
    {

        Context context = getApplicationContext();
        // Create layout inflator object to inflate toast.xml file
        LayoutInflater inflater = getLayoutInflater();

        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.toast, null);
        TextView textView=(TextView)toastRoot.findViewById(R.id.toastText);
        textView.setText(value);
        Toast toast = new Toast(context);


        // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();

    }
}
