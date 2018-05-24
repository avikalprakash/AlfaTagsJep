package com.organization.jeptagsalpha.ui.activity.product;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.activity.product.JepTagsFragment.OnListFragmentInteractionListener;

import java.util.Collections;
import java.util.List;


public class JepTagRecyclerViewAdapter extends RecyclerView.Adapter<JepTagRecyclerViewAdapter.ViewHolder> {

    private final List<JepTagList> mValues;
    private final OnListFragmentInteractionListener mListener;

    public JepTagRecyclerViewAdapter(List<JepTagList> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.jeptag_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.slno.setText(String.valueOf(position+1));
        holder.jeptagId.setText(mValues.get(position).getJepTag_id());
        holder.scanTime.setText(mValues.get(position).getJepTagTime());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView slno;
        public final TextView jeptagId;
        public final TextView scanTime;
        public JepTagList mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            slno = (TextView) view.findViewById(R.id.id);
            jeptagId = (TextView) view.findViewById(R.id.nfcTagID);
            scanTime = (TextView) view.findViewById(R.id.nfcScanTime);
        }

        @Override
        public String toString() {
            return super.toString() + " ";
        }
    }
}
