package com.organization.jeptagsalpha.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.organization.jeptagsalpha.R;
import com.organization.jeptagsalpha.ui.controls.MyTextView;
import com.organization.jeptagsalpha.ui.utils.ProductAddAdpterNotif;
import com.organization.jeptagsalpha.wordpress.model.wc.Product;

import java.util.List;



public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.MyViewHolder> {

    public List<Product> productList;
    private ProductAddAdpterNotif productAddAdpterNotif;
    private Context mContext;
    class MyViewHolder extends RecyclerView.ViewHolder {
        public MyTextView id;
        public ImageView image,pic;
        public MyTextView name,price,category, subcategory;
        public MyViewHolder(View view) {
            super(view);
            id = (MyTextView) view.findViewById(R.id.prod_id);
            name = (MyTextView) view.findViewById(R.id.prod_name);
            pic = (ImageView) view.findViewById(R.id.prod_image);
            category = (MyTextView) view.findViewById(R.id.prod_category);
           // category = (MyTextView) view.findViewById(R.id.prod_sub_category);
            price = (MyTextView) view.findViewById(R.id.prod_price);
            image =(ImageView) view.findViewById(R.id.BtnClose);
        }
    }
    public ProductListAdapter(Context mContext, List<Product> productList, ProductAddAdpterNotif productAddAdpterNotif)
    {
        this.mContext = mContext;
        this.productList = productList;
        this.productAddAdpterNotif = productAddAdpterNotif;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_item, parent, false);

        return new ProductListAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(ProductListAdapter.MyViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(product.getName());
        holder.price.setText(String.valueOf(product.getRegular_price()));
        holder.category.setText(product.getCategoriesToString());
        holder.subcategory.setText(product.getCategoriesToString());
        Picasso.with(mContext).load(product.getImageURL()).placeholder(R.drawable.default_product).error(R.drawable.default_product).into(holder.pic);
      /*  holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productList.remove(position);
                notifyDataSetChanged();
                if(productAddAdpterNotif!=null) {
                    productAddAdpterNotif.onProductRemoved(position);
                }
            }
        });*/

    }
    @Override
    public int getItemCount() {
        return productList.size();
    }


}
