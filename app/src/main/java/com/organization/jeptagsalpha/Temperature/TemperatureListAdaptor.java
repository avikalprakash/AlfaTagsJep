package com.organization.jeptagsalpha.Temperature;

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

public class TemperatureListAdaptor extends BaseAdapter {

  //  LayoutInflater mInflater;
   // private Activity context;
   ImageView circleImageView;
  //  ArrayList<User> Contact=new ArrayList<>();
    ArrayList<TempListDetails> productDetailsArrayList =new ArrayList<>();
     TextView mSerialNo, deriation, time, temp;

    ProgressDialog pDialog;
    String ReciverMobile="";
    private static final int SHOW_PROCESS_DIALOG = 1;
    private static final int HIDE_PROCESS_DIALOG = 0;
    AVLoadingIndicatorView dialog;
    LayoutInflater mInflater;
    private Activity context;//
    public TemperatureListAdaptor(FragmentActivity activity, ArrayList<TempListDetails> storeContacts) {
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
        view = mInflater.inflate(R.layout.temperature_adapter_layout, null);
        mSerialNo = (TextView) view.findViewById(R.id.serial_no);
        deriation = (TextView) view.findViewById(R.id.duriation);
        time = (TextView) view.findViewById(R.id.time);
        temp = (TextView) view.findViewById(R.id.temp);

        if (productDetailsArrayList.get(i).getId() != null) {
            mSerialNo.setText(productDetailsArrayList.get(i).getId());
        } else {
            mSerialNo.setText(" ");
        }
        if (productDetailsArrayList.get(i).getInterval_time() != null) {
            deriation.setText(productDetailsArrayList.get(i).getInterval_time());
        } else {
            deriation.setText(" ");
        }
        if (productDetailsArrayList.get(i).getTiming_now() != null) {
            time.setText(productDetailsArrayList.get(i).getTiming_now());
        } else {
            time.setText(" ");
        }
        if (productDetailsArrayList.get(i).getUpper_limit() != null) {
            temp.setText(productDetailsArrayList.get(i).getUpper_limit());
        } else {
            temp.setText(" ");
        }
    return  view;
    }
}
