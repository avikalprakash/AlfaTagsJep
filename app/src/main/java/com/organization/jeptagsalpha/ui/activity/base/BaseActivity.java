package com.organization.jeptagsalpha.ui.activity.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Base64;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.MyConstants;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.LoginActivity;
import com.organization.jeptagsalpha.wordpress.model.User;
import com.organization.jeptagsalpha.wordpress.rest.HttpServerErrorResponse;
import com.organization.jeptagsalpha.wordpress.rest.WordPressRestResponse;
import com.organization.jeptagsalpha.wordpress.rest.WpClientRetrofit;
import com.organization.jeptagsalpha.wordpress.util.LoginAccountHelper;

import org.apache.http.HttpResponse;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;


public class BaseActivity extends AppCompatActivity{


    public static String TAG = BaseActivity.class.getSimpleName();
    ProgressDialog progressDialog;
    public Toolbar toolbar;
    public LoginAccountHelper loginAccountHelper;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        loginAccountHelper = new LoginAccountHelper(this);
    }
    public void setActionBar()
    {
       setActionBar(getString(R.string.app_name_website));
    }
    public void setActionBarEmpty()
    {
        setActionBar("");
    }
    public void setActionBar(String title,boolean homeUp)
    {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null)
        {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(homeUp);
            applyFontForToolbarTitle(toolbar,title);
        }
    }
    public void setActionBar(String title)
    {
       setActionBar(title,true);
    }
    public void showHttpError(HttpServerErrorResponse e) {
        if(e!=null) {
            String errorMsg = !TextUtils.isEmpty(e.getMessage()) ? e.getMessage() : null;
            if (errorMsg != null) {
                Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
            }
        }

    }
    public void applyFontForToolbarTitle(Toolbar toolbar,String title){
        TextView textView = (TextView)findViewById(R.id.appbarTitle);
        if(textView!=null)
        {
            textView.setText(title);
        }
    }
    public void showToast(String message) {
        String Msg = !TextUtils.isEmpty(message) ? message : null;
        if (Msg != null) {
            Toast.makeText(this, Msg, Toast.LENGTH_LONG).show();
        }
    }
    public void showProgress(String message)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void showProgress()
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.wait_loading));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public  void hideProgress()
    {
        if(progressDialog != null)
        {
            progressDialog.dismiss();
        }
    }
    public void login(final String username, final String password,final boolean backgorundMode)
    {
        if(!backgorundMode)
            showProgress(getString(R.string.wait_login_into_system));
        WpClientRetrofit service = new WpClientRetrofit(context,MyConstants.WP_BASE_URL,username,password,MyConstants.DEBUG_MODE);
        service.getUserMe(new WordPressRestResponse<User>() {
            @Override
            public void onSuccess(User user) {
                hideProgress();
                if(!backgorundMode)
                showToast(getString(R.string.login_success));
                String role = MyConstants.ROLE_INTERNATIONAL;
                if(user.getRoles().length > 0)
                {
                    role = user.getRoles()[0];
                }
                String profilePic ="";
                loginAccountHelper.setUser(Long.parseLong(user.getId()),username,password,user.getName(),user.getFirst_name(),user.getLast_name(),user.getEmail(),role,profilePic,true);

                MyConstants.WPService = new WpClientRetrofit(context,MyConstants.WP_BASE_URL,username,password,MyConstants.DEBUG_MODE);
                MyConstants.WCService = new WpClientRetrofit(context,MyConstants.WC_BASE_URL,username,password,MyConstants.DEBUG_MODE);
                MyConstants.WOService = new WpClientRetrofit(context,MyConstants.WP_BASE_ORIGNAL_URL,username,password,MyConstants.DEBUG_MODE);
            }

            @Override
            public void onFailure(HttpServerErrorResponse errorResponse) {
                hideProgress();
                if(backgorundMode) {
                    LoginActivity.start(BaseActivity.this);
                    finish();
                }
                else
                {
                    showHttpError(errorResponse);
                }
            }
        });
    }

    public void createUser(final User user)
    {
        showProgress(getString(R.string.wait_create_user));
        WpClientRetrofit service = new WpClientRetrofit(context,MyConstants.WP_BASE_URL,"","",MyConstants.DEBUG_MODE);
        service.createUser(user, new WordPressRestResponse<User>() {
            @Override
            public void onSuccess(User result) {
                hideProgress();
                showToast(getString(R.string.success_user_create));
                login(user.getUsername(),user.getPassword(),false);
            }

            @Override
            public void onFailure(HttpServerErrorResponse errorResponse) {
                hideProgress();
                showHttpError(errorResponse);
            }
        });
    }
    public void updateUser(User user) {
        showProgress(getString(R.string.wait_update_user));
        MyConstants.WPService.updateUser(user, new WordPressRestResponse<User>() {
            @Override
            public void onSuccess(User result) {
                hideProgress();
                showToast(getString(R.string.success_update_create));
            }

            @Override
            public void onFailure(HttpServerErrorResponse errorResponse) {
                hideProgress();
                showHttpError(errorResponse);
            }
        });
    }
    public void Logout()
    {
        loginAccountHelper.resetUserState();
        LoginActivity.start(this);
        finish();
    }
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }
    protected void showAlert(String msg,@NotNull DialogInterface.OnClickListener onClickListener) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setIconAttribute(android.R.attr.alertDialogIcon)
                .setPositiveButton("OK",onClickListener).create().show();
    }


    protected String convertToBase64(byte[] bitmapByte)
    {
        String encodedImage= android.util.Base64.encodeToString(bitmapByte, Base64.DEFAULT);
        return encodedImage;
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
