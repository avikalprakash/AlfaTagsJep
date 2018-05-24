package com.organization.jeptagsalpha.ui.activity.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


/**
 * Created by lue on 23-06-2017.
 */

public class ProductPendingListAdaptor extends BaseAdapter {

  //  LayoutInflater mInflater;
   // private Activity context;
   ImageView circleImageView;
  //  ArrayList<User> Contact=new ArrayList<>();
    ArrayList<ProductDetails> productDetailsArrayList =new ArrayList<>();
     TextView mProdName;
    ImageView mImageView, delete;
    AppCompatTextView info;
    ProgressDialog pDialog;
    String ReciverMobile="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    LayoutInflater mInflater;
    private Activity context;//
    public ProductPendingListAdaptor(FragmentActivity activity, ArrayList<ProductDetails> storeContacts) {
        this.context=activity;
        this.productDetailsArrayList =storeContacts;
    }
    @Override
    public int getCount() {
        return productDetailsArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = mInflater.inflate(R.layout.pending_adapter_layout, null);
        mProdName = (TextView) view.findViewById(R.id.textViewPDNAME);
        mImageView=(ImageView)view.findViewById(R.id.pd_img);
        delete=(ImageView)view.findViewById(R.id.delete);
        info=(AppCompatTextView)view.findViewById(R.id.infoJepTagBtn);
        if(productDetailsArrayList.get(i).getName()!=null){
            mProdName.setText(productDetailsArrayList.get(i).getName());
        }else {
            mProdName.setText(" ");
        }
        if(productDetailsArrayList.get(i).getImage()!=null){
            Picasso.with(ProductPendingListActivity.context).load(productDetailsArrayList.get(i).getImage()).into(mImageView);
        }
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckBox checkBox;
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProductPendingListActivity.context);
                LayoutInflater layoutInflater = LayoutInflater.from(ProductPendingListActivity.context);
                View eulaLayout = layoutInflater.inflate(R.layout.alert_dialog, null);
                checkBox=(CheckBox)eulaLayout.findViewById(R.id.checkbox);
                alertDialog.setView(eulaLayout);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkBox.isChecked()) {
                            new DeleteData().execute(productDetailsArrayList.get(i).getProduct_id());
                            //Toast.makeText(ProductListActivity.context, mValues.get(position).getProduct_id(), Toast.LENGTH_LONG).show();
                        }else {
                            alertDialog.setCancelable(false);
                            Toast.makeText(ProductListActivity.context, ProductPendingListActivity.context.getString(R.string.toast_alert_delete_msg), Toast.LENGTH_LONG).show();
                        }
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                alertDialog.create().show();
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iEdit = new Intent(ProductPendingListActivity.context, ProductDetailsActivity.class);
                iEdit.putExtra("product_id", productDetailsArrayList.get(i).getProduct_id());
                iEdit.putExtra("qty", productDetailsArrayList.get(i).getProduct_qty());
                //  iEdit.putExtra("listCount", listCount);
                ProductPendingListActivity.context.startActivity(iEdit);
            }
        });


        return view;
    }

    class  DeleteData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ProductListActivity.context);
            pDialog.setMessage(ProductListActivity.context.getString(R.string.pr_deleting));
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
                        Toast.makeText(ProductListActivity.context, ProductListActivity.context.getString(R.string.delete_msg), Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(ProductListActivity.context, ProductListActivity.class);
                        ProductListActivity.context.startActivity(iSave);
                        ((Activity) ProductListActivity.context).finish();
                    }
                }
                else Toast.makeText(ProductListActivity.context, ProductListActivity.context.getString(R.string.error), Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
