package com.organization.jeptagsalpha.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.controls.MyTextView;
import com.organization.jeptagsalpha.ui.model.Product;
import com.organization.jeptagsalpha.ui.utils.ProductAddAdpterNotif;

import java.util.List;



public class ProductAddAdapter extends RecyclerView.Adapter<ProductAddAdapter.MyViewHolder> {

    public List<Product> productList;

    private ProductAddAdpterNotif productAddAdpterNotif;
    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView id;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            id = (MyTextView) view.findViewById(R.id.prod_id);
            image =(ImageView) view.findViewById(R.id.BtnClose);
        }
    }
    public ProductAddAdapter(List<Product> productList, ProductAddAdpterNotif productAddAdpterNotif)
    {
        this.productList = productList;
        this.productAddAdpterNotif = productAddAdpterNotif;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_add_item, parent, false);

        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Product product = productList.get(position);
        holder.id.setText(product.getId());
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.remove(position);
                notifyDataSetChanged();
                productAddAdpterNotif.onProductRemoved(position);
            }
        });

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }


}
