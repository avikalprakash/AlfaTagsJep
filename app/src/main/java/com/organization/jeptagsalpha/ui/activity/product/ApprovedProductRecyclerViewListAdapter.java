package com.organization.jeptagsalpha.ui.activity.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.authorization.Urls;
import com.organization.jeptagsalpha.ui.activity.product.ApprovedProductListFragment.OnListFragmentInteractionListener;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ApprovedProductRecyclerViewListAdapter extends RecyclerView.Adapter<ApprovedProductRecyclerViewListAdapter.ViewHolder> {

    private final List<ProductDetails> mValues;
    private final OnListFragmentInteractionListener mListener;
    ProgressDialog pDialog;

    public ApprovedProductRecyclerViewListAdapter(List<ProductDetails> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approved_adapter_layout, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
    //    holder.mId.setText(mValues.get(position).getProduct_id());
        holder.mName.setText(mValues.get(position).getName());
    //    holder.mCategory.setText(mValues.get(position).getCategory());

      //  getImage(mValues.get(position).getImage(),holder.pd_img);
        Picasso.with(ApprovedProductList.context).load(mValues.get(position).getImage()).into(holder.pd_img);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
        holder.addJepTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent adJepTagIntent=new Intent(ApprovedProductList.context,AddJepTag.class);
                adJepTagIntent.putExtra("pr_id", mValues.get(position).getProduct_id());
                adJepTagIntent.putExtra("pr_name", mValues.get(position).getName());
                adJepTagIntent.putExtra("pr_url", mValues.get(position).getProduct_url());
                adJepTagIntent.putExtra("pr_qty", mValues.get(position).getProduct_qty());
                adJepTagIntent.putExtra("longi", mValues.get(position).getProduct_longitude());
                adJepTagIntent.putExtra("lati", mValues.get(position).getProduct_latitude());
                adJepTagIntent.putExtra("image", mValues.get(position).getImage());
                ApprovedProductList.context.startActivity(adJepTagIntent);
            }
        });

        holder.infoJepTagBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent iEdit = new Intent(ApprovedProductList.context, EditProductDetails.class);
                iEdit.putExtra("product_id", mValues.get(position).getProduct_id());
                iEdit.putExtra("qty", mValues.get(position).getProduct_qty());
              //  iEdit.putExtra("listCount", listCount);
                ApprovedProductList.context.startActivity(iEdit);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CheckBox checkBox;
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(ApprovedProductList.context);
                LayoutInflater layoutInflater = LayoutInflater.from(ApprovedProductList.context);
                View eulaLayout = layoutInflater.inflate(R.layout.alert_dialog, null);
                checkBox=(CheckBox)eulaLayout.findViewById(R.id.checkbox);
                alertDialog.setView(eulaLayout);
                alertDialog.setCancelable(false);
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkBox.isChecked()) {
                            new DeleteData().execute(mValues.get(position).getProduct_id());
                            //Toast.makeText(ProductListActivity.context, mValues.get(position).getProduct_id(), Toast.LENGTH_LONG).show();
                        }else {
                            alertDialog.setCancelable(false);
                            Toast.makeText(ApprovedProductList.context, ProductListActivity.context.getString(R.string.toast_alert_delete_msg), Toast.LENGTH_LONG).show();
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

        mValues.get(position).getApproved();
        int a = Integer.parseInt(mValues.get(position).getApproved());
        if (a==0){
            holder.addJepTagBtn.setVisibility(View.INVISIBLE);
        }else {
            holder.addJepTagBtn.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
       return mValues.size();

    }

    class  DeleteData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(ApprovedProductList.context);
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
                        Toast.makeText(ApprovedProductList.context, ProductListActivity.context.getString(R.string.delete_msg), Toast.LENGTH_LONG).show();
                        Intent iSave=new Intent(ApprovedProductList.context, ApprovedProductList.class);
                        ApprovedProductList.context.startActivity(iSave);
                        ((Activity) ApprovedProductList.context).finish();
                    }
                }
                else Toast.makeText(ApprovedProductList.context, ProductListActivity.context.getString(R.string.error), Toast.LENGTH_LONG).show();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
      //  public final TextView mId;
        public final TextView mName;
      //  public final TextView mCategory;
        public ProductDetails mItem;
        public CircleImageView pd_img;
        public AppCompatTextView addJepTagBtn;
        public AppCompatTextView infoJepTagBtn;
        public ImageView delete;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           // mId = (TextView) view.findViewById(R.id.textViewPDID);
            mName = (TextView) view.findViewById(R.id.textViewPDNAME);
           // mCategory=(TextView)view.findViewById(R.id.textViewCategory);
            pd_img=(CircleImageView)view.findViewById(R.id.pd_img);
            addJepTagBtn=(AppCompatTextView)view.findViewById(R.id.addJepTagBtn);
            infoJepTagBtn=(AppCompatTextView)view.findViewById(R.id.infoJepTagBtn);
            delete = (ImageView)view.findViewById(R.id.delete);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mName.getText() + "'";
        }
    }


    void getImage(String url, final CircleImageView imageView)
    {
        RequestQueue requestAdministrator = null;
        ImageRequest request = new ImageRequest(url,
                new Response.Listener<Bitmap>() {

                    @Override
                    public void onResponse(Bitmap bitmap) {
                        if(bitmap!=null) imageView.setBackground(new
                                BitmapDrawable(ProductListActivity.context.getResources(),bitmap));

                    }
                },0,0,null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });

        MySingleton.getInstance(ApprovedProductList.context).addToRequestQueue(request);
    }


}
