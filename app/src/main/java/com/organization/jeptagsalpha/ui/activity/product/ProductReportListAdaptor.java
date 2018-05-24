package com.organization.jeptagsalpha.ui.activity.product;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.organization.jeptagsalpha.R;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;


/**
 * Created by lue on 23-06-2017.
 */

public class ProductReportListAdaptor extends BaseAdapter {

  //  LayoutInflater mInflater;
   // private Activity context;
   ImageView circleImageView;
  //  ArrayList<User> Contact=new ArrayList<>();
    ArrayList<ReportListDetails> productDetailsArrayList =new ArrayList<>();
     TextView mProductValue, mProductName;

    ProgressDialog pDialog;
    String ReciverMobile="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    LayoutInflater mInflater;
    private Activity context;//
    public ProductReportListAdaptor(FragmentActivity activity, ArrayList<ReportListDetails> storeContacts) {
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
        view = mInflater.inflate(R.layout.product_report_list_adapter, null);
        mProductValue = (TextView) view.findViewById(R.id.product_value);
        mProductName = (TextView) view.findViewById(R.id.product_name);

        if(productDetailsArrayList.get(i).getTotal_product()!=null){
            mProductValue.setText(productDetailsArrayList.get(i).getTotal_product());
          //  mProductName.setText();
        }else {
            mProductValue.setText(" ");
        }
        if(productDetailsArrayList.get(i).getProduct_name()!=null){
            mProductName.setText(productDetailsArrayList.get(i).getProduct_name());
            //  mProductName.setText();
        }else {
            mProductValue.setText(" ");
        }
        return view;
    }
}
