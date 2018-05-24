package com.organization.jeptagsalpha.ui.controls.Spinner;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.controls.MyTextView;

import java.util.List;

/**
 * Created by Lord-Shiva on 10/10/2016.
 */

public class MySpinnerAdapter<T> extends ArrayAdapter<T> {

    private Context mContext;
    private final List<T> items;

    public MySpinnerAdapter(Context context, List<T> items) {
       super(context,android.R.layout.simple_dropdown_item_1line,items);
        this.mContext = context;
        this.items = items;
    }

    @Override public int getCount() {
        return items.size();
    }

    @Override public T getItem(int position) {
            return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyTextView textView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.ms__list_item, parent, false);
            textView = (MyTextView) convertView.findViewById(R.id.tv_tinted_spinner);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                Configuration config = mContext.getResources().getConfiguration();
                if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                    textView.setTextDirection(View.TEXT_DIRECTION_RTL);
                }
            }
            convertView.setTag(new MySpinnerAdapter.ViewHolder(textView));
        } else {
            textView = ((MySpinnerAdapter.ViewHolder) convertView.getTag()).textView;
        }
        textView.setText(getItem(position).toString());
        textView.setTextSize(15);
        return convertView;
    }

    private static class ViewHolder {

        private MyTextView textView;

        private ViewHolder(MyTextView textView) {
            this.textView = textView;
        }

    }


}
